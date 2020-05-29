package com.jy.fibre.service;

import com.jy.fibre.dao.BuiltInTemplateDao;
import com.jy.fibre.domain.BuiltInTemplate;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class BuiltInTemplateServiceImpl extends BaseService<BuiltInTemplateDao> implements SimpleCurdViewService<String, BuiltInTemplate> {

    /**
     * 根据类别查内置模板
     *
     * @param equipmentTypeId 类别
     * @return 内置模板
     */
    public List<BuiltInTemplate> findByEquipmentTypeId(@NotNull String equipmentTypeId) {
        return this.getMapper().findByEquipmentTypeId(equipmentTypeId);
    }
}
