package com.jy.fibre.service;

import com.jy.fibre.TestClassBase;
import com.jy.fibre.bean.pe.ComputerRoomEnvironmentVO;
import com.jy.fibre.dao.*;
import com.jy.fibre.domain.*;
import com.xmgsd.lan.gwf.core.generator.LanVueField;
import com.xmgsd.lan.gwf.dao.AttachmentDao;
import com.xmgsd.lan.gwf.domain.DictionaryCode;
import com.xmgsd.lan.gwf.service.DictionaryCodeServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * @author LinGuoHua
 */
public class BrandServiceTest extends TestClassBase {
    @Autowired
    private BrandDao brandDao;

    private BrandServiceImpl brandService;

    @Autowired
    private BrandModelDao brandModelDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ComputerRoomDao computerRoomDao;

    @Autowired
    private EquipmentDao equipmentDao;

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    public BrandDetailsDao brandDetailDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    DictionaryCodeServiceImpl dictionaryCodeService;

    @Autowired
    private BuiltInTemplateDao builtInTemplateDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.brandService = spy(new BrandServiceImpl(brandModelDao, brandDetailDao));

        doReturn(this.brandDao).when(this.brandService).getMapper();
        assertThat(this.brandService.getMapper()).isEqualTo(this.brandDao);
    }

    /**
     * 根据ID品牌
     */
    @Test
    public void testFindById() {
//        Brand brand = new Brand();
//        brand.setId("1");
//        brand.setName("华为");
//        doReturn(brand).when(brandDao).selectByPrimaryKey("1");
//        Brand brandById = this.brandService.get("1");
//        assertThat(brandById).isEqualToComparingFieldByField(brand);

//            Brand brand= new Brand();
//            brand.setName("品牌");
//            brand.setOrderNumber(1);
//            brand.setRemark("测试品牌");
//            brandDao.insert(brand);
//        System.out.println(brand);

        List<String> brandModelIds = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Brand brand = new Brand();
            brand.setName("品牌" + (i + 1));
            brand.setOrderNumber(i + 1);
            brand.setRemark("测试品牌");
            brandDao.insert(brand);
            System.out.println("444444" + brand);
            for (int i1 = 0; i1 < 2; i1++) {
                BrandModel brandModel = new BrandModel();
                System.out.println(brand.getId());
                brandModel.setBrandId(brand.getId());
                brandModel.setName("型号" + (i + 1) + "-" + (i1 + 1));
                brandModel.setEquipmentTypeId("38b1765bc5b042b5885ab76708d5b160");
                brandModel.setOrderNumber(i1 + 1);
                brandModelDao.insert(brandModel);
                brandModelIds.add(brandModel.getId());
            }
        }
        int brandModelArrayIndex = 0;
        for (int i = 0; i < 500; i++) {
            Customer customer = new Customer();
            customer.setName("客户" + (i + 1));
            customer.setChargeUserPhone("15454244745" + i);
            customer.setChargeUserEmail("4554@ss.com");
            customer.setChargeUserFullName("张" + (i + 1));
            this.customerDao.insert(customer);
            for (int i1 = 0; i1 < 2; i1++) {
                ComputerRoom computerRoom = new ComputerRoom();
                computerRoom.setName("机房" + (i1 + 1));
                computerRoom.setAddress("海沧教师进修学校（模拟地址）");
                computerRoom.setContactsName("张" + (i + 1));
                computerRoom.setContactsPhone("1545424745" + i);
                computerRoom.setCustomerId(customer.getId());
                computerRoom.setOrderNumber(i1);
                computerRoomDao.insert(computerRoom);
                for (int i2 = 0; i2 < 2; i2++) {
                    Equipment equipment = new Equipment();
                    equipment.setComputerRoomId(computerRoom.getId());
                    equipment.setBrandModelId(brandModelIds.get(brandModelArrayIndex));
                    equipment.setName("设备" + brandModelArrayIndex);
                    equipment.setSerialNumber(String.valueOf(brandModelArrayIndex));
                    equipmentDao.insert(equipment);
                }
                brandModelArrayIndex++;
            }
        }

//        computerRoomDao.deleteaaa("型");


    }

    @Test
    public void fin() throws Exception {
//        File file = new File("C:\\Users\\13794\\Desktop\\信息中心机房\\AirCondition\\机房环境检查表.xlsx");
//        FileInputStream fis = new FileInputStream(file);
//        int len = fis.available();
//        byte[] xml = new byte[len];
//        fis.read(xml);
//        List<ComputerRoomEnvironmentVO> computerRoomEnvironmentVOS = LanDescExcelHelper.parseExcel(xml, ComputerRoomEnvironmentVO.class, ComputerRoomEnvironmentVO::new, true, resDictionaryCodeHandler, null);
//        System.out.println(computerRoomEnvironmentVOS + "6666");
        BuiltInTemplate builtInTemplate = new BuiltInTemplate();
        builtInTemplate.setName("机房环境检查表");
        builtInTemplate.setContrastClass(ComputerRoomEnvironmentVO.class.getName());
        builtInTemplate.setEquipmentTypeId("e37969fa517b432d9cb3a5602c81c3e9");
        builtInTemplateDao.insert(builtInTemplate);

    }

    @Test
    public void testTaskDao() {
    }

    private BiFunction<LanVueField, Cell, String> resDictionaryCodeHandler = (lanVueField, cell) -> {
        Optional<DictionaryCode> result = this.dictionaryCodeService.list().stream().filter(dictionaryCode -> Objects.equals(dictionaryCode.getName(), cell.getStringCellValue())).findFirst();
        return result.get().getId();
    };
    private Function<LanVueField, List<DictionaryCode>> dictionaryCodeHandler = lanVueField -> {
        // 会调用这个方法的，一定是检查到这个字段是数据字典字段
        DictionaryCode dc = this.dictionaryCodeService.getByCode(lanVueField.getDicCode());
        // 返回所有的设备类型
        return this.dictionaryCodeService.list().stream()
                .filter(i -> Objects.equals(i.getParentId(), dc.getId()))
                .collect(Collectors.toList());
    };
    private Function<Field, Boolean> fieldFilter = field -> {
        //判断字段是否需要过滤，并返回boolean值
        return true;
    };
    private Function<LanVueField, Boolean> constraintHandler = lanVueField -> {
        //是否需要判断非法值，返回boolean值
        return true;
    };
}
