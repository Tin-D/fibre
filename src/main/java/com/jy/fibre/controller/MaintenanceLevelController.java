package com.jy.fibre.controller;

import com.jy.fibre.service.MaintenanceLevelServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "维护级别")
@RestController
@RequestMapping("/maintenance_level")
public class MaintenanceLevelController extends AbstractAuditCurdController<MaintenanceLevelServiceImpl> {

    /**
     * 根据类别查询
     *
     * @param equipmentTypeId 类别
     * @return 维护等级
     */
    @GetMapping("/find_by_type_id/{equipmentTypeId}")
    public List<IdNameEntry> findByTypeId(@PathVariable String equipmentTypeId) {
        return this.getService().findByTypeId(equipmentTypeId);
    }
}
