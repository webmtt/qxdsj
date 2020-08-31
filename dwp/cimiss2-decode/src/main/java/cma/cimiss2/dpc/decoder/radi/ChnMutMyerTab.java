package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMyerTab;
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
 * @date ：Created in 2019/10/29 0029 10:42
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMutMyerTab {
    /**
     * 结果集
     */
    private ParseResult<RadiChnMutMyerTab> parseResult = new ParseResult<RadiChnMutMyerTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<RadiChnMutMyerTab> decode(File file) {
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
                        RadiChnMutMyerTab radiChnMutMyerTab = new RadiChnMutMyerTab();
                        String post_list = txtFileContent.get(i);
                        radiChnMutMyerTab.setdRecordId(UUID.randomUUID().toString());
                        radiChnMutMyerTab.setV01301(post_list.substring(0, 5));
                        radiChnMutMyerTab.setV05001(new BigDecimal(dealDates(post_list.substring(5, 10))));
                        radiChnMutMyerTab.setV06001(new BigDecimal(dealDates(post_list.substring(10, 16))));
                        radiChnMutMyerTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(16, 22))) / 10)));
                        radiChnMutMyerTab.setV14320(new BigDecimal(dealDates(post_list.substring(22, 30))));
                        radiChnMutMyerTab.setV14308(new BigDecimal(dealDates(post_list.substring(30, 38))));
                        radiChnMutMyerTab.setV14309(new BigDecimal(dealDates(post_list.substring(38, 46))));
                        radiChnMutMyerTab.setV14302(new BigDecimal(dealDates(post_list.substring(46, 54))));
                        radiChnMutMyerTab.setV14306(new BigDecimal(dealDates(post_list.substring(54, 62))));
                        radiChnMutMyerTab.setV143St(new BigDecimal(dealDates(post_list.substring(62, 70))));
                        radiChnMutMyerTab.setV14027(new BigDecimal(changeType(post_list.substring(70, 79))));
                        try {
                            radiChnMutMyerTab.setV143201(Integer.parseInt(changeType(post_list.substring(79, 87))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV143201(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV14320Stime(Integer.parseInt(changeType(post_list.substring(87, 96))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV14320Stime(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV14320Etime(Integer.parseInt(changeType(post_list.substring(96, 105))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV14320Etime(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV143081(Integer.parseInt(changeType(post_list.substring(105, 113))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV143081(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV14308Stime(Integer.parseInt(changeType(post_list.substring(113, 122))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV14308Stime(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV14308Etime(Integer.parseInt(changeType(post_list.substring(122, 131))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV14308Etime(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV143St1(Integer.parseInt(changeType(post_list.substring(131, 139))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV143St1(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV143StStime(Integer.parseInt(changeType(post_list.substring(139, 148))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV143StStime(999999);
                        }
                        try {
                            radiChnMutMyerTab.setV143StEtim(Integer.parseInt(changeType(post_list.substring(148, 157))));
                        } catch (Exception e) {
                            radiChnMutMyerTab.setV143StEtim(999999);
                        }
                        parseResult.put(radiChnMutMyerTab);
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
        dates = dates.replaceAll(" ", "");
        if (dates.equals("32766") || dates.equals("32766.0")) {
            dates = "999999";
        }
        return dates;
    }

    public static String dealDates(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf("-") > -1 || dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if (dates.equals("32766") || dates.equals("32766.0")) {
            dates = "999999";
        }else {
            dates = String.valueOf(Double.parseDouble(dates) / 100);
        }
        return dates;
    }
}
