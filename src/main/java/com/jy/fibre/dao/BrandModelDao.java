package com.jy.fibre.dao;

import com.jy.fibre.domain.BrandModel;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author LinGuoHua
 */
@Repository
public interface BrandModelDao extends CurdMapper<BrandModel, String> {
    /**
     * 根据品牌id查改品牌下的型号数量
     *
     * @param brandId 品牌id
     * @return 查询到的数量
     */
    int theNumberOfFindByBrandId(@Param("brandId") String brandId);

}
