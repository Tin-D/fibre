package com.jy.fibre.service;

import com.jy.fibre.TestClassBase;
import com.jy.fibre.dao.BrandModelDao;
import com.jy.fibre.dao.EquipmentDao;
import com.xmgsd.lan.gwf.dao.AttachmentDao;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * @author LinGuoHua
 */
public class BrandModelServiceTest extends TestClassBase {
    @Mock
    private BrandModelDao brandModelDaoDao;

    @Mock
    private AttachmentDao attachmentDao;

    private BrandModelServiceImpl brandModelService;

    private EquipmentDao equipmentDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.brandModelService = spy(new BrandModelServiceImpl(attachmentDao,equipmentDao));

        doReturn(this.brandModelDaoDao).when(this.brandModelService).getMapper();
        assertThat(this.brandModelService.getMapper()).isEqualTo(this.brandModelDaoDao);
    }

}
