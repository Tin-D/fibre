package com.jy.fibre.controller;

import com.jy.fibre.bean.pe.PatrolExcelVO;
import com.jy.fibre.service.PatrolExcelServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "巡检表管理")
@RestController
@RequestMapping("/patrol_excel")
public class PatrolExcelController extends AbstractAuditCurdController<PatrolExcelServiceImpl> {

    @GetMapping("/find_by_equipment_type_id/{equipmentTypeId}")
    public List<PatrolExcelVO> findByEquipmentTypeId(@PathVariable String equipmentTypeId) {
        return this.getService().findByEquipmentTypeId(equipmentTypeId);
    }
}
