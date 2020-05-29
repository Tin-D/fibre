package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.ToString;

/**
 * 类型品牌中间表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@LanDesc("类型品牌中间表")
public class BrandDetails {

    /**
     * 类型编号，关联数据字典
     */
    @LanDesc(value = "类型编号", isDictionaryCode = true)
    private String equipmentTypeId;

    /**
     * 品牌编号，关联品牌表
     */
    @LanDesc(value = "品牌编号", foreignTo = Brand.class, guessDictionaryCode = false)
    private String brandId;

}
