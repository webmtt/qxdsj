package com.thinkgem.jeesite.modules.UserDataRole.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.UserDataRole.entity.DataRolelimits;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据角色权限
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 14:45
 */
@Repository
public class DataroleLimitsDao extends BaseDao<DataRolelimits> {

  public void delDataroleLimitsById(String dataroleId) {
    String sql = "delete from SUP_DATAROLELIMITS where roledataid='" + dataroleId + "'";
    this.saveTest(sql);
    this.getSession().flush(); // 清理缓存，执行批量插入
    this.getSession().clear(); // 清空缓存中的 对象
  }

  public List<Object[]> getdataroleLimitsById(String dataroleid) {
    String sql = "select * from SUP_DATAROLELIMITS  where roledataid=:p1";
    List<Object[]> list = this.findBySql(sql, new Parameter(dataroleid));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public void insertData(List<DataRolelimits> list) {
    List<String> sqlList = new ArrayList<String>();
    String sql = null;
    for (DataRolelimits drl : list) {
      sql =
          "insert into sup_DataroleLimits values('"
              + drl.getDataId()
              + "','"
              + drl.getRoledataId()
              + "')";
      sqlList.add(sql);
    }
    for (String insertsql : sqlList) {
      this.saveTest(insertsql);
    }
    this.getSession().flush(); // 清理缓存，执行批量插入
    this.getSession().clear(); // 清空缓存中的 对象
  }

  private void saveTest(String sql) {
    this.getSession().createSQLQuery(sql).executeUpdate();
  }

  public void batchSave(List<DataRolelimits> list) {
      Session session=this.getSession();
      Transaction tx = session.beginTransaction();
      for (int i=0,size=list.size();i<size ;i++ ) {
        DataRolelimits drl=list.get(i);
        session.save(drl);
        if (i!=0&& i % 1000 == 0 ) {  //20,与JDBC批量设置相同
          //将本批插入的对象立即写入数据库并释放内存
          session.flush();
          session.clear();
        }
      }
      tx.commit();
      session.flush(); // 清理缓存，执行批量插入
      session.clear(); // 清空缓存中的 对象
//
//    Connection connection = null;
//    try {
//      connection = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
//      //    session.c
//      PreparedStatement pstm =
//          connection.prepareStatement(
//              "insert into SUP_DATAROLELIMITS (DATAID,ROLEDATAID)values(?,?)");
//
//      connection.setAutoCommit(false); // 将Auto commit设置为false,不允许自动提交
//      for (DataRolelimits drl : list) {
//        // 设置第一条语句
//        pstm.setString(1, drl.getDataId());
//
//        pstm.setString(2, drl.getRoledataId());
//
//        pstm.addBatch(); // 将一组参数添加到此 PreparedStatement 对象的批处理命令中。
//      }
//
//      pstm.executeBatch(); // 将一批参数提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组
//
//      connection.commit();
//
//      connection.setAutoCommit(true); // 将Auto commit还原为true
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } finally {
//      this.getSession().flush(); // 清理缓存，执行批量插入
//      this.getSession().clear(); // 清空缓存中的 对象
//    }
  }

  private SessionFactory getSessionFactory() {
    Session session = this.getSession();
    SessionFactory sf = session.getSessionFactory();
    return sf;
  }
}
