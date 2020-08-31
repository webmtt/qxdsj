package cma.cimiss2.dpc.indb.core.etl.mysqlETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.core.tools.StationInfo;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutTxDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 多线程和storm etl 父类
 *
 * @param <T>
 */
@IocBean
public abstract class AbstractEtlNew<T> implements Serializable {

    private static final long serialVersionUID = 4927559591371928671L;


    /**
     * rdb 库
     */
    protected static Dao rdb;
    /**
     * cimiss 库
     */
    protected static Dao cimiss;
    /**
     * 站点信息文件路径 多线程不需要传值
     */
    protected String path = "";
    //    protected Map<String, Object> proMap = StationInfo1.getProMap(path);
    /**
     * 站点信息
     */
    private static Map<String, Object> proMap;
    /**
     * 正确数量
     */
    ThreadLocal<Integer> errorNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };
    /**
     * 错误数量
     */
    ThreadLocal<Integer> correctNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };


    /**
     * 解码数据类型 rabbit的队列名
     */
    private String event = null;
    /**
     * 文件
     */
    protected File file = null;

    /**
     * 批量数量
     */
    protected int batchSize;
    /**
     * 数据库选择
     */
    protected String dataSource;
    /**
     * 站点信息刷新频率
     */
    protected Long intervalTime;

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public AbstractEtlNew() {
    }

    abstract ParseResult<T> extract(File file);

    abstract StatTF transform(ParseResult<T> beans, File file, String... tables);
    public Map<String, Object> getStationInfo() {
        if (proMap == null || proMap.size() == 0) {
            proMap = StationInfo.getProMap(path);
        }

        long judgeEndTime = System.currentTimeMillis();
        if (judgeEndTime - StationInfo.getJudgeTime() > intervalTime) {
            String jarWholePath;
            if ("".equals(path)) {
                jarWholePath = ConfigurationManager.getConfigPath() + "StationInfo_Config.lua";
            } else {
                jarWholePath = path + "config/StationInfo_Config.lua";
            }
            File file = new File(jarWholePath);
            long fileEndTime = file.lastModified();
            long fileStartTime = (Long) proMap.get("time");
            System.out.println("进入reload--" + (fileStartTime - fileEndTime) + "--" + fileStartTime + "--" + fileEndTime);
            if (fileStartTime - fileEndTime != 0) {
                System.out.println("重新加载--StationInfo_Config.lua-");
                LogUtil.info("loggerInfo","重新加载--StationInfo_Config.lua-");
                proMap = StationInfo.getProMap(path);
                proMap.put("time", file.lastModified());//加入文件修改时间
            }

            StationInfo.setJudgeTime(judgeEndTime);
        }

        return proMap;
    }

    /**
     * 站点信息更新
     * 间隔时间到后判断文件修改时间，时间修改重新加载站点信息文件
     *
     * @param intervalTime 间隔时间 毫秒
     */
//    public void reload(Long intervalTime) {
//        long judgeEndTime = System.currentTimeMillis();
//
////        System.out.println("进入reload--" + (judgeEndTime - judgeStartTime) + "--" + intervalTime + "--" + path);
//        if (judgeEndTime - StationInfo.getJudgeTime() > intervalTime) {
//            String jarWholePath;
//            if ("".equals(path)) {
//                jarWholePath = ConfigurationManager.getConfigPath() + "StationInfo_Config.lua";
//            } else {
//                jarWholePath = path + "config/StationInfo_Config.lua";
//            }
//            File file = new File(jarWholePath);
//            long fileEndTime = file.lastModified();
//            long fileStartTime = (Long) getStationInfo().get("time");
//            System.out.println("进入reload--" + (fileStartTime - fileEndTime) + "--" + fileStartTime + "--" + fileEndTime);
//            if (fileStartTime - fileEndTime != 0) {
//                System.out.println("从新加载---");
//                proMap = StationInfo.getProMap(path);
//                proMap.put("time", file.lastModified());//加入文件修改时间
//            }
//
//            StationInfo.setJudgeTime(judgeEndTime);
//        }
//    }


    /**
     * 万里开源 日值特殊处理
     * 根据D_RECORD_ID 先update 如果失败，insert
     *
     * @param datas
     * @return
     */
    public Map<String, Object> loadAndUpdateOrInsert(List<Map<String, Object>> datas) {
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> data = datas.get(i);
            String table = (String) data.get(".table");
            String d_record_id = (String) data.get("D_RECORD_ID");
            int update = 0;
            try {
//                rdb.insertOrUpdate(table, Chain.make("V14032", data.get("V14032")), Cnd.where("D_RECORD_ID", "=", d_record_id));
                update = rdb.update(table, Chain.make("V14032", data.get("V14032")), Cnd.where("D_RECORD_ID", "=", d_record_id));
            } catch (Exception e) {
                System.out.println("update 错误，" + e.getCause());
            }
            if (update == 0) {
                Map<String, Object> insert = rdb.insert(data);
            }
        }
        return null;
    }

    /**
     * 阿里 日值特殊处理
     * 根据D_RECORD_ID 先update 如果失败，insert
     *
     * @param datas
     * @return
     */
    public Map<String, Object> loadAndUpdateOrInsert_ali(List<Map<String, Object>> datas) {
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> data = datas.get(i);
            String table = (String) data.get(".table");
            String d_record_id = (String) data.get("D_RECORD_ID");
            Double v14032 = (Double) data.get("V14032");
            NutTxDao tx1 = new NutTxDao(rdb);
            NutTxDao tx2 = new NutTxDao(rdb);
            try {
                tx1.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
                tx2.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
//tx1.insertOrUpdate(data,null, FieldFilter.create())
//                tx1.execute(Sqls.create("select last_txc_xid()"));
//                int d_record_id1 = tx1.update(table,Chain.make("V14032",v14032), Cnd.where("D_RECORD_ID", "=", d_record_id));
                tx1.execute(Sqls.create("select last_txc_xid()"));
                Map<String, Object> insert = tx1.insert(data);
                tx1.commit();
            } catch (Exception e) {
                tx1.rollback();
                tx2.execute(Sqls.create("select last_txc_xid()"));
                tx2.execute(Sqls.create("UPDATE " + table + " SET V14032 = " + v14032 + "  WHERE D_RECORD_ID = '" + d_record_id + "'"));
                tx2.commit();
            } finally {
                tx1.close();
                tx2.close();
            }
        }
        return null;
    }

    /**
     * 阿里批量入库
     *
     * @param datas
     * @param file
     * @param dao
     * @return
     */
    private Map<String, List<RestfulInfo>> insert_ali(List<Map<String, Object>> datas, String path, StringBuffer buffer, File file, String dao) {
        Map<String, List<RestfulInfo>> eidiInfo = new HashMap<String, List<RestfulInfo>>();
        List<RestfulInfo> eiInfo = new ArrayList<>();
        List<RestfulInfo> diInfo = new ArrayList<>();
        NutTxDao tx1 = null;
        if ("rdb".equals(dao)) {
            tx1 = new NutTxDao(rdb);
        } else if ("cimiss".equals(dao)) {
            tx1 = new NutTxDao(cimiss);
        }
        String table = null;
        String D_RECORD_ID = null;
        try {
//            tx1.beginRC();
            tx1.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
            tx1.execute(Sqls.create("select last_txc_xid()"));
            tx1.fastInsert(datas);
            tx1.commit();
            System.out.println("大批量成功1 数量：" + datas.size());
            correctNum.set(correctNum.get() + datas.size());/////
            for (int i = 0; i < datas.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
//                map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
//                map.put("time", new SimpleDateFormat("yyyyMMddHHmmss").format(datas.get(i).get("D_DATETIME")));
                if(datas.get(i).get("V01301")!=null){
                	map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
                }
                if(datas.get(i).get("D_DATETIME")!=null){
                	map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                }
                map.put("fileName", file.getName());
                map.put("event_type", event);
                map.put("event_type1", String.valueOf(datas.get(i).get("D_DATA_ID")));
                map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                map.put("height", String.valueOf(datas.get(i).get("V07001")));
                map.put("file_size", String.valueOf(file.length()));
                diInfo.addAll(DIEISender.makeDI(map));
            }
        } catch (Throwable e) {
            tx1.rollback();
            tx1.close();
            for (int i = 0; i < datas.size(); i++) {
                Map<String, Object> data = datas.get(i);
                table = (String) data.get(".table");
                D_RECORD_ID = (String) data.get("D_RECORD_ID");
                try {
                    if ("rdb".equals(dao)) {
                        rdb.insert(data);
                    } else if ("cimiss".equals(dao)) {
                        cimiss.insert(data);
                    }
                    System.out.println("单条插入成功:" + data.get("V01301"));
                    HashMap<String, String> map = new HashMap<>();
                    if(datas.get(i).get("V01301")!=null){
                    	map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
                    }
                    if(datas.get(i).get("D_DATETIME")!=null){
                    	map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                    }
                    map.put("fileName", file.getName());
                    map.put("event_type", event);
                    map.put("event_type1", String.valueOf(datas.get(i).get("D_DATA_ID")));
                    map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                    map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                    map.put("height", String.valueOf(datas.get(i).get("V07001")));
                    map.put("file_size", String.valueOf(file.length()));
                    diInfo.addAll(DIEISender.makeDI(map));
                    correctNum.set(correctNum.get() + 1);
                } catch (Exception e1) {
                    insertError_ali(file, data, buffer, eiInfo, diInfo, e1, dao);
                }
            }
        } finally {
            if (tx1 != null) {
                tx1.close();
            }
        }
        eidiInfo.put("di", diInfo);
        eidiInfo.put("ei", eiInfo);
        return eidiInfo;
    }

    private void insertError_ali(File file, Map<String, Object> data, StringBuffer buffer, List<RestfulInfo> eiInfo, List<RestfulInfo> diInfo, Exception e1, String dao) {
        if ("A.0001.0030.R001".equals(getEvent())) {//日值
//        if ("d".equals(getEvent())) {//日值
            String table = (String) data.get(".table");
            String D_RECORD_ID = (String) data.get("D_RECORD_ID");
            data.remove(".table");
            data.remove("D_RECORD_ID");
            data.remove("V01301");
            int update = 1;
            if ("rdb".equals(dao)) {
                NutTxDao tx1 = new NutTxDao(rdb);
                try {
                    tx1.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
                    tx1.execute(Sqls.create("select last_txc_xid()"));
                    update = tx1.update(table, Chain.from(data), Cnd.where("D_RECORD_ID", "=", D_RECORD_ID));
                    tx1.commit();
                } catch (Exception e) {
                    tx1.rollback();
                    System.out.println("日值信息单条更新错误1:" + file.getPath());
                    buffer.append("ERROR :" + "----" + e.getMessage() + "\n");
                    errorNum.set(errorNum.get() + 1);
                    try {
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("message", e1.getMessage());
                        map1.put("path", path);
                        eiInfo.addAll(DIEISender.makeEI(map1));
                    } catch (Exception e2) {
                        System.out.println("ei error!");
                    }
                } finally {
                    tx1.close();
                }
                if (update == 0) {
                    System.out.println("日值信息单条更新错误2:" + file.getPath());
                    buffer.append("ERROR :" + "---- update error" + "\n");
                    errorNum.set(errorNum.get() + 1);
                    try {
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("message", e1.getMessage());
                        map1.put("path", path);
                        eiInfo.addAll(DIEISender.makeEI(map1));
                    } catch (Exception e) {
                        System.out.println("ei error!");
                    }
                } else {
                    correctNum.set(correctNum.get() + 1);
                }
                data.put(".table", table);
            } else {
                System.out.println("单条插入错误:" + file.getPath() + "--" + data.get("V01301") + "--" + data.get("D_RECORD_ID"));
                buffer.append("ERROR :" + "----" + e1.getMessage() + "\n");
                errorNum.set(errorNum.get() + 1);
                try {
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("message", e1.getMessage());
                    map1.put("path", path);
                    eiInfo.addAll(DIEISender.makeEI(map1));
                } catch (Exception e) {
                    System.out.println("ei error!");
                }
            }

        } else if ("A.0001.0028.R001".equals(getEvent())) {//区域站
            System.out.println("单条插入错误:" + file.getPath() + "--" + data.get("V01301") + "--" + data.get("D_RECORD_ID"));
            buffer.append("ERROR :" + "----" + e1.getMessage() + "\n");
            errorNum.set(errorNum.get() + 1);
            try {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("message", e1.getMessage());
                map1.put("path", path);
                eiInfo.addAll(DIEISender.makeEI(map1));
            } catch (Exception e) {
                System.out.println("ei error!");
            }
        } else {
//            System.out.println("单条插入错误:" + file.getPath() + "--" + data.get("V01301") + "--" + data.get("D_RECORD_ID"));
            buffer.append("ERROR :" + "----" + e1.getMessage() + "\n");
            errorNum.set(errorNum.get() + 1);
            try {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("message", e1.getMessage());
                map1.put("path", path);
                eiInfo.addAll(DIEISender.makeEI(map1));


                HashMap<String, String> map = new HashMap<>();
                map.put("stationNumberChina", (String) data.get("V01301"));///发rabbit需要主键，就是time_stationaNumberChina 主要注意
                map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.get("D_DATETIME")));
                map.put("fileName", file.getName());
                map.put("event_type", event);
                map.put("event_type1", String.valueOf(data.get("D_DATA_ID")));
                map.put("longitude", String.valueOf(data.get("V06001")));
                map.put("latitude", String.valueOf(data.get("V05001")));
                map.put("height", String.valueOf(data.get("V07001")));
                map.put("file_size", String.valueOf(file.length()));
                diInfo.addAll(DIEISender.makeDI_fail(map));

            } catch (Exception e) {
                System.out.println("ei error!");
            }
        }
    }

    /**
     * 统一数据入库方法，根据getDataSource（）判断使用哪个数据库、万里 1 阿里 0
     *
     * @param datas 解码数据的map集合
     * @param dao   数据源选择  rdb  cimiss
     * @return eiInfo, diInfo, correctNum, errorNum, buffer
     */
    public StatInsert load(List<Map<String, Object>> datas, String dao) {
        StatInsert statInsert = new StatInsert();
//        //日志
        StringBuffer buffer = new StringBuffer();
//        //返回数量
        errorNum.set(0);
        correctNum.set(0);

        if (datas.size() <= batchSize) {
            Map<String, List<RestfulInfo>> subEIDIInfo = null;

            subEIDIInfo = subload(datas, path, buffer, file, dao);


            statInsert.getDiInfo().addAll(subEIDIInfo.get("di"));
            statInsert.getEiInfo().addAll(subEIDIInfo.get("ei"));
        } else {
            int ceil = (int) Math.ceil(datas.size() / Double.valueOf(batchSize));
            for (int i = 0; i < ceil; i++) {
                if (i == ceil - 1) {
                    List<Map<String, Object>> subList = datas.subList(i * batchSize, datas.size());
                    Map<String, List<RestfulInfo>> subEIDIInfo = null;

                    subEIDIInfo = subload(subList, path, buffer, file, dao);

                    statInsert.getDiInfo().addAll(subEIDIInfo.get("di"));
                    statInsert.getEiInfo().addAll(subEIDIInfo.get("ei"));
                    continue;
                }
                List<Map<String, Object>> subList = datas.subList(i * batchSize, (i + 1) * (batchSize));
                Map<String, List<RestfulInfo>> subEIDIInfo = null;

                subEIDIInfo = subload(subList, path, buffer, file, dao);

                statInsert.getDiInfo().addAll(subEIDIInfo.get("di"));
                statInsert.getEiInfo().addAll(subEIDIInfo.get("ei"));
            }
        }
        statInsert.setErrorNum(errorNum.get());
        statInsert.setCorrectNum(correctNum.get());
        statInsert.setBuffer(buffer);
        return statInsert;
    }

    private Map<String, List<RestfulInfo>> subload(List<Map<String, Object>> datas, String path, StringBuffer
            buffer, File file, String dao) {
        Map<String, List<RestfulInfo>> eidiInfo = new HashMap<String, List<RestfulInfo>>();
        List<RestfulInfo> eiInfo = new ArrayList<>();
        List<RestfulInfo> diInfo = new ArrayList<>();
        try {
            if ("rdb".equals(dao)) {
                if ("1".equals(dataSource)) {
                    rdb.fastInsert(datas);
                    correctNum.set(correctNum.get() + datas.size());/////
                } else if ("0".equals(dataSource)) {
                    Map<String, List<RestfulInfo>> subEIDIInfo = insert_ali(datas, path, buffer, file, dao);
                    diInfo.addAll(subEIDIInfo.get("di"));
                    eiInfo.addAll(subEIDIInfo.get("ei"));
                } else {
                    System.out.println("getDataSource error!");
                }
            } else if ("cimiss".equals(dao)) {
                if ("1".equals(dataSource)) {
                    cimiss.fastInsert(datas);
                    correctNum.set(correctNum.get() + datas.size());/////
                } else if ("0".equals(dataSource)) {
                    Map<String, List<RestfulInfo>> subEIDIInfo = insert_ali(datas, path, buffer, file, dao);
                    diInfo.addAll(subEIDIInfo.get("di"));
                    eiInfo.addAll(subEIDIInfo.get("ei"));
                } else {
                    System.out.println("getDataSource error!");
                }
            }
            System.out.println("大批量成功1 数量：" + datas.size());
            for (int i = 0; i < datas.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
//                map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
//                map.put("time", new SimpleDateFormat("yyyyMMddHHmmss").format(datas.get(i).get("D_DATETIME")));
                if(datas.get(i).get("V01301")!=null){
                	map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
                }
                if(datas.get(i).get("D_DATETIME")!=null){
                	map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                }
                map.put("fileName", file.getName());
                map.put("event_type", event);
                map.put("event_type1", String.valueOf(datas.get(i).get("D_DATA_ID")));
                map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                map.put("height", String.valueOf(datas.get(i).get("V07001")));
                map.put("file_size", String.valueOf(file.length()));
                diInfo.addAll(DIEISender.makeDI(map));

            }
        } catch (Exception e) {
            int count = 100;//小批量次数100
            int ceil = (int) Math.ceil(datas.size() / Double.valueOf(count));
            for (int i = 0; i < ceil; i++) {
                if (i == ceil - 1) {
                    List<Map<String, Object>> subList = datas.subList(i * count, datas.size());
                    if ("1".equals(dataSource)) {
                        Map<String, List<RestfulInfo>> subEIDIInfo = insert(subList, path, buffer, file, dao);
                        diInfo.addAll(subEIDIInfo.get("di"));
                        eiInfo.addAll(subEIDIInfo.get("ei"));
                    } else if ("0".equals(dataSource)) {
                        Map<String, List<RestfulInfo>> subEIDIInfo = insert_ali(datas, path, buffer, file, dao);
                        diInfo.addAll(subEIDIInfo.get("di"));
                        eiInfo.addAll(subEIDIInfo.get("ei"));
                        System.out.println("最后一批成功" + i + " 数量：" + datas.size());
                    } else {
                        System.out.println("getDataSource error!");
                    }
                    continue;
                }
                List<Map<String, Object>> subList = datas.subList(i * count, (i + 1) * (count));

                if ("1".equals(dataSource)) {
                    Map<String, List<RestfulInfo>> subEIDIInfo = insert(subList, path, buffer, file, dao);
                    diInfo.addAll(subEIDIInfo.get("di"));
                    eiInfo.addAll(subEIDIInfo.get("ei"));
                } else if ("0".equals(dataSource)) {
                    Map<String, List<RestfulInfo>> subEIDIInfo = insert_ali(datas, path, buffer, file, dao);
                    diInfo.addAll(subEIDIInfo.get("di"));
                    eiInfo.addAll(subEIDIInfo.get("ei"));
                    System.out.println("阿里小批量成功" + i + " 数量：" + datas.size());
                } else {
                    System.out.println("getDataSource error!");
                }
            }
        }
        eidiInfo.put("di", diInfo);
        eidiInfo.put("ei", eiInfo);
        return eidiInfo;
    }

    /**
     * 万里 入库
     *
     * @param datas
     * @param path
     * @param buffer
     * @param file
     * @param dao
     * @return
     */
    private Map<String, List<RestfulInfo>> insert(List<Map<String, Object>> datas, String path, StringBuffer
            buffer, File file, String dao) {
        List<RestfulInfo> eiInfo = new ArrayList<>();
        List<RestfulInfo> diInfo = new ArrayList<>();
        Map<String, List<RestfulInfo>> retMap = new HashMap<>();
        try {
            if ("rdb".equals(dao)) {
                rdb.fastInsert(datas);
            } else if ("cimiss".equals(dao)) {
                cimiss.fastInsert(datas);
            }
            System.out.println("小批量成功2 数量：" + datas.size() + "--" + file.getPath());
            for (int i = 0; i < datas.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
                map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                map.put("fileName", file.getName());
                map.put("event_type", event);
                map.put("event_type1", String.valueOf(datas.get(i).get("D_DATA_ID")));
                map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                map.put("height", String.valueOf(datas.get(i).get("V07001")));
                map.put("file_size", String.valueOf(file.length()));
                diInfo.addAll(DIEISender.makeDI(map));
            }
            correctNum.set(correctNum.get() + datas.size());
        } catch (Exception e2) {
            for (int i = 0; i < datas.size(); i++) {
                Map<String, Object> data = datas.get(i);
                try {
                    if ("rdb".equals(dao)) {
                        rdb.insert(data);
                    } else if ("cimiss".equals(dao)) {
                        cimiss.insert(data);
                    }
                    System.out.println("单条插入成功:" + data.get("V01301"));
                    HashMap<String, String> map = new HashMap<>();
                    map.put("stationNumberChina", (String) datas.get(i).get("V01301"));///发rabbit需要主键，就是time_stationaNumberChina 主要注意
                    map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                    map.put("fileName", file.getName());
                    map.put("event_type", event);
                    map.put("event_type1", String.valueOf(datas.get(i).get("D_DATA_ID")));
                    map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                    map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                    map.put("height", String.valueOf(datas.get(i).get("V07001")));
                    map.put("file_size", String.valueOf(file.length()));
                    diInfo.addAll(DIEISender.makeDI(map));
                    correctNum.set(correctNum.get() + 1);
                } catch (Exception e1) {
                    insertError(file, data, buffer, eiInfo, diInfo, e1, dao);//
                }
            }
        }
        retMap.put("di", diInfo);
        retMap.put("ei", eiInfo);
        return retMap;
    }


    /**
     * 万里单条插入失败处理逻辑
     *
     * @param file
     * @param data
     * @param buffer
     * @param eiInfo
     * @param e1     异常
     */
    private void insertError(File file, Map<String, Object> data, StringBuffer buffer, List<RestfulInfo> eiInfo, List<RestfulInfo> diInfo, Exception e1, String dao) {

        if ("A.0001.0030.R001".equals(getEvent())) {//日值 查询主键 包含-update（除了14032）
            String table = (String) data.get(".table");
            String D_RECORD_ID = (String) data.get("D_RECORD_ID");
            data.remove(".table");
            data.remove("D_RECORD_ID");
            data.remove("V01301");
            int update = 1;
            if ("rdb".equals(dao)) {
                try {
                    update = rdb.update(table, Chain.from(data), Cnd.where("D_RECORD_ID", "=", D_RECORD_ID));
                } catch (Exception e) {
                    System.out.println("日值信息单条更新错误1:" + file.getPath());
                    buffer.append("ERROR :" + "----" + e.getMessage() + "\n");
                    errorNum.set(errorNum.get() + 1);
                    try {
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("message", e1.getMessage());
                        map1.put("path", path);
                        eiInfo.addAll(DIEISender.makeEI(map1));
                    } catch (Exception e2) {
                        System.out.println("ei error!");
                    }
                }
                if (update == 0) {
                    System.out.println("日值信息单条更新错误2:" + file.getPath());
                    buffer.append("ERROR :" + "---- update error" + "\n");
                    errorNum.set(errorNum.get() + 1);
                    try {
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("message", e1.getMessage());
                        map1.put("path", path);
                        eiInfo.addAll(DIEISender.makeEI(map1));
                    } catch (Exception e) {
                        System.out.println("ei error!");
                    }
                } else {
                    correctNum.set(correctNum.get() + 1);
                }
            } else {
                System.out.println("单条插入错误:" + file.getPath() + "--" + data.get("V01301") + "--" + data.get("D_RECORD_ID"));
                buffer.append("ERROR :" + "----" + e1.getMessage() + "\n");
                errorNum.set(errorNum.get() + 1);
                try {
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("message", e1.getMessage());
                    map1.put("path", path);
                    eiInfo.addAll(DIEISender.makeEI(map1));
                } catch (Exception e) {
                    System.out.println("ei error!");
                }
            }

        } else {
            System.out.println("单条插入错误:" + file.getPath() + "--" + data.get("V01301") + "--" + data.get("D_RECORD_ID"));
            buffer.append("ERROR :" + "----" + e1.getMessage() + "\n");
            errorNum.set(errorNum.get() + 1);
            try {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("message", e1.getMessage());
                map1.put("path", path);
                eiInfo.addAll(DIEISender.makeEI(map1));

                HashMap<String, String> map = new HashMap<>();
                map.put("stationNumberChina", (String) data.get("V01301"));///发rabbit需要主键，就是time_stationaNumberChina 主要注意
                map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.get("D_DATETIME")));
                map.put("fileName", file.getName());
                map.put("event_type", event);
                map.put("event_type1", String.valueOf(data.get("D_DATA_ID")));
                map.put("longitude", String.valueOf(data.get("V06001")));
                map.put("latitude", String.valueOf(data.get("V05001")));
                map.put("height", String.valueOf(data.get("V07001")));
                map.put("file_size", String.valueOf(file.length()));
                diInfo.addAll(DIEISender.makeDI_fail(map));
            } catch (Exception e) {
                System.out.println("ei error!");
            }
        }
    }

    /**
     * 更正报判断
     *
     * @param D_RECORD_ID 主键
     * @param table       表名
     * @param dao         数据库
     */
    public void corrMessage(String D_RECORD_ID, String V_BBB, String table, String dao) {
        if ("1".equals(dataSource)) {
            corrMessage_wali(D_RECORD_ID, V_BBB, table, dao);
        } else if ("0".equals(dataSource)) {
            corrMessage_ali2(D_RECORD_ID, V_BBB, table, dao);
        } else {
            System.out.println("getDataSource error!");
        }
    }

    /**
     * 更正报判断
     *
     * @param D_RECORD_ID 主键
     * @param table       表名
     * @param dao         数据库
     */
    private void corrMessage_wali(String D_RECORD_ID, String V_BBB, String table, String dao) {
        try {
            String HORSelect = "select V_BBB from " + table + " where D_RECORD_ID = '" + D_RECORD_ID + "'";
            String HORDelete = "delete from " + table + " where D_RECORD_ID = '" + D_RECORD_ID + "'";
            Sql HORSelectSql = Sqls.create(HORSelect);
            Sql HORDeleteSql = Sqls.create(HORDelete);
            HORSelectSql.setCallback(new SqlCallback() {
                @Override
                public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                    List<String> list = new LinkedList<String>();
                    while (rs.next())
                        list.add(rs.getString("V_BBB"));
                    return list;
                }
            });
            if ("rdb".equals(dao)) {
                rdb.execute(HORSelectSql);
                List<String> HORList = HORSelectSql.getList(String.class);
                if (!HORList.isEmpty() && HORList.get(0).hashCode() < V_BBB.hashCode()) {
                    rdb.execute(HORDeleteSql);
                }
            } else if ("cimiss".equals(dao)) {
                cimiss.execute(HORSelectSql);
                List<String> HORList = HORSelectSql.getList(String.class);
                if (!HORList.isEmpty() && HORList.get(0).hashCode() < V_BBB.hashCode()) {
                    cimiss.execute(HORDeleteSql);
                }
            }
        } catch (Exception e) {
            System.out.println("更正报处理错误！！");
        }

    }

    /**
     * 更正报判断  阿里库
     * 不通过gts
     *
     * @param D_RECORD_ID 主键
     * @param V_BBB        更正字段
     * @param table       表名
     * @param dao         数据库
     */
    private void corrMessage_ali2(String D_RECORD_ID, String V_BBB, String table, String dao) {
        try {
            String V01301 = D_RECORD_ID.split("_")[1];
            String HORSelect = "select V_BBB from " + table + " where D_RECORD_ID = '" + D_RECORD_ID + "'" + " and V01301=" + "'" + V01301 + "'";
            String HORDelete = "delete from " + table + " where D_RECORD_ID = '" + D_RECORD_ID + "'" + " and V01301=" + "'" + V01301 + "'";
            Sql HORSelectSql = Sqls.create(HORSelect);
            Sql HORDeleteSql = Sqls.create(HORDelete);
            HORSelectSql.setCallback(new SqlCallback() {
                @Override
                public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                    List<String> list = new LinkedList<String>();
                    while (rs.next())
                        list.add(rs.getString("V_BBB"));
                    return list;
                }
            });
            if ("rdb".equals(dao)) {
                rdb.execute(HORSelectSql);
                List<String> HORList = HORSelectSql.getList(String.class);
                if (!HORList.isEmpty() && HORList.get(0).hashCode() < V_BBB.hashCode()) {
                    rdb.execute(HORDeleteSql);
                }
            } else if ("cimiss".equals(dao)) {
                cimiss.execute(HORSelectSql);
                List<String> HORList = HORSelectSql.getList(String.class);
                if (!HORList.isEmpty() && HORList.get(0).hashCode() < V_BBB.hashCode()) {
                    cimiss.execute(HORDeleteSql);
                }
            }
        } catch (Exception e) {
            System.out.println("更正报处理错误！！");
        }

    }

//    /**
//     * 更正报判断  阿里库
//     * 前期测试 通过gts
//     *
//     * @param D_RECORD_ID 主键
//     * @param
//     * @param table       表名
//     * @param dao         数据库
//     */
//    public void corrMessage_ali(String D_RECORD_ID, String V_BBB, String table, String dao) {
//        try {
//            String HORSelect = "select V_BBB from " + table + " where D_RECORD_ID = '" + D_RECORD_ID + "'";
//            String HORDelete = "delete from " + table + " where D_RECORD_ID = '" + D_RECORD_ID + "'";
//            Sql HORSelectSql = Sqls.create(HORSelect);
//            Sql HORDeleteSql = Sqls.create(HORDelete);
//            HORSelectSql.setCallback(new SqlCallback() {
//                @Override
//                public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
//                    List<String> list = new LinkedList<String>();
//                    while (rs.next())
//                        list.add(rs.getString("V_BBB"));
//                    return list;
//                }
//            });
//            if ("rdb".equals(dao)) {
//                NutTxDao tx1 = new NutTxDao(rdb);
//                NutTxDao tx2 = new NutTxDao(rdb);
//                try {
//                    tx1.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
//                    tx2.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
//                    tx1.execute(Sqls.create("select last_txc_xid()"));
//                    tx1.execute(HORSelectSql);
//                    List<String> HORList = HORSelectSql.getList(String.class);
//                    if (!HORList.isEmpty() && HORList.get(0).hashCode() < V_BBB.hashCode()) {
//                        tx2.execute(Sqls.create("select last_txc_xid()"));
//                        tx2.execute(HORDeleteSql);
//                    }
//                    tx1.commit();
//                    tx2.commit();
//                } catch (Exception e) {
//                    tx2.rollback();
//                } finally {
//                    tx1.close();
//                    tx2.close();
//                }
//            } else if ("cimiss".equals(dao)) {
//                NutTxDao tx1 = new NutTxDao(cimiss);
//                NutTxDao tx2 = new NutTxDao(cimiss);
//                try {
//                    tx1.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
//                    tx2.begin(Connection.TRANSACTION_READ_UNCOMMITTED);
//                    tx1.execute(Sqls.create("select last_txc_xid()"));
//                    tx1.execute(HORSelectSql);
//                    List<String> HORList = HORSelectSql.getList(String.class);
//                    if (!HORList.isEmpty() && HORList.get(0).hashCode() < V_BBB.hashCode()) {
//                        tx2.execute(Sqls.create("select last_txc_xid()"));
//                        tx2.execute(HORDeleteSql);
//                    }
//                    tx1.commit();
//                    tx2.commit();
//                } catch (Exception e) {
//                    tx2.rollback();
//                } finally {
//                    tx1.close();
//                    tx2.close();
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("更正报处理错误！！");
//        }
//
//    }

    /**
     * 报文格式化
     *
     * @param reports   报文集合
     * @param D_DATA_ID D_DATA_ID
     * @param file      报文文件
     * @param table     表名
     * @return
     * @throws ParseException
     */
    public ArrayList<Map<String, Object>> transformReports(List<ReportInfo> reports, String D_DATA_ID, File file, String table) throws ParseException {
        //报文集合
        ArrayList<Map<String, Object>> repList = new ArrayList<>();
        //文件名处理
        String fileName = file.getName();
        String[] fileNameSplit = fileName.split("_");
        //报文处理
        for (int i = 0; i < reports.size(); i++) {
            String report = reports.get(i).getReport();
            Map<String, Object> repmap = (Map<String, Object>) reports.get(i).getT();//报文头
            Map<String, Object> repData = new HashMap<>();
            String stationNumberChina = (String) repmap.get("V01301");
            int num = stationNumberChina.substring(0, 1).hashCode();
            String observationTime = (String) repmap.get("D_DATETIME");
            String V_TT = fileNameSplit[6].split("-")[0];
            String V_CCCC = fileNameSplit[3].substring(0, 4);
            String D_RECORD_ID = observationTime + "_" + stationNumberChina + "_" + getEvent() + "_" + V_TT;
            repData.put(".table", table);
            repData.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            repData.put("D_DATA_ID", D_DATA_ID);
            repData.put("D_IYMDHM", new Date());//入库时间
            repData.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            repData.put("D_UPDATE_TIME", new Date());//更新时间
            repData.put("D_DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(observationTime));
            repData.put("V_BBB", repmap.get("V_BBB"));
            repData.put("V_CCCC", V_CCCC);
            repData.put("V_TT", V_TT);//待确定
            repData.put("V01301", repmap.get("V01301"));
            if (num >= 48 & num <= 57) {
                repData.put("V01300", stationNumberChina);
            } else {
                repData.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            repData.put("V05001", repmap.get("V5001"));
            repData.put("V06001", repmap.get("V6001"));
            repData.put("V_NCODE", 1);
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                repData.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                repData.put("V_ACODE", "999999");
            }
            repData.put("V04001", observationTime.substring(0, 4));//资料观测年
            repData.put("V04002", observationTime.substring(4, 6));//资料观测月
            repData.put("V04003", observationTime.substring(6, 8));//资料观测日
            repData.put("V04004", observationTime.substring(8, 10));//资料观测时
            repData.put("V04005", observationTime.substring(10, 12));//资料观测分
            repData.put("V_LEN", report.length());
            repData.put("V_REPORT", report);
            repList.add(repData);
        }
        return repList;
    }


    public int getCloudType1Detail(String cloudType1, int i) {
        char c = cloudType1.charAt(i);
        switch (i) {
            case 0:
                if (c == '/') {
                    return 999999;
                } else {
                    int temp = Integer.parseInt(String.valueOf(c));
                    return (temp + 30);
                }
            case 1:
                if (c == '/') {
                    return 999999;
                } else {
                    int temp = Integer.parseInt(String.valueOf(c));
                    return (temp + 20);
                }
            case 2:
                if (c == '/') {
                    return 999999;
                } else {
                    int temp = Integer.parseInt(String.valueOf(c));
                    return (temp + 10);
                }
            default:
                return -1;
        }
    }
}

