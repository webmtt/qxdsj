package cma.cimiss2.dpc.decoder.upar;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmMadTab;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/22 0022 17:40
 * @description：中国高空规定等压面定时值数据集
 * @modified By：
 * @version: 1.0$
 */
public class ChnMulFtmMadTab {

    /**
     * 结果集
     */
    private ParseResult<UparChnMulFtmMadTab> parseResult = new ParseResult<UparChnMulFtmMadTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<UparChnMulFtmMadTab> decode(File file) {
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
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        UparChnMulFtmMadTab uparChnMulFtmMadTab = new UparChnMulFtmMadTab();
                        String multab_list = txtFileContent.get(i);
                        String uuid = UUID.randomUUID().toString();
                        uparChnMulFtmMadTab.setdRecordId(uuid);
                        uparChnMulFtmMadTab.setdDatetime(changeType(multab_list.substring(5, 10)) + "-" + changeType(multab_list.substring(10, 13)) + "-" + changeType(multab_list.substring(13, 16)) + " " + "00:00:00");
                        uparChnMulFtmMadTab.setV04001(Short.parseShort(changeType(multab_list.substring(5, 10))));
                        uparChnMulFtmMadTab.setV04002(Short.parseShort(changeType(multab_list.substring(10, 13))));
                        uparChnMulFtmMadTab.setV04003(Short.parseShort(changeType(multab_list.substring(13, 16))));
                        if(isNumeric(changeType(multab_list.substring(0, 5)))){
                            uparChnMulFtmMadTab.setV01300(Integer.parseInt(changeType(multab_list.substring(0, 5))));
                            uparChnMulFtmMadTab.setV01301(changeType(multab_list.substring(0, 5)));
                        }else{
                            uparChnMulFtmMadTab.setV01300(Integer.parseInt("0"));
                            uparChnMulFtmMadTab.setV01301(changeType(multab_list.substring(0, 5)));
                        }
                        uparChnMulFtmMadTab.setvTimeLevel(new BigDecimal(changeType(multab_list.substring(16, 19))));
                        if(changeType(multab_list.substring(19, 25)).equals("999999")){
                            uparChnMulFtmMadTab.setV10004(new BigDecimal(999999));
                        }else {
                            uparChnMulFtmMadTab.setV10004(new BigDecimal(String.valueOf(Double.parseDouble(changeType(multab_list.substring(19, 25)))/10)));
                        }

                        uparChnMulFtmMadTab.setV10009(new BigDecimal(changeType(multab_list.substring(25, 31))));
                        if("999999".equals(changeType(multab_list.substring(31, 37)))){
                            uparChnMulFtmMadTab.setV12001(new BigDecimal(999999));
                        }else {
                            uparChnMulFtmMadTab.setV12001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(multab_list.substring(31, 37)))/10)));
                        }
                        if("999999".equals(changeType(multab_list.substring(37, 43)))){
                            uparChnMulFtmMadTab.setV12003(new BigDecimal(999999));
                        }else {
                            uparChnMulFtmMadTab.setV12003(new BigDecimal(String.valueOf(Double.parseDouble(changeType(multab_list.substring(37, 43)))/10)));
                        }
                        uparChnMulFtmMadTab.setV11001(new BigDecimal(changeType(multab_list.substring(43, 49))));
                        uparChnMulFtmMadTab.setV11002(new BigDecimal(changeType(multab_list.substring(49, 51))));
                        uparChnMulFtmMadTab.setQ10009(Short.parseShort(changeType(multab_list.substring(51, 53))));
                        uparChnMulFtmMadTab.setQ12001(new BigDecimal(changeType(multab_list.substring(53, 55))));
                        uparChnMulFtmMadTab.setQ12003(Short.parseShort(changeType(multab_list.substring(55, 57))));
                        uparChnMulFtmMadTab.setQ11001(Short.parseShort(changeType(multab_list.substring(57, 59))));
                        uparChnMulFtmMadTab.setQ11002(Short.parseShort(changeType(multab_list.substring(59, 61))));
                        parseResult.put(uparChnMulFtmMadTab);
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
        dates = dates.replaceAll(" ", "");
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf("-") > -1 || dates.equals('"') ||"99999".equals(dates)||"9999".equals(dates)||"999".equals(dates)||"99".equals(dates)) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ChnMulFtmMadTab chnMulFtmMadTab=new ChnMulFtmMadTab();
        File file=new File("H:\\gitproject\\QXDSJ\\doc\\内蒙本地数据资料\\资料样例\\结构化数据\\1.2.3  中国高空规定等压面定时值数据集(v2.0)\\UPAR_CLI_CHN_MUL_FTM_MAD-50527-12.TXT");
        chnMulFtmMadTab.decode(file);
    }
}
