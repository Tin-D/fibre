package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 机房表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("机房")
public class ComputerRoom extends BaseDomainWithGuidKey {

    /**
     * 名称
     */
    @LanDesc(value = "名称")
    private String name;

    /**
     * 地址
     */
    @LanDesc(value = "地址")
    private String address;


    /**
     * 联系人名称
     */
    @LanDesc(value = "联系人名称")
    private String contactsName;

    /**
     * 联系人电话
     */
    @LanDesc(value = "联系人电话")
    private String contactsPhone;

    /**
     * 客户编号
     */
    @LanDesc(value = "客户编号", foreignTo = Customer.class, guessDictionaryCode = false)
    private String customerId;

    /**
     * 排序
     */
    @LanDesc(value = "排序")
    private Integer orderNumber;
}
