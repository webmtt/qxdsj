package com.nmpiesat.idata.interfacedata.service;

import com.nmpiesat.idata.interfacedata.dao.InterfaceDataDAO;
import com.nmpiesat.idata.interfacedata.entity.InterfaceData;
import com.nmpiesat.idata.user.entity.MusicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InterfaceDataService {

    @Autowired
    private InterfaceDataDAO interfaceDataDao;

    public List<InterfaceData> getAllInter(String userid) {
        return interfaceDataDao.getAllInter(userid);
    }

    public List<Map<String,Object>> getInterface(String id,String userid) {
        return interfaceDataDao.getInterface(id,userid);
    }

    public Map<String,Object> getFactor(String id) {
        return interfaceDataDao.getFactor(id);
    }

    public List<Map<String,Object>> newInterface() {
        return interfaceDataDao.newInterface();
    }

    public Map<String, Object> getInterForId(String api) {
        return interfaceDataDao.getInterForId(api);
    }

    public List<Map<String, Object>> getEleForInter(String id) {
        return interfaceDataDao.getEleForInter(id);
    }

    public Map<String, Object> getEleMent(String eleCode) {
        return interfaceDataDao.getEleMent(eleCode);
    }

    public Map<String, Object> getAdminCode(String api) {
        return interfaceDataDao.getAdminCode(api);

    }

    public List<Map<String, Object>> getAdminCodes() {
        return interfaceDataDao.getAdminCodes();
    }

    public MusicInfo getMusic(String userid) {
        return interfaceDataDao.getMusic(userid);
    }

    public List<Map<String, Object>> getAllElements() {

        return interfaceDataDao.getAllElements();
    }
}
