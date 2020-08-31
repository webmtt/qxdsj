package com.nmpiesat.idata.aboutus.service;


import com.nmpiesat.idata.aboutus.dao.AboutUsDao;
import com.nmpiesat.idata.aboutus.entity.AboutUs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AboutUsService {

    @Autowired
    private AboutUsDao aboutUsDao;

    public List<AboutUs> findList(){
        return aboutUsDao.findList();
    }
}
