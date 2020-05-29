package com.jy.fibre.dao;

import com.jy.fibre.bean.brand.BrandVO;
import com.jy.fibre.domain.Brand;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface BrandDao extends CurdMapper<Brand, String> {

    List<BrandVO> selectAllAndTypeId();

    String findTypeIdById(@Param("id") String id);
}
