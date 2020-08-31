package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutYerTab;
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
public class ChnMutYerTab {
    /**
     * 结果集
     */
    private ParseResult<RadiChnMutYerTab> parseResult = new ParseResult<RadiChnMutYerTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<RadiChnMutYerTab> decode(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if(file.length() <= 0){
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
                        RadiChnMutYerTab radiChnMutYerTab = new RadiChnMutYerTab();
//                        String post_list = list.get(i);
                        String[] messages = txtFileContent.get(i).split("\\s+");
                        radiChnMutYerTab.setdRecordId(UUID.randomUUID().toString());
                        radiChnMutYerTab.setV01301(messages[0]);
                        radiChnMutYerTab.setV05001(new BigDecimal(dealDates(messages[1])));
                        radiChnMutYerTab.setV06001(new BigDecimal(dealDates(messages[2])));
                        radiChnMutYerTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(messages[3]))/10)));
                        radiChnMutYerTab.setV04001(Short.parseShort(changeType(messages[4])));
                        radiChnMutYerTab.setV14320(new BigDecimal(dealDates(messages[5])));
                        radiChnMutYerTab.setV14308(new BigDecimal(dealDates(messages[6])));
                        radiChnMutYerTab.setV14309(new BigDecimal(dealDates(messages[7])));
                        radiChnMutYerTab.setV14302(new BigDecimal(dealDates(messages[8])));
                        radiChnMutYerTab.setV14306(new BigDecimal(dealDates(messages[9])));
                        radiChnMutYerTab.setV143St(new BigDecimal(dealDates(messages[10])));
//                        radiChnMutYerTab.setV01301(post_list.substring(0,5));
//                        radiChnMutYerTab.setV05001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(5,11)))/100)));
//                        radiChnMutYerTab.setV06001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(11,17)))/100)));
//                        radiChnMutYerTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(17,23)))/10)));
//                        radiChnMutYerTab.setV04001(Short.parseShort(changeType(post_list.substring(23,28))));
//                        radiChnMutYerTab.setV14320(new BigDecimal(changeType(post_list.substring(28,36))));
//                        radiChnMutYerTab.setV14308(new BigDecimal(changeType(post_list.substring(36,44))));
//                        radiChnMutYerTab.setV14309(new BigDecimal(changeType(post_list.substring(44,52))));
//                        radiChnMutYerTab.setV14302(new BigDecimal(changeType(post_list.substring(52,60))));
//                        radiChnMutYerTab.setV14306(new BigDecimal(changeType(post_list.substring(60,68))));
//                        radiChnMutYerTab.setV143St(new BigDecimal(changeType(post_list.substring(68,76))));
                        parseResult.put(radiChnMutYerTab);
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
    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf("-") > -1 || dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ","");
        if (dates.equals("9932744")) {
            dates = "999998";
        } else if (dates.equals("9932766")) {
            dates = "999999";
        }
        return dates;
    }
    public static String dealDates(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf("-") > -1 || dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if (dates.equals("9932744")) {
            dates = "999998";
        } else if (dates.equals("9932766")) {
            dates = "999999";
        } else {
            dates = String.valueOf(Double.parseDouble(dates) / 100);
        }
        return dates;
    }
}
