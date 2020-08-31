package com.thinkgem.jeesite.modules.data.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DataCategoryDefDao extends BaseDao<DataCategoryDef> {
  @Override
  public List<DataCategoryDef> findAll() {
    return this.find("from DataCategoryDef  order by orderno,categoryid desc");
  }

  public List<DataCategoryDef> findAllOne() {
    return this.find("from DataCategoryDef where categorylayer=1  order by orderno,categoryid");
  }

  public List<DataCategoryDef> findDataCategoryDefTop() {
    return this.find("from DataCategoryDef where  parentid = '0' order by orderno");
  }

  public List<DataCategoryDef> findDataCategoryDefById(int id) {
    return this.find(
        "from DataCategoryDef where categoryid =:p1  order by orderno", new Parameter(id));
  }

  public DataCategoryDef findDataCategoryDefByIdUnique(int id) {
    List<DataCategoryDef> list =
        this.find("from DataCategoryDef where  categoryid =:p1", new Parameter(id));
    return list.get(0);
  }

  public void delDataCategoryDef(String id) {
    String sql = "update DataCategoryDef set invalid='1' where categoryid=:p1";
    this.update(sql, new Parameter(Integer.parseInt(id)));
  }

  public Integer getDataCategoryDefId(Integer pid) {
    String sql = "select max(categoryid) from BMD_DATACATEGORYDEF where parentId=:p1";
    List list = this.findBySql(sql, new Parameter(pid));
    Integer categoryid = 0;
    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i) != null && !"".equals(list.get(i))) {
        categoryid = Integer.valueOf(list.get(i).toString());
      } else {
        categoryid = 0;
      }
    }
    return categoryid;
  }

  public List<Object[]> getDataEleSetCode(int categoryid) {
    String sql =
        "select f.* from (Select d.elesetcode,c.chnname,d.datacode From (Select b.chnname,a.datacode From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or b.ServiceMode=9 Or b.ServiceMode=13)  And  categoryid=:p1) c,BMD_DATASEARCHDEF d Where c.datacode=d.datacode union all "
            + "Select d.elesetcode,c.chnname,d.datacode From (Select b.chnname,a.datacode From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or b.ServiceMode=9 Or b.ServiceMode=13)  And  categoryid=:p2) c,BMD_DATASEARCHDEF d Where c.datacode=d.parentdatacode) f";
    return this.findBySql(sql, new Parameter(categoryid, categoryid));
  }

  public List<Object[]> getDataSearchSetCode(int categoryid) {
    String sql =
        "select f.* from (Select d.searchsetcode,c.chnname,d.datacode From (Select b.chnname,a.datacode From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or b.ServiceMode=9 Or b.ServiceMode=13)  And  categoryid=:p1) c,BMD_DATASEARCHDEF d Where c.datacode=d.datacode union all "
            + "(Select d.searchsetcode,c.chnname,d.datacode From (Select b.chnname,a.datacode From BMD_CATEGORYDATARELT a,bmd_datadef b Where a.datacode=b.datacode "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or b.ServiceMode=9 Or b.ServiceMode=13)  And  categoryid=:p2) c,BMD_DATASEARCHDEF d Where c.datacode=d.parentdatacode)) f";
    return this.findBySql(sql, new Parameter(categoryid, categoryid));
  }

  public List<Object[]> getelesetcodeList(String elesetcode) {
    String sql =
        "select f.* from (Select b.Datacode,a.chnname From BMD_DATASEARCHDEF b,bmd_datadef a  Where  a.datacode=b.datacode  And a.isIncludeSub=0 "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or a.ServiceMode=9 Or a.ServiceMode=13) And b.elesetcode=:p1 Union All "
            + " (Select b.parentdatacode,b.datachnname From BMD_DATASEARCHDEF b,bmd_datadef a  Where  a.datacode=b.parentdatacode  And a.isIncludeSub=1 "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or a.ServiceMode=9 Or a.ServiceMode=13) And b.elesetcode=:p2) f";
    return this.findBySql(sql, new Parameter(elesetcode, elesetcode));
  }

  public List<Object[]> getDataSearchSetList(String searchsetcode) {
    String sql =
        "select f.* from (Select b.Datacode,a.chnname From BMD_DATASEARCHDEF b,bmd_datadef a  Where  a.datacode=b.datacode  And a.isIncludeSub=0 "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or a.ServiceMode=9 Or a.ServiceMode=13) And b.searchsetcode=:p1  Union All "
            + " Select b.parentdatacode,b.datachnname From BMD_DATASEARCHDEF b,bmd_datadef a  Where  a.datacode=b.parentdatacode  And a.isIncludeSub=1 "
            + " And (b.ServiceMode=1 or b.ServiceMode=5 or a.ServiceMode=9 Or a.ServiceMode=13) And b.searchsetcode=:p2) f";
    return this.findBySql(sql, new Parameter(searchsetcode, searchsetcode));
  }

  @Override
  public void save(DataCategoryDef entity) {
    Session session = getSession();
    session.saveOrUpdate(entity);
    //		session.clear();
  }

  public List<DataCategoryDef> findDataReferByPid(String pid) {
    String sql = "from DataCategoryDef where parent.categoryid=:p1";
    return this.find(sql, new Parameter(Integer.parseInt(pid)));
  }

  public List<Object[]> findDataCategory() {
    String sql =
        "select f.* from (select cd.DATACODE as categoryid, dd.chnname as chnname ,cd.CATEGORYID as pid from BMD_CATEGORYDATARELT cd,bmd_datadef dd "
            + "where dd.DATACODE=cd.DATACODE and dd.INVALID='0') f";
    List<Object[]> list = this.findBySql(sql);
    return list;
  }
}
