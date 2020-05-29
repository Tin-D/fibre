package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("类别关键点")
public class TypeKeyPoint extends BaseDomainWithGuidKey {

    /**
     * 类型编号，关联数据字典
     */
    @LanDesc(value = "类型编号", isDictionaryCode = true)
    private String typeId;

    /**
     * 名称
     */
    @LanDesc(value = "名称")
    private String name;
}
