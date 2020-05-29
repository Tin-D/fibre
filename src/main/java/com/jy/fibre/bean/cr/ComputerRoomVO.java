package com.jy.fibre.bean.cr;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jy.fibre.domain.ComputerRoom;
import com.xmgsd.lan.gwf.bean.JsonFileVO;
import com.xmgsd.lan.gwf.bean.attachment.AbstractFormDataWithAttachments;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComputerRoomVO extends BaseFormData<ComputerRoom> implements AbstractFormDataWithAttachments {

    private String id;

    private String name;

    private String address;

    private String contactsName;

    private String contactsPhone;

    private String customerId;

    private String customerName;

    private Integer orderNumber;

    /**
     * 附件
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "attachments")
    private List<JsonFileVO> attachments;
}
