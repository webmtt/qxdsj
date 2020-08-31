package com.piesat.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.core.dto.BootTablePage;
import com.piesat.core.mapper.KDataDao;
import com.piesat.core.model.KData;
import com.piesat.core.model.KTrans;
import com.piesat.web.utils.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DataService {
    @Autowired
    private KDataDao kDataDao;

    @Value("${idata.url}")
    private String idataUrl;


    public List<KData> getList(Integer uId) {
        List<KData> resultList = new ArrayList<KData>();
        KData kData = new KData();
        kData.setDelFlag(1);
        kData.setAddUser(uId);
        resultList.addAll(kDataDao.template(kData));
        return resultList;
    }

    /**
      * 分页条件查询
      *
      * @param start
      * @param size
      * @param uId
      * @param condition
      * @return com.piesat.core.dto.BootTablePage
      * @author YuWenjie
      * @date 2020/7/24 15:46
      **/
    public BootTablePage getList(Integer start, Integer size, Integer uId, KData condition) {
        KData kData = new KData();
        kData.setDelFlag(1);
        kData.setAddUser(uId);

        if (condition != null) {
            kData.setDataName(condition.getDataName());
            if (StringUtils.isNotEmpty(condition.getDataName())) {
                kData.setDataName(condition.getDataName());
            }
            if (StringUtils.isNotEmpty(condition.getDataBigClass()) && !"无".equals(condition.getDataBigClass())) {
                kData.setDataBigClass(condition.getDataBigClass());
            }
            if (StringUtils.isNotEmpty(condition.getDataSmallClass()) && !"无".equals(condition.getDataSmallClass())) {
                kData.setDataSmallClass(condition.getDataSmallClass());
            }
            if (StringUtils.isNotEmpty(condition.getDataServiceObject())) {
                kData.setDataServiceObject(condition.getDataServiceObject());
            }
        }

//        List<KData> kQuartzList = kDataDao.template(kData, start, size);
//        long allCount = kDataDao.templateCount(kData);
        List<KData> kQuartzList = kDataDao.pageQuery(kData, start, size);
        Long allCount = kDataDao.allCount(kData);
        BootTablePage bootTablePage = new BootTablePage();
        bootTablePage.setRows(kQuartzList);
        bootTablePage.setTotal(allCount);
        return bootTablePage;
    }

    public KData getQuartz(Integer dataId) {
        return kDataDao.single(dataId);
    }

    public void insert(KData kData, Integer uId) {
        kData.setAddTime(new Date());
        kData.setAddUser(uId);
        kData.setEditTime(new Date());
        kData.setEditUser(uId);
        kData.setDelFlag(1);
        kDataDao.insert(kData);
    }

    public void delete(Integer dataId) {
        KData kData = kDataDao.unique(dataId);
        kData.setDelFlag(0);
        kDataDao.updateById(kData);
    }


    public void update(KData kData, Integer uId) {
        kData.setEditTime(new Date());
        kData.setEditUser(uId);
        //只有不为null的字段才参与更新
        kDataDao.updateTemplateById(kData);
    }

    public boolean IsCategoryExist(Integer dataId, String dataName) {
        KData template = new KData();
        template.setDelFlag(1);
        template.setDataName(dataName);
        KData kData = kDataDao.templateOne(template);
        if (null == kData) {
            return false;
        } else if (dataId != null && kData.getDataId() == dataId) {
            return false;
        } else {
            return true;
        }
    }

    public List<KData> getAll() {
        return kDataDao.all();
    }

    /**
     * 获取资料大类
     *
     * @return java.util.List<java.lang.String>
     * @author YuWenjie
     * @date 2020/7/21 11:10
     **/
    public List<String> getDataBigClassList() {
        List<String> bigClassList = new ArrayList<>();
        String bigClassURL = "data/getBigData";
        try {
            String bigClassListS = HttpUtil.getData(idataUrl + bigClassURL, null);
            JSONObject object = JSONObject.parseObject(bigClassListS);
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                bigClassList.add(jsonArray.getJSONObject(i).getString("chnname"));
            }
        } catch (Exception e) {

        }
        return bigClassList;
    }

    /**
     * 获取资料小类
     *
     * @param dataBigClass 资料大类
     * @return java.util.List<java.lang.String>
     * @author YuWenjie
     * @date 2020/7/21 11:11
     **/
    public List<String> getDataSmallClassList(String dataBigClass) {

        HashMap<String, String> params = new HashMap<>();
        params.put("name", dataBigClass);
        List<String> smallClassList = new ArrayList<>();
        String smallClassURL = "data/getSmallData";
        try {
            String smallClassListS = HttpUtil.getData(idataUrl + smallClassURL, params);
            JSONObject object = JSONObject.parseObject(smallClassListS);
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                smallClassList.add(jsonArray.getJSONObject(i).getString("chnname"));
            }
        } catch (Exception e) {

        }
        return smallClassList;
    }
}
