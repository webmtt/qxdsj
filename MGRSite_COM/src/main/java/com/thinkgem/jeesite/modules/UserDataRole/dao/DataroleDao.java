package com.thinkgem.jeesite.modules.UserDataRole.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色-Dao
 *
 * @author RainingTime
 * @version 1.0 2018年7月5日
 */
@Repository
public class DataroleDao extends BaseDao<Datarole> {

  public List<Datarole> getDataroleList() {
    String sql = "from Datarole where dataroleId != 'default'";
    return this.find(sql);
  }

  public void delDataroleById(String id) {
    String sql = "delete from Datarole where dataroleId=:p1";
    this.update(sql, new Parameter(id));
  }

  public Datarole getDataroleById(String dataroleId) {
    String sql = "from Datarole where dataroleId=:p1";
    List<Datarole> list = this.find(sql, new Parameter(dataroleId));
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public Datarole getDataroleByName(String name) {
    String sql = "from Datarole where datarolename=:p1";
    List<Datarole> list = this.find(sql, new Parameter(name));
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public List<Datarole> findByName(String dataroleName) {
    String sql = "from Datarole where dataroleName like :p1";
    return this.find(sql, new Parameter(dataroleName));
  }

  public Page<Datarole> getDataRoleList(int no, int size) {
    Page<Datarole> plist = new Page<Datarole>();
    String sql = "from Datarole where delFlag='0' order by orderno";
    // sql+=" order by created DESC";
    plist = this.find(new Page(no, size), sql);
    return plist;
  }

  public Page<Datarole> getDataRoleListBy(int no, int size, String idOrName) {
    Page<Datarole> plist = new Page<Datarole>();
    String sql =
        "from Datarole where delFlag='0' and (DATAROLEID like '%"
            + idOrName
            + "%' or DATAROLENAME like '%"
            + idOrName
            + "%') order by orderno";
    plist = this.find(new Page(no, size), sql);
    return plist;
  }

  public void updateRole(Datarole datarole) {
    String sql =
        "update Datarole set DATAROLENAME='"
            + datarole.getDataroleName()
            + "',DESCRIPTIONCHN='"
            + datarole.getDescriptionChn()
            + "',ORDERNO="
            + datarole.getOrderNo()
            + " where dataroleId='"
            + datarole.getDataroleId()
            + "'";

    this.update(sql);
  }

  public void insertRole(Datarole datarole) {
    String sql = null;
    if (null == datarole.getDescriptionChn() || "".equals(datarole.getDescriptionChn())) {
      sql =
          "insert into SUP_DATAROLE (dataRoleId,dataRoleName,orderNo)values( '"
              + datarole.getDataroleId()
              + "','"
              + datarole.getDataroleName()
              + "',"
              + datarole.getOrderNo()
              + ")";
    } else {
      sql =
          "insert into SUP_DATAROLE (dataRoleId,dataRoleName,descriptionChn,orderNo)values('"
              + datarole.getDataroleId()
              + "','"
              + datarole.getDataroleName()
              + "','"
              + datarole.getDescriptionChn()
              + "',"
              + datarole.getOrderNo()
              + ")";
    }
    this.saveTest(sql);
    //    this.update(sql);
  }

  private void saveTest(String sql) {
    this.getSession().createSQLQuery(sql).executeUpdate();
    this.getSession().flush(); // 清理缓存，执行批量插入
    this.getSession().clear(); // 清空缓存中的 对象
  }

  public List<Object[]> getdataroleByUserId(String userid) {
    String sql =
        "select d.* from sup_DATAROLE d,sup_userdatarole u "
            + "where u.DATAROLEID=d.DATAROLEID  and u.USERID=:p1 and d.DELFLAG=0";
    List<Object[]> list = this.findBySql(sql, new Parameter(userid));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

    public int getMaxOrderNo() {
      String sql="from Datarole where orderNo=(select max(orderNo) from Datarole) ";
      List<Datarole> list = this.find(sql);
      if (list != null && list.size() > 0) {
        return list.get(0).getOrderNo();
      } else {
        return 0;
      }
    }
}
