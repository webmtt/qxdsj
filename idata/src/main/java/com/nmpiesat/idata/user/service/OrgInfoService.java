package com.nmpiesat.idata.user.service;

import com.nmpiesat.idata.user.dao.UserDao;
import com.nmpiesat.idata.user.entity.OrgInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
@Service
public class OrgInfoService {
    @Autowired
    private UserDao uDao;
    public String getOrgListBypId(String orgName) {
        List<OrgInfo> list= uDao.getOrgByName(orgName);
        String orgid=null;
        if(list.size()==0){
            OrgInfo oi=new OrgInfo();
            orgid=(int)(Math.random()*1000)+"";
            oi.setParentId("0");
            oi.setId(orgid);
            oi.setName(orgName);
            oi.setCode(""+((int)(Math.random()*1000)));
            uDao.insertOrg(oi);
        }else{
            orgid=list.get(0).getId();
        }
        return orgid;
    }

    public List<OrgInfo> getOrgInfo(String keyword) {
        return uDao.getOrgInfo(keyword);
    }
}
