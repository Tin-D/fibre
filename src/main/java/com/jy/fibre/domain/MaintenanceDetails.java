package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.ToString;

/**
 * 维护级别巡检表中间表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@LanDesc("维护级别详情表")
public class MaintenanceDetails {

    /**
     * 维护级别id
     */
    @LanDesc(value = "维护级别id", foreignTo = MaintenanceLevel.class, guessDictionaryCode = false)
    private String maintenanceLevelId;

    /**
     * 巡检表id
     */
    @LanDesc(value = "巡检表id", foreignTo = PatrolExcel.class, guessDictionaryCode = false)
    private String patrolExcelId;

    /**
     * 内置模板id
     */
    @LanDesc(value = "内置模板id", foreignTo = BuiltInTemplate.class, guessDictionaryCode = false)
    private String builtInTemplateId;
}
