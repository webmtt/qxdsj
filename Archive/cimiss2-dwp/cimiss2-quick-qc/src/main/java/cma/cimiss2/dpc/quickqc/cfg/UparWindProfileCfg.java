package cma.cimiss2.dpc.quickqc.cfg;

import cma.cimiss2.dpc.quickqc.bean.StationData;
import cma.cimiss2.dpc.quickqc.tools.MathUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class UparWindProfileCfg {
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
    
    private static String configPath = "config/";
    
    private UparWindProfileCfg(){
    	init();
    }
    
    private static final UparWindProfileCfg Instance = new UparWindProfileCfg();  
    
    public static UparWindProfileCfg getInstance(){   
        return Instance;  
    }
    
    /**
     * 初始化值
     */
    private void init() {
        List<Object> configs = Arrays.asList("station/w_t_station.txt", "station/120stationlist.txt", "station/range.txt");
        InputStreamReader is = null;
        BufferedReader reader = null;
        for (int i = 0; i < configs.size(); i++) {
            try {
                // 存wtMap
                if (i == 0) {
                    is = new InputStreamReader(new FileInputStream(configPath + configs.get(i)),"UTF-8");
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
                    is = new InputStreamReader(new FileInputStream(configPath + configs.get(i)),"UTF-8");
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
                    is = new InputStreamReader(new FileInputStream(configPath + configs.get(i)),"UTF-8");
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
    public boolean isExist(String wStaion) {
    	if(wtMap==null){
    		System.out.println("wtMap为空");
    		return false;
    	}else {
            if (wtMap.containsKey(wStaion)) {
                tStation = wtMap.get(wStaion);
                setStation(tStation);
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * 根据探空站点查找界限值参数文件
     * @param station
     */
    public void setStation(String station) {
        String name = "station/limit_value/SQCLimit-Wsp-" + station + ".txt";
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            // 存argMap
            is = new InputStreamReader(new FileInputStream(configPath + name),"UTF-8");
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
    public StationData getTStation() {
        return stationMap.get(tStation);
    }

    /**
     * 根据高度厚度层查找序号
     * 根据序号读取风速界值
     * @param hight
     * @return
     */
    public int getWSpeed(double hight) {
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
    private void close(InputStreamReader is, BufferedReader reader) {
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
  	
  	/**
     * 半正矢公式计算距离:业务同步
     * @param onelat 风廓线站点纬度
     * @param onelon 风廓线站点经度
     * @return
     */
    public Map<String, Integer> haversineFormula(double onelat, double onelon) {
        // 存放站点与120个探空站点的距离
        Map<String, Integer> map = new HashMap<>();
        for (String tStaNum : stationMap.keySet()) {
            StationData tStation = stationMap.get(tStaNum);
            // 获取探空站点经纬度
            double secondlat = tStation.getLatitude();
            double secondlon = tStation.getLongitude();
            final int R = 6371229;
            double dis = 0D;
            double dlon = MathUtil.mul(MathUtil.mul((secondlon - onelon), MathUtil.div(Math.PI, 180, 16)), 0.5);
            double dlat = MathUtil.mul(MathUtil.mul((secondlat - onelat), MathUtil.div(Math.PI, 180, 16)), 0.5);
            //如果经度相同
            if ((int) onelon * 100 == (int) secondlon * 100) {
                double value1 = MathUtil.div(R, 1000, 16);
                double value2 = MathUtil.sub(onelat, secondlat);
                double value3 = MathUtil.div(Math.PI, 180, 16);
                dis = MathUtil.mul(MathUtil.mul(value1, Math.abs(value2)), value3);
            } else if ((int) onelat * 100 == (int) secondlat * 100) {
                double value1 = MathUtil.div(R, 500, 16);
                dis = value1 * Math.asin(Math.abs(Math.cos(onelat * Math.PI / 180) * Math.sin(dlon)));
            } else {
                double value1 = MathUtil.div(R, 500, 8);
                dis = value1 * Math.asin(Math.sqrt(Math.pow(Math.sin(dlat), 2) + Math.cos(onelat * Math.PI / 180) * Math.cos(secondlat * Math.PI / 180) * Math.pow(Math.sin(dlon), 2)));
            }
            if (dis <= 200) {
                map.put(tStaNum, (int) dis);
            }
        }
        // 获取匹配站点最小值是否符合条件
        return map;
    }
}



