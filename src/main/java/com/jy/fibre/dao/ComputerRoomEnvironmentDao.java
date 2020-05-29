package com.jy.fibre.dao;

import com.jy.fibre.domain.ComputerRoomEnvironment;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author LinGuoHua
 */
@Repository
public interface ComputerRoomEnvironmentDao extends CurdMapper<ComputerRoomEnvironment, String> {
    /**
     * 根据任务id和机房id去查询记录
     *
     * @param taskId         任务id
     * @param computerRoomId 机房id
     * @return ComputerRoomEnvironment
     */
    ComputerRoomEnvironment findRecordByTaskIdAndComputerRoomId(@Param("taskId") String taskId, @Param("computerRoomId") String computerRoomId);

    ComputerRoomEnvironment findByTaskDetailsId(@Param("taskDetailsId") String taskDetailsId);
}
