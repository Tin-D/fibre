package com.jy.fibre.bean.ml;

import com.jy.fibre.domain.MaintenanceLevel;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MaintenanceLevelVO extends BaseFormData<MaintenanceLevel> {

    private String id;

    private String name;

    private String equipmentTypeId;

    private Integer orderNumber;

    /**
     * 巡检表id和文件名称
     */
    private List<IdNameEntry> patrolExcelIdAndFileName;

    /**
     * 内置模板id和名称
     */
    private List<IdNameEntry> builtInTemplateIdAndName;
}
