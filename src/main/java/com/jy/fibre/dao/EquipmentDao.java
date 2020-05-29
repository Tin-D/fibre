package com.jy.fibre.dao;

import com.jy.fibre.bean.equipment.EquipmentQueryVO;
import com.jy.fibre.bean.equipment.EquipmentVO;
import com.jy.fibre.domain.Equipment;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import com.xmgsd.lan.roadhog.mybatis.mappers.PaginationWithoutDuplicateMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author LinGuoHua
 */
@Repository
public interface EquipmentDao extends CurdMapper<Equipment, String>, PaginationWithoutDuplicateMapper<EquipmentVO, String, EquipmentQueryVO> {
    @Override
    List<String> paginationIds(@Param("query") EquipmentQueryVO query);

    @Override
    List<EquipmentVO> findPaginationResultByIds(@Param("ids") Collection<String> ids, @Param("query") EquipmentQueryVO query);

    @Override
    default Function<EquipmentVO, String> idGetter() {
        return EquipmentVO::getId;
    }

    /**
     * 根据型号id查询设备数量
     *
     * @param brandModelId 型号id
     * @return 设备个数
     */
    int theNumberOfFindByBrandModelId(@Param("brandModelId") String brandModelId);

    /**
     * 根据机房id查询设备数量
     *
     * @param computerRoomId 机房id
     * @return 设备个数
     */
    int theNumberOfFindByComputerRoomId(@Param("computerRoomId") String computerRoomId);

    /**
     * 根据code查询文件名称和巡检表id
     *
     * @param code code
     * @return 文件名称和巡检表id
     */
    List<IdNameEntry> findPatrolExcelIdAndFileName(@Param("code") String code);

    /**
     * 根据id查询设备以及类别
     *
     * @param id 设备id
     * @return 设备id、名称以及类别code
     */
    EquipmentVO findById(@Param("id") String id);

    String findTypeById(@Param("id") String id);

    /**
     * 根据机房和类别查设备数量
     *
     * @param computerRoomId  机房
     * @param equipmentTypeId 类别
     * @return 设备数量
     */
    int theNumberOfFindByComputerRoomIdAndTypeId(@Param("computerRoomId") String computerRoomId, @Param("equipmentTypeId") String equipmentTypeId);
}
