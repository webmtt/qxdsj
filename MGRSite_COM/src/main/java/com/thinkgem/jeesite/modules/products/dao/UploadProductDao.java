package com.thinkgem.jeesite.modules.products.dao;

import com.thinkgem.jeesite.modules.products.entity.UploadProduct;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;

import java.util.List;

@MyBatisDao
public interface UploadProductDao {

    void uploadProduct(UploadProduct uploadProduct);

    List<UploadProduct> getAllUploadProduct();
}
