package com.jy.fibre.bean.pe;

import com.jy.fibre.domain.AirConditionPatrol;
import com.jy.fibre.domain.ComputerRoomEnvironment;
import lombok.Data;
import lombok.ToString;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
public class PatrolExcelListVO {

    private AirConditionPatrol airConditionPatrol;

    private ComputerRoomEnvironment computerRoomEnvironment;
}
