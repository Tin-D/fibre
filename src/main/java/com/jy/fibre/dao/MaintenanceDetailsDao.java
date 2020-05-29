package com.jy.fibre.dao;

import com.jy.fibre.domain.MaintenanceDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author LinGuoHua
 */
@Repository
public interface MaintenanceDetailsDao {
    int insert(@Param("md") MaintenanceDetails maintenanceDetails);

    void deleteByPatrolExcelId(@Param("patrolExcelId") String patrolExcelId);

    void deleteByBuiltInTemplateId(@Param("builtInTemplateId") String builtInTemplateId, @Param("maintenanceLevelId") String maintenanceLevelId);
}
