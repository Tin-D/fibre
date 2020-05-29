package com.jy.fibre.dao;

import com.jy.fibre.bean.task.TaskDetailsVO;
import com.jy.fibre.domain.TaskDetails;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface TaskDetailsDao extends CurdMapper<TaskDetails, String> {
    List<TaskDetails> findByTaskId(@Param("taskId") String taskId);

    default void deleteByTaskIdAndEquipmentIds(String taskId, Collection<String> equipmentIds) {
        Weekend<TaskDetails> weekend = Weekend.of(TaskDetails.class);
        weekend.weekendCriteria().andEqualTo(TaskDetails::getTaskId, taskId)
                .andIn(TaskDetails::getEquipmentId, equipmentIds);
        this.deleteByExample(weekend);
    }

    /**
     * 查询出包含设备信息
     *
     * @param taskId 任务id
     * @return 任务明细
     */
    List<TaskDetailsVO> findTaskDetailsWithEquipmentInfoAndPatrolExcelTemplateInfo(@Param("taskId") String taskId);

    TaskDetails findByEquipmentIdAndTaskId(@Param("equipmentId") String equipmentId, @Param("taskId") String taskId);

    /**
     * 根据任务id查询出taskId中的机房id和名称（无重）
     *
     * @param taskId 任务编号
     * @return IdNameEntry集合
     */
    List<IdNameEntry> findComputerRoomNameByTaskId(@Param("taskId") String taskId);
}
