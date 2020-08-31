package com.thinkgem.jeesite.mybatis.modules.report.dao;

import com.thinkgem.jeesite.mybatis.common.utils.ConnectionPoolFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

/**
 * 周学东软件R文件查询接口Dao
 * @author zhongrongjie
 * @date 2020-3-10
 */
@Repository
public class SearchRDataDao {

    private JdbcTemplate  jdbcTemplate =  ConnectionPoolFactory.getInstance().getJdbcTemplate("xugu");

     public List<Map<String, Object>> selectByMon(Integer v04002){
         String sql = "select * from radi_chn_mut_mmon_tab where v04002="+v04002;
         List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
         return list;
     }

    public List<Map<String, Object>> selectByMPen(Integer v04002,Integer v04290){
        String sql = "select * from radi_chn_mut_mpen_tab where v04002="+v04002 +"and v04290="+v04290;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectByMTen(Integer v04002,Integer v04290){
        String sql = "select * from radi_chn_mut_mten_tab where v04002="+v04002 +"and v04290="+v04290;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectByPen(Integer v04001,Integer v04002,Integer v04290){
        String sql = "select * from radi_chn_mut_pen_tab where v04001="+v04001+"and v04002="+v04002 +"and v04290="+v04290;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectByTen(Integer v04001,Integer v04002,Integer v04290){
        String sql = "select * from radi_chn_mut_ten_tab where v04001="+v04001+"and v04002="+v04002 +"and v04290="+v04290;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectByYer(Integer v04001){
        String sql = "select * from radi_chn_mut_yer_tab where v04001="+v04001;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> selectByDate(Integer v04001,Integer v04002,Integer v04003,Integer v04004,Integer v04005){
        String sql = "select * from radi_dig_chn_mul_tab where v04001="+v04001+"and v04002="+v04002+"and v04003="+v04003+"and v04004="+v04004+"and v04005="+v04005;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

}
