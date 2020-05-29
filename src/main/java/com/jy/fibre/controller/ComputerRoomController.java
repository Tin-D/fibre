package com.jy.fibre.controller;

import com.jy.fibre.service.ComputerRoomServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import com.xmgsd.lan.gwf.domain.AuditLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "机房管理")
@RestController
@RequestMapping("/computer_room")
public class ComputerRoomController extends AbstractAuditCurdController<ComputerRoomServiceImpl> {

    @Override
    public List list(AuditLog al) throws Exception {
        return super.list(al);
    }
}
