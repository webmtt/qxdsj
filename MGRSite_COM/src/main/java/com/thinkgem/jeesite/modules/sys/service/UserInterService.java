package com.thinkgem.jeesite.modules.sys.service;


import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import com.thinkgem.jeesite.modules.sys.dao.InterfaceDao;
import com.thinkgem.jeesite.modules.sys.dao.UserInterDao;
import com.thinkgem.jeesite.modules.sys.entity.Userinterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInterService {

    @Autowired
    private UserInterDao userInterDao;

    @Autowired
    private InterfaceDao interfaceDao;

    public boolean findById(String id){
        return userInterDao.findById(id);
    }

    public void saveUserinter(String id){
        userInterDao.saveUserinter(id);
    }

    public void saveUserinterface(Userinterface userinterface) {
        userInterDao.saveUserinterface(userinterface);
    }

    public void deltetUserinterface(String dataroleId) {
        userInterDao.deltetUserinterface(dataroleId);
    }

    public List<SysInterface> findAllInterface() {
        List<SysInterface> menuList = interfaceDao.findAllList(new SysInterface());
        return menuList;
    }

}
