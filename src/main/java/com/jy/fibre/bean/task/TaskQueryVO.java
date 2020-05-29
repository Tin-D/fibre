package com.jy.fibre.bean.task;

import com.xmgsd.lan.roadhog.bean.BasePaginationQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TaskQueryVO extends BasePaginationQuery {

    private String name;

    private String code;

    private String createUserFullName;

    private String finishUserFullName;

    private Boolean finish;

    private String taskType;

    private Date[] searchTime;

    private String dateField;
}
