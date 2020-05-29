package com.jy.fibre.dao;

import com.jy.fibre.domain.MaintenanceLevel;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface MaintenanceLevelDao extends CurdMapper<MaintenanceLevel, String> {

    /**
     * 根据id查询巡检表id和文件名称
     *
     * @param id id
     * @return 巡检表id和文件名称
     */
    List<IdNameEntry> findPatrolExcelIdAndFileNameById(@Param("id") String id);

    /**
     * 根据类别查询
     *
     * @param equipmentTypeId 类别
     * @return 维护等级
     */
    List<IdNameEntry> findByTypeId(@Param("equipmentTypeId") String equipmentTypeId);
}
