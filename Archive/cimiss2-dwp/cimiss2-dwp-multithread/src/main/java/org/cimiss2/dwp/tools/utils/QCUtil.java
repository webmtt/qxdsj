package org.cimiss2.dwp.tools.utils;

import cma.cimiss2.dpc.decoder.StationData;
import com.hitec.bufr.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ************************************
 * @ClassName: QCUtil
 * @Auther: dangjinhu
 * @Date: 2019/4/1 11:46
 * @Description: 台站气候学界限值util
 * @Copyright: All rights reserver.
 * ************************************
 */

public class QCUtil {

    private static String tStation;

    /**
     * 存放风廓线站点与探空站
     * key为风廓线站点
     * value为探空站点
     */
    private static Map<String, String> wtMap = new HashMap();

    /**
     * 存放中国120个探空站数据
     * key为站号
     * value为站点参数值
     */
    private static Map<String, StationData> stationMap = new HashMap<>();

    /**
     * 层号与高度厚度层对应关系
     * 如：层号1 对应 高度厚度层[-220，-56)
     * 最大值时特殊处理：[22696,∞),∞改为Integer.MAX_VALUE
     */
    private static Map<String, Integer[]> bMap = new HashMap(); // 未使用
    /**
     * 分隔高度厚度层成数组
     */
    private static Integer[] bInt;

    /**
     * 存放各探空站点阈值参数
     * 根据站号查找各探空站点阈值参数文件
     * 将参数存放在map
     * key为层号
     * value为某高度厚度层对应的风速界限值(m/s)
     */
    private static Map<Integer, Integer> argMap = new HashMap();

    /**
     * 初始化值
     */
    public static void init() {
        List<Object> configs = Arrays.asList("station/w_t_station.txt", "station/120stationlist.txt", "station/range.txt");
        InputStreamReader is = null;
        BufferedReader reader = null;
        for (int i = 0; i < configs.size(); i++) {
            try {
                // 存wtMap
                if (i == 0) {
                    is = new InputStreamReader(new FileInputStream(StringUtil.getConfigPath() + configs.get(i)),"UTF-8");
                    reader = new BufferedReader(is);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (StringUtils.isBlank(line)) {
                            continue;
                        }
                        String[] values = line.trim().split("\\s+");
                        if (values.length == 3) {
                            wtMap.put(values[1].trim(), values[2].trim());
                        } else {
                            System.out.println(line);
                        }
                    }
                    continue;
                }
                // 存stationMap
                if (i == 1) {
                    is = new InputStreamReader(new FileInputStream(StringUtil.getConfigPath() + configs.get(i)),"UTF-8");
                    reader = new BufferedReader(is);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (StringUtils.isBlank(line)) {
                            continue;
                        }
                        String[] values = line.trim().split("\\s+");
                        if (values.length == 4) {
                            StationData data = new StationData();
                            stationMap.put(values[0].trim(), data);
                            data.setLongitude(Double.parseDouble(values[1].trim()));
                            data.setLatitude(Double.parseDouble(values[2].trim()));
                            data.setAltitude(Double.parseDouble(values[3].trim()));
                        } else {
                            System.out.println(line);
                        }

                    }
                    continue;
                }
                // 存bMap
                if (i == 2) {
                    is = new InputStreamReader(new FileInputStream(StringUtil.getConfigPath() + configs.get(i)),"UTF-8");
                    reader = new BufferedReader(is);
                    String line;
                    int ii = 0;
                    if (bInt == null) {
                        bInt = new Integer[64];
                    }
                    while ((line = reader.readLine()) != null) {
                        if (StringUtils.isBlank(line)) {
                            continue;
                        }
                        String[] values = line.trim().split("\\s+");
                        if (values.length == 2) {
                            String[] val = values[1].substring(1, values[1].length() - 1).split(",");
                            Integer[] cel = new Integer[2];
                            if (val.length == 2) {
                                bInt[ii] = Integer.parseInt(val[0]);
                                ii++;
                                cel[0] = Integer.parseInt(val[0].trim());
                                if (val[1].trim().equals("∞")) {
                                    cel[1] = Integer.MAX_VALUE;
                                } else {
                                    cel[1] = Integer.parseInt(val[1].trim());
                                }
                            } else {
                                System.out.println(line);
                            }
                            bMap.put(values[0].trim(), cel);
                        } else {
                            System.out.println(line);
                        }
                    }
                }
            } catch (IOException e) {
                close(is, reader);
            }
        }
    }

    /**
     * 是否存在对应站点
     * 并初始化探空站点对应阈值表
     * @param wStaion
     * @return
     */
    public static boolean isExist(String wStaion,StringBuffer log) {
    	if(wtMap==null){
    		log.append("wtMap为空");
    	}
        if (wtMap.containsKey(wStaion)) {
            tStation = wtMap.get(wStaion);
            setStation(tStation);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据探空站点查找界限值参数文件
     * @param station
     */
    private static void setStation(String station) {
        String name = "station/limit_value/SQCLimit-Wsp-" + station + ".txt";
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            // 存argMap
            is = new InputStreamReader(new FileInputStream(StringUtil.getConfigPath() + name),"UTF-8");
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                String[] values = line.trim().split("\\s+");
                if (values.length == 2) {
                    argMap.put(Integer.parseInt(values[0].trim()), Integer.parseInt(values[1].trim()));
                } else {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            close(is, reader);
        }
    }

    /**
     * 获取站点对象
     * @return
     */
    public static StationData getTStation() {
        return stationMap.get(tStation);
    }

    /**
     * 根据高度厚度层查找序号
     * 根据序号读取风速界值
     * @param hight
     * @return
     */
    public static int getWSpeed(int hight) {
        if (hight < -220){
            return 999999;
        }
        if (bInt != null && bInt.length == 64 && argMap.size() == 64) {
            int low = 0;
            int high = bInt.length - 1;
            int mid;
            while (low <= high) {
                mid = (low + high) >>> 1;
                if (bInt[mid] <= hight)
                    low = mid + 1;
                else
                    high = mid - 1;
            }
            return argMap.get(low); // 根据层号查找风速
        } else {
            return -1; // 数组未初始化
        }
    }

    /**
     * @param is
     * @param reader
     */
    private static void close(InputStreamReader is, BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

 /*   public static void main(String[] args) {
        // 质控步骤
        // 1 初始化值
        init();
        setStation("50527");
        getWSpeed(22697);
        // 2 根据风廓线雷达站号查找探空站
        if (true) {
            // 3 根据探空站号查找阈值参数文件
            // 4 根据高度厚度获取层号
            // 5 根据层号查找风速界值
            // 6 判断风速是否超出界值
        } else {
            // 未找到对应探空站
        }
    }*/

}



