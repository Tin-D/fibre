package com.jy.fibre.bean.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jy.fibre.domain.Task;
import com.jy.fibre.enums.TaskType;
import com.xmgsd.lan.gwf.bean.AbstractFormDataWithCreateUserAndTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TaskVO extends AbstractFormDataWithCreateUserAndTime<Task> {
    private String id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private String serialNumber;

    private String finishUserId;

    private String finishUserName;

    private String finishUserFullName;

    private Date finishTime;

    private Boolean finish;

    private TaskType taskType;

    private String computerRoomName;

    private String customerName;

    private String remark;

    private List<TaskDetailsVO> taskDetailsVOS;

    /**
     * 已选中的设备及该设备对应的excel模板编号
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Map<String, List<String>> equipmentExcelTemplates;


}
