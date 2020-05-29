package com.jy.fibre.controller;

import com.jy.fibre.TestClassBase;
import com.jy.fibre.bean.bm.BrandModelVO;
import com.jy.fibre.service.BrandModelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author LinGuoHua
 */
public class BrandModelControllerTest extends TestClassBase {
    private MockMvc mockMvc;

    @Mock
    private BrandModelServiceImpl brandModelService;

    @InjectMocks
    private BrandModelController brandModelController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.brandModelController).build();
    }


    /**
     * 测试查询
     *
     * @throws Exception 异常
     */
    @Test
    @WithMockUser(username = "jy")
    public void ListTest() throws Exception {
        BrandModelVO brandModelVO = new BrandModelVO();
        brandModelVO.setId("1");
        brandModelVO.setName("p30");
        List<BrandModelVO> brandModelVOS = new ArrayList<>();
        brandModelVOS.add(brandModelVO);

        final String url = "/brand_model";

        when(this.brandModelService.list()).thenReturn(brandModelVOS);
        MvcResult mvcResult = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        verify(this.brandModelService, times(1)).list();
        assertThat(mvcResult.getResponse().getContentAsString()).contains("p30");
    }
}
