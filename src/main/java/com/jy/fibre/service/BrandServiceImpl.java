package com.jy.fibre.service;

import com.jy.fibre.bean.brand.BrandVO;
import com.jy.fibre.dao.BrandDao;
import com.jy.fibre.dao.BrandDetailsDao;
import com.jy.fibre.dao.BrandModelDao;
import com.jy.fibre.domain.Brand;
import com.jy.fibre.domain.BrandDetails;
import com.xmgsd.lan.roadhog.mybatis.BaseService;
import com.xmgsd.lan.roadhog.mybatis.DbItem;
import com.xmgsd.lan.roadhog.mybatis.service.SimpleCurdViewService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author LinGuoHua
 */
@Service
@Slf4j
public class BrandServiceImpl extends BaseService<BrandDao> implements SimpleCurdViewService<String, BrandVO> {

    private BrandModelDao brandModelDao;

    private BrandDetailsDao brandDetailsDao;

    public BrandServiceImpl(BrandModelDao brandModelDao, BrandDetailsDao brandDetailsDao) {
        this.brandModelDao = brandModelDao;
        this.brandDetailsDao = brandDetailsDao;
    }

    @Override
    public void remove(@NotNull String id) throws Exception {
        if (brandModelDao.theNumberOfFindByBrandId(id) > 0) {
            throw new Exception("请先删除该品牌下的型号。");
        } else {
            this.getMapper().deleteByPrimaryKey(id);
        }
    }

    @Override
    public void afterAdd(@NotNull BrandVO item, @NotNull DbItem recorder) {
        if (!Strings.isEmpty(item.getEquipmentTypeId())) {
                BrandDetails brandDetails = new BrandDetails();
                brandDetails.setBrandId(item.getId());
            brandDetails.setEquipmentTypeId(item.getEquipmentTypeId());
                brandDetailsDao.insert(brandDetails);
            }
        }

    @Nullable
    @Override
    public BrandVO get(@NotNull String id) {
        Brand brand = this.getMapper().selectByPrimaryKey(id);
        BrandVO brandVO = new BrandVO();
        BeanUtils.copyProperties(brand, brandVO);
        brandVO.setEquipmentTypeId(this.getMapper().findTypeIdById(id));
        return brandVO;
    }

    @Override
    public List<BrandVO> list() {
        return this.getMapper().selectAllAndTypeId();
    }

    @Override
    public BrandVO update(@NotNull String id, @NotNull BrandVO item) throws Exception {
        this.getMapper().updateByPrimaryKey(item.toDbUpdateItem());
        return this.get(id);
    }
}
