package com.jy.fibre.controller;

import com.jy.fibre.service.BrandModelServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import com.xmgsd.lan.gwf.domain.AuditLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "型号管理")
@RestController
@RequestMapping("/brand_model")
public class BrandModelController extends AbstractAuditCurdController<BrandModelServiceImpl> {
    @Override
    public List list(AuditLog al) throws Exception {
        return super.list(al);
    }
}
