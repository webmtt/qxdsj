package com.nmpiesat.idata.interfacedata.dao;

import com.nmpiesat.idata.interfacedata.entity.InterfaceData;
import com.nmpiesat.idata.user.entity.MusicInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * InterfaceDataDAO继承基类
 */
@Repository
public interface InterfaceDataDAO {
    List<InterfaceData> getAllInter(@Param("userid") String userid);

    List<Map<String,Object>> getInterface(@Param("id") String id,@Param("userid") String userid);

    InterfaceData selectByPrimaryKey(@Param("id") String id);

    Map<String,Object> getFactor(@Param("id") String id);

    List<Map<String,Object>> newInterface();

    Map<String, Object> getInterForId(@Param("api") String api);

    List<Map<String, Object>> getEleForInter(@Param("id") String id);

    Map<String, Object> getEleMent(@Param("eleCode") String eleCode);

    Map<String, Object> getAdminCode(@Param("api") String api);

    List<Map<String, Object>> getAdminCodes();

    MusicInfo getMusic(@Param("userid") String userid);

    List<Map<String, Object>> getAllElements();
}