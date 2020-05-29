package com.jy.fibre.service.patrol.excel.parsers;

import com.google.common.base.Preconditions;
import com.jy.fibre.bean.pe.ComputerRoomEnvironmentVO;
import com.jy.fibre.dao.ComputerRoomEnvironmentDao;
import com.jy.fibre.dao.TaskDetailsDao;
import com.jy.fibre.domain.ComputerRoomEnvironment;
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

/**
 * @author hzhou
 * @date 2020/5/22 11:14
 */
@Service
public class ComputerRoomParser implements PatrolExcelParser<ComputerRoomEnvironmentVO> {

    private DictionaryCodeServiceImpl dictionaryCodeService;

    private ComputerRoomEnvironmentDao computerRoomEnvironmentDao;

    private TaskDetailsDao taskDetailsDao;

    public ComputerRoomParser(DictionaryCodeServiceImpl dictionaryCodeService, ComputerRoomEnvironmentDao computerRoomEnvironmentDao, TaskDetailsDao taskDetailsDao) {
        this.dictionaryCodeService = dictionaryCodeService;
        this.computerRoomEnvironmentDao = computerRoomEnvironmentDao;
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
        return attachment.getName().contains("机房环境");
    }

    /**
     * 解析Excel，多数情况下，解析出来的都应该是个列表，某些情况不是列表的，请包装成一个列表
     *
     * @param attachment 附件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StringBuffer parseExcel(@NotNull Attachment attachment, @NotNull String taskId) throws Exception {
        StringBuffer news = new StringBuffer("机房环境检查表：");
        StringBuffer record = new StringBuffer();
        List<ComputerRoomEnvironmentVO> computerRoomEnvironmentVos;
        try {
            computerRoomEnvironmentVos = LanDescExcelHelper.parseExcel(attachment.getContent(), ComputerRoomEnvironmentVO.class, ComputerRoomEnvironmentVO::new, true, resDictionaryCodeHandler, null);
        } catch (IllegalStateException ie) {
            return new StringBuffer(attachment.getName() + "中，数据格式错误，请检查后上传n");
        }
        List<IdNameEntry> computerRoomIdNames = this.taskDetailsDao.findComputerRoomNameByTaskId(taskId);
        for (IdNameEntry idNameEntry : computerRoomIdNames) {
            for (ComputerRoomEnvironmentVO computerRoomEnvironmentVo : computerRoomEnvironmentVos) {
                if (idNameEntry.getId().equals(computerRoomEnvironmentVo.getComputerRoomId())) {
                    record.append(idNameEntry.getName());
                    record.append(";");
                    ComputerRoomEnvironment oldComputerRoomEnvironment = haveComputerRoomEnvironmentRecord(taskId, idNameEntry.getId());
                    computerRoomEnvironmentVo.setTaskId(taskId);
                    ComputerRoomEnvironment computerRoomEnvironment = computerRoomEnvironmentVo.toDbInsertItem();

                    //当数据库无记录时插入
                    if (StringUtils.isEmpty(oldComputerRoomEnvironment)) {
                        computerRoomEnvironmentDao.insert(computerRoomEnvironment);
                    } else {
                        //有则修改
                        computerRoomEnvironment.setId(oldComputerRoomEnvironment.getId());
                        computerRoomEnvironmentDao.updateByPrimaryKey(computerRoomEnvironment);
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

    private ComputerRoomEnvironment haveComputerRoomEnvironmentRecord(@NotNull String taskId, @NotNull String computerRoomId) {
        return computerRoomEnvironmentDao.findRecordByTaskIdAndComputerRoomId(taskId, computerRoomId);
    }
}
