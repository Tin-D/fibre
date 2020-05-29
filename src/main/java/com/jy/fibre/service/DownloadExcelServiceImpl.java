package com.jy.fibre.service;

import com.jy.fibre.bean.equipment.EquipmentVO;
import com.jy.fibre.bean.pe.AirConditionPatrolVO;
import com.jy.fibre.bean.pe.ComputerRoomEnvironmentVO;
import com.jy.fibre.core.ZipFileUtil;
import com.jy.fibre.dao.BuiltInTemplateDao;
import com.jy.fibre.domain.BuiltInTemplate;
import com.xmgsd.lan.gwf.core.generator.LanDescExcelHelper;
import com.xmgsd.lan.gwf.core.generator.LanVueField;
import com.xmgsd.lan.gwf.domain.Attachment;
import com.xmgsd.lan.gwf.domain.DictionaryCode;
import com.xmgsd.lan.gwf.service.DictionaryCodeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 下载巡检表格模板
 *
 * @author dzq
 */
@Slf4j
@Service
public class DownloadExcelServiceImpl {

    private TaskServiceImpl taskService;

    private BuiltInTemplateDao builtInTemplateDao;

    private DictionaryCodeServiceImpl dictionaryCodeService;

    @Autowired
    public DownloadExcelServiceImpl(TaskServiceImpl taskService, BuiltInTemplateDao builtInTemplateDao, DictionaryCodeServiceImpl dictionaryCodeService) {
        this.taskService = taskService;
        this.builtInTemplateDao = builtInTemplateDao;
        this.dictionaryCodeService = dictionaryCodeService;
    }


    public byte[] downloadExcel(@NotNull String taskId) throws Exception {
        List<EquipmentVO> equipmentVos = taskService.findTaskRelationEquipments(taskId);
        if (CollectionUtils.isEmpty(equipmentVos)) {
            throw new Exception("此条记录已被删除，请刷新页面");
        }
        List<String> computerRoomNames = equipmentVos.stream().map(EquipmentVO::getComputerRoomName).distinct().collect(Collectors.toList());
        Map<String, List<Attachment>> files = new HashMap<>(computerRoomNames.size());
        for (String computerRoomName : computerRoomNames) {
            String path = "";
            //路径拼接
            path = String.format("%s%s/", path, computerRoomName);


                List<EquipmentVO> insertTemplates = new ArrayList<>();

                for (EquipmentVO equipmentVo : equipmentVos) {
                    //如果是同一个机房并且为同一个设备就加入insertTemplates
                    if (equipmentVo.getComputerRoomName().equals(computerRoomName)) {
                        insertTemplates.add(equipmentVo);
                    }
                }
            files.put(path, createAndInsertMessageForExcel(insertTemplates));

        }
        return ZipFileUtil.compressAsMultipleFiles(files);
    }

    /**
     * 创建模板和插入设备资料
     *
     * @param equipmentVos 设备集合
     * @return List<Attachment>
     * @throws Exception 异常
     */
    private List<Attachment> createAndInsertMessageForExcel(List<EquipmentVO> equipmentVos) throws Exception {
        List<Attachment> files = new ArrayList<>();
        //内置模板的id集合
        List<String> repetitionPatrolExcelIds = new ArrayList<>();
        for (EquipmentVO equipmentVo : equipmentVos) {
            repetitionPatrolExcelIds.addAll(equipmentVo.getPatrolExcelIds());
        }
        List<String> patrolExcelIds = repetitionPatrolExcelIds.stream().distinct().collect(Collectors.toList());
        for (String patrolExcelId : patrolExcelIds) {
            List<EquipmentVO> sameTemplateEquipmentVo = equipmentVos.stream().filter(equipmentVO -> equipmentVO.getPatrolExcelIds().contains(patrolExcelId)).collect(Collectors.toList());
            BuiltInTemplate builtInTemplate = this.builtInTemplateDao.selectByPrimaryKey(patrolExcelId);
            byte[] resultFile = null;
            //是否为精密空调
            if (builtInTemplate.getContrastClass().equals(AirConditionPatrolVO.class.getName())) {
                byte[] file = LanDescExcelHelper.generateExcel(builtInTemplate.getName(), AirConditionPatrolVO.class, dictionaryCodeHandler, fieldFilter, constraintHandler);
                resultFile = insertMessageToExcelForAirConditionPatrolVO(file, sameTemplateEquipmentVo);
            }
            //是否为机房环境
            if (builtInTemplate.getContrastClass().equals(ComputerRoomEnvironmentVO.class.getName())) {
                byte[] file = LanDescExcelHelper.generateExcel(builtInTemplate.getName(), ComputerRoomEnvironmentVO.class, dictionaryCodeHandler, fieldFilter, constraintHandler);
                resultFile = insertMessageToExcelForComputerRoomEnvironmentVO(file, sameTemplateEquipmentVo);
            }
            Attachment attachment = new Attachment();
            if (!StringUtils.isEmpty(resultFile)) {
                attachment.setSize(resultFile.length);
            }
            attachment.setContent(resultFile);
            attachment.setName(String.format("%s%s", builtInTemplate.getName(), ".xlsx"));
            files.add(attachment);
        }
        return files;
    }

    /**
     * 精密空调模板
     *
     * @param bytes        系统生成的excel模板
     * @param equipmentVos 对应模板的设备集合
     * @return 往文件里面插入信息后的文件
     * @throws IOException io异常
     */
    private byte[] insertMessageToExcelForAirConditionPatrolVO(byte[] bytes, List<EquipmentVO> equipmentVos) throws IOException {
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
        Sheet sheet = workbook.getSheetAt(0);
        for (EquipmentVO equipmentVO : equipmentVos) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0, CellType.STRING).setCellValue(equipmentVO.getId());
            row.createCell(1, CellType.STRING).setCellValue(equipmentVO.getSerialNumber());
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 机房环境模板
     *
     * @param bytes        系统生成的excel模板
     * @param equipmentVos 对应模板的设备集合
     * @return 往文件里面插入信息后的文件
     * @throws IOException io异常
     */
    private byte[] insertMessageToExcelForComputerRoomEnvironmentVO(byte[] bytes, List<EquipmentVO> equipmentVos) throws IOException {
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
        Sheet sheet = workbook.getSheetAt(0);
        //创建最后一行row
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0, CellType.STRING).setCellValue(equipmentVos.get(0).getComputerRoomId());
        row.createCell(1, CellType.STRING).setCellValue(equipmentVos.get(0).getComputerRoomName());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    private Function<LanVueField, List<DictionaryCode>> dictionaryCodeHandler = lanVueField -> {
        // 会调用这个方法的，一定是检查到这个字段是数据字典字段
        DictionaryCode dc = this.dictionaryCodeService.getByCode(lanVueField.getDicCode());
        // 返回所有的设备类型
        return this.dictionaryCodeService.list().stream()
                .filter(i -> Objects.equals(i.getParentId(), dc.getId()))
                .collect(Collectors.toList());
    };
    private Function<Field, Boolean> fieldFilter = field -> {
        if ("id".equals(field.getName()) || "taskId".equals(field.getName()) || "tTypeToken".equals(field.getName())) {
            return false;
        }
        return true;
    };
    private Function<LanVueField, Boolean> constraintHandler = lanVueField -> {
        //是否需要判断非法值，返回boolean值
        System.out.println("-----" + lanVueField.getName());
        return true;
    };


}

