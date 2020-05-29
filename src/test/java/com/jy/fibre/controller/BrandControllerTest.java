package com.jy.fibre.controller;

import com.jy.fibre.TestClassBase;
import com.jy.fibre.service.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author LinGuoHua
 */
public class BrandControllerTest extends TestClassBase {
    private MockMvc mockMvc;

    @Mock
    private BrandServiceImpl brandService;

    @InjectMocks
    private BrandController brandController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.brandController).build();
    }


}
