package com.jy.fibre.dao;

import com.jy.fibre.bean.cr.ComputerRoomQueryVO;
import com.jy.fibre.bean.cr.ComputerRoomVO;
import com.jy.fibre.domain.ComputerRoom;
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
public interface ComputerRoomDao extends CurdMapper<ComputerRoom, String>, PaginationWithoutDuplicateMapper<ComputerRoomVO, String, ComputerRoomQueryVO> {
    @Override
    List<String> paginationIds(@Param("query") ComputerRoomQueryVO query);

    @Override
    List<ComputerRoomVO> findPaginationResultByIds(@Param("ids") Collection<String> ids, @Param("query") ComputerRoomQueryVO query);

    @Override
    default Function<ComputerRoomVO, String> idGetter() {
        return ComputerRoomVO::getId;
    }

    /**
     * 根据客户id查询改客户下的机房个数
     *
     * @param customerId 客户id
     * @return 机房个数
     */
    int theNumberOfFindByCustomerId(@Param("customerId") String customerId);

}
