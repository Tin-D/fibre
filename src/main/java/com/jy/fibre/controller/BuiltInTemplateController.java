package com.jy.fibre.controller;

import com.jy.fibre.domain.BuiltInTemplate;
import com.jy.fibre.service.BuiltInTemplateServiceImpl;
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
@AuditModule(moduleName = "内置模板")
@RestController
@RequestMapping("/built_in_template")
public class BuiltInTemplateController extends AbstractAuditCurdController<BuiltInTemplateServiceImpl> {

    /**
     * 根据类别查内置模板
     *
     * @param equipmentTypeId 类别
     * @return 内置模板
     */
    @GetMapping("/find_by_equipment_type_id/{equipmentTypeId}")
    public List<BuiltInTemplate> findByEquipmentTypeId(@PathVariable String equipmentTypeId) {
        return this.getService().findByEquipmentTypeId(equipmentTypeId);

    }
}
