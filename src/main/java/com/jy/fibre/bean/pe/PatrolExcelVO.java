package com.jy.fibre.bean.pe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jy.fibre.domain.PatrolExcel;
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
public class PatrolExcelVO extends BaseFormData<PatrolExcel> implements AbstractFormDataWithAttachments {

    private String id;

    private String equipmentTypeId;

    private String remark;

    private String fileName;

    /**
     * 附件
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "attachments")
    private List<JsonFileVO> attachments;
}
