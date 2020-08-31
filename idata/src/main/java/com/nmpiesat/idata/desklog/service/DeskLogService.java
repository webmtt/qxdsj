package com.nmpiesat.idata.desklog.service;


import com.nmpiesat.idata.desklog.dao.DeskLogDao;
import com.nmpiesat.idata.desklog.entity.DeskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeskLogService {

    @Autowired
    private DeskLogDao deskLogDao;

    public int insert(DeskLog deskLog){
        return deskLogDao.insert(deskLog);
    }

}
