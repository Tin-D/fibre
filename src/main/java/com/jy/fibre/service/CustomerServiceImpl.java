package com.jy.fibre.service;

import com.jy.fibre.bean.customer.CustomerQueryVO;
import com.jy.fibre.bean.customer.CustomerVO;
import com.jy.fibre.dao.ComputerRoomDao;
import com.jy.fibre.dao.CustomerDao;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.mappers.BasePaginationMapper;
import com.xmgsd.lan.roadhog.mybatis.service.PaginationService;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class CustomerServiceImpl extends BaseService<CustomerDao> implements SimpleCurdViewService<String, CustomerVO>, PaginationService<CustomerQueryVO, CustomerVO> {

    private ComputerRoomDao computerRoomDao;

    public CustomerServiceImpl(ComputerRoomDao computerRoomDao) {
        this.computerRoomDao = computerRoomDao;
    }

    @Override
    public BasePaginationMapper<CustomerVO> getPaginationMapper() {
        return this.getMapper();
    }

    @Override
    public void remove(@NotNull String id) throws Exception {
        if (computerRoomDao.theNumberOfFindByCustomerId(id) > 0) {
            throw new Exception("请先删除该客户下的机房。");
        } else {
            this.getMapper().deleteByPrimaryKey(id);
        }
    }
}
