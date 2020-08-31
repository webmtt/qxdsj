package com.nmpiesat.idata.subject.service;

import com.nmpiesat.idata.subject.dao.SubjectDao;
import com.nmpiesat.idata.subject.entity.DSAccessDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
@Service
public class DSAccessDefService {
    @Autowired
    private SubjectDao dsaccessDefDao;
    public DSAccessDef getDSAccessDef(String dSAccessCode){
        return dsaccessDefDao.getDSAccessDef(dSAccessCode);
    }
}
