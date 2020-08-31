package com.thinkgem.jeesite.modules.products.service;


import com.thinkgem.jeesite.modules.products.dao.BmpProductinfoDAO;
import com.thinkgem.jeesite.modules.products.dao.ProductsDao;
import com.thinkgem.jeesite.modules.products.dao.UploadProductDao;
import com.thinkgem.jeesite.modules.products.entity.Newproducts;
import com.thinkgem.jeesite.modules.products.entity.ProductRepert;
import com.thinkgem.jeesite.modules.products.entity.Products;
import com.thinkgem.jeesite.modules.products.entity.UploadProduct;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 产品库展示Service
 * @author zhaoxiaojun
 * @version 2020-02-14
 */
@Service
public class ProductsService extends CrudService<ProductsDao, Products> {

    @Autowired
    BmpProductinfoDAO bmpProductinfoDAO;

    @Autowired
    ProductsDao productsDao;

    @Autowired
    UploadProductDao uploadProductDao;

    public Products get(String id) {
        return super.get(id);
    }

    public Page<Products> findPage(Page<Products> page, Products products) {
        return super.findPage(page, products);
    }

    public Page<UploadProduct> productUploadList(Page<UploadProduct> page) {
        page.setCount(uploadProductDao.getAllUploadProduct().size());
        page.setList(uploadProductDao.getAllUploadProduct());
        return page;
    }

    @Transactional(readOnly = false)
    public void update(Products products) {
        super.save(products);
    }

    public List<Map<String, Object>> getAllUnit(Newproducts newproducts) {
        List<Map<String,Object>> list = bmpProductinfoDAO.getAllUnit(newproducts);
        return list;
    }

    public List<Map<String, Object>> getAllUnitType(Newproducts newproducts) {
        List<Map<String,Object>> list = bmpProductinfoDAO.getAllUnitType(newproducts);
        return list;
    }

    public List<Map<String, Object>> getAllUnitProduct(Newproducts newproducts) {
        List<Map<String,Object>> list = bmpProductinfoDAO.getAllUnitProduct(newproducts);
        return list;
    }

    @Transactional(readOnly = false)
    public int saveAlert(Newproducts newproducts){
        int code = productsDao.saveAlert(newproducts);
        if(code==1){
            return code;
        }else{
            return 0;
        }
    }

    @Transactional(readOnly = false)
    public int saveForecast(Newproducts newproducts){
        int code = productsDao.saveForecast(newproducts);
        if(code==1){
            return code;
        }else{
            return 0;
        }
    }

    @Transactional(readOnly = false)
    public void uploadProduct(String id, String s) {
        UploadProduct uploadProduct = new UploadProduct();
        uploadProduct.setCreats(new Date());
        uploadProduct.setId(id);
        uploadProduct.setUrl(s);
        uploadProductDao.uploadProduct(uploadProduct);
    }

    public List<UploadProduct> getAllUploadProduct() {
        return uploadProductDao.getAllUploadProduct();
    }

    @Transactional(readOnly = false)
    public void updateUserName(String users,String id) {
        Products products = new Products();
        products.setUsername(users);
        products.setId(id);
        productsDao.updateUserName(products);
    }

    @Transactional(readOnly = false)
    public int saveProductRepert(ProductRepert productRepert) {
        int code = productsDao.saveProductRepert(productRepert);
        if(code==1){
            return code;
        }else{
            return 0;
        }
    }

    public List<Products> findAllList() {
        return productsDao.findAllLists();
    }

    public String getProductUpload(String id) {
        return productsDao.getProductUpload(id);
    }

    @Transactional(readOnly = false)
    public void deleteProductUp(String id) {
        productsDao.deleteProductUp(id);
    }

    @Transactional(readOnly = false)
    public void saveProductUpload(UploadProduct uploadProduct) {
        productsDao.saveProductUpload(uploadProduct);

    }

    public ProductRepert getProductRepert() {
        return productsDao.getProductRepert();
    }

    public List<Map<String,Object>> getNewproducts() {
        return productsDao.getNewproducts();
    }
}
