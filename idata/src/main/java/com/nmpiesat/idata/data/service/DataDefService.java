package com.nmpiesat.idata.data.service;

import com.nmpiesat.idata.data.dao.DataDefDao;
import com.nmpiesat.idata.data.entity.CategoryDataRelt;
import com.nmpiesat.idata.data.entity.DataDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataDefService {
    @Autowired
    private DataDefDao dataDefDao;
    public Map findDataDefByCodes(List<CategoryDataRelt> list,int pageNum){
        List<String> inList = new ArrayList<String>();
        List<DataDef> dataDefList = new ArrayList();
        int maxPage = 0;
        Integer countInt = 0;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    CategoryDataRelt cd = list.get(i);
                    inList.add(cd.getDatacode());
                } else {
                    inList.add(list.get(i).getDatacode());
                }
            }

            Integer pageSize = 5;
            countInt = dataDefDao.findCount(inList);

            int beginNum = pageSize * (pageNum - 1);
//            int endNum = pageSize * pageNum;
            int endNum=5;
            maxPage =
                    (int)
                            Math.ceil(
                                    Double.parseDouble(countInt+"")
                                            / Double.parseDouble(pageSize.toString()));

            dataDefList = dataDefDao.findlist(inList, beginNum, endNum);

        }
        Map map = new HashMap();
        map.put("dataDefList", dataDefList);
        map.put("maxPage", maxPage);
        map.put("countInt", countInt);
        return map;
    }

    public DataDef findDataDefByDataCode(String dataCode) {
        DataDef dataDef = dataDefDao.findDataDefByDataCode(dataCode);
        return dataDef;
    }

    public List<DataDef> findDataDefByCateId(int categoryid) {
        List<DataDef> dataDefList =dataDefDao.finlistByCateId(categoryid);
        return dataDefList;
    }

    public List<DataDef> findDataDefByCateName(String name) {
        name='%'+name+'%';
        List<DataDef> dataDefList =dataDefDao.finlistByCateName(name);
        return dataDefList;
    }
}
