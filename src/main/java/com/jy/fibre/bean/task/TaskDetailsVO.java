package com.jy.fibre.bean.task;

import com.jy.fibre.domain.TaskDetails;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author hzhou
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TaskDetailsVO extends BaseFormData<TaskDetails> implements DbItem {

    private String id;

    private String equipmentId;

    private String taskId;

    private String equipmentName;

    private String equipmentTypeId;

    private String serialNumber;

    private String computerRoomId;

    private String computerRoomName;

    private String equipmentBrandModelId;

    private String maintenanceLevelId;

    private String maintenanceLevelName;

    private String customerId;

    private String customerName;

    /**
     * 巡检表格模板
     */
    private List<IdNameEntry> patrolExcelTemplates;
}
