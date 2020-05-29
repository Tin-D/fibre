package com.jy.fibre.bean.pe;

import com.jy.fibre.domain.ComputerRoomEnvironment;
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
public class ComputerRoomEnvironmentVO extends BaseFormData<ComputerRoomEnvironment> implements PatrolExcelData {

    private String id;

    private String taskId;

    @LanDesc(value = "机房编号(请勿修改)", guessDictionaryCode = false)
    private String computerRoomId;
    @LanDesc(value = "机房名称")
    private String computerRoomName;

    @LanDesc(value = "告警声")
    private String alarmSound;

    @LanDesc(value = "异味")
    private String peculiarSmell;

    @LanDesc(value = "异响")
    private String abnormalSound;

    @LanDesc(value = "温度(℃)带单位填数字")
    private Double temperature;

    @LanDesc(value = "湿度(%)一位小数，+-50.0之间")
    private Double humidity;

    @LanDesc(value = "漏水")
    private String makeWater;

    @LanDesc(value = "老鼠")
    private String mouse;

    @LanDesc(value = "杂物及易燃易爆物")
    private String sundryAndInflammableExplosive;

    @LanDesc(value = "照明")
    private String illumination;
}
