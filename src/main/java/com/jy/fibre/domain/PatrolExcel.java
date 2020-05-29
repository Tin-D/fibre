package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 巡检表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("巡检")
public class PatrolExcel extends BaseDomainWithGuidKey {

    /**
     * 设备类型编号，关联数据字典
     */
    @LanDesc(value = "设备类型编号", isDictionaryCode = true)
    private String equipmentTypeId;

    /**
     * 备注
     */
    @LanDesc(value = "备注")
    private String remark;

}
