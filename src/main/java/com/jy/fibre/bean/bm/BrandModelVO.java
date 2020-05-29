package com.jy.fibre.bean.bm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jy.fibre.domain.BrandModel;
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
public class BrandModelVO extends BaseFormData<BrandModel> implements AbstractFormDataWithAttachments {

    private String id;

    private String name;

    private String brandId;

    private String equipmentTypeId;

    private Integer orderNumber;

    /**
     * 附件
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "attachments")
    private List<JsonFileVO> attachments;
}
