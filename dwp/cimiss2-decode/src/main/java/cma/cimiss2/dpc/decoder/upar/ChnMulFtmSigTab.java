package cma.cimiss2.dpc.decoder.upar;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmSigTab;
import cma.cimiss2.dpc.decoder.fileDecode.util.CommonUtil;
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
 * @date ：Created in 2019/10/23 0023 11:09
 * @description：中国高空温湿特性层定时值数据集
 * @modified By：
 * @version: 1.0$
 */
public class ChnMulFtmSigTab {
    /**
     * 结果集
     */
    private ParseResult<UparChnMulFtmSigTab> parseResult = new ParseResult<UparChnMulFtmSigTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<UparChnMulFtmSigTab> decode(File file) {
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
                String fileName=file.getName();
                String[] filenames=fileName.split("-");
                String V01301= filenames[1];
                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        UparChnMulFtmSigTab uparChnMulFtmSigTab = new UparChnMulFtmSigTab();
                        String multab_list = txtFileContent.get(i);
                        String uuid = UUID.randomUUID().toString();
                        uparChnMulFtmSigTab.setdRecordId(uuid);
                        uparChnMulFtmSigTab.setV01301(V01301);
                        uparChnMulFtmSigTab.setV04001(Short.parseShort(changeType(multab_list.substring(0,4))));
                        uparChnMulFtmSigTab.setV04002(Short.parseShort(changeType(multab_list.substring(4,7))));
                        uparChnMulFtmSigTab.setdDatetime(changeType(multab_list.substring(0,4))+"-"+changeType(multab_list.substring(4,7))+"-"+changeType(multab_list.substring(7,10))+" "+"00:00:00");
                        uparChnMulFtmSigTab.setdUpdateTime(changeType(multab_list.substring(10,13)));
                        uparChnMulFtmSigTab.setvLevelC(changeType(multab_list.substring(13,16)));
                        if(changeType(multab_list.substring(16,22)).equals("999999")){
                            uparChnMulFtmSigTab.setV10004(new BigDecimal(999999));
                        }else {
                            uparChnMulFtmSigTab.setV10004(new BigDecimal(String.valueOf(Double.parseDouble(changeType(multab_list.substring(16,22)))/10)));
                        }
                        String v12001=multab_list.substring(24,28);
                        if("999999".equals(changeType(v12001))){
                            uparChnMulFtmSigTab.setV12001(new BigDecimal(999999));
                        }else {
                            try {
                                uparChnMulFtmSigTab.setV12001(new BigDecimal(String.valueOf(Double.parseDouble(changeType(v12001)) / 10)));
                            } catch (Exception e) {
                                uparChnMulFtmSigTab.setV12001(new BigDecimal(999999));
                            }
                        }
                        String v12003=multab_list.substring(28,34);
                        if("999999".equals(changeType(v12003))){
                            uparChnMulFtmSigTab.setV12003(new BigDecimal(999999));
                        }else {
                            try {
                                uparChnMulFtmSigTab.setV12003(new BigDecimal(String.valueOf(Double.parseDouble(changeType(v12003))/10)));
                            }catch (Exception e){
                                uparChnMulFtmSigTab.setV12003(new BigDecimal(999999));
                            }
                        }
                        uparChnMulFtmSigTab.setqBasicparameter(Short.parseShort(changeType(multab_list.substring(34,36))));
                        uparChnMulFtmSigTab.setQ07004(Short.parseShort(changeType(multab_list.substring(36,38))));
                        uparChnMulFtmSigTab.setQ12001(Short.parseShort(changeType(multab_list.substring(38,40))));
                        if(multab_list.length() > 40){
                            uparChnMulFtmSigTab.setQ12003(new BigDecimal(changeType(multab_list.substring(40,42))));
                        }else {
                            uparChnMulFtmSigTab.setQ12003(new BigDecimal("999999"));
                        }
                        parseResult.put(uparChnMulFtmSigTab);
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
        if (dates == null ||"".equals(dates) || dates.indexOf("/") > -1 || dates.indexOf("99999") > -1 || dates.equals('"')||"99999".equals(dates)||"9999".equals(dates)||"999".equals(dates) ) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }

    public static void main(String[] args) {
        File file=new File("F:\\QXDSJ\\doc\\内蒙本地数据资料\\资料样例\\结构化数据\\1.2.4  中国高空温湿特性层定时值数据集(v1.0)\\UPAR_CLI_CHN_MUL_FTM_SIG-50527-00.TXT");
        ChnMulFtmSigTab chnMulFtmSigTab=new ChnMulFtmSigTab();
        chnMulFtmSigTab.decode(file);
    }
}
