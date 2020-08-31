package cma.cimiss2.dpc.decoder.upar;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmWewTab;
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
 * @date ：Created in 2019/10/23 0023 17:52
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMulFtmWewTab {
    /**
     * 结果集
     */
    private ParseResult<UparChnMulFtmWewTab> parseResult = new ParseResult<UparChnMulFtmWewTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<UparChnMulFtmWewTab> decode(File file) {
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
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        UparChnMulFtmWewTab uparChnMulFtmWewTab = new UparChnMulFtmWewTab();
                        String multab_list = txtFileContent.get(i);
                        String uuid = UUID.randomUUID().toString();
                        uparChnMulFtmWewTab.setdRecordId(uuid);
                        uparChnMulFtmWewTab.setV01301(changeType(multab_list.substring(0,5)));
                        uparChnMulFtmWewTab.setV04001(Short.parseShort(changeType(multab_list.substring(5,10))));
                        uparChnMulFtmWewTab.setV04002(Short.parseShort(changeType(multab_list.substring(10,13))));
                        uparChnMulFtmWewTab.setV04003(Short.parseShort(changeType(multab_list.substring(13,16))));
                        uparChnMulFtmWewTab.setTimes(file.getName().substring(file.getName().indexOf(".") - 2,file.getName().indexOf(".")));
                        uparChnMulFtmWewTab.setdDatetime(changeType(multab_list.substring(5,10))+"-"+changeType(multab_list.substring(10,13))+"-"+changeType(multab_list.substring(13,16))+" "+"00:00:00");
                        uparChnMulFtmWewTab.setArrivaltime(new BigDecimal("999999"));
                        uparChnMulFtmWewTab.setV10009(new BigDecimal(changeType(multab_list.substring(16,22))));
                        uparChnMulFtmWewTab.setV11001(new BigDecimal(changeType(multab_list.substring(22,27))));
                        uparChnMulFtmWewTab.setV11002(new BigDecimal(changeType(multab_list.substring(27,31))));
                        try{
                            uparChnMulFtmWewTab.setQ11001(Short.parseShort(changeType(multab_list.substring(31,32))));
                        }catch (Exception e){
                            uparChnMulFtmWewTab.setQ11001(Short.parseShort("9"));
                        }
                        try{
                            uparChnMulFtmWewTab.setQ11002(Short.parseShort(changeType(multab_list.substring(32,34))));
                        }catch (Exception e){
                            uparChnMulFtmWewTab.setQ11002(Short.parseShort("9"));
                        }

                        parseResult.put(uparChnMulFtmWewTab);
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
        return dates;
    }
}
