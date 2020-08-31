package com.nmpiesat.idata.user.service;

import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.user.dao.UserDao;
import com.nmpiesat.idata.user.entity.MusicInfo;
import com.nmpiesat.idata.user.entity.UserExamInfo;
import com.nmpiesat.idata.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
@Service
public class UserInfoService {
    @Autowired
    private UserDao uDao;
    public void saveUserInfo(UserInfo userInfo) {
        uDao.saveUser(userInfo);
    }

    public UserInfo getUserInfo(String userName) {
        return  uDao.getUserInfo(userName);
    }

    public void setActiveCode(UserInfo userInfo) {
        uDao.setActiveCode(userInfo);
    }

    public void updatePassword(String username, String password) {
        uDao.updatePassword(password,username);
    }

    public MusicInfo getMusicInfo(UserInfo userInfo) {
        MusicInfo musicInfo=uDao.getMusicInfo(userInfo.getOrgID());
        return musicInfo;
    }

    public boolean checkUserName(String userName) {
        UserInfo ui=uDao.getUserInfo(userName);
        if(ui==null){
            return true;
        }else{
            return false;
        }
    }

    public List<UserExamInfo> getExamInfo(String userName) {
        return uDao.getExamInfo(userName);
    }
}
