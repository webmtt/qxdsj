package com.thinkgem.jeesite.modules.UserDataRole.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.UserDataRole.entity.UserDatarole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色-DAO
 *
 * @author RainingTime
 * @version 1.0 2018年7月6日
 */
@Repository
public class UserDataroleDao extends BaseDao<UserDatarole> {

  public Long getcount(String roleCodeOrName, String userCodeOrName) {
    String sql =
        "select count(s.id)"
            + " from UserDatarole s, Datarole t, UserInfo p"
            + " where s.userId = p.iD and s.dataroleId = t.dataroleId";
    if (roleCodeOrName != null) {
      sql +=
          " and (t.dataroleName like '%"
              + roleCodeOrName
              + "%' or t.dataroleId like '%"
              + roleCodeOrName
              + "%')";
    }
    if (userCodeOrName != null) {
      sql +=
          " and (p.chName like '%"
              + userCodeOrName
              + "%' or p.userName like '%"
              + userCodeOrName
              + "%')";
    }
    List<Object> list = this.find(sql);
    if (list != null && list.size() > 0) {
      return (Long) list.get(0);
    } else {
      return 0L;
    }
  }

  public List<Object> getUserDataroleList(
      String roleCodeOrName, String userCodeOrName, int pageNo, int pageSize) {
    /** oracle下写法 */
    /*String sql = "select s.id, s.dataroleId, t.dataroleName, p.userName, p.chName, rownum as rowno" +
    		" from UserDatarole s, Datarole t, UserInfo p" +
    		" where s.userId = p.iD and s.dataroleId = t.dataroleId";
    if(roleCodeOrName != null) {
    	sql += " and ( t.dataroleName like '%" + roleCodeOrName + "%' or t.dataroleId like '%" + roleCodeOrName + "%' )";
    }
    if(userCodeOrName != null) {
    	sql += " and ( p.chName like '%" + userCodeOrName + "%' or p.userName like '%" + userCodeOrName + "%' )";
    }
    sql += " and rownum <= " + pageNo*pageSize;*/
    /** mysql下写法 */
    String sql =
        "select s.id, s.dataroleId, t.dataroleName, p.userName, p.chName"
            + " from UserDatarole s, Datarole t, UserInfo p"
            + " where s.userId = p.iD and s.dataroleId = t.dataroleId";
    if (roleCodeOrName != null) {
      sql +=
          " and ( t.dataroleName like '%"
              + roleCodeOrName
              + "%' or t.dataroleId like '%"
              + roleCodeOrName
              + "%' )";
    }
    if (userCodeOrName != null) {
      sql +=
          " and ( p.chName like '%"
              + userCodeOrName
              + "%' or p.userName like '%"
              + userCodeOrName
              + "%' )";
    }
    //		sql += " limit " + pageNo*pageSize;
    return this.find(sql, pageNo * pageSize);
  }

  public void deleteUserDataroleById(String id) {
    String sql = "delete from UserDatarole where id=:p1";
    this.update(sql, new Parameter(id));
  }

  public UserDatarole getById(String id) {
    String sql = "from UserDatarole where id=:p1";
    List<UserDatarole> list = this.find(sql, new Parameter(id));
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public UserDatarole findByDataroleIdAndUserId(String userId, String dataroleId) {
    String sql = "from UserDatarole where userId=:p1 and dataroleId=:p2 ";
    List<UserDatarole> list = this.find(sql, new Parameter(userId, dataroleId));
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public UserDatarole findByUserId(String userId) {
    String sql = "from UserDatarole where userId=:p1";
    List<UserDatarole> list = this.find(sql, new Parameter(userId));
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public void deleteUserDataroleByUserId(String userid) {
    String sql = "delete from UserDatarole where userid=:p1";
    this.update(sql, new Parameter(userid));
  }

  public void insertValue(UserDatarole udr) {
    String sql =
        "insert into sup_userdatarole values('"
            + udr.getId()
            + "','"
            + udr.getDataroleId()
            + "','"
            + udr.getUserId()
            + "')";
    this.saveTest(sql);
  }

  private void saveTest(String sql) {
    this.getSession().createSQLQuery(sql).executeUpdate();
    this.getSession().flush(); // 清理缓存，执行批量插入
    this.getSession().clear(); // 清空缓存中的 对象
  }
}
