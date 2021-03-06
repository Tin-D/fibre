package com.jy.fibre.bean.customer;

import com.xmgsd.lan.roadhog.bean.BasePaginationQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomerQueryVO extends BasePaginationQuery {

    private String name;

    private String chargeUserFullName;

    private String chargeUserEmail;

    private String chargeUserPhone;
}
