package com.nmpiesat.idata.data.service;

import com.nmpiesat.idata.data.dao.CategoryDataReltDao;
import com.nmpiesat.idata.data.entity.CategoryDataRelt;
import com.nmpiesat.idata.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryDataReltService {
    @Autowired
    private CategoryDataReltDao categoryDataReltDao;
    public List<CategoryDataRelt> findCategoryDataReltById(int id) {
        return categoryDataReltDao.findCategoryDataReltById(id);
    }
    public List<CategoryDataRelt> findCategoryDataReltByUserId(String userId, int id) {
        List<CategoryDataRelt> list = categoryDataReltDao.findCategoryDataReltByUserId(userId, id);
        return list;
    }

}
