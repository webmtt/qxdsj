package com.thinkgem.jeesite.mybatis.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupReportsearchconf;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class SupReportsearchconfUploadDao extends BaseDao<SupReportsearchconf> {

    public void upload(List<List<Object>> list){
        String sql="";
        List<String> listSql=new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            String sqlField ="";
            String sqlvalue="";
            List<Object> ob = list.get(i);
            SupReportsearchconf supReportsearchconf = new SupReportsearchconf();
            User user = UserUtils.getUser();
            supReportsearchconf.setCreateBy(user);
            supReportsearchconf.setUpdateBy(user);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            supReportsearchconf.setCreateDate(Timestamp.valueOf(simpleDate.format(new Date())));
            supReportsearchconf.setUpdateDate(Timestamp.valueOf(simpleDate.format(new Date())));
            sqlField ="insert into sup_reportsearchconf(create_by,create_date,update_by,update_date,del_flag";
            sqlvalue+="'"+supReportsearchconf.getCreateBy().getId()+"',";
            sqlvalue+="'"+supReportsearchconf.getCreateDate()+"',";
            sqlvalue+="'"+supReportsearchconf.getCreateBy().getId() +"',";
            sqlvalue+="'"+supReportsearchconf.getUpdateDate()+"',";
            sqlvalue+="'"+0+"'";
            if(ob.size()==5){
                sqlField+= ",id,param_name,param_code,parent_id,remarks";
                sqlvalue+=",'"  +ob.get(0)+ "','"+ob.get(1)+"','"+ob.get(2)+"','"+ob.get(3)+"','"+ob.get(4)+"')";
            }else if(ob.size()==4){
                sqlField+=",id,param_name,param_code,parent_id";
                sqlvalue+=",'"  +ob.get(0)+ "','"+ob.get(1)+"','"+ob.get(2)+"','"+ob.get(3)+"')";
            }
           sql=sqlField+")values( "+sqlvalue;
            listSql.add(sql);
        }
        this.saveTest(listSql);
    }
    private void saveTest(List<String> listSql) {
        Session session=this.getSession();
        for(String sql:listSql) {
          session.createSQLQuery(sql).executeUpdate();
        }
        this.getSession().flush(); // 清理缓存，执行批量插入
        this.getSession().clear(); // 清空缓存中的 对象
    }
}
