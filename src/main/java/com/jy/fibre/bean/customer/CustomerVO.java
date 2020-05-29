package com.jy.fibre.bean.customer;

import com.jy.fibre.domain.Customer;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomerVO extends BaseFormData<Customer> {

    private String id;

    private String name;

    private String chargeUserFullName;

    private String chargeUserEmail;

    private String chargeUserPhone;
}
