package com.jy.fibre.controller;

import com.jy.fibre.service.EquipmentServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "设备管理")
@RestController
@RequestMapping("/equipment")
public class EquipmentController extends AbstractAuditCurdController<EquipmentServiceImpl> {

    /**
     * 自动生成设备名
     *
     * @param computerRoomId  机房
     * @param equipmentTypeId 类别
     * @return 设备名称
     */
    @GetMapping("/the_number_of_find_by_computer_room_id_and_type_id/{computerRoomId}/{equipmentTypeId}")
    public String theNumberOfFindByComputerRoomIdAndTypeId(@PathVariable String computerRoomId, @PathVariable String equipmentTypeId) {
        return this.getService().theNumberOfFindByComputerRoomIdAndTypeId(computerRoomId, equipmentTypeId);
    }
}
