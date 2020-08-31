package com.nmpiesat.idata.subject.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nmpiesat.idata.subject.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
@Repository
public interface SubjectDao {
    List<PortalImageRull> getPortalImageRullListByType();

    List<PortalImageProDef> getPortalImageProDefList();

    List<String> getNowPngList(String startime, String endtime, String tablename, String productcode);

    List<String> getNowPngList1(String tablename, String productcode, int defaultcount);

    String getNowPng(String productcode);

    List<NationWarnFileInfo> findNationWarnFile(String startime, String endtime, String warnTypeName);

    List<String> getNowPng1(String tablename, String maxDate, List<String> list);

    DSAccessDef getDSAccessDef(String dSAccessCode);

    List<Object> getProImgMaxDate(String tablename, List<String> list);

    /**
     * 查询数据列表
     * @param
     * @return
     */
    List<SupSubjectdef> findMainList(Integer beginNum,Integer endNum,String userid);
    @DS("slave")
    List<ProductImgDef> findProductImg(@Param("kind") String kind, @Param("procode")String procode, @Param("startTime")String startTime, @Param("endTime")String endTime);

    List<SupSubjectdef> getHomeProduct();

    List<SupSubjectdef> getHotProduct();

    List<SupSubjectdef> getTreeProduct(String id,String userid);

    List<SupSubjectdef> getFeaturesProduct();

    List<String> ispermissions(String userid);

    List<SupSubjectdef> getTreeProductById(String id);

    List<SupSubjectdef> getTreeProductFree(List<String> list);

    SupSubjectdef getProductById(String id);
}
