package com.jy.fibre.service.patrol.excel.parsers;

import com.google.common.base.Preconditions;
import com.jy.fibre.bean.pe.AirConditionPatrolVO;
import com.jy.fibre.dao.AirConditionPatrolDao;
import com.jy.fibre.dao.TaskDetailsDao;
import com.jy.fibre.domain.AirConditionPatrol;
import com.xmgsd.lan.gwf.core.generator.LanDescExcelHelper;
import com.xmgsd.lan.gwf.core.generator.LanVueField;
import com.xmgsd.lan.gwf.domain.Attachment;
import com.xmgsd.lan.gwf.domain.DictionaryCode;
import com.xmgsd.lan.gwf.service.DictionaryCodeServiceImpl;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import org.apache.poi.ss.usermodel.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author hzhou
 * @date 2020/5/22 11:14
 */
@Service
public class AirConditionParser implements PatrolExcelParser<AirConditionPatrolVO> {

    private DictionaryCodeServiceImpl dictionaryCodeService;

    private AirConditionPatrolDao airConditionPatrolDao;

    private TaskDetailsDao taskDetailsDao;

    public AirConditionParser(DictionaryCodeServiceImpl dictionaryCodeService, AirConditionPatrolDao airConditionPatrolDao, TaskDetailsDao taskDetailsDao) {
        this.dictionaryCodeService = dictionaryCodeService;
        this.airConditionPatrolDao = airConditionPatrolDao;
        this.taskDetailsDao = taskDetailsDao;
    }

    /**
     * 匹配附件
     *
     * @param attachment 附件
     * @return true: 当前附件可以匹配这个解析器，false：不匹配这个解析器
     */
    @Override
    public boolean fitExcelFile(@NotNull Attachment attachment) {
        return attachment.getName().contains("精密空调");
    }

    /**
     * 解析Excel，多数情况下，解析出来的都应该是个列表，某些情况不是列表的，请包装成一个列表
     *
     * @param attachment 附件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StringBuffer parseExcel(@NotNull Attachment attachment, @NotNull String taskId) throws Exception {
        // todo: 加上解析Excel的逻辑
        StringBuffer news = new StringBuffer("精密空调检查表:");
        StringBuffer record = new StringBuffer();
        List<AirConditionPatrolVO> airConditionPatrolVos;
        try {
            airConditionPatrolVos = LanDescExcelHelper.parseExcel(attachment.getContent(), AirConditionPatrolVO.class, AirConditionPatrolVO::new, true, resDictionaryCodeHandler, null);
        } catch (IllegalStateException ie) {
            return new StringBuffer(attachment.getName() + "中，数据格式错误，请检查后上传n");
        }
        List<IdNameEntry> equipmentIdNames = this.taskDetailsDao.findTaskDetailsWithEquipmentInfoAndPatrolExcelTemplateInfo(taskId).stream().map(taskDetailsVO -> {
            IdNameEntry idNameEntry = new IdNameEntry();
            idNameEntry.setId(taskDetailsVO.getEquipmentId());
            idNameEntry.setName(taskDetailsVO.getEquipmentName());
            return idNameEntry;
        }).collect(Collectors.toList());
        for (IdNameEntry idNameEntry : equipmentIdNames) {
            for (AirConditionPatrolVO airConditionPatrolVo : airConditionPatrolVos) {
                if (idNameEntry.getId().equals(airConditionPatrolVo.getEquipmentId())) {
                    record.append(idNameEntry.getName());
                    record.append(";");
                    String equipmentTaskDetailId = taskDetailsDao.findByEquipmentIdAndTaskId(airConditionPatrolVo.getEquipmentId(), taskId).getId();
                    AirConditionPatrol oldAirConditionPatrol = haveAirConditionPatrolRecord(equipmentTaskDetailId);
                    AirConditionPatrol airConditionPatrol;
                    airConditionPatrol = airConditionPatrolVo.toDbInsertItem();
                    airConditionPatrol.setTaskDetailsId(equipmentTaskDetailId);
                    //当数据库无记录时插入
                    if (StringUtils.isEmpty(oldAirConditionPatrol)) {
                        airConditionPatrolDao.insert(airConditionPatrol);
                    } else {
                        //有则修改
                        airConditionPatrol.setId(oldAirConditionPatrol.getId());
                        airConditionPatrolDao.updateByPrimaryKey(airConditionPatrol);
                    }

                }
            }
        }
        if (record.length() == 0) {
            record.append("该表无对应设备记录");
        }
        news.append(record);
        return news.append("n");
    }

    private BiFunction<LanVueField, Cell, String> resDictionaryCodeHandler = (lanVueField, cell) -> {
        Optional<DictionaryCode> result = Preconditions.checkNotNull(this.dictionaryCodeService.list().stream().filter(dictionaryCode -> Objects.equals(dictionaryCode.getName(), cell.getStringCellValue())).findFirst(), "找不到相同code");
        assert result.isPresent();
        return result.get().getId();
    };

    private AirConditionPatrol haveAirConditionPatrolRecord(@NotNull String taskDetailId) {
        return airConditionPatrolDao.findRecordByTaskDetailsId(taskDetailId);
    }
}
