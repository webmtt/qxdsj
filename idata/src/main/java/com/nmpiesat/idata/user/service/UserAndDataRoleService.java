package com.nmpiesat.idata.user.service;

import com.alibaba.fastjson.JSONObject;
import com.nmpiesat.idata.user.dao.UserDao;
import com.nmpiesat.idata.user.entity.DataRole;
import com.nmpiesat.idata.user.entity.DataRolelimits;
import com.nmpiesat.idata.user.entity.UserDataRole;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
@Service
public class UserAndDataRoleService {
    @Autowired
    private UserDao udrDao;
    /**
     * 批量添加角色
     *
     * @param checkRole
     * @param userid
     */
    public void saveBatch(String checkRole, String userid) {
        List<String> list = new ArrayList<String>();
        list = JSONObject.parseArray(checkRole, String.class);
        udrDao.deleteUserDataroleByUserId(userid);
        UserDataRole udr = null;
        List<UserDataRole> insertList=new ArrayList<UserDataRole>();
        for (int i = 0, length = list.size(); i < length; i++) {
            udr = new UserDataRole();
            udr.setId(UUID.randomUUID().toString());
            udr.setUserId(userid);
            udr.setDataroleId(list.get(i));
            insertList.add(udr);
        }
        udrDao.insertValue(insertList);
        udrDao.updateUserIsSpecDataroleById( "1",userid);
    }

    public void insertDataRole(String orgName,String checkRole) {
        DataRole dataRole=new DataRole();
        dataRole.setDataroleId(""+((int)(Math.random()*1000)));
        dataRole.setDataroleName(orgName);
        dataRole.setOrderNo(0);
        //添加新的数据角色
        udrDao.insertRole(dataRole);
        JSONArray array = JSONArray.fromObject(checkRole);
        List<DataRolelimits> list = new ArrayList<DataRolelimits>();
        DataRolelimits drl = null;

        for (int i = 0, length = array.size(); i < length; i++) {
            drl = new DataRolelimits();
            net.sf.json.JSONObject jsonObject = array.getJSONObject(i);
            String dataid = jsonObject.getString("id");
            drl.setRoledataId(dataRole.getDataroleId());
            drl.setDataId(dataid);
            list.add(drl);
        }
        udrDao.insertDataRoleLimits(list);
    }
}
