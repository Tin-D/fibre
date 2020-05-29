package com.jy.fibre.dao;

import com.jy.fibre.bean.task.TaskQueryVO;
import com.jy.fibre.bean.task.TaskVO;
import com.jy.fibre.domain.Task;
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
public interface TaskDao extends CurdMapper<Task, String>, PaginationWithoutDuplicateMapper<TaskVO, String, TaskQueryVO> {
    @Override
    List<String> paginationIds(@Param("query") TaskQueryVO query);

    @Override
    List<TaskVO> findPaginationResultByIds(@Param("ids") Collection<String> ids, @Param("query") TaskQueryVO query);

    @Override
    default Function<TaskVO, String> idGetter() {
        return TaskVO::getId;
    }

    /**
     * 根据任务编号查询任务
     *
     * @param code 任务编号
     * @return 任务
     */
    TaskVO getByCode(@Param("code") String code);

}
