package com.jy.fibre.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.jy.fibre.bean.equipment.EquipmentVO;
import com.jy.fibre.bean.pe.PatrolExcelListVO;
import com.jy.fibre.bean.task.TaskDetailsVO;
import com.jy.fibre.bean.task.TaskQueryVO;
import com.jy.fibre.bean.task.TaskVO;
import com.jy.fibre.dao.AirConditionPatrolDao;
import com.jy.fibre.dao.ComputerRoomEnvironmentDao;
import com.jy.fibre.dao.TaskDao;
import com.jy.fibre.dao.TaskDetailsDao;
import com.jy.fibre.domain.Task;
import com.jy.fibre.domain.TaskDetails;
import com.xmgsd.lan.gwf.domain.User;
import com.xmgsd.lan.gwf.service.DictionaryCodeServiceImpl;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.exception.NoEntityWithIdException;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import com.xmgsd.lan.roadhog.mybatis.mappers.BasePaginationMapper;
import com.xmgsd.lan.roadhog.mybatis.service.PaginationService;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class TaskServiceImpl extends BaseService<TaskDao> implements SimpleCurdViewService<String, TaskVO>, PaginationService<TaskQueryVO, TaskVO> {

    private final TaskDetailsDao taskDetailsDao;

    private final TaskCodeServiceImpl taskCodeService;

    private final DictionaryCodeServiceImpl dictionaryCodeService;

    private final AirConditionPatrolDao airConditionPatrolDao;

    private final ComputerRoomEnvironmentDao computerRoomEnvironmentDao;

    public TaskServiceImpl(TaskDetailsDao taskDetailsDao, TaskCodeServiceImpl taskCodeService, DictionaryCodeServiceImpl dictionaryCodeService, AirConditionPatrolDao airConditionPatrolDao, ComputerRoomEnvironmentDao computerRoomEnvironmentDao) {
        this.taskDetailsDao = taskDetailsDao;
        this.taskCodeService = taskCodeService;
        this.dictionaryCodeService = dictionaryCodeService;
        this.airConditionPatrolDao = airConditionPatrolDao;
        this.computerRoomEnvironmentDao = computerRoomEnvironmentDao;
    }

    @Override
    public BasePaginationMapper<TaskVO> getPaginationMapper() {
        return this.getMapper();
    }

    @Override
    public TaskVO add(TaskVO item) throws Exception {
        Task task = item.toDbInsertItem();
        task.setCode(taskCodeService.generateCode(item.getTaskType(), false));
        task.setFinish(false);
        this.getMapper().insert(task);
        item.setId(task.getId());
        afterAdd(item, task);
        return this.get(item.getId());
    }


    @Override
    public void afterAdd(@NotNull TaskVO item, @NotNull DbItem recorder) throws Exception {
        Task task = (Task) recorder;
        for (TaskDetailsVO taskDetailsVO : item.getTaskDetailsVOS()) {
            taskDetailsVO.setTaskId(task.getId());
            taskDetailsDao.insert(taskDetailsVO.toDbInsertItem());
        }
    }

    @Override
    public void afterUpdate(TaskVO item, @NotNull DbItem recorder) throws Exception {
        Task task = (Task) recorder;

        List<TaskDetails> taskDetails = this.taskDetailsDao.findByTaskId(task.getId());
        Set<String> newEquipmentIds = item.getTaskDetailsVOS().stream().map(TaskDetailsVO::getEquipmentId).collect(Collectors.toSet());
        Set<String> oldEquipmentIds = taskDetails.stream().map(TaskDetails::getEquipmentId).collect(Collectors.toSet());

        Sets.SetView<String> deleteEquipmentIds = Sets.difference(oldEquipmentIds, newEquipmentIds);

        for (TaskDetailsVO taskDetailsVO : item.getTaskDetailsVOS()) {
            if (Strings.isNullOrEmpty(taskDetailsVO.getId())) {
                List<TaskDetails> oldEquipment = taskDetails.stream().filter(s -> Objects.equals(s.getEquipmentId(), taskDetailsVO.getEquipmentId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(oldEquipment)) {
                    this.taskDetailsDao.insert(taskDetailsVO.toDbInsertItem());
                } else {
                    taskDetailsVO.setId(oldEquipment.get(0).getId());
                    taskDetailsDao.updateByPrimaryKey(taskDetailsVO.toDbUpdateItem());
                }
            }
        }
        if (!deleteEquipmentIds.isEmpty()) {
            this.taskDetailsDao.deleteByTaskIdAndEquipmentIds(task.getId(), deleteEquipmentIds);
        }
    }


    @Nullable
    @Override
    public TaskVO get(@NotNull String id) {
        return this.getMapper().findPaginationResultByIds(ImmutableList.of(id), null).get(0);
    }

    public List<TaskDetailsVO> findTaskDetails(@NotNull String taskId) {
        return taskDetailsDao.findTaskDetailsWithEquipmentInfoAndPatrolExcelTemplateInfo(taskId);
    }

    /**
     * 根据任务id和设备id查询改任务下的设备和设备所包含的模板表格
     *
     * @param taskId 任务id
     * @return EquipmentVOs
     */
    public List<EquipmentVO> findTaskRelationEquipments(@NotNull String taskId) {
        List<TaskDetailsVO> taskDetailsVo = findTaskDetails(taskId);
        return taskDetailsVo.stream().map(t -> {
            EquipmentVO equipmentVO = new EquipmentVO();
            equipmentVO.setId(t.getEquipmentId());
            equipmentVO.setName(t.getEquipmentName());
            equipmentVO.setMaintenanceLevelId(t.getMaintenanceLevelId());
            equipmentVO.setComputerRoomId(t.getComputerRoomId());
            equipmentVO.setSerialNumber(t.getSerialNumber());
            equipmentVO.setComputerRoomName(t.getComputerRoomName());
            equipmentVO.setTypeCode(dictionaryCodeService.getOrError(t.getEquipmentTypeId()).getCode());
            equipmentVO.setPatrolExcelIds(t.getPatrolExcelTemplates().stream().map(IdNameEntry::getId).collect(Collectors.toList()));
            return equipmentVO;
        }).collect(Collectors.toList());
    }

    /**
     * 录入此任务的巡检表
     *
     * @param patrolExcelListVO 所有巡检表
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertPatrolExcel(@NotNull PatrolExcelListVO patrolExcelListVO) {
        if (!StringUtils.isEmpty(patrolExcelListVO.getAirConditionPatrol())) {
            if (Strings.isNullOrEmpty(patrolExcelListVO.getAirConditionPatrol().getId())) {
                airConditionPatrolDao.insert(patrolExcelListVO.getAirConditionPatrol());
            } else {
                airConditionPatrolDao.updateByPrimaryKey(patrolExcelListVO.getAirConditionPatrol());
            }
        }

        if (!StringUtils.isEmpty(patrolExcelListVO.getComputerRoomEnvironment())) {
            if (Strings.isNullOrEmpty(patrolExcelListVO.getComputerRoomEnvironment().getId())) {
                computerRoomEnvironmentDao.insert(patrolExcelListVO.getComputerRoomEnvironment());
            } else {
                computerRoomEnvironmentDao.updateByPrimaryKey(patrolExcelListVO.getComputerRoomEnvironment());
            }
        }
    }

    /**
     * 根据任务编号查询任务详情
     *
     * @param code 任务编号
     * @return 任务详情
     */
    public TaskVO findTaskDetailsByCode(@NotNull String code) {
        TaskVO taskVO = this.getMapper().getByCode(code);
        if (taskVO == null) {
            throw new NullPointerException("该任务编号不存在");
        }
        taskVO.setTaskDetailsVOS(this.findTaskDetails(taskVO.getId()));
        return taskVO;
    }


    @Override
    public void processQuery(TaskQueryVO query) {
        if ("computer_room_name".equals(query.getSortField())) {
            query.setSortField("cr.name");
        }
        if ("name".equals(query.getSortField())) {
            query.setSortField("t.name");
        }
    }

    /**
     * 根据任务详情id查询巡检表
     *
     * @param taskDetailsId 详情id
     * @return 巡检表
     */
    public PatrolExcelListVO findPatrolExcelByTaskDetailsId(String taskId, String taskDetailsId, String computerRoomId) {
        PatrolExcelListVO patrolExcelListVO = new PatrolExcelListVO();
        patrolExcelListVO.setAirConditionPatrol(airConditionPatrolDao.findRecordByTaskDetailsId(taskDetailsId));
        patrolExcelListVO.setComputerRoomEnvironment(computerRoomEnvironmentDao.findRecordByTaskIdAndComputerRoomId(taskId, computerRoomId));
        return patrolExcelListVO;
    }

    /**
     * 更新任务状态为完成
     *
     * @param taskId 任务id
     * @throws NullPointerException 任务不存在 传入id有误
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishTask(@NotNull String taskId, @NotNull User user) {
        Task task = Preconditions.checkNotNull(this.getMapper().selectByPrimaryKey(taskId), new NoEntityWithIdException(taskId).getMessage());
        task.setFinish(true);
        task.setFinishTime(new Date());
        task.setFinishUserId(user.getId());
        task.setFinishUserName(user.getUsername());
        task.setFinishUserFullName(user.getFullName());
        this.getMapper().updateByPrimaryKey(task);
    }
}
