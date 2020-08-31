package org.cimiss2.dwp.tools.utils;

import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.glb.BaseQcUtil;
import cma.cimiss2.dpc.quickqc.glb.QcBean;
import cma.cimiss2.dpc.quickqc.glb.amdar.GlbAmdarQcUtil;
import cma.cimiss2.dpc.quickqc.glb.ocean.GlbOceanQcUtil;
import cma.cimiss2.dpc.quickqc.glb.upar.GlbUparQcUtil;
import cma.cimiss2.dpc.quickqc.util.ret2excel.MyAppender;
import cma.cimiss2.dpc.quickqc.util.ret2excel.ResultBean;
import cma.cimiss2.dpc.quickqc.util.ret2excel.TestUtil;

import java.util.*;

import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.*;

/**
 * ************************************
 * @author : dangjinhu
 * @version : 1.0
 * ************************************
 */

public class GlbQcUtil {

    /**
     * 质控开关:1为高空,2为飞机报,3为海洋
     */
    private String flag;

    /**
     * 区站号
     */
    private String staId = null;

    /**
     * 资料月份
     */
    private String month = null;

    /**
     * 资料时次
     */
    private String times = null;

    /**
     * 台站信息指控码
     */
    private Map<String, Integer> baseCheckMap = new HashMap<>();

    private String fileName;
    private static final String STANDARD_COLUMN = "V08042";
    private static final String PRS_LEVEL = "V07004";
    private static final String STANDARD_FLOOR = "65536";

    /**
     * 1为高空,2为飞机报,3为海洋
     * @param flag     质控选择
     * @param fileName 文件名
     * @param params   可选参数：高空实时天气报质控
     */
    public GlbQcUtil(String flag, String fileName, String... params) {
        this.flag = flag;
        this.fileName = fileName;
        int size = 3;
        if (params.length == size) {
            this.staId = params[0];
            this.month = params[1];
            this.times = params[2];
        }
    }

    public void setBaseCheckMap(String qColumn, Integer code) {
        baseCheckMap.put(qColumn, code);
    }

    public Map<String, Integer> getBaseCheckMap() {
        return baseCheckMap;
    }

    public String[] getQcAfterSql(String keyBuffer, String valueBuffer) {
        String[] columns = keyBuffer.replace("`", "").split(",");
        String[] colValues = valueBuffer.replace("'", "").split(",");
        Map<String, Object> qcMap = new HashMap<>(10);
        for (int i = 0; i < columns.length; i++) {
            qcMap.put(columns[i], colValues[i]);
        }
        Properties vProperties = LoadPropertiesFile.getInstance().getGlbqcVProperties();
        String prsLevel = null;
        if (qcMap.containsKey(PRS_LEVEL)) {
            prsLevel = (int) Double.parseDouble(qcMap.get(PRS_LEVEL).toString()) + "";
        }
        Map<MeteoElement, Double> innerMap = null;
        switch (flag) {
            case "1":
                innerMap = loadUparInnerMap(vProperties, qcMap);
                break;
            case "2":
                innerMap = loadAmdarInnerMap(vProperties, qcMap);
                break;
            case "3":
                innerMap = loadOceanInnerMap(vProperties, qcMap);
                break;
            default:
                break;
        }
        // 判断垂直探测意义是否为标准层
        if (!(qcMap.containsKey(STANDARD_COLUMN) && qcMap.get(STANDARD_COLUMN).toString().startsWith(STANDARD_FLOOR))) {
            // TODO: 2020/1/10 标准层:待确认
            // return new String[]{keyBuffer, valueBuffer};
        }
        List<ResultBean> list = new ArrayList<>(1);
        Boolean testFlag = TestUtil.isTest();
        int index = 0;
        HashMap<MeteoElement, Integer> elementCodeMap = new HashMap<>(1);
        for (Object key : vProperties.keySet()) {
            try {
                Object obj = vProperties.get(key);
                MeteoElement element = Enum.valueOf(MeteoElement.class, obj.toString());
                String qColumn = key.toString().substring(4);
                String vColumn = qColumn.replace("Q", "V");
                Object obsValue = qcMap.get(vColumn);
                Object obsCode = qcMap.get(qColumn);
                if (obsValue == null || obsCode == null) {
                    continue;
                }
                double originCode = 0;
                try {
                    originCode = Double.parseDouble(obsCode.toString());
                } catch (NumberFormatException e) {
                    originCode = 9;
                }
                String value = obsValue.toString();
                QcBean glbBean = new QcBean(qColumn, value, element, qcMap, innerMap);
                if (key.toString().startsWith("PHE.") && "1".equals(flag)) {
                    if (staId == null || month == null || times == null || prsLevel == null) {
                        System.out.println("this staId、month、times 、prsLevel cannot null !");
                        return new String[]{keyBuffer, valueBuffer};
                    }
                    elementCodeMap.put(element, (int) originCode);
                    glbBean.setHelpValue(staId, month, times, prsLevel);
                    BaseQcUtil qcUtil = new GlbUparQcUtil();
                    long start = System.currentTimeMillis();
                    int qc = qcUtil.doQc(glbBean);
                    // 获取质控码结果,覆盖原始质控码
                    qcMap.put(qColumn, qc);
                    long end = System.currentTimeMillis();
                    long spendTime = end - start;
                    if (testFlag) {
                        /// Date date = DateUtils.parseDate(dateTime, "yyyy-MM-dd HH:mm:ss");
                        ResultBean retBean = MyAppender.poll(++index, element, fileName, glbBean.getStaId(), spendTime, new Date());
                        list.add(retBean);
                    }

                } else if (key.toString().startsWith("AMD.") && "2".equals(flag)) {
                    elementCodeMap.put(element, (int) originCode);
                    BaseQcUtil qcUtil = new GlbAmdarQcUtil();
                    int qc = qcUtil.doQc(glbBean);
                    // 获取质控码结果,覆盖原始质控码
                    qcMap.put(qColumn, qc);
                } else if (key.toString().startsWith("OCE.") && "3".equals(flag)) {
                    elementCodeMap.put(element, (int) originCode);
                    BaseQcUtil qcUtil = new GlbOceanQcUtil();
                    long start = System.currentTimeMillis();
                    int qc = qcUtil.doQc(glbBean);
                    // 获取质控码结果,覆盖原始质控码
                    if (qColumn.contains("V")) {
                        // 台站信息质控
                        baseCheckMap.put(qColumn, qc);
                    } else {
                        qcMap.put(qColumn, qc);
                    }
                }
            } catch (Exception e) {
                System.out.println("质控异常");
            }
        }
        StringBuffer keySql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();
        for (String column : qcMap.keySet()) {
            keySql.append("`").append(column).append("`,");
            valueSql.append("'").append(qcMap.get(column)).append("',");
        }
        return new String[]{keySql.substring(0, keySql.length() - 1), valueSql.substring(0, valueSql.length() - 1)};
    }

    private static Map<MeteoElement, Double> loadUparInnerMap(Properties qProperties, Map<String, Object> qcMap) {
        Set<MeteoElement> keySet = new HashSet<>(Arrays.asList(PHE_Tem, PHE_Dpt, PHE_Wind_D, PHE_Wind_S, PHE_Prs_Level, PHE_Sta_Height));
        Map<MeteoElement, Double> map = new HashMap<>();
        for (Object key : qProperties.keySet()) {
            Object obj = qProperties.get(key);
            String column = key.toString().substring(4).replace("Q", "V");
            MeteoElement element = Enum.valueOf(MeteoElement.class, obj.toString());
            if (keySet.contains(element)) {
                map.put(element, Double.parseDouble(qcMap.get(column).toString()));
            }
        }
        return map;
    }

    private static Map<MeteoElement, Double> loadAmdarInnerMap(Properties qProperties, Map<String, Object> qcMap) {
        Set<MeteoElement> keySet = new HashSet<>(Arrays.asList(AMD_ABS_FLIGHT_HGT,AMD_TEMP,AMD_WIND_S,AMD_WIND_D));
        Map<MeteoElement, Double> map = new HashMap<>();
        for (Object key : qProperties.keySet()) {
            Object obj = qProperties.get(key);
            String column = key.toString().substring(4).replace("Q", "V");
            MeteoElement element = Enum.valueOf(MeteoElement.class, obj.toString());
            if (keySet.contains(element)) {
                map.put(element, Double.parseDouble(qcMap.get(column).toString()));
            }
        }
        return map;
    }

    private static Map<MeteoElement, Double> loadOceanInnerMap(Properties qProperties, Map<String, Object> qcMap) {
        Set<MeteoElement> keySet = new HashSet<>(Arrays.asList(OCE_LAT, OCE_SST, OCE_SST_MIN, OCE_SST_MAX, OCE_PRS_03, OCE_PRS_03_TREND, OCE_WIND_D, OCE_WIND_S));
        Map<MeteoElement, Double> map = new HashMap<>();
        for (Object key : qProperties.keySet()) {
            Object obj = qProperties.get(key);
            String column = key.toString().substring(4).replace("Q", "V");
            MeteoElement element = Enum.valueOf(MeteoElement.class, obj.toString());
            if (keySet.contains(element)) {
                Object val = qcMap.get(column);
                double value = 999999;
                if (val == null) {
                    map.put(element, value);
                } else {
                    map.put(element, Double.parseDouble(val.toString()));
                }
            }
        }
        return map;
    }

}
