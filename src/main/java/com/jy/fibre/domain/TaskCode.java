package com.jy.fibre.domain;

import com.jy.fibre.enums.TaskType;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;

@Data
@ToString(callSuper = true)
public class TaskCode implements DbItem {
    /**
     * 任务编号
     */
    @Id
    @LanDesc(value = "任务编号")
    private TaskType taskType;

    @LanDesc(value = "下一个数字")
    private String nextNumber;
}

