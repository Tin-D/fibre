package com.jy.fibre.service;

import com.jy.fibre.bean.cr.ComputerRoomQueryVO;
import com.jy.fibre.bean.cr.ComputerRoomVO;
import com.jy.fibre.dao.ComputerRoomDao;
import com.jy.fibre.dao.EquipmentDao;
import com.jy.fibre.domain.ComputerRoom;
import com.jy.fibre.enums.AttachmentRecorderType;
import com.jy.fibre.enums.AttachmentType;
import com.xmgsd.lan.gwf.bean.LoginUser;
import com.xmgsd.lan.gwf.dao.AttachmentDao;
import com.xmgsd.lan.gwf.domain.Attachment;
import com.xmgsd.lan.gwf.utils.SecurityUtil;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import com.xmgsd.lan.roadhog.mybatis.mappers.BasePaginationMapper;
import com.xmgsd.lan.roadhog.mybatis.service.PaginationService;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
public class ComputerRoomServiceImpl extends BaseService<ComputerRoomDao> implements SimpleCurdViewService<String, ComputerRoomVO>, PaginationService<ComputerRoomQueryVO, ComputerRoomVO> {

    private AttachmentDao attachmentDao;

    private EquipmentDao equipmentDao;

    public ComputerRoomServiceImpl(AttachmentDao attachmentDao, EquipmentDao equipmentDao) {
        this.attachmentDao = attachmentDao;
        this.equipmentDao = equipmentDao;
    }

    @Override
    public BasePaginationMapper<ComputerRoomVO> getPaginationMapper() {
        return this.getMapper();
    }

    /**
     * 插入记录后，插入附件
     *
     * @param item     页面传递上来的记录
     * @param recorder 插入数据库的记录
     * @throws Exception 异常
     */
    @Override
    public void afterAdd(@NotNull ComputerRoomVO item, @NotNull DbItem recorder) throws Exception {
        if (!CollectionUtils.isEmpty(item.getAttachments())) {
            LoginUser loginUser = SecurityUtil.getLoginUserOrError();
            List<Attachment> attachments = item.getAttachments().stream().map(a -> {
                Attachment attachment = new Attachment(a, AttachmentRecorderType.ComputerRoom.name(), loginUser);
                attachment.setType(AttachmentType.PictureInformation.name());
                return attachment;
            }).collect(Collectors.toList());
            ComputerRoom computerRoom = (ComputerRoom) recorder;
            for (Attachment attachment : attachments) {
                attachment.setRecorderId(computerRoom.getId());
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
    public void afterUpdate(@NotNull ComputerRoomVO item, @NotNull DbItem recorder) throws Exception {
        ComputerRoom computerRoom = (ComputerRoom) recorder;
        LoginUser loginUser = SecurityUtil.getLoginUserOrError();
        List<Attachment> attachments = item.getAttachments() == null ? Collections.emptyList() : item.getAttachments().stream().map(a -> new Attachment(a, AttachmentRecorderType.ComputerRoom.name(), loginUser)).collect(Collectors.toList());
        attachmentDao.updateRecorderAttachments(computerRoom.getId(), AttachmentType.PictureInformation.name(), attachments);
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

    @Override
    public void remove(@NotNull String id) throws Exception {
        if (equipmentDao.theNumberOfFindByComputerRoomId(id) > 0) {
            this.getMapper().deleteByPrimaryKey(id);
        } else {
            throw new Exception("请先删除该客户下的机房。");
        }
    }

    @Override
    public void processQuery(ComputerRoomQueryVO query) {

        if ("name".equals(query.getSortField())) {
            query.setSortField("cr.name");
        }
        if ("customer_name".equals(query.getSortField())) {
            query.setSortField("c.name");
        }
        if ("address".equals(query.getSortField())) {
            query.setSortField("cr.address");
        }
        if ("contacts_name".equals(query.getSortField())) {
            query.setSortField("cr.contacts_name");
        }
        if ("contacts_phone".equals(query.getSortField())) {
            query.setSortField("cr.contacts_phone");
        }
        if ("order_number".equals(query.getSortField())) {
            query.setSortField("cr.order_number");
        }
    }
}

