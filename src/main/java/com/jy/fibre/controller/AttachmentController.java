package com.jy.fibre.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.jy.fibre.enums.AttachmentRecorderType;
import com.jy.fibre.enums.AttachmentType;
import com.xmgsd.lan.gwf.bean.JsonFileVO;
import com.xmgsd.lan.gwf.bean.LoginUser;
import com.xmgsd.lan.gwf.bean.attachment.AttachmentSummaryVO;
import com.xmgsd.lan.gwf.bean.attachment.AttachmentTokenVO;
import com.xmgsd.lan.gwf.core.audit.AuditMethod;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import com.xmgsd.lan.gwf.dao.AttachmentDao;
import com.xmgsd.lan.gwf.domain.Attachment;
import com.xmgsd.lan.gwf.domain.AuditLog;
import com.xmgsd.lan.gwf.service.OneOffTokenServiceImpl;
import com.xmgsd.lan.gwf.utils.FileDownloadUtil;
import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzhou
 */
@Slf4j
@AuditModule(moduleName = "附件管理")
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private OneOffTokenServiceImpl oneOffTokenService;

    private Attachment getTokenBasedAttachment(String id, String token) {
        AttachmentTokenVO tokenVO = AttachmentTokenVO.check(token);
        if (tokenVO != null) {
            return this.attachmentDao.selectByPrimaryKey(id);
        } else {
            throw new IllegalArgumentException("附件令牌无效，请尝试刷新页面");
        }
    }

    /**
     * 临时存储附件
     *
     * @return 文件id
     */
    @PostMapping("/save_temp/{recorderType}")
    public AttachmentSummaryVO tempSave(@RequestBody JsonFileVO fileVO, @PathVariable String recorderType, @AuthenticationPrincipal LoginUser user) throws JsonProcessingException {
        Attachment attachment = new Attachment(fileVO, user);
        attachment.setRecorderType(recorderType);
        attachment.setContentType(AttachmentType.Temp.name());
        this.attachmentDao.insert(attachment);
        return new AttachmentSummaryVO(attachment, null, true);
    }

    @AuditMethod(name = "保存附件")
    @PutMapping("/save/{attachmentId}/{token}")
    public AttachmentSummaryVO saveWithAttachmentIdAndAttachmentId(@PathVariable String attachmentId,
                                                                   @RequestBody String payload,
                                                                   @PathVariable String token,
                                                                   AuditLog al) throws IOException {
        al.setParams("附件id：" + attachmentId);
        Attachment attachment = getTokenBasedAttachment(attachmentId, token);

        String content = JSON.deserializeToNode(payload).get("content").asText();
        attachment.setContent(Base64.getDecoder().decode(content));
        this.attachmentDao.updateByPrimaryKey(attachment);

        return new AttachmentSummaryVO(attachment, null, true);
    }

    /**
     * 上传单个附件
     *
     * @param file 附件
     * @return 附件ID
     * @throws IOException 读取附件错误
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal LoginUser user) throws IOException {
        Attachment attachment = new Attachment(file, user);
        attachment.setRecorderType(AttachmentRecorderType.Unknown.name());
        attachmentDao.insert(attachment);
        return attachment.getId();
    }

    /**
     * 上传多个附件
     *
     * @param files 附件列表
     * @return 附件ID列表
     * @throws Exception 异常
     */
    @PostMapping("/upload_multi")
    public List<String> uploadFiles(@RequestParam("attachments") MultipartFile[] files, @AuthenticationPrincipal LoginUser user) throws Exception {
        List<Attachment> attachments = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            attachments.add(new Attachment(file, user));
        }
        attachmentDao.insert(attachments);
        return attachments.stream().map(BaseDomainWithGuidKey::getId).collect(Collectors.toList());
    }

    @AuditMethod(name = "获取附件列表")
    @GetMapping(path = {
            "/recorder_attachments/{token}",
            "/recorder_attachments/{token}/{attachmentType}"
    })
    public List<AttachmentSummaryVO> getAttachments(@PathVariable String token,
                                                    @PathVariable(required = false) AttachmentType attachmentType,
                                                    AuditLog al) throws JsonProcessingException {
        AttachmentTokenVO tokenVO = AttachmentTokenVO.check(token);
        if (tokenVO != null) {
            al.setParams(MessageFormat.format("记录id: {0}, 附件类别: {1}", tokenVO.getRecorderId(), attachmentType));
            List<Attachment> attachments = attachmentDao.findAttachmentsByRecorderIdAndAttachmentType(tokenVO.getRecorderId(), attachmentType == null ? null : attachmentType.toString(), false);
            List<AttachmentSummaryVO> result = new ArrayList<>(attachments.size());
            for (Attachment attachment : attachments) {
                result.add(new AttachmentSummaryVO(attachment, 60 * 30, true));
            }
            return result;
        }
        throw new IllegalArgumentException("附件令牌无效");
    }

    @AuditMethod(name = "下载附件")
    @GetMapping("/download/{id}/{token}")
    public void download(@PathVariable String id, @PathVariable String token, AuditLog al, HttpServletResponse response) throws IOException {
        al.setParams("附件id: " + id);
        Attachment attachment = getTokenBasedAttachment(id, token);
        FileDownloadUtil.download(attachment.getName(), attachment.getContent(), response);
    }

    @GetMapping("/preview_image/{id}/{token}")
    public byte[] previewImage(@PathVariable String id, @PathVariable String token, HttpServletResponse response) throws IOException {
        Attachment attachment = getTokenBasedAttachment(id, token);
        if (attachment == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.flushBuffer();
            return null;
        } else {
            return attachment.getContent();
        }
    }

    @AuditMethod(name = "获取附件内容base64")
    @GetMapping("/get_base64/{id}/{token}")
    public String getContentWithBase64(@PathVariable String id, @PathVariable String token, AuditLog al) {
        al.setParams("附件id: " + id);
        Attachment attachment = getTokenBasedAttachment(id, token);
        if (attachment == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(attachment.getContent());
    }

    @GetMapping("/get_content/{id}/{token}")
    public byte[] getContent(@PathVariable String id, @PathVariable String token) {
        Attachment attachment = this.getTokenBasedAttachment(id, token);
        return attachment.getContent();
    }

    @GetMapping("/{id}/{token}")
    public void getAttachment(@PathVariable String id, HttpServletResponse response, @PathVariable String token) throws IOException {
        Attachment attachment = getTokenBasedAttachment(id, token);

        response.setContentType(attachment.getContentType());
        response.setHeader("Content-Disposition", MessageFormat.format("inline;filename=\"{0}\"", attachment.getName()));
        response.addHeader("Program", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ServletOutputStream outputStream = response.getOutputStream();
        ByteStreams.copy(new ByteArrayInputStream(attachment.getContent()), outputStream);

        response.flushBuffer();
    }

    @GetMapping("/content/{oneOffToken}")
    public byte[] getAttachment(@PathVariable String oneOffToken) {
        Attachment attachment = this.oneOffTokenService.get(oneOffToken, Attachment.class);
        if (attachment == null) {
            log.warn("没有找到一次性令牌: {}", oneOffToken);
            return null;
        }
        return attachment.getContent();
    }

    @GetMapping("/download_one_off/{oneOffToken}")
    public void downloadOneOffAttachment(@PathVariable String oneOffToken, HttpServletResponse response) throws IOException {
        Attachment attachment = this.oneOffTokenService.get(oneOffToken, Attachment.class);
        Preconditions.checkNotNull(attachment, "找不到附件");
        FileDownloadUtil.download(attachment.getName(), attachment.getContent(), response);
    }
}
