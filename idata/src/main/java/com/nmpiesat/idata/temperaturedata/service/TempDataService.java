package com.nmpiesat.idata.temperaturedata.service;


import com.nmpiesat.idata.temperaturedata.dao.TempDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class TempDataService {

    @Autowired
    private TempDataDao tempDataDao;

    public List<HashMap> selectStaTemp(){
        return tempDataDao.selectStaTemp();
    }

    public List<HashMap> selectStaMulTemp(){
        return tempDataDao.selectStaMulTemp();
    }
}
