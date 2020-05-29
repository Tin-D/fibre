package com.jy.fibre.service;

import com.jy.fibre.bean.pe.PatrolExcelVO;
import com.jy.fibre.dao.PatrolExcelDao;
import com.jy.fibre.domain.PatrolExcel;
import com.jy.fibre.enums.AttachmentRecorderType;
import com.jy.fibre.enums.AttachmentType;
import com.xmgsd.lan.gwf.bean.LoginUser;
import com.xmgsd.lan.gwf.dao.AttachmentDao;
import com.xmgsd.lan.gwf.domain.Attachment;
import com.xmgsd.lan.gwf.utils.SecurityUtil;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class PatrolExcelServiceImpl extends BaseService<PatrolExcelDao> implements SimpleCurdViewService<String, PatrolExcelVO> {

    private final AttachmentDao attachmentDao;

    @Autowired
    public PatrolExcelServiceImpl(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    @Override
    public List<PatrolExcelVO> list() {
        return this.getMapper().findAll();
    }

    /**
     * 插入记录后，插入附件
     *
     * @param item     巡检表
     * @param recorder 插入数据库的记录
     * @throws Exception 异常
     */
    @Override
    public void afterAdd(@NotNull PatrolExcelVO item, @NotNull DbItem recorder) throws Exception {
        if (!CollectionUtils.isEmpty(item.getAttachments())) {
            LoginUser loginUser = SecurityUtil.getLoginUserOrError();
            List<Attachment> attachments = item.getAttachments().stream().map(a -> {
                Attachment attachment = new Attachment(a, AttachmentRecorderType.patrolExcel.name(), loginUser);
                attachment.setType(AttachmentType.FileTemplate.name());
                return attachment;
            }).collect(Collectors.toList());
            PatrolExcel patrolExcel = (PatrolExcel) recorder;
            for (Attachment attachment : attachments) {
                attachment.setRecorderId(patrolExcel.getId());
            }
            attachmentDao.insert(attachments);
        }
    }

    /**
     * 更新记录后，要更新附件
     *
     * @param item     巡检表
     * @param recorder 插入数据库的记录
     * @throws Exception 异常
     */
    @Override
    public void afterUpdate(@NotNull PatrolExcelVO item, @NotNull DbItem recorder) throws Exception {
        PatrolExcel patrolExcel = (PatrolExcel) recorder;
        LoginUser loginUser = SecurityUtil.getLoginUserOrError();
        List<Attachment> attachments = item.getAttachments() == null ? Collections.emptyList() : item.getAttachments().stream().map(a -> new Attachment(a, AttachmentRecorderType.patrolExcel.name(), loginUser)).collect(Collectors.toList());
        attachmentDao.updateRecorderAttachments(patrolExcel.getId(), null, attachments);
    }

    /**
     * 删除后要删除附件
     *
     * @param id 记录id
     */
    @Override
    public void afterRemove(String id) {
        this.attachmentDao.deleteByRecorderId(id);
    }

    /**
     * 根据设备类型查找表格模板
     *
     * @param equipmentTypeId 设备类型
     */
    public List<PatrolExcelVO> findByEquipmentTypeId(@NotNull String equipmentTypeId) {
        return this.getMapper().findByEquipmentTypeId(equipmentTypeId);
    }

}

