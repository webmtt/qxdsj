package com.thinkgem.jeesite.modules.data.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataLinks;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DataLinksDao extends BaseDao<DataLinks> {
  public List<DataLinks> findAll() {
    return this.find("from DataLinks order by orderno");
  }

  public List<DataLinks> findDataLinksByDataCode(String dataCode) {
    return this.find(
        "from DataLinks where  datacode =:p1 order by orderno", new Parameter(dataCode));
  }

  public List<DataLinks> findDataLinksByDataCode2(String dataCode) {
    return this.find(
        "from DataLinks where  datacode =:p1 and invalid=0 order by orderno",
        new Parameter(dataCode));
  }

  public void delDataLink(String id) {
    String sql = "update DataLinks set invalid=1 where id=:p1";
    this.update(sql, new Parameter(id));
  }

  public Page<DataLinks> findDataLinksPage(Page<DataLinks> page, String dataCode) {

    StringBuffer sb = new StringBuffer("from DataLinks where 1=1 ");

    if (!"".equals(dataCode) && null != dataCode) {
      sb.append(" and dataCode='" + dataCode + "'");
    }

    return this.find(page, sb.toString(), new Parameter());
  }
  /*
     public Page<Object[]> findDataLinksPages(Page<Object[]> page,String chnname,String categoryid,String dataCode){
     	if(dataCode==null||"".equals(dataCode)||"0".equals(dataCode)){
  		if(chnname!=null&&!"".equals(chnname)){
  			StringBuffer sb=new StringBuffer("Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno From bmd_datadef a right join " +
  					"( Select * From BMD_DATALINKS Where invalid=0) b on"+
  			         " a.datacode=b.datacode where a.invalid=0 and a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2) "+
  			         " And b.invalid=0 And a.invalid=0  Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter("%"+chnname+"%",categoryid));
  		}else{
  			StringBuffer sb=new StringBuffer("Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno From bmd_datadef a right join " +
  					 " ( Select * From BMD_DATALINKS Where invalid=0) b on "+
  			         " a.datacode=b.datacode where a.invalid=0 And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) "+
  			         " And b.invalid=0 And a.invalid=0 Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter(categoryid));
  		}
  	}else{
  		if(chnname!=null&&!"".equals(chnname)){
  			StringBuffer sb=new StringBuffer("Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno From bmd_datadef a right join " +
  					"( Select * From BMD_DATALINKS Where invalid=0) b on"+
  			         " a.datacode=b.datacode where a.invalid=0 and a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2" +
  			         " and datacode=:p3) "+
  			         " And b.invalid=0 And a.invalid=0  Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter("%"+chnname+"%",categoryid,dataCode));
  		}else{
  			StringBuffer sb=new StringBuffer("Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno From bmd_datadef a right join " +
  					 " ( Select * From BMD_DATALINKS Where invalid=0) b on "+
  			         " a.datacode=b.datacode where a.invalid=0 And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1" +
  			         " and datacode=:p2) "+
  			         " And b.invalid=0 And a.invalid=0 Order By datacode,b.orderno");
  			return this.findBySql(page,sb.toString(),new Parameter(categoryid,dataCode));
  		}
  	}
  }
  */
  public Page<Object[]> findDataLinksPages(
      Page<Object[]> page, String chnname, String categoryid, String dataCode) {
    if (dataCode == null || "".equals(dataCode) || "0".equals(dataCode)) {
      if (chnname != null && !"".equals(chnname)) {
        StringBuffer sb =
            new StringBuffer(
                "Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno,b.invalid From bmd_datadef a right join "
                    + "( Select * From BMD_DATALINKS ) b on"
                    + " a.datacode=b.datacode where a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2) "
                    + "   Order By datacode,b.orderno");
        return this.findBySql(page, sb.toString(), new Parameter("%" + chnname + "%", categoryid));
      } else {
        StringBuffer sb =
            new StringBuffer(
                "Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno,b.invalid From bmd_datadef a right join "
                    + " ( Select * From BMD_DATALINKS ) b on "
                    + " a.datacode=b.datacode where  a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(page, sb.toString(), new Parameter(categoryid));
      }
    } else {
      if (chnname != null && !"".equals(chnname)) {
        StringBuffer sb =
            new StringBuffer(
                "Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno,b.invalid From bmd_datadef a right join "
                    + "( Select * From BMD_DATALINKS ) b on"
                    + " a.datacode=b.datacode where  a.chnname like :p1  And a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p2"
                    + " and datacode=:p3) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(
            page, sb.toString(), new Parameter("%" + chnname + "%", categoryid, dataCode));
      } else {
        StringBuffer sb =
            new StringBuffer(
                "Select b.linkid,a.datacode,a.chnname,b.linkname,b.linkurl,b.linktype,b.orderno,b.invalid From bmd_datadef a right join "
                    + " ( Select * From BMD_DATALINKS ) b on "
                    + " a.datacode=b.datacode where  a.datacode In(Select datacode From BMD_CATEGORYDATARELT Where categoryid=:p1"
                    + " and datacode=:p2) "
                    + "  Order By datacode,b.orderno");
        return this.findBySql(page, sb.toString(), new Parameter(categoryid, dataCode));
      }
    }
  }

  @Override
  public void save(DataLinks dl) {
    super.save(dl);
  }

  public DataLinks findById(Integer linkid) {
    List<DataLinks> List = new ArrayList<DataLinks>();
    String sql = "from DataLinks where linkid=:p1";
    List = this.find(sql, new Parameter(linkid));
    return List.get(0);
  }

  public void saveUpdate(
      Integer linkid,
      String datacode,
      Integer linktype,
      String linkname,
      String linkurl,
      Integer orderno,
      Integer invalid) {
    String sql =
        "update DataLinks set datacode=:p2,linktype=:p3,linkname=:p4,linkurl=:p5,orderno=:p6,invalid=:p7 where linkid=:p1";
    this.update(
        sql, new Parameter(linkid, datacode, linktype, linkname, linkurl, orderno, invalid));
  }

  public void delById(Integer linkid) {
    //		this.update("delete from DataLinks where linkid='"+linkid+"'");
    this.update("update DataLinks set invalid=1 where linkid='" + linkid + "'");
  }

  public void delDataLinkByCode(String datacode) {
    //		String sql="delete from  DataLinks  where datacode=:p1";
    String sql = "update DataLinks set invalid=1 where datacode=:p1";
    this.update(sql, new Parameter(datacode));
  }

  public void upload(List<List<Object>> list) {
    String sql = null;
    for (int i = 0; i < list.size(); i++) {
      List<Object> ob = list.get(i);
      DataLinks dataLinks = new DataLinks();
      dataLinks.setDatacode(String.valueOf(ob.get(0)));
      dataLinks.setLinktype(Integer.valueOf(String.valueOf(ob.get(1))));
      dataLinks.setLinkname(String.valueOf(ob.get(2)));
      dataLinks.setLinkurl(String.valueOf(ob.get(3)));
      dataLinks.setOrderno(Integer.valueOf(String.valueOf(ob.get(4))));
      dataLinks.setInvalid(Integer.valueOf(String.valueOf(ob.get(5))));
      sql =
              "insert into BMD_DATALINKS(datacode,linktype,linkname,linkurl,orderno,invalid)values( '"
                      +dataLinks.getDatacode()
                      +"','"
                      +dataLinks.getLinktype()
                      +"','"
                      +dataLinks.getLinkname()
                      +"','"
                      +dataLinks.getLinkurl()
                      +"','"
                      +dataLinks.getOrderno()
                      +"','"
                      +dataLinks.getInvalid()
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
