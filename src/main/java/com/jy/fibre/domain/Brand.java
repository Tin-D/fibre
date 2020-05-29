package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 品牌表
 *
 * @author dzq
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("品牌")
public class Brand extends BaseDomainWithGuidKey {

    /**
     * 品牌名称
     */
    @LanDesc(value = "品牌名称")
    private String name;

    /**
     * 排序
     */
    @LanDesc(value = "排序")
    private Integer orderNumber;

    /**
     * 备注
     */
    @LanDesc(value = "备注")
    private String remark;
}
