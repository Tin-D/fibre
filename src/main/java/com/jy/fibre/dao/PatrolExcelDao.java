package com.jy.fibre.dao;

import com.jy.fibre.bean.pe.PatrolExcelVO;
import com.jy.fibre.domain.PatrolExcel;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface PatrolExcelDao extends CurdMapper<PatrolExcel, String> {

    /**
     * 查询所有巡检表附带附件名称
     *
     * @return 巡检表
     */
    List<PatrolExcelVO> findAll();


    /**
     * 根据类别Id查询对应的巡检表id和文件名称
     *
     * @param typeId 类别id
     * @return 对应的巡检表id和文件名称
     */
    List<IdNameEntry> findByTypeId(@Param("typeId") String typeId);

    List<PatrolExcelVO> findByEquipmentTypeId(@Param("equipmentTypeId") String equipmentTypeId);
}
