package com.nmpiesat.idata.data.service;

import com.nmpiesat.idata.data.dao.DataReferDefDao;
import com.nmpiesat.idata.data.entity.DataReferDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataReferDefService {
    @Autowired
    private DataReferDefDao dataReferDefDao;
    public List<DataReferDef> findDataReferDefByDataCode(String dataCode){
        return dataReferDefDao.findDataReferDefByDataCode(dataCode);
    }
}
