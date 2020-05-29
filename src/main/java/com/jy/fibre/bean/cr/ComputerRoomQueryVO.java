package com.jy.fibre.bean.cr;

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
public class ComputerRoomQueryVO extends BasePaginationQuery {

    private String name;

    private String address;

    private String contactsName;

    private Integer contactsPhone;

    private String customerId;

    private String customerName;

    private Integer orderNumber;
}
