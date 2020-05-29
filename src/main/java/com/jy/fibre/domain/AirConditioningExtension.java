package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 空调扩展表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("空调扩展")
public class AirConditioningExtension extends BaseDomainWithGuidKey {

    /**
     * 设备id
     */
    @LanDesc(value = "设备id", foreignTo = Equipment.class, guessDictionaryCode = false)
    private String equipmentId;

    /**
     * 额定功率
     */
    @LanDesc(value = "额定功率")
    private Integer ratedPower;

    /**
     * 送风方式
     */
    @LanDesc(value = "送风方式")
    private String airSupplyMode;

    /**
     * 气流组织
     */
    @LanDesc(value = "气流组织")
    private String airDistribution;

    /**
     * 操作记录
     */
    @LanDesc(value = "操作记录")
    private String operationRecord;

    /**
     * 设定温度°C
     */
    @LanDesc(value = "设定温度°C")
    private Integer settingTemperature;

    /**
     * 设定湿度%
     */
    @LanDesc(value = "设定湿度%")
    private Double settingHumidity;

    /**
     * 实际回风温度°C
     */
    @LanDesc(value = "实际回风温度°C")
    private Integer actualReturnAirTemperature;

    /**
     * 实际回风湿度%
     */
    @LanDesc(value = "实际回风湿度%")
    private Double actualReturnAirHumidity;

    /**
     * 市电输入电流A
     */
    @LanDesc(value = "市电输入电流A")
    private Double mainsInputCurrentA;

    /**
     * 压缩机运行电流A
     */
    @LanDesc(value = "压缩机运行电流A")
    private Double compressorRunningCurrentA;

    /**
     * 室内风机运行电流A
     */
    @LanDesc(value = "室内风机运行电流A")
    private Double operatingCurrentOfIndoorFanA;

    /**
     * 室外风机运行电流A
     */
    @LanDesc(value = "室外风机运行电流A")
    private Double operatingCurrentOfOutdoorFanA;
}
