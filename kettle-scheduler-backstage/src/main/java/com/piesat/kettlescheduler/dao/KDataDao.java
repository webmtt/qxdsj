//package com.piesat.kettlescheduler.dao;
//
//import com.piesat.kettlescheduler.model.KData;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface KDataDao {
//    int deleteByPrimaryKey(Integer dataId);
//
//    int insert(KData record);
//
//    int insertSelective(KData record);
//
//    KData selectByPrimaryKey(Integer dataId);
//
//    List<KData> template(KData template);
//
//    int updateByPrimaryKeySelective(KData record);
//
//    int updateByPrimaryKey(KData record);
//
//    List<KData> pageQuery(KData kData, Integer offset, Integer limit);
//}