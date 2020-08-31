package cma.cimiss2.dpc.decoder.upar;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparDigNmStlFtmTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/21 0021 17:26
 * @description：数字化-高空规定实体类
 * @modified By：
 * @version: 1.0$
 */
public class DigNmStlFtmTab {

    /**
     * 结果集
     */
    private ParseResult<UparDigNmStlFtmTab> parseResult = new ParseResult<UparDigNmStlFtmTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<UparDigNmStlFtmTab> decode(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if (file.length() <= 0) {
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                return parseResult;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;

                List<String> txtFileContent = FileUtil.getTxtFileContents(file, fileCode);
                String[] pm10_list = txtFileContent.get(0).split("\\s+");
                String V01301 = pm10_list[0];
                String V04001 = pm10_list[3];
                String V04002 = pm10_list[4];
                String V04003 = null;
                String TIMES = null;
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    for (int i = 1; i < txtFileContent.size(); i++) {
                        UparDigNmStlFtmTab uparLightAdtdMulTab = new UparDigNmStlFtmTab();
                        uparLightAdtdMulTab.setdRecordId(getOrderIdByUUId());
                        uparLightAdtdMulTab.setV01301(V01301);
                        uparLightAdtdMulTab.setV04001(Short.parseShort(V04001));
                        uparLightAdtdMulTab.setV04002(Short.parseShort(V04002));
                        if (txtFileContent.get(i).length() == 8) {
                            V04003 = txtFileContent.get(i).substring(0, 2);
                            TIMES = txtFileContent.get(i).substring(2, 4);
                            continue;
                        }
                        if (txtFileContent.get(i).length() != 8) {
                            uparLightAdtdMulTab.setdDataId("B.0011.0001.S001");
                            uparLightAdtdMulTab.setV04003(Short.parseShort(changeType(V04003)));
                            uparLightAdtdMulTab.setdDatetime(V04001 + "-" + V04002 + "-" + V04003 + " " + TIMES + ":" + "00" + ":00");
                            uparLightAdtdMulTab.setTimes(Short.parseShort(TIMES));
                            String[] pm11_list = txtFileContent.get(i).split("\\s+");
                            String V10004 = pm11_list[0];
                            String V10009 = pm11_list[1];
                            String ARRIVALTIME = pm11_list[2];
                            String V11001 = pm11_list[3];
                            String V11002 = pm11_list[4];
                            if (pm11_list[4].endsWith("=")) {
                                V11002 = pm11_list[4].substring(0, pm11_list[4].indexOf("="));
                            } else if (pm11_list[4].endsWith(",")) {
                                V11002 = pm11_list[4].substring(0, pm11_list[4].indexOf(","));
                            }
                            if (V10009.equals("/////") || V10009.equals("\\\\\\\\\\") || V10009.equals("////")) {
                                V10009 = "999999";
                            }
                            if (ARRIVALTIME.equals("////")) {
                                ARRIVALTIME = "999999";
                            }
                            if (V11001.equals("///") || V11001.equals("\\\\\\\\\\") || V11001.equals("////")) {
                                V11001 = "999999";
                            }
                            if (V11002.equals("///") || V11002.equals("\\\\\\\\\\") || V11002.equals("////")) {
                                V11002 = "999999";
                            }
                            if (V10004.equals("///") || V10004.equals("\\\\\\\\\\") ||"\\\\\\\\".equals(V10004) ||V10004.equals("\\\\")||"/////".equals(V10004)) {
                                V10004 = "999999";
                            }
                            uparLightAdtdMulTab.setV10004(Integer.parseInt(V10004));
                            uparLightAdtdMulTab.setV10009(new BigDecimal(changeType(V10009)));
                            uparLightAdtdMulTab.setArrivaltime(new BigDecimal(changeType(ARRIVALTIME)));
                            try {
                                uparLightAdtdMulTab.setV11001(new BigDecimal(changeType(V11001)));
                                if (changeType(V11002).equals("999999")) {
                                    uparLightAdtdMulTab.setV11002(new BigDecimal(999999));
                                } else {
                                    uparLightAdtdMulTab.setV11002(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V11002)))));
                                }
                            } catch (Exception e) {
                                uparLightAdtdMulTab.setV11001(new BigDecimal(999999));
                                uparLightAdtdMulTab.setV11002(new BigDecimal(999999));
                            }
                        }
                        parseResult.put(uparLightAdtdMulTab);
                        parseResult.setSuccess(true);
                        if (txtFileContent.get(i).endsWith(",")) {
                            continue;
                        }
                    }
                } else {
                    if (txtFileContent == null || txtFileContent.size() == 0) {
                        // 空文件
                        parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                    } else {
                        // 数据只有一行，格式不正确
                        parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
            } catch (FileNotFoundException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
            }
        } else {
            // file not exsit
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        return parseResult;
    }

    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf("-") > -1 || dates.equals('"')) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }

    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public static void main(String[] args) {
        File file=new File("H:\\gitproject\\QXDSJ\\doc\\内蒙本地数据资料\\资料样例\\结构化数据\\1.2.2  数字化-高空规定等压面录入文件\\GAAF-53068-198105.TXT");
                DigNmStlFtmTab digNmStlFtmTab=new DigNmStlFtmTab();
                digNmStlFtmTab.decode(file);
    }
}