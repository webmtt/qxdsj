package com.nmpiesat.idata.industry.dao;


import com.nmpiesat.idata.industry.entity.IndustryApplication;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryApplicationDao {

    List<IndustryApplication> findList();

    IndustryApplication findExampleById(String id);
}
