package com.nmpiesat.idata.industry.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nmpiesat.idata.industry.dao.IndustryApplicationDao;
import com.nmpiesat.idata.industry.entity.IndustryApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustryApplicationService {

    @Autowired
    private IndustryApplicationDao industryApplicationDao;

    public List<IndustryApplication> findList(){
        return industryApplicationDao.findList();
    }

    public IndustryApplication findExampleById(String id) {
        return industryApplicationDao.findExampleById(id);
    }
}
