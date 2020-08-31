package com.nmpiesat.idata.data.dao;

import com.nmpiesat.idata.data.entity.CategoryDataRelt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDataReltDao {
    List<CategoryDataRelt> findCategoryDataReltById(int id);

    List<CategoryDataRelt> findCategoryDataReltByUserId(String userid, int id);
}
