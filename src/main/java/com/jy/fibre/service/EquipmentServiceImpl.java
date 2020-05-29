package com.jy.fibre.service;

import com.google.common.base.Preconditions;
import com.jy.fibre.bean.equipment.EquipmentQueryVO;
import com.jy.fibre.bean.equipment.EquipmentVO;
import com.jy.fibre.bean.pe.AirConditionPatrolVO;
import com.jy.fibre.dao.EquipmentDao;
import com.jy.fibre.domain.Equipment;
import com.xmgsd.lan.gwf.domain.DictionaryCode;
import com.xmgsd.lan.gwf.service.DictionaryCodeServiceImpl;
import com.xmgsd.lan.roadhog.exception.NoEntityWithIdException;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.mappers.BasePaginationMapper;
import com.xmgsd.lan.roadhog.mybatis.service.PaginationService;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import com.xmgsd.lan.roadhog.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class EquipmentServiceImpl extends BaseService<EquipmentDao> implements SimpleCurdViewService<String, EquipmentVO>, PaginationService<EquipmentQueryVO, EquipmentVO> {

    private DictionaryCodeServiceImpl dictionaryCodeService;

    @Autowired
    public EquipmentServiceImpl(DictionaryCodeServiceImpl dictionaryCodeService) {
        this.dictionaryCodeService = dictionaryCodeService;
    }

    @Override
    public BasePaginationMapper<EquipmentVO> getPaginationMapper() {
        return this.getMapper();
    }

    /**
     * 上传文件覆盖扩展信息
     *
     * @param airConditionPatrolVO 精密空调表
     * @throws IOException 异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateByAirConditionPatrolVO(@NotNull AirConditionPatrolVO airConditionPatrolVO) throws IOException {
        Equipment equipment = Preconditions.checkNotNull(this.getMapper().selectByPrimaryKey(airConditionPatrolVO.getEquipmentId()), new NoEntityWithIdException(airConditionPatrolVO.getEquipmentId()).getMessage());
        String configs = equipment.getConfigs();
        Map map = JSON.deserialize(configs, HashMap.class);
        map.put("settingTemperature", airConditionPatrolVO.getTemperatureSetting());
        map.put("settingHumidity", airConditionPatrolVO.getHumiditySetting());
        map.put("actualReturnAirTemperature", airConditionPatrolVO.getTemperatureMeasurementOfAirReturnPort());
        equipment.setConfigs(JSON.serialize(map));
        this.getMapper().updateByPrimaryKey(equipment);
    }

    /**
     * 自动生成设备名
     *
     * @param computerRoomId  机房
     * @param equipmentTypeId 类别
     * @return 设备名称
     */
    public String theNumberOfFindByComputerRoomIdAndTypeId(@NotNull String computerRoomId, @NotNull String equipmentTypeId) {
        int num = this.getMapper().theNumberOfFindByComputerRoomIdAndTypeId(computerRoomId, equipmentTypeId);
        DictionaryCode dictionaryCode = Preconditions.checkNotNull(dictionaryCodeService.get(equipmentTypeId), new NoEntityWithIdException(equipmentTypeId).getMessage());

        return dictionaryCode.getName() + (num + 1);
    }

    @Override
    public void processQuery(EquipmentQueryVO query) {
        if ("computer_room_name".equals(query.getSortField())) {
            query.setSortField("cr.name");
        }
        if ("name".equals(query.getSortField())) {
            query.setSortField("e.name");
        }
    }
}
