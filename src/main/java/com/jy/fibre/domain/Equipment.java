package com.jy.fibre.domain;

import com.jy.fibre.enums.EquipmentState;
import com.xmgsd.lan.roadhog.mybatis.BaseDomainWithGuidKey;
import com.xmgsd.lan.roadhog.mybatis.LanDesc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Date;

/**
 * 设备表
 *
 * @author LinGuoHua
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@LanDesc("设备")
public class Equipment extends BaseDomainWithGuidKey {

    /**
     * 名称
     */
    @LanDesc(value = "名称")
    private String name;

    /**
     * 型号编号
     */
    @LanDesc(value = "型号编号", foreignTo = BrandModel.class, guessDictionaryCode = false)
    private String brandModelId;

    /**
     * 机房编号
     */
    @LanDesc(value = "机房编号", foreignTo = ComputerRoom.class, guessDictionaryCode = false)
    private String computerRoomId;

    /**
     * 序列号
     */
    @LanDesc(value = "序列号")
    private String serialNumber;


    /**
     * 生产日期
     */
    @LanDesc(value = "生产日期")
    private Date manufactureDate;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    @LanDesc(value = "创建时间")
    private Date createTime;


    /**
     * 收货日期
     */
    @LanDesc(value = "收货日期")
    private Date deliveryDate;

    /**
     * 安装日期
     */
    @LanDesc(value = "安装日期")
    private Date installDate;

    /**
     * 启用日期
     */
    @LanDesc(value = "启用日期")
    private String startDate;

    /**
     * 是否故障
     */
    @LanDesc(value = "是否故障")
    private EquipmentState state;

    /**
     * 停用日期
     */
    @LanDesc(value = "停用日期")
    private String stopUsingTime;

    /**
     * 报废日期
     */
    @LanDesc(value = "报废日期")
    private Date scrapTime;

    /**
     * 取得价值
     */
    @LanDesc(value = "取得价值")
    private String value;

    /**
     * 扩展信息
     */
    @LanDesc(value = "扩展信息")
    private String configs;

    public Equipment() {
        this.createTime = new Date();
    }
}
