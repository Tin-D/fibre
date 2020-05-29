package com.jy.fibre.dao;

import com.jy.fibre.domain.BuiltInTemplate;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface BuiltInTemplateDao extends CurdMapper<BuiltInTemplate, String> {

    /**
     * 根据维护级别id查询内置模板
     *
     * @param maintenanceLevelId 维护级别id
     * @return 内置模板id和名称
     */
    List<IdNameEntry> findByMaintenanceLevelId(@Param("maintenanceLevelId") String maintenanceLevelId);

    /**
     * 根据类别查内置模板
     *
     * @param equipmentTypeId 类别
     * @return 内置模板
     */
    List<BuiltInTemplate> findByEquipmentTypeId(@Param("equipmentTypeId") String equipmentTypeId);
}
