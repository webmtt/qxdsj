package com.thinkgem.jeesite.modules.data.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataReferDef;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DataReferDefDao extends BaseDao<DataReferDef> {
  public List<DataReferDef> findAll() {
    return this.find("from DataReferDef where invalid = '0' order by orderno");
  }

  public List<DataReferDef> findDataReferDefByDataCode(String dataCode) {
    return this.find(
        "from DataReferDef where invalid = '0' and datacode =:p1 order by orderno",
        new Parameter(dataCode));
  }

  public Page<DataReferDef> findDataReferDefPage(Page<DataReferDef> page, String dataCode) {

    StringBuffer sb = new StringBuffer("from DataReferDef where 1=1 ");

    if (!"".equals(dataCode) && null != dataCode) {
      sb.append(" and dataCode='" + dataCode + "'");
    }

    return this.find(page, sb.toString(), new Parameter());
  }
  /*
  public Page<Object[]> findPageByName(Page<Object[]> page,String chnname,String categoryid,String dataCode){
  	if(dataCode==null||"".equals(dataCode)||"0".equals(dataCode)){
  		if(chnname!=null&&!"".equals(chnname)){
  			StringBuffer sb=new StringBuffer("Select b.referid,a.datacode,a.chnname,b.refername,b.orderno From bmd_datadef a right join " +
  					"( Select * From BMD_DATAREFERDEF Where invalid=0) b on"+
  			         " a.datacode=b.datacode where a.invalid=0 and a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2) "+
  			         " And b.invalid=0 And a.invalid=0  Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter("%"+chnname+"%",categoryid));
  		}else{
  			StringBuffer sb=new StringBuffer("Select b.referid,a.datacode,a.chnname,b.refername,b.orderno From bmd_datadef a right join " +
  					 " ( Select * From BMD_DATAREFERDEF Where invalid=0) b on "+
  			         " a.datacode=b.datacode where a.invalid=0 And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) "+
  			         " And b.invalid=0 And a.invalid=0 Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter(categoryid));
  		}
  	}else{
  		if(chnname!=null&&!"".equals(chnname)){
  			StringBuffer sb=new StringBuffer("Select b.referid,a.datacode,a.chnname,b.refername,b.orderno From bmd_datadef a right join " +
  					"( Select * From BMD_DATAREFERDEF Where invalid=0) b on"+
  			         " a.datacode=b.datacode where a.invalid=0 and a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2" +
  			         " and datacode=:p3) "+
  			         " And b.invalid=0 And a.invalid=0  Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter("%"+chnname+"%",categoryid,dataCode));
  		}else{
  			StringBuffer sb=new StringBuffer("Select b.referid,a.datacode,a.chnname,b.refername,b.orderno From bmd_datadef a right join " +
  					 " ( Select * From BMD_DATAREFERDEF Where invalid=0) b on "+
  			         " a.datacode=b.datacode where a.invalid=0 And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1" +
  			         " and datacode=:p2) "+
  			         " And b.invalid=0 And a.invalid=0 Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter(categoryid,dataCode));
  		}
  	}
  }
  */
  public Page<Object[]> findPageByName(
      Page<Object[]> page, String chnname, String categoryid, String dataCode) {
    if (dataCode == null || "".equals(dataCode) || "0".equals(dataCode)) {
      if (chnname != null && !"".equals(chnname)) {
        StringBuffer sb =
            new StringBuffer(
                "Select b.referid,a.datacode,a.chnname,b.refername,b.orderno,b.invalid From bmd_datadef a right join "
                    + "( Select * From BMD_DATAREFERDEF) b on"
                    + " a.datacode=b.datacode where  a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(page, sb.toString(), new Parameter("%" + chnname + "%", categoryid));
      } else {
        StringBuffer sb =
            new StringBuffer(
                "Select b.referid,a.datacode,a.chnname,b.refername,b.orderno,b.invalid From bmd_datadef a right join "
                    + " ( Select * From BMD_DATAREFERDEF) b on "
                    + " a.datacode=b.datacode where  a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(page, sb.toString(), new Parameter(categoryid));
      }
    } else {
      if (chnname != null && !"".equals(chnname)) {
        StringBuffer sb =
            new StringBuffer(
                "Select b.referid,a.datacode,a.chnname,b.refername,b.orderno,b.invalid From bmd_datadef a right join "
                    + "( Select * From BMD_DATAREFERDEF) b on"
                    + " a.datacode=b.datacode where  a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2"
                    + " and datacode=:p3) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(
            page, sb.toString(), new Parameter("%" + chnname + "%", categoryid, dataCode));
      } else {
        StringBuffer sb =
            new StringBuffer(
                "Select b.referid,a.datacode,a.chnname,b.refername,b.orderno,b.invalid From bmd_datadef a right join "
                    + " ( Select * From BMD_DATAREFERDEF) b on "
                    + " a.datacode=b.datacode where  a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1"
                    + " and datacode=:p2) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(page, sb.toString(), new Parameter(categoryid, dataCode));
      }
    }
  }

  @Override
  public void save(DataReferDef drd) {
    super.save(drd);
  }

  public DataReferDef findById(Integer referid) {
    List<DataReferDef> dList = new ArrayList<DataReferDef>();
    String sql = "from DataReferDef where referid=:p1";
    dList = this.find(sql, new Parameter(referid));
    return dList.get(0);
  }

  public void saveUpdate(
      Integer referid, String datacode, String refername, Integer orderno, Integer invalid) {

    String sql =
        "update DataReferDef set datacode=:p2,refername=:p3,orderno=:p4,invalid=:p5 where referid=:p1";
    this.update(sql, new Parameter(referid, datacode, refername, orderno, invalid));
  }

  public void delById(Integer referid) {
    //		this.update("delete from DataReferDef where referid='"+referid+"'");
    this.update("update DataReferDef set invalid=1 where referid='" + referid + "'");
  }

  public void delDataReferByCode(String datacode) {
    //		String sql="delete from DataReferDef where datacode=:p1";
    String sql = "update DataReferDef set invalid=1 where datacode=:p1";
    this.update(sql, new Parameter(datacode));
  }

  public void upload(List<List<Object>> list) {
    String sql = null;
    for (int i = 0; i < list.size(); i++) {
       List<Object> ob = list.get(i);
       DataReferDef dataReferDef = new DataReferDef();
       dataReferDef.setDatacode(String.valueOf(ob.get(0)));
       dataReferDef.setRefername(String.valueOf(ob.get(1)));
       dataReferDef.setOrderno(Integer.valueOf(String.valueOf(ob.get(2))));
       dataReferDef.setInvalid(Integer.valueOf(String.valueOf(ob.get(3))));
       sql =
              "insert into BMD_DATAREFERDEF(datacode,refername,orderno,invalid)values( '"
                      +dataReferDef.getDatacode()
                      +"','"
                      +dataReferDef.getRefername()
                      +"','"
                      +dataReferDef.getOrderno()
                      +"','"
                      +dataReferDef.getInvalid()
                      +"')";

       this.saveTest(sql);
    }
  }
  private void saveTest(String sql) {
    this.getSession().createSQLQuery(sql).executeUpdate();
    this.getSession().flush(); // 清理缓存，执行批量插入
    this.getSession().clear(); // 清空缓存中的 对象
  }
}
