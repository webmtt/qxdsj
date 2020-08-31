package com.piesat.kettlescheduler.dao;//package com.piesat.kettlescheduler.dao;
//
//import com.piesat.kettlescheduler.model.KTrans;
//import com.piesat.kettlescheduler.model.KTransWithBLOBs;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface KTransDao {
//    int deleteByPrimaryKey(Integer transId);
//
//    int insert(KTransWithBLOBs record);
//
//    int insertSelective(KTransWithBLOBs record);
//
//    KTransWithBLOBs selectByPrimaryKey(Integer transId);
//
//    List<KTransWithBLOBs> template(KTransWithBLOBs template);
//
//    int updateByPrimaryKeySelective(KTransWithBLOBs record);
//
//    int updateByPrimaryKeyWithBLOBs(KTransWithBLOBs record);
//
//    int updateByPrimaryKey(KTrans record);
//}