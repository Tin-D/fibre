package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 型号表
 *
 * @author dzq
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("型号")
public class BrandModel extends BaseDomainWithGuidKey {

    /**
     * 型号名称
     */
    @LanDesc(value = "型号名称")
    private String name;

    /**
     * 品牌编号，关联品牌表
     */
    @LanDesc(value = "品牌编号",foreignTo = Brand.class,guessDictionaryCode = false)
    private String brandId;


    /**
     * 类型编号，关联数据字典
     */
    @LanDesc(value = "类型编号",isDictionaryCode = true)
    private String equipmentTypeId;

    /**
     * 排序
     */
    @LanDesc(value = "排序")
    private Integer orderNumber;

}
