package com.nmpiesat.idata.user.dao;

import com.nmpiesat.idata.user.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
@Repository
public interface UserDao {
    void saveUser(UserInfo userInfo);

    void deleteUserDataroleByUserId(String userid);

    void insertValue(List<UserDataRole> list);

    void updateUserIsSpecDataroleById(String specDatarole,String userid);

    List<OrgInfo> getOrgByName(String orgName);

    void insertOrg(OrgInfo oi);

    void insertDataRoleLimits(List<DataRolelimits> list);

    void insertRole(DataRole dataRole);

    UserInfo getUserInfo(String userName);

    void setActiveCode(UserInfo userInfo);

    void updatePassword(String password,String username);

    List<OrgInfo> getOrgInfo(String keyword);

    MusicInfo getMusicInfo(String orgID);

    List<UserExamInfo> getExamInfo(String userName);
}
