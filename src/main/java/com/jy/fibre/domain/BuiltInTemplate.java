package com.jy.fibre.domain;

import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author dzq
 * 内置模板表
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@LanDesc("内置模板")
public class BuiltInTemplate extends BaseDomainWithGuidKey {

    @LanDesc(value = "名称")
    private String name;

    @LanDesc(value = "对照表名称")
    private String contrastClass;

    @LanDesc(value = "设备类型编号", isDictionaryCode = true)
    private String equipmentTypeId;
}
