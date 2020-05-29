package com.jy.fibre.controller;

import com.jy.fibre.service.BrandServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import com.xmgsd.lan.gwf.domain.AuditLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "品牌管理")
@RestController
@RequestMapping("/brand")
public class BrandController extends AbstractAuditCurdController<BrandServiceImpl> {

    @Override
    public List list(AuditLog al) throws Exception {
        return super.list(al);
    }
}
