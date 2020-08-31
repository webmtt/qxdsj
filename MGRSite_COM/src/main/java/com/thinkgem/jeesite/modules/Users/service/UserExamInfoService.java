/*
 * @(#)UserExamInfoService.java 2016年3月22日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.UserExamInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.UserExamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月22日
 */
@Service
public class UserExamInfoService {
  @Autowired private UserExamInfoDAO ueDao;

  @Transactional
  public void saveUserExam(
      String email,
      String name,
      String checkemail,
      String org,
      String photo,
      String protocal,
      String others,
      String content) {
    List<UserExamInfo> ulist = new ArrayList<UserExamInfo>();
    UserExamInfo ue;
    if (checkemail != null && !"".equals(checkemail)) {
      ue = new UserExamInfo();
      ue.setCode(checkemail);
      ue.setEmail(email);
      ue.setReason("邮箱不存在");
      ue.setId(UUID.randomUUID().toString());
      ulist.add(ue);
    }
    if (name != null && !"".equals(name)) {
      ue = new UserExamInfo();
      ue.setCode(name);
      ue.setEmail(email);
      ue.setReason("姓名不正确");
      ue.setId(UUID.randomUUID().toString());
      ulist.add(ue);
    }
    if (org != null && !"".equals(org)) {
      ue = new UserExamInfo();
      ue.setCode(org);
      ue.setEmail(email);
      ue.setReason("工作单位不正确");
      ue.setId(UUID.randomUUID().toString());
      ulist.add(ue);
    }
    if (photo != null && !"".equals(photo)) {
      ue = new UserExamInfo();
      ue.setCode(photo);
      ue.setEmail(email);
      ue.setReason("照片不清晰");
      ue.setId(UUID.randomUUID().toString());
      ulist.add(ue);
    }
    if (others != null && !"".equals(others)) {
      ue = new UserExamInfo();
      ue.setCode(others);
      ue.setEmail(email);
      ue.setReason("其它");
      ue.setId(UUID.randomUUID().toString());
      ue.setRemarks(content);
      ulist.add(ue);
    }
    if (protocal != null && !"".equals(others)) {
      ue = new UserExamInfo();
      ue.setCode(protocal);
      ue.setEmail(email);
      ue.setReason("协议未加盖单位印章");
      ue.setId(UUID.randomUUID().toString());
      ulist.add(ue);
    }
    ueDao.save(ulist);
  }

  public List<UserExamInfo> getExamInfo(String email) {
    return ueDao.getExamInfo(email);
  }

  @Transactional
  public void deleteExamInfo(String email) {
    ueDao.deleteExamInfo(email);
  }
}
