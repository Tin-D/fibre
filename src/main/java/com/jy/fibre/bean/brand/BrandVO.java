package com.jy.fibre.bean.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jy.fibre.domain.Brand;
import com.xmgsd.lan.gwf.bean.JsonFileVO;
import com.xmgsd.lan.gwf.bean.attachment.AbstractFormDataWithAttachments;
import com.xmgsd.lan.roadhog.bean.BaseFormData;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
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
public class BrandVO extends BaseFormData<Brand> implements AbstractFormDataWithAttachments, DbItem {

    private String id;

    private String name;

    private Integer orderNumber;

    private String remark;

    private String equipmentTypeId;

    /**
     * 附件
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "attachments")
    private List<JsonFileVO> attachments;
}
