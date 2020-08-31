package com.nmpiesat.idata.data.service;

import com.nmpiesat.idata.data.dao.DataLinksDao;
import com.nmpiesat.idata.data.entity.DataLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/16
 */
@Service
public class DataLinksService {
    @Autowired
    private DataLinksDao dataLinksDao;

    public List<DataLinks> findDataLinksByDataCode(String dataCode){
        return dataLinksDao.findDataLinksByDataCode(dataCode);
    }

}
