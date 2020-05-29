package com.jy.fibre.dao;

import com.jy.fibre.domain.AirConditionPatrol;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author LinGuoHua
 */
@Repository
public interface AirConditionPatrolDao extends CurdMapper<AirConditionPatrol, String> {
    AirConditionPatrol findRecordByTaskDetailsId(@Param("taskDetailsId") String taskDetailsId);
}
