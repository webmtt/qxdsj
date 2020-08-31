package com.piesat.kettlescheduler.dao;//package com.piesat.kettlescheduler.dao;
//
//import com.piesat.kettlescheduler.model.KJobRecord;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface KJobRecordDao {
//    int deleteByPrimaryKey(Integer recordId);
//
//    int insert(KJobRecord record);
//
//    int insertSelective(KJobRecord record);
//
//    KJobRecord selectByPrimaryKey(Integer recordId);
//
//    List<KJobRecord> template(KJobRecord template);
//
//    int updateByPrimaryKeySelective(KJobRecord record);
//
//    int updateByPrimaryKeyWithBLOBs(KJobRecord record);
//
//    int updateByPrimaryKey(KJobRecord record);
//}