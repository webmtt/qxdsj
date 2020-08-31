package com.piesat.kettlescheduler.service;

import com.piesat.kettlescheduler.mapper.KJobDao;
import com.piesat.kettlescheduler.mapper.KmKettleLogDao;
import com.piesat.kettlescheduler.model.KmKettleLog;
import com.piesat.kettlescheduler.model.vo.DataDirectoryExchangeVO;
import com.piesat.kettlescheduler.model.vo.DataResourceExchangeVO;
import com.piesat.kettlescheduler.model.vo.ResourceDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @title: kettle-scheduler->CloudIndexService
 * @description: 云上北疆-前端-数据统计
 * @author: YuWenjie
 * @date: 2020-04-29 11:32
 **/

@Service
public class CloudIndexService {

    @Autowired
    private KJobDao kJobDao;
    @Autowired
    private KmKettleLogDao kmKettleLogDao;

    public DataDirectoryExchangeVO countDataDirectoryExchange() {
        DataDirectoryExchangeVO dataDirectoryExchangeVO = new DataDirectoryExchangeVO();

        // 只统计作业，不统计转换，作业里可能包含转换，所有的转换用作业执行
        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        Long pushFileCount = kJobDao.countByCategoryId(1);
        Long pushTableCount = kJobDao.countByCategoryId(2);
        Long pushInterfaceCount = kJobDao.countByCategoryId(3);
        Long gainFileCount = kJobDao.countByCategoryId(4);
        Long gainTableCount = kJobDao.countByCategoryId(5);
        Long gainInterfaceCount = kJobDao.countByCategoryId(6);

        // 推送
        dataDirectoryExchangeVO.setPushFileCount(pushFileCount);
        dataDirectoryExchangeVO.setPushTableCount(pushTableCount);
        dataDirectoryExchangeVO.setPushInterfaceCount(pushInterfaceCount);
        // 获取
        dataDirectoryExchangeVO.setGainFileCount(gainFileCount);
        dataDirectoryExchangeVO.setGainTableCount(gainTableCount);
        dataDirectoryExchangeVO.setGainInterfaceCount(gainInterfaceCount);

        return dataDirectoryExchangeVO;
    }

    public DataResourceExchangeVO countDataResourceExchange() {
        DataResourceExchangeVO dataResourceExchangeVO = new DataResourceExchangeVO();

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // new Date()为获取当前系统时间
        String date = df.format(new Date());
        String startDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";

        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        Long pushFileToday = kmKettleLogDao.countByDateAndCategoryId(1, startDate, endDate);
        Long gainFileToday = kmKettleLogDao.countByDateAndCategoryId(4, startDate, endDate);
        Long pushFileAll = kmKettleLogDao.countByDateAndCategoryId(1, null, null);
        Long gainFileAll = kmKettleLogDao.countByDateAndCategoryId(4, null, null);

        Long pushTableToday = kmKettleLogDao.countByDateAndCategoryId(2, startDate, endDate);
        Long gainTableToday = kmKettleLogDao.countByDateAndCategoryId(5, startDate, endDate);
        Long pushTableAll = kmKettleLogDao.countByDateAndCategoryId(2, null, null);
        Long gainTableAll = kmKettleLogDao.countByDateAndCategoryId(5, null, null);

        Long pushInterfaceToday = kmKettleLogDao.countByDateAndCategoryId(3, startDate, endDate);
        Long gainInterfaceToday = kmKettleLogDao.countByDateAndCategoryId(6, startDate, endDate);
        Long pushInterfaceAll = kmKettleLogDao.countByDateAndCategoryId(3, null, null);
        Long gainInterfaceAll = kmKettleLogDao.countByDateAndCategoryId(6, null, null);

        // 文件
        dataResourceExchangeVO.setPushFileToday(pushFileToday);
        dataResourceExchangeVO.setGainFileToday(gainFileToday);
        dataResourceExchangeVO.setPushFileAll(pushFileAll);
        dataResourceExchangeVO.setGainFileAll(gainFileAll);
        // 库表
        dataResourceExchangeVO.setPushTableToday(pushTableToday);
        dataResourceExchangeVO.setGainTableToday(gainTableToday);
        dataResourceExchangeVO.setPushTableAll(pushTableAll);
        dataResourceExchangeVO.setGainTableAll(gainTableAll);
        // 接口
        dataResourceExchangeVO.setPushInterfaceToday(pushInterfaceToday);
        dataResourceExchangeVO.setGainInterfaceToday(gainInterfaceToday);
        dataResourceExchangeVO.setPushInterfaceAll(pushInterfaceAll);
        dataResourceExchangeVO.setGainInterfaceAll(gainInterfaceAll);

        return dataResourceExchangeVO;
    }

    public ResourceDetailsVO countFileResourceExchangeDetails(String startDate, String endDate) {
        // 初始化时间轴
        Map<String, Long> pushFileDateMap = initDate(startDate, endDate);
        Map<String, Long> gainFileDateMap = initDate(startDate, endDate);

        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        List<KmKettleLog> pushFile = kmKettleLogDao.countByDateAndCategoryIdGroupByDate(1, startDate, endDate);
        List<KmKettleLog> gainFile = kmKettleLogDao.countByDateAndCategoryIdGroupByDate(4, startDate, endDate);

        // 赋值对应的时间轴 接收量、推送量
        ResourceDetailsVO fileResourceDetailsVO = assignValue(pushFileDateMap, gainFileDateMap, pushFile, gainFile);
        return fileResourceDetailsVO;
    }

    public ResourceDetailsVO countTableResourceExchangeDetails(String startDate, String endDate) {
        // 初始化时间轴
        Map<String, Long> pushFileDateMap = initDate(startDate, endDate);
        Map<String, Long> gainFileDateMap = initDate(startDate, endDate);

        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        List<KmKettleLog> pushFile = kmKettleLogDao.countByDateAndCategoryIdGroupByDate(2, startDate, endDate);
        List<KmKettleLog> gainFile = kmKettleLogDao.countByDateAndCategoryIdGroupByDate(5, startDate, endDate);

        // 赋值对应的时间轴 接收量、推送量
        ResourceDetailsVO fileResourceDetailsVO = assignValue(pushFileDateMap, gainFileDateMap, pushFile, gainFile);
        return fileResourceDetailsVO;
    }

    public ResourceDetailsVO countInterfaceResourceExchangeDetails(String startDate, String endDate) {
        // 初始化时间轴
        Map<String, Long> pushFileDateMap = initDate(startDate, endDate);
        Map<String, Long> gainFileDateMap = initDate(startDate, endDate);

        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        List<KmKettleLog> pushFile = kmKettleLogDao.countByDateAndCategoryIdGroupByDate(3, startDate, endDate);
        List<KmKettleLog> gainFile = kmKettleLogDao.countByDateAndCategoryIdGroupByDate(6, startDate, endDate);

        // 赋值对应的时间轴 接收量、推送量
        ResourceDetailsVO fileResourceDetailsVO = assignValue(pushFileDateMap, gainFileDateMap, pushFile, gainFile);
        return fileResourceDetailsVO;
    }

    /**
     * 赋值 资源交换详情 对应的时间轴 接收量、推送量
     *
     * @param pushDateMap 推送量Map
     * @param gainDateMap 接收量Map
     * @param pushData    推送量List
     * @param gainData    接收量List
     * @return ResourceDetailsVO
     * @author YuWenjie
     * @date 2020/5/7 14:34
     **/
    private ResourceDetailsVO assignValue(Map<String, Long> pushDateMap, Map<String, Long> gainDateMap, List<KmKettleLog> pushData, List<KmKettleLog> gainData) {
        ResourceDetailsVO fileResourceDetailsVO = new ResourceDetailsVO();
        List<String> dateList = new ArrayList<>();
        List<Long> pushDataList = new ArrayList<>();
        List<Long> gainDataList = new ArrayList<>();

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // 赋值对应的时间轴 接收量、推送量
        for (KmKettleLog kmKettleLog : pushData) {
            pushDateMap.put(df.format(kmKettleLog.getDate()), kmKettleLog.getSumCount());
        }
        for (KmKettleLog kmKettleLog : gainData) {
            gainDateMap.put(df.format(kmKettleLog.getDate()), kmKettleLog.getSumCount());
        }

        // 替换为list 时间轴 接收量、推送量
        for (Map.Entry<String, Long> entry : pushDateMap.entrySet()) {
            dateList.add(entry.getKey());
            pushDataList.add(entry.getValue());
        }
        for (Map.Entry<String, Long> entry : gainDateMap.entrySet()) {
            gainDataList.add(entry.getValue());
        }

        fileResourceDetailsVO.setDateList(dateList);
        fileResourceDetailsVO.setPushDataList(pushDataList);
        fileResourceDetailsVO.setGainDataList(gainDataList);
        return fileResourceDetailsVO;
    }

    /**
     * 初始化时间轴
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return java.util.Map<java.lang.String,java.lang.Long>
     * @author YuWenjie
     * @date 2020/5/7 14:27
     **/
    private Map<String, Long> initDate(String startDate, String endDate) {
        Map<String, Long> pushFileDateMap = new LinkedHashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        try {
            startDay.setTime(sdf.parse(startDate));
            endDay.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int daySub = startDay.compareTo(endDay);
        while (daySub <= 0) {
            String dateStr = sdf.format(startDay.getTime());
            startDay.add(Calendar.DAY_OF_MONTH, 1);
            daySub = startDay.compareTo(endDay);
            pushFileDateMap.put(dateStr, 0L);
        }
        return pushFileDateMap;
    }
}
