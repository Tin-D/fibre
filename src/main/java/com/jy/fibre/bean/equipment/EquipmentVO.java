package com.jy.fibre.bean.equipment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jy.fibre.domain.Equipment;
import com.jy.fibre.enums.EquipmentState;
import com.jy.fibre.enums.TemplateType;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EquipmentVO extends BaseFormData<Equipment> {

    private String id;

    private String name;

    private String brandModelId;

    private String computerRoomId;

    private String computerRoomName;

    private String serialNumber;

    private Date manufactureDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createTime;

    private Date deliveryDate;

    private Date installDate;

    private String startDate;

    private EquipmentState state;

    private String stopUsingTime;

    private Date scrapTime;

    private String value;

    private String typeCode;

    private String typeName;

    private String maintenanceLevelId;

    private String configs;

    private String customerName;

    private TemplateType templateType;

    /**
     * 已选中的巡检表ids/内置模板的ids
     */
    private List<String> patrolExcelIds;
}
