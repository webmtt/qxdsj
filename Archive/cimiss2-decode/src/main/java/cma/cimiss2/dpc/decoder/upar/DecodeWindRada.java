package cma.cimiss2.dpc.decoder.upar;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.upar.WindData;
import cma.cimiss2.dpc.decoder.bean.upar.WindRada;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ************************************
 * @ClassName: DecodeWindRada
 * @Auther: dangjinhu
 * @Date: 2019/3/20 11:29
 * @Description: 实时、半小时平均、一小时平均的采样高度产品数据文件解析
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DecodeWindRada {

    /**
     * 对外接口
     * @param fileName
     * @return
     */
    public ParseResult<WindRada> decodeFile(String fileName) {
        ParseResult<WindRada> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.length() <= 0) {
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
            } else {
                readFileInfo(file, parseResult);
            }
        } else {
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        return parseResult;
    }

    /**
     * 读取文件信息
     * @param file
     * @return
     */
    private void readFileInfo(File file, ParseResult<WindRada> parseResult) {
        // 获取文件编码
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
        bufferReader(file, fileCode, parseResult);
    }

    /**
     * BufferedReader读文件
     * @param file
     * @param fileCode
     */
    private void bufferReader(File file, String fileCode, ParseResult<WindRada> pr) {
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), fileCode);
            reader = new BufferedReader(is);
            String line;
            int count = 0; // 记录值
            boolean flag = false;
            boolean flagOBS = false;
            List<WindRada> windRadas = new ArrayList<>();
            WindRada windRada = null;
            List<WindData> windData = null;
            while ((line = reader.readLine()) != null) {
                try {
                    // 过滤空行中占有空字符
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] values = line.trim().split("\\s+");
                    if (values.length == 0 && values == null) {
                        continue;
                    }
                    String lineTrim=line.trim();
//                    cleanNullValue(values);
                    if (count == 0) { // 数据格式和文件版本号
                        // 位0时初始化对象
                        windRada = new WindRada();
                        windData = new ArrayList<>();
                        if (lineTrim.length() == 13) {
                            readFirstData(windRada, lineTrim);
                        } else {
                            ReportError error = new ReportError();
                            error.setMessage("读取文件版本号异常");
                            error.setSegment(line);
                            pr.put(error);
                        }
                        count++;
                        continue;
                    }
                    if (count == 1) { // 测站基本参数
                        if (lineTrim.length() == 50) {
                            try {
                                readSecondData(windRada, lineTrim);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        					    Date dataTime=simpleDateFormat.parse(windRada.getObeTime());
        	                    if(!TimeCheckUtil.checkTime(dataTime)){
        							ReportError re = new ReportError();
        							re.setMessage("DataTime out of range：time:"+dataTime+" stationCode:"+windRada.getStaionId());
        							pr.put(re);
        							continue;
        						}
                            } catch (Exception e) {
                                ReportError error = new ReportError();
                                error.setMessage("测站基本参数数据格式转换异常" + e.getMessage());
                                error.setSegment(line);
                                pr.put(error);
//                                pr.setSuccess(false);
                                // break;
                            }
                        } else { // 基本参数异常
                            ReportError error = new ReportError();
                            error.setMessage("读取测站基本参数异常");
                            error.setSegment(line);
                            pr.put(error);
//                            pr.setSuccess(false);
                            // break;
                        }
                        count++;
                        continue;
                    }
                    if (count == 2) { // 数据标识
                        if (lineTrim.length() == 4 && StringUtils.isNotBlank(lineTrim)) {
                            flagOBS = ("ROBS".equals(lineTrim) || "HOBS".equals(lineTrim)  || "OOBS".equals(lineTrim));
                            if (flagOBS) {
                                windRada.set_OBS(lineTrim);
                                flag = true;
                            } else {
                                ReportError error = new ReportError();
                                error.setMessage("读取产品标识异常数据标识异常");
                                error.setSegment(line);
                                pr.put(error);
                            }
                        } else {
                            ReportError error = new ReportError();
                            error.setMessage("读取_OBS数据标识异常");
                            error.setSegment(line);
                            pr.put(error);
                        }
                        count++;
                        continue;
                    }
                    // 若出现NNNN结束标志,一个站点数据读完,可能存在多个站点
                    if (lineTrim.length() == 4 && WindRada.NNNN.equals(lineTrim) && flagOBS) {
                        // 数据保存到parseResult
                        windRada.setwData(windData);
                        windRadas.add(windRada);
                        // 若有一个站点数据读取成功,设置success为true
                        pr.setSuccess(true);
                        flag = false; // 还原默认
                        count = 0;
                        continue;
                    }
                    if (flag) {
                        if (lineTrim.length() == 41) {
                            try {
                                readThirdData(windData, lineTrim);
                            } catch (Exception e) {
                                ReportError error = new ReportError();
                                error.setMessage("采样高度数据格式转换异常");
                                error.setSegment(line);
                                pr.put(error);
                            }
                        } else {
                            ReportError error = new ReportError();
                            error.setMessage("读取采样高度数据异常");
                            error.setSegment(line);
                            pr.put(error);
                        }
                    }
                } catch (Exception e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("读取文件异常!");
                    error.setSegment(line);
                    pr.put(error);
                }
            }
            // 数据读取成功
            pr.setData(windRadas);
        } catch (IOException e) {
            ReportError error = new ReportError(); // 文件异常捕获
            error.setMessage("异常" + e.getMessage());
            pr.put(error);
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("流关闭异常" + e.getMessage());
                    pr.put(error);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("流关闭异常" + e.getMessage());
                    pr.put(error);
                }
            }
        }
    }

    /**
     * 读取第三段数据
     * @param wDatas
     * @param values
     */
    private void readThirdData(List<WindData> wDatas, String lineTrim) {
        WindData wData = new WindData();
        String value0=lineTrim.substring(0,5).contains("/")?"999999":lineTrim.substring(0,5);
        String value1=lineTrim.substring(6,11).contains("/")?"999999":lineTrim.substring(6,11);
        String value2=lineTrim.substring(12,17).contains("/")?"999999":lineTrim.substring(12,17);
        String value3=lineTrim.substring(18,24).contains("/")?"999999":lineTrim.substring(18,24);
        String value4=lineTrim.substring(25,28).contains("/")?"999999":lineTrim.substring(25,28);
        String value5=lineTrim.substring(29,32).contains("/")?"999999":lineTrim.substring(29,32);
        String value6=lineTrim.substring(33,41).contains("/")?"999999":lineTrim.substring(33,41);
        // 采样高度
        wData.setHeight(Integer.parseInt(value0));
        // 水平风向
        wData.setWindDirection(Double.parseDouble(value1));
        // 水平风速
        wData.setWindSpeed(Double.parseDouble(value2));
        // 垂直风速
        wData.setVwindSpeed(Double.parseDouble(value3));
        // 水平方向可信度
        wData.setHcredibility(Integer.parseInt(value4));
        // 垂直方向可信度
        wData.setVcredibility(Integer.parseInt(value5));
        // 垂直方向cn2
        wData.setCn2(value6);
        wDatas.add(wData);
    }

    /**
     * 缺测值处理
     * @param items
     */
    private void cleanNullValue(String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].contains("/")) {
                items[i] = WindRada.NINE;
            }
        }
    }

    /**
     * 读取第二行数据
     * @param rs
     * @param values
     */
    private void readSecondData(WindRada rs, String lineTrim) {
    	String value0=lineTrim.substring(0,5).contains("/")?"999999":lineTrim.substring(0,5);// 区站号
    	String value1=lineTrim.substring(6,15).contains("/")?"999999":lineTrim.substring(6,15);// 经度
    	String value2=lineTrim.substring(16,24).contains("/")?"999999":lineTrim.substring(16,24);// 纬度
    	String value3=lineTrim.substring(25,32).contains("/")?"999999":lineTrim.substring(25,32);// 海拔
    	String value4=lineTrim.substring(33,35).contains("/")?"999999":lineTrim.substring(33,35);// 雷达版本号
    	String value5=lineTrim.substring(36,50).contains("/")?"999999":lineTrim.substring(36,50);//观测结束时间
        // 区站号
        rs.setStaionId(value0);
        // 经度
        rs.setLongitude(Double.parseDouble(value1));
        // 纬度
        rs.setLatitude(Double.parseDouble(value2));
        // 海拔
        rs.setAltitude(Double.parseDouble(value3));
        // 雷达版本号
        rs.setRadaModel(value4);
        // 观测结束时间
        rs.setObeTime(value5);
    }

    /**
     * 读取第一行数据
     * @param rs
     * @param values
     */
    private void readFirstData(WindRada rs, String lineTrim) {
    	String value0=lineTrim.substring(0,7).contains("/")?"999999":lineTrim.substring(0,7);
    	String value1=lineTrim.substring(8,13).contains("/")?"999999":lineTrim.substring(8,13);
        rs.setWND_OBS(value0);
        rs.setVersionNumber(value1);
    }

    public static void main(String[] args) {
        String filePathO = "D:\\TEMP\\B.7.3.1\\2\\Z_RADA_I_58463_20190411064500_P_WPRD_LC_ROBS.TXT";
        DecodeWindRada dr = new DecodeWindRada();
        ParseResult<WindRada> pr = dr.decodeFile(filePathO);
        System.out.println(pr);
    }
}
