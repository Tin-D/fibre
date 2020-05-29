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
@LanDesc("机房环境")
public class ComputerRoomEnvironment extends BaseDomainWithGuidKey {

    @LanDesc(value = "任务详情编号", foreignTo = Task.class, guessDictionaryCode = false)
    private String taskId;

    @LanDesc(value = "机房编号", foreignTo = ComputerRoom.class, guessDictionaryCode = false)
    private String computerRoomId;

    @LanDesc(value = "告警声(有是/无否)")
    private String alarmSound;

    @LanDesc(value = "异味(有是/无否)")
    private String peculiarSmell;

    @LanDesc(value = "异响(有是/无否)")
    private String abnormalSound;

    @LanDesc(value = "温度(有是/无否)")
    private Double temperature;

    @LanDesc(value = "湿度(一位小数，+-50.0之间)")
    private Double humidity;

    @LanDesc(value = "漏水(有是/无否)")
    private String makeWater;

    @LanDesc(value = "老鼠(有是/无否)")
    private String mouse;

    @LanDesc(value = "杂物及易燃易爆物(有是/无否)")
    private String sundryAndInflammableExplosive;

    @LanDesc(value = "照明(有是/无否)")
    private String illumination;
}
