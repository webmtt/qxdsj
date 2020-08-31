package com.thinkgem.jeesite.modules.products.dao;

import com.thinkgem.jeesite.modules.products.entity.Newproducts;
import com.thinkgem.jeesite.modules.products.entity.ProductRepert;
import com.thinkgem.jeesite.modules.products.entity.Products;
import com.thinkgem.jeesite.modules.products.entity.UploadProduct;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品库展示DAO接口
 * @author zhaoxiaojun
 * @version 2020-02-14
 */
@MyBatisDao
public interface ProductsDao extends CrudDao<Products> {
    int saveAlert(Newproducts newproducts);

    int saveForecast(Newproducts newproducts);

    void updateUserName(Products products);

    int saveProductRepert(ProductRepert productRepert);

    List<Products> findAllLists();

    String getProductUpload(String id);

    void deleteProductUp(String id);

    void saveProductUpload(UploadProduct uploadProduct);

    ProductRepert getProductRepert();

    List<Map<String,Object>> getNewproducts();
}
