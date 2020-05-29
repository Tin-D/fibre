package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 任务详情表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("任务详情")
@NoArgsConstructor
public class TaskDetails extends BaseDomainWithGuidKey {

    /**
     * 办理人电话
     */
    @LanDesc(value = "办理人员名字")
    private String transactionUserName;

    /**
     * 办理人电话
     */
    @LanDesc(value = "办理人电话")
    private String transactionUserPhone;

    /**
     * 设备编号
     */
    @LanDesc(value = "设备编号", foreignTo = Equipment.class, guessDictionaryCode = false)
    private String equipmentId;

    /**
     * 任务编号
     */
    @LanDesc(value = "任务编号", foreignTo = Task.class, guessDictionaryCode = false, deleteCascade = true)
    private String taskId;

    /**
     * 维护等级编号
     */
    @LanDesc(value = "任务编号", foreignTo = MaintenanceLevel.class, guessDictionaryCode = false, deleteCascade = true)
    private String maintenanceLevelId;

    public TaskDetails(String taskId, String equipmentId) {
        this.taskId = taskId;
        this.equipmentId = equipmentId;
    }
}
