package com.nmpiesat.idata.compara.services;

import com.nmpiesat.idata.compara.dao.ComparasDao;
import com.nmpiesat.idata.data.entity.Comparas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComparasService {

    @Autowired
    private ComparasDao comparasDao;
    public Object getComparasByKey(String key){
        List<Comparas> list=comparasDao.getComparasByKey(key);
        if(list.size()>0){
            Comparas comparas=list.get(0);
            if(comparas.getType().equals("String")){
                return comparas.getStringValue();
            }
            if(comparas.getType().equals("int")){
                return comparas.getIntValue();
            }
            if(comparas.getType().equals("boolean")){
                return comparas.getBoolEanValue();
            }
        }
        return null;
    }
}
