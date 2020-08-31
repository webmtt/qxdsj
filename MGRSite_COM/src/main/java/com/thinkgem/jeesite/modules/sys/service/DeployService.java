package com.thinkgem.jeesite.modules.sys.service;

import com.thinkgem.jeesite.modules.sys.dao.DeployDao;
import com.thinkgem.jeesite.modules.sys.entity.Deploy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeployService {

    @Autowired
    private DeployDao deployDao;

    public List<Deploy> getById(String id) {
        return deployDao.getById(id);
    }

    public void insertDeploys(Deploy deploy) {
        deployDao.insertDeploys(deploy);
    }

    public void updateDeploys(Deploy deploy) {
        deployDao.updateDeploys(deploy);
    }

    public List<String> getALLUrl(String id) {
        return deployDao.getALLUrl(id);
    }
}
