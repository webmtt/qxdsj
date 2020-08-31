package cma.cimiss2.dpc.decoder.surf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.HdsqBean;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * ------------------------------------------------------------------------------- <br>
 * 
 * @Title: DecodeBrightnessTemprature.java
 * @Package cma.cimiss2.dpc.decoder.other
 * @Description: TODO(水利部河道水情资料Z.2001.0003.R001 数据解码类)
 * 
 *               <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年3月28日 17:30:00   fengmingyang    Initial creation.
 *               </pre>
 * 
 * @author fengmingyang
 * @version 1.0
 * 
 *          ---------------------------------------------------------------------------------
 */
public class DecodeHdsq {

    private static final int LIMIT_SIZE1 = 7;
    private static final int LIMIT_SIZE2 = 5;
    private static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    /**
     * 对外接口
     * @param fileName
     * @return
     */
    public ParseResult<HdsqBean> decodeFile(String fileName) {
        ParseResult<HdsqBean> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            // 可增加一个对文件名的匹配，不匹配的判断为非法格式
            if(fileName.length() < 12 || !fileName.trim().endsWith(".txt")) {
                infoLogger.error(fileName + "为非法格式文件，无法解析");
                parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
            }
            if (file.length() <= 0) {
                parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
            } else {
                decode(file, parseResult);
            }
        } else {
            parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
        }
        return parseResult;
    }

    /**
     * 解码过程
     * @param file
     * @param parseResult
     * @return
     */
    @SuppressWarnings("static-access")
    private void decode(File file, ParseResult<HdsqBean> pr) {
        try {
            InputStreamReader read = null;
            BufferedReader bufferedReader = null;
            try {
                // 获取文件的编码
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
                // 获取文件流
                read = new InputStreamReader(new FileInputStream(file), fileCode);
                bufferedReader = new BufferedReader(read);
                String obsTime = "999999";
                String lineTxt = null;
                String categorg = "";
                int line = 0;
                // 判断是否是异常情况退出
                boolean flag = false;
                // 循环读取文件的行
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    try {
                        // 判断是否为空行
                        if (lineTxt.trim().equals("")) {
                            continue;
                        }
                        line++;
                        if (1 == line) {
                            // 第一个头部信息异常
                            if (!lineTxt.contains("超警") && !lineTxt.contains("蓄水量")
                                    && !lineTxt.contains("闸上水势")) {
                                ReportError error = new ReportError();
                                error.setMessage("测站数据头部信息异常");
                                error.setPositionx(line);
                                error.setSegment("文件异常");
                                pr.put(error);
                                flag = true;
                                break;
                            }
                        }
                        if (lineTxt.contains("超警")) {
                            categorg = "categorg1";
                            continue;
                        } else if (lineTxt.contains("蓄水量")) {
                            categorg = "categorg2";
                            continue;
                        } else if (lineTxt.contains("闸上水势")) {
                            categorg = "categorg3";
                            continue;
                        }
                        String[] strs = lineTxt.split(",");
                        if ("categorg1".equals(categorg) || "categorg2".equals(categorg)) {
                            // 判断大小是否符合要求
                            if (strs.length < LIMIT_SIZE1) {
                                ReportError error = new ReportError();
                                error.setMessage("format error!");
                                error.setPositionx(line);
//                                error.setSegment("数据异常");
                                pr.put(error);
                                continue;
                            }
                        } else {
                            if (strs.length < LIMIT_SIZE2) {
                                ReportError error = new ReportError();
                                error.setMessage("format error!");
                                error.setPositionx(line);
                                pr.put(error);
                                continue;
                            }
                        }
                        // 缺站号等关键要素，不存入
                        if (isEmpty(strs[0])) {
                            ReportError error = new ReportError();
                            error.setMessage("station is empty");
                            error.setPositionx(line);
//                            error.setSegment("数据异常");
                            pr.put(error);
                            continue;
                        }
                        HdsqBean hdsqBean = new HdsqBean();
                        if (file.getName().length() >= 12) {
                            hdsqBean.setFileInfo(file.getName().substring(0, 8));
                        }else {
                            hdsqBean.setFileInfo("999999");
                        }
                        hdsqBean.setStationNumber(getStr(strs[0]));
                        hdsqBean.setStationName(getStr(strs[1]));
                        String timeStr = "999999";
                        // 时间为空，改为存当前时间
                        if (isEmpty(strs[2])) {
                            ReportError error = new ReportError();
                            error.setPositionx(line);
                            error.setMessage("datetime is empty!");
                            pr.put(error);
                            continue;
                        } else {
                            timeStr = getTime(strs[2]);
                            obsTime = timeStr;
                        }
                        hdsqBean.setTime(timeStr);
                        hdsqBean.setObsTime(obsTime);
                        
                        try{
    						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    					    Date dataTime=simpleDateFormat.parse(obsTime);
    						if(!TimeCheckUtil.checkTime(dataTime)){
    							ReportError re = new ReportError();
    							re.setMessage("DataTime out of range：time:"+dataTime+" stationCode:"+getStr(strs[0]));
    							re.setSegment("时间异常");
    							pr.put(re);
    							continue;
    						}
    					}catch (Exception e) {
    						// TODO: handle exception
    					}
                        hdsqBean.setWaterLevel(getNumValue(strs[3]));
                        if ("categorg1".equals(categorg)) {
                            hdsqBean.setTraffic(getNumValue(strs[4]));
                            hdsqBean.setChaoJing(getNumValue(strs[5]));
                            hdsqBean.setShuiShi(getStr(strs[6]));
                        } else if ("categorg2".equals(categorg)) {
                            hdsqBean.setInflow(getNumValue(strs[4]));
                            hdsqBean.setOutflow(getNumValue(strs[5]));
                            hdsqBean.setPondage(getNumValue(strs[6]));
                        } else {
                            hdsqBean.setShuiShi(getStr(strs[4]));
                        }
                        pr.put(hdsqBean);
                    } catch (NumberFormatException e) {
                        ReportError error = new ReportError();
                        error.setMessage("format error: " + e.getMessage());
                        error.setPositionx(line);
                        pr.put(error);
                    }
                }
                if (flag) {
                    pr.setSuccess(false);
                } else {
                    pr.setSuccess(true);
                }
            } finally {
                // 关闭资源
                if (read != null) {
                    read.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
        } catch (IOException e) {
            ReportError error = new ReportError(); // 文件异常捕获
            error.setMessage("流关闭异常" + e.getMessage());
            pr.put(error);
        } catch (Exception e) {
            pr.setParseInfo(ParseInfo.ILLEGAL_FORM);
        }
    }

    private String getTime(String str) {
        str = str.replaceAll("\"", "").trim();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        if(str.length() > 0){
        	try {
				calendar.setTime(simpleDateFormat.parse(str));
				calendar.add(Calendar.HOUR_OF_DAY, -8);
	        	str = simpleDateFormat.format(calendar.getTime());
			} catch (ParseException e) {
				infoLogger.error("Datetime error: " + str);
			}
        }
        return str;
    }

    private String getStr(String str) {
        str = str.replaceAll("\"", "").trim();
        return str.length() == 0 ? "999999" : str;
    }
    
    private Double getNumValue(String str) {
        str = str.replaceAll("\"", "").trim();
        return str.length() == 0 ? 999999 : Double.parseDouble(str);
    }

    private boolean isEmpty(String str) {
        return str.replaceAll("\"", "").trim().length() == 0;
    }

    
    
}
