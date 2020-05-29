package com.jy.fibre.dao;

import com.jy.fibre.bean.customer.CustomerQueryVO;
import com.jy.fibre.bean.customer.CustomerVO;
import com.jy.fibre.domain.Customer;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;
import com.xmgsd.lan.roadhog.mybatis.mappers.PaginationMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinGuoHua
 */
@Repository
public interface CustomerDao extends CurdMapper<Customer, String>, PaginationMapper<CustomerVO, CustomerQueryVO> {
    @Override
    List<CustomerVO> pagination(@Param("query") CustomerQueryVO query);
}
