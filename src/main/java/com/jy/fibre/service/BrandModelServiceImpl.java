package com.jy.fibre.service;

import com.jy.fibre.bean.bm.BrandModelVO;
import com.jy.fibre.dao.BrandModelDao;
import com.jy.fibre.dao.EquipmentDao;
import com.jy.fibre.domain.BrandModel;
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
import org.springframework.beans.BeanUtils;
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
public class BrandModelServiceImpl extends BaseService<BrandModelDao> implements SimpleCurdViewService<String, BrandModelVO> {

    private AttachmentDao attachmentDao;

    private EquipmentDao equipmentDao;

    public BrandModelServiceImpl(AttachmentDao attachmentDao, EquipmentDao equipmentDao) {
        this.attachmentDao = attachmentDao;
        this.equipmentDao = equipmentDao;
    }

    @Override
    public List<BrandModelVO> list() {
        return this.getMapper().selectAll().stream().map(i -> {
            BrandModelVO modelVO = new BrandModelVO();
            BeanUtils.copyProperties(i, modelVO);
            return modelVO;
        }).collect(Collectors.toList());
    }

    /**
     * 插入记录后，插入附件
     *
     * @param item     页面传递上来的记录
     * @param recorder 插入数据库的记录
     * @throws Exception 异常
     */
    @Override
    public void afterAdd(@NotNull BrandModelVO item, @NotNull DbItem recorder) throws Exception {
        if (!CollectionUtils.isEmpty(item.getAttachments())) {
            LoginUser loginUser = SecurityUtil.getLoginUserOrError();
            List<Attachment> attachments = item.getAttachments().stream().map(a -> {
                Attachment attachment = new Attachment(a, AttachmentRecorderType.BrandModel.name(), loginUser);
                attachment.setType(AttachmentType.PictureInformation.name());
                return attachment;
            }).collect(Collectors.toList());
            BrandModel brandModel = (BrandModel) recorder;
            for (Attachment attachment : attachments) {
                attachment.setRecorderId(brandModel.getId());
            }
            attachmentDao.insert(attachments);
        }
    }

    /**
     * 更新记录后，要更新附件
     *
     * @param item     页面传递上来的记录
     * @param recorder 插入数据库的记录
     * @throws Exception 异常
     */
    @Override
    public void afterUpdate(@NotNull BrandModelVO item, @NotNull DbItem recorder) throws Exception {
        BrandModel bm = (BrandModel) recorder;
        LoginUser loginUser = SecurityUtil.getLoginUserOrError();
        List<Attachment> attachments = item.getAttachments() == null ? Collections.emptyList() : item.getAttachments().stream().map(a -> new Attachment(a, AttachmentRecorderType.BrandModel.name(), loginUser)).collect(Collectors.toList());
        attachmentDao.updateRecorderAttachments(bm.getId(), AttachmentType.PictureInformation.name(), attachments);
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
     * 删除型号时，检查该型号下有无设备
     *
     * @param id 型号id
     * @throws Exception 异常
     */
    @Override
    public void remove(@NotNull String id) throws Exception {
        if (equipmentDao.theNumberOfFindByBrandModelId(id) > 0) {
            throw new Exception("请先删除该型号下的设备。");
        } else {
            this.getMapper().deleteByPrimaryKey(id);
        }
    }
}
