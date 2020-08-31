package com.piesat.kettlescheduler.dao;//package com.piesat.kettlescheduler.dao;
//
//import com.piesat.kettlescheduler.model.KTransRecord;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface KTransRecordDao {
//    int deleteByPrimaryKey(Integer recordId);
//
//    int insert(KTransRecord record);
//
//    int insertSelective(KTransRecord record);
//
//    KTransRecord selectByPrimaryKey(Integer recordId);
//
//    List<KTransRecord> template(KTransRecord template);
//
//    int updateByPrimaryKeySelective(KTransRecord record);
//
//    int updateByPrimaryKeyWithBLOBs(KTransRecord record);
//
//    int updateByPrimaryKey(KTransRecord record);
//}