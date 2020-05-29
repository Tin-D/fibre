package com.jy.fibre.domain;

import com.jy.fibre.enums.TemplateType;
import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 维护等级
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@LanDesc("维护等级")
public class MaintenanceLevel extends BaseDomainWithGuidKey {

    /**
     * 名称
     */
    @LanDesc(value = "名称")
    private String name;


    /**
     * 设备类型编号，关联数据字典
     */
    @LanDesc(value = "设备类型编号", isDictionaryCode = true)
    private String equipmentTypeId;

    /**
     * 级别
     */
    @LanDesc(value = "级别")
    private Integer orderNumber;

    /**
     * 模板类型
     */
    @LanDesc(value = "模板类型")
    private TemplateType templateType;
}
