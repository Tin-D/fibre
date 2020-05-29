package com.jy.fibre.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.jy.fibre.bean.ml.MaintenanceLevelVO;
import com.jy.fibre.dao.BuiltInTemplateDao;
import com.jy.fibre.dao.MaintenanceDetailsDao;
import com.jy.fibre.dao.MaintenanceLevelDao;
import com.jy.fibre.domain.MaintenanceDetails;
import com.jy.fibre.domain.MaintenanceLevel;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.exception.NoEntityWithIdException;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class MaintenanceLevelServiceImpl extends BaseService<MaintenanceLevelDao> implements SimpleCurdViewService<String, MaintenanceLevelVO> {

    private MaintenanceDetailsDao maintenanceDetailsDao;

    private BuiltInTemplateDao builtInTemplateDao;

    @Autowired
    public MaintenanceLevelServiceImpl(MaintenanceDetailsDao maintenanceDetailsDao, BuiltInTemplateDao builtInTemplateDao) {
        this.maintenanceDetailsDao = maintenanceDetailsDao;
        this.builtInTemplateDao = builtInTemplateDao;
    }

    @Override
    public MaintenanceLevelVO add(@NotNull MaintenanceLevelVO item) throws Exception {
        MaintenanceLevel maintenanceLevel = item.toDbInsertItem();
        this.getMapper().insert(maintenanceLevel);
        List<IdNameEntry> builtInTemplate = item.getBuiltInTemplateIdAndName();
        if (!CollectionUtils.isEmpty(builtInTemplate)) {
            for (IdNameEntry idNameEntry : builtInTemplate) {
                MaintenanceDetails maintenanceDetails = new MaintenanceDetails();
                maintenanceDetails.setMaintenanceLevelId(maintenanceLevel.getId());
                maintenanceDetails.setBuiltInTemplateId(idNameEntry.getId());
                maintenanceDetailsDao.insert(maintenanceDetails);
            }
        }
        return this.get(maintenanceLevel.getId());
    }

    @Nullable
    @Override
    public MaintenanceLevelVO get(@NotNull String id) {
        MaintenanceLevel maintenanceLevel = Preconditions.checkNotNull(this.getMapper().selectByPrimaryKey(id), new NoEntityWithIdException(id).getMessage());
        MaintenanceLevelVO maintenanceLevelVO = new MaintenanceLevelVO();
        BeanUtils.copyProperties(maintenanceLevel, maintenanceLevelVO);
        List<IdNameEntry> patrolExcelIdAndFileName = this.getMapper().findPatrolExcelIdAndFileNameById(id);
        maintenanceLevelVO.setPatrolExcelIdAndFileName(patrolExcelIdAndFileName);
        List<IdNameEntry> builtInTemplate = builtInTemplateDao.findByMaintenanceLevelId(id);
        maintenanceLevelVO.setBuiltInTemplateIdAndName(builtInTemplate);
        return maintenanceLevelVO;
    }

    @Override
    public void afterUpdate(MaintenanceLevelVO item, @NotNull DbItem recorder) {
        MaintenanceLevel maintenanceLevel = (MaintenanceLevel) recorder;

        List<IdNameEntry> oldBuiltInTemplate = builtInTemplateDao.findByMaintenanceLevelId(maintenanceLevel.getId());
        Set<String> newBuiltInTemplateIds = item.getBuiltInTemplateIdAndName().stream().map(IdNameEntry::getId).collect(Collectors.toSet());
        Set<String> oldBuiltInTemplateIds = oldBuiltInTemplate.stream().map(IdNameEntry::getId).collect(Collectors.toSet());

        Sets.SetView<String> addBuiltInTemplateIds = Sets.difference(newBuiltInTemplateIds, oldBuiltInTemplateIds);
        Sets.SetView<String> deleteBuiltInTemplateIds = Sets.difference(oldBuiltInTemplateIds, newBuiltInTemplateIds);

        if (!deleteBuiltInTemplateIds.isEmpty()) {
            for (String deleteBuiltInTemplateId : deleteBuiltInTemplateIds) {
                this.maintenanceDetailsDao.deleteByBuiltInTemplateId(deleteBuiltInTemplateId, maintenanceLevel.getId());
            }
        }

        if (!addBuiltInTemplateIds.isEmpty()) {
            for (String addBuiltInTemplateId : addBuiltInTemplateIds) {
                MaintenanceDetails maintenanceDetails = new MaintenanceDetails();
                maintenanceDetails.setMaintenanceLevelId(maintenanceLevel.getId());
                maintenanceDetails.setBuiltInTemplateId(addBuiltInTemplateId);
                this.maintenanceDetailsDao.insert(maintenanceDetails);
            }
        }
    }

    /**
     * 根据类别查询
     *
     * @param equipmentTypeId 类别
     * @return 维护等级
     */
    public List<IdNameEntry> findByTypeId(String equipmentTypeId) {
        return this.getMapper().findByTypeId(equipmentTypeId);
    }
}
