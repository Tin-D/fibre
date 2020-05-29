package com.jy.fibre.dao;

import com.jy.fibre.domain.TaskCode;
import com.jy.fibre.enums.TaskType;
import com.xmgsd.lan.roadhog.mybatis.mappers.CurdMapper;

public interface TaskCodeDao extends CurdMapper<TaskCode, TaskType> {
}