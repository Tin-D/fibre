package com.jy.fibre.dao;

import com.jy.fibre.domain.TypeKeyPoint;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface TypeKeyPointDao extends CurdMapper<TypeKeyPoint, String> {

    /**
     * 根据类别查关键点
     *
     * @param typeId 类别id
     * @return 关键点
     */
    List<TypeKeyPoint> findByTypeId(@Param("typeId") String typeId);
}
