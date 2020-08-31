package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMtenTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/28 0028 14:45
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMutMtenTab {
    /**
     * 结果集
     */
    private ParseResult<RadiChnMutMtenTab> parseResult = new ParseResult<RadiChnMutMtenTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<RadiChnMutMtenTab> decode(File file) {
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

                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
//                    ArrayList<String> list = new ArrayList<>();
                    //去掉空格
//                    for (int i = 0; i < txtFileContent.size(); i++) {
//                        String dates = txtFileContent.get(i).replaceAll(" ", "0");
//                        list.add(dates);
//                    }
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        RadiChnMutMtenTab radiChnMutMtenTab = new RadiChnMutMtenTab();
                        String post_list = txtFileContent.get(i);
                        radiChnMutMtenTab.setdRecordId(UUID.randomUUID().toString());
                        radiChnMutMtenTab.setV01301(post_list.substring(0, 5));
                        radiChnMutMtenTab.setV05001(new BigDecimal(dealDates(post_list.substring(5, 10))));
                        radiChnMutMtenTab.setV06001(new BigDecimal(dealDates(post_list.substring(10, 16))));
                        radiChnMutMtenTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(16, 22))) / 10)));
                        radiChnMutMtenTab.setV04002(Short.parseShort(changeType(post_list.substring(22, 25))));
                        radiChnMutMtenTab.setV04290(Short.parseShort(changeType(post_list.substring(25, 28))));
                        radiChnMutMtenTab.setV14320(new BigDecimal(dealDates(post_list.substring(28, 34))));
                        radiChnMutMtenTab.setV14308(new BigDecimal(dealDates(post_list.substring(34, 40))));
                        radiChnMutMtenTab.setV14309(new BigDecimal(dealDates(post_list.substring(40, 46))));
                        radiChnMutMtenTab.setV14302(new BigDecimal(dealDates(post_list.substring(46, 52))));
                        radiChnMutMtenTab.setV14306(new BigDecimal(dealDates(post_list.substring(52, 58))));
                        radiChnMutMtenTab.setV143St(new BigDecimal(dealDates(post_list.substring(58, 64))));
                        radiChnMutMtenTab.setV14027(new BigDecimal(changeType(post_list.substring(64, 72))));
                        try {
                            radiChnMutMtenTab.setV143201(Integer.parseInt(changeType(post_list.substring(72, 78))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV143201(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV14320Stime(Integer.parseInt(changeType(post_list.substring(78, 85))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV14320Stime(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV14320Etime(Integer.parseInt(changeType(post_list.substring(85, 92))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV14320Etime(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV143081(Integer.parseInt(changeType(post_list.substring(92, 98))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV143081(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV14308Stime(Integer.parseInt(changeType(post_list.substring(98, 105))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV14308Stime(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV14308Etime(Integer.parseInt(changeType(post_list.substring(105, 112))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV14308Etime(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV143St1(Integer.parseInt(changeType(post_list.substring(112, 118))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV143St1(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV143StStime(Integer.parseInt(changeType(post_list.substring(118, 125))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV143StStime(999999);
                        }
                        try {
                            radiChnMutMtenTab.setV143StEtim(Integer.parseInt(changeType(post_list.substring(125, 132))));
                        } catch (Exception e) {
                            radiChnMutMtenTab.setV143StEtim(999999);
                        }
                        parseResult.put(radiChnMutMtenTab);
                        parseResult.setSuccess(true);
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

    //    public static String changeType(String dates) {
//        if(dates.contains("0-")){
//            dates = dates.substring(dates.indexOf("-"),dates.length());
//        }else if(dates.startsWith("0")){
//            dates = dates.substring(1,dates.length());
//            if(dates.startsWith("0")){
//                dates = dates.substring(1,dates.length());
//                if(dates.startsWith("0")){
//                    dates = dates.substring(1,dates.length());
//                }
//            }
//        }
//        return dates;
//    }
    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if (dates.equals("32766") || dates.equals("32766.0")) {
            dates = "999999";
        }
        return dates;
    }

    public static String dealDates(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.equals('"')||"99999".equals(dates) ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if (dates.equals("32766") || dates.equals("32766.0")) {
            dates = "999999";
        } else {
            dates = String.valueOf(Double.parseDouble(dates) / 100);
        }
        return dates;
    }
}
