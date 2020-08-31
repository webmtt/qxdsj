package com.thinkgem.jeesite.mybatis.modules.report.service;


import com.thinkgem.jeesite.mybatis.modules.report.dao.SearchRDataDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 周学东软件R文件查询接口Service
 * @author zhongrongjie
 * @date 2020-3-10
 */
@Service
public class SearchRDataService {

    @Resource
    private SearchRDataDao searchRDataDao;

    public List<Map<String, Object>> selectByMon(Integer v04002){
        return searchRDataDao.selectByMon(v04002);
    }

    public List<Map<String, Object>> selectByMPen(Integer v04002,Integer v04290){
        return searchRDataDao.selectByMPen(v04002,v04290);
    }

    public List<Map<String, Object>> selectByMTen(Integer v04002,Integer v04290){
        return searchRDataDao.selectByMTen(v04002,v04290);
    }

    public List<Map<String, Object>> selectByPen(Integer v04001,Integer v04002,Integer v04290){
        return searchRDataDao.selectByPen(v04001,v04002,v04290);
    }

    public List<Map<String, Object>> selectByTen(Integer v04001,Integer v04002,Integer v04290){
        return searchRDataDao.selectByTen(v04001,v04002,v04290);
    }

    public List<Map<String, Object>> selectByYer(Integer v04001){
        return searchRDataDao.selectByYer(v04001);
    }

    public List<Map<String, Object>> selectByDate(Integer v04001,Integer v04002,Integer v04003,Integer v04004,Integer v04005){
        return searchRDataDao.selectByDate(v04001,v04002,v04003,v04004,v04005);
    }
}
