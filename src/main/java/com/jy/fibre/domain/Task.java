package com.jy.fibre.domain;

import com.jy.fibre.enums.TaskType;
import com.xmgsd.lan.gwf.domain.BaseDomainWithCreateUserAndTime;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Date;

/**
 * 任务表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("任务")
public class Task extends BaseDomainWithCreateUserAndTime {
    /**
     * 名称
     */
    @LanDesc(value = "名称")
    private String name;

    /**
     * 任务编号
     */
    @Column(updatable = false)
    @LanDesc(value = "任务编号")
    private String code;

    /**
     * 序列号
     */
    @LanDesc(value = "序列号")
    private String serialNumber;

    /**
     * 完成用户id
     */
    @LanDesc(value = "完成用户id", guessDictionaryCode = false)
    private String finishUserId;

    /**
     * 完成用户账号
     */
    @LanDesc(value = "完成用户账号")
    private String finishUserName;

    /**
     * 完成用户名称
     */
    @LanDesc(value = "完成用户名称")
    private String finishUserFullName;

    /**
     * 完成时间
     */
    @LanDesc(value = "完成时间")
    private Date finishTime;

    /**
     * 是否完成
     */
    @LanDesc(value = "是否完成")
    private Boolean finish;

    /**
     * 任务类型
     */
    @LanDesc(value = "任务类型")
    private TaskType taskType;

    /**
     * 备注
     */
    @LanDesc(value = "备注")
    private String remark;

}
