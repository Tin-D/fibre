package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
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
@LanDesc("精密空调")
public class AirConditionPatrol extends BaseDomainWithGuidKey {

    @LanDesc(value = "任务详情编号", foreignTo = TaskDetails.class, guessDictionaryCode = false)
    private String taskDetailsId;

    @LanDesc(value = "温度")
    private Double temperature;

    @LanDesc(value = "温度设定值")
    private Double temperatureSetting;

    @LanDesc(value = "温度读取值")
    private Double temperatureReadingValue;

    @LanDesc(value = "温度回风口实测值")
    private Double temperatureMeasurementOfAirReturnPort;

    @LanDesc(value = "温度出风口实测值")
    private Double temperatureMeasurementOfAirOutlet;

    @LanDesc(value = "标识标签(正常/损坏)")
    private String logoLabel;

    @LanDesc(value = "湿度设定值")
    private Double humiditySetting;

    @LanDesc(value = "湿度读取值")
    private Double humidityReadingValue;

    @LanDesc(value = "A相主机输入电流")
    private Double mainEngineInputCurrentOfA;

    @LanDesc(value = "B相主机输入电流")
    private Double mainEngineInputCurrentOfB;

    @LanDesc(value = "C相主机输入电流")
    private Double mainEngineInputCurrentOfC;
}
