package com.jy.fibre.dao;

import com.jy.fibre.domain.BrandDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author LinGuoHua
 */
@Repository
public interface BrandDetailsDao {
    int insert(@Param("bd") BrandDetails brandDetails);
}
