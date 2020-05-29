package com.jy.fibre.bean.equipment;

import com.xmgsd.lan.roadhog.bean.BasePaginationQuery;
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
public class EquipmentQueryVO extends BasePaginationQuery {

    private String name;

    private String brandModelId;

    private String equipmentTypeId;

    private List<String> computerRoomIds;

    private String serialNumber;

    private String state;

    private Date[] searchTime;

    private String dateField;

}
