package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutPenTab;
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
 * @date ：Created in 2019/10/28 0028 11:26
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMutPenTab {
    /**
     * 结果集
     */
    private ParseResult<RadiChnMutPenTab> parseResult = new ParseResult<RadiChnMutPenTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<RadiChnMutPenTab> decode(File file) {
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
                        RadiChnMutPenTab radiChnMutPenTab = new RadiChnMutPenTab();
                        String post_list = txtFileContent.get(i);
                        radiChnMutPenTab.setdRecordId(UUID.randomUUID().toString());
                        radiChnMutPenTab.setV01301(post_list.substring(0, 5));
                        radiChnMutPenTab.setV05001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(5, 11))) / 100)));
                        radiChnMutPenTab.setV06001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(11, 17))) / 100)));
                        radiChnMutPenTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(17, 23))) / 10)));
                        radiChnMutPenTab.setV04001(Short.parseShort(changeType(post_list.substring(23, 28))));
                        radiChnMutPenTab.setV04002(Short.parseShort(changeType(post_list.substring(28, 33))));
                        radiChnMutPenTab.setV04290(Short.parseShort(changeType(post_list.substring(33, 38))));
                        radiChnMutPenTab.setV14320(new BigDecimal(dealDates(post_list.substring(38, 46))));
                        radiChnMutPenTab.setV14308(new BigDecimal(dealDates(post_list.substring(46, 54))));
                        radiChnMutPenTab.setV14309(new BigDecimal(dealDates(post_list.substring(54, 62))));
                        radiChnMutPenTab.setV14302(new BigDecimal(dealDates(post_list.substring(62, 70))));
                        radiChnMutPenTab.setV14306(new BigDecimal(dealDates(post_list.substring(70, 78))));
                        radiChnMutPenTab.setV143St(new BigDecimal(dealDates(post_list.substring(78, 86))));
                        parseResult.put(radiChnMutPenTab);
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
        if (dates == null || dates == "" || dates.indexOf("/") > -1  || dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if (dates.equals("9932744")) {
            dates = "999998";
        } else if (dates.equals("9932766")) {
            dates = "999999";
        }
        return dates;
    }

    public static String dealDates(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if ("9932744".equals(dates)) {
            dates = "999998";
        } else if ("9932766".equals(dates)) {
            dates = "999999";
        } else if((!"999999".equals(dates))&&(!"999998".equals(dates))){
            dates = String.valueOf(Double.parseDouble(dates) / 100);
        }
        return dates;
    }

}