package com.piesat.kettlescheduler.dao;//package com.piesat.kettlescheduler.dao;
//
//import com.piesat.kettlescheduler.model.KJob;
//import com.piesat.kettlescheduler.model.KJobWithBLOBs;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface KJobDao {
//    int deleteByPrimaryKey(Integer jobId);
//
//    int insert(KJobWithBLOBs record);
//
//    int insertSelective(KJobWithBLOBs record);
//
//    KJobWithBLOBs selectByPrimaryKey(Integer jobId);
//
//    List<KJobWithBLOBs> template(KJobWithBLOBs template);
//
//    int updateByPrimaryKeySelective(KJobWithBLOBs record);
//
//    int updateByPrimaryKeyWithBLOBs(KJobWithBLOBs record);
//
//    int updateByPrimaryKey(KJob record);
//}