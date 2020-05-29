package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 客户表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("客户")
public class Customer extends BaseDomainWithGuidKey {

    /**
     * 名称
     */
    @LanDesc(value = "名称")
    private String name;

    /**
     * 负责人名称
     */
    @LanDesc(value = "负责人名称")
    private String chargeUserFullName;


    /**
     * 负责人邮箱
     */
    @LanDesc(value = "负责人邮箱")
    private String chargeUserEmail;

    /**
     * 负责人电话
     */
    @LanDesc(value = "负责人电话")
    private String chargeUserPhone;
}
