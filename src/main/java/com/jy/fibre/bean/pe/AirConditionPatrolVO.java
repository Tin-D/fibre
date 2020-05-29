package com.jy.fibre.bean.pe;

import com.jy.fibre.domain.AirConditionPatrol;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author dzq
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("制冷系统巡检模板")
public class AirConditionPatrolVO extends BaseFormData<AirConditionPatrol> implements PatrolExcelData {

    private String id;

    @LanDesc(value = "设备编号(请勿修改)", guessDictionaryCode = false)
    private String equipmentId;

    @LanDesc(value = "序列号")
    private String serialNumber;

    @LanDesc(value = "温度(℃)带单位填数字")
    private Double temperature;

    @LanDesc(value = "温度设定值(℃)")
    private Double temperatureSetting;

    @LanDesc(value = "温度读取值(℃)")
    private Double temperatureReadingValue;

    @LanDesc(value = "温度回风口实测值(℃)")
    private Double temperatureMeasurementOfAirReturnPort;

    @LanDesc(value = "温度出风口实测值(℃)")
    private Double temperatureMeasurementOfAirOutlet;

    @LanDesc(value = "标识标签(正常/损坏)")
    private String logoLabel;

    @LanDesc(value = "湿度设定值(%)")
    private Double humiditySetting;

    @LanDesc(value = "湿度读取值(%)")
    private Double humidityReadingValue;

    @LanDesc(value = "A相主机输入电流(A)")
    private Double mainEngineInputCurrentOfA;

    @LanDesc(value = "B相主机输入电流(A)")
    private Double mainEngineInputCurrentOfB;

    @LanDesc(value = "C相主机输入电流(A)")
    private Double mainEngineInputCurrentOfC;
}
