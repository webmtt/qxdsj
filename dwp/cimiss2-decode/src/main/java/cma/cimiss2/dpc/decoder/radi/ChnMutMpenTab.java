package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMpenTab;
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
public class ChnMutMpenTab {
    /**
     * 结果集
     */
    private ParseResult<RadiChnMutMpenTab> parseResult = new ParseResult<RadiChnMutMpenTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<RadiChnMutMpenTab> decode(File file) {
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
                        RadiChnMutMpenTab radiChnMutMpenTab = new RadiChnMutMpenTab();
                        String post_list = txtFileContent.get(i);
                        radiChnMutMpenTab.setdRecordId(UUID.randomUUID().toString());
                        radiChnMutMpenTab.setV01301(post_list.substring(0,5));
                        radiChnMutMpenTab.setV05001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(5,10)))/100)));
                        radiChnMutMpenTab.setV06001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(10,16)))/100)));
                        radiChnMutMpenTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(post_list.substring(16,22)))/10)));
                        radiChnMutMpenTab.setV04002(Short.parseShort(changeType(post_list.substring(22,25))));
                        radiChnMutMpenTab.setV04290(Short.parseShort(changeType(post_list.substring(25,28))));
                        radiChnMutMpenTab.setV14320(new BigDecimal(dealDates(post_list.substring(28,34))));
                        radiChnMutMpenTab.setV14308(new BigDecimal(dealDates(post_list.substring(34,40))));
                        radiChnMutMpenTab.setV14309(new BigDecimal(dealDates(post_list.substring(40,46))));
                        radiChnMutMpenTab.setV14302(new BigDecimal(dealDates(post_list.substring(46,52))));
                        radiChnMutMpenTab.setV14306(new BigDecimal(dealDates(post_list.substring(52,58))));
                        radiChnMutMpenTab.setV143St(new BigDecimal(dealDates(post_list.substring(58,64))));
                        radiChnMutMpenTab.setV14027(new BigDecimal(changeType(post_list.substring(64,72))));
                        radiChnMutMpenTab.setV143201(Integer.parseInt(changeType(post_list.substring(72,78))));
                        radiChnMutMpenTab.setV14320Stime(Integer.parseInt(changeType(post_list.substring(78,85))));
                        try{
                            radiChnMutMpenTab.setV14320Etime(Integer.parseInt(changeType(post_list.substring(85,92))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV14320Etime(999999);
                        }
                        try{
                            radiChnMutMpenTab.setV14308Etime(Integer.parseInt(changeType(post_list.substring(105,112))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV14308Etime(999999);
                        }
                        try{
                            radiChnMutMpenTab.setV143StStime(Integer.parseInt(changeType(post_list.substring(118,125))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV143StStime(999999);
                        }
                        try{
                            radiChnMutMpenTab.setV143StEtim(Integer.parseInt(changeType(post_list.substring(125,132))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV143StEtim(999999);
                        }
                        try{
                            radiChnMutMpenTab.setV143081(Integer.parseInt(changeType(post_list.substring(92,98))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV143081(999999);
                        }
                        try{
                            radiChnMutMpenTab.setV14308Stime(Integer.parseInt(changeType(post_list.substring(98,105))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV14308Stime(999999);
                        }
                        try{
                            radiChnMutMpenTab.setV143St1(Integer.parseInt(changeType(post_list.substring(112,118))));
                        }catch (Exception e){
                            radiChnMutMpenTab.setV143St1(999999);
                        }

                        parseResult.put(radiChnMutMpenTab);
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
        if (dates == null || dates == "" || dates.indexOf("/") > -1 ||  dates.equals('"') ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        if (dates.equals("32766") || dates.equals("32766.0")) {
            dates = "999999";
        }
        return dates;
    }

    public static String dealDates(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.equals('"') ||"99999".equals(dates)) {
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
