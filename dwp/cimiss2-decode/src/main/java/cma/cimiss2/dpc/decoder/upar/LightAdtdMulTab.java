package cma.cimiss2.dpc.decoder.upar;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparLightAdtdMulTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/11 0011 17:56
 * @description：闪电定位实体
 * @modified By：
 * @version: $
 */
public class LightAdtdMulTab {
    /**
     * 结果集
     */
    private ParseResult<UparLightAdtdMulTab> parseResult = new ParseResult<UparLightAdtdMulTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<UparLightAdtdMulTab> decode(File file) {
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
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        UparLightAdtdMulTab uparLightAdtdMulTab = new UparLightAdtdMulTab();
                        String[] multab_list = txtFileContent.get(i).split("\\s+");
                        String[] riqi = multab_list[1].split("-");
                        String[] shifen = multab_list[2].split(":");
                        uparLightAdtdMulTab.setdRecordId(getOrderIdByUUId());
                        uparLightAdtdMulTab.setdSourceId("999999");
                        uparLightAdtdMulTab.setdIymdhm(sdf.format(calendar.getTime()));
                        uparLightAdtdMulTab.setdUpdateTime(sdf.format(calendar.getTime()));
                        uparLightAdtdMulTab.setdRymdhm(sdf.format(calendar.getTime()));
                        uparLightAdtdMulTab.setV04001(Short.parseShort(changeType(riqi[0])));
                        uparLightAdtdMulTab.setV04002(Short.parseShort(changeType(riqi[1])));
                        uparLightAdtdMulTab.setV04003(Short.parseShort(changeType(riqi[2])));
                        uparLightAdtdMulTab.setV04004(Short.parseShort(changeType(shifen[0])));
                        uparLightAdtdMulTab.setV04005(Short.parseShort(changeType(shifen[1])));
                        uparLightAdtdMulTab.setV04006(Short.parseShort(changeType(shifen[2].substring(0, shifen[2].indexOf(".")))));
                        uparLightAdtdMulTab.setV04007(Integer.parseInt(changeType(shifen[2].substring(shifen[2].indexOf(".") + 1, shifen[2].length()))));
                        uparLightAdtdMulTab.setV05001(new BigDecimal(changeType(multab_list[3].substring(multab_list[3].indexOf("=") + 1, multab_list[3].length()))));
                        uparLightAdtdMulTab.setV06001(new BigDecimal(changeType(multab_list[4].substring(multab_list[4].indexOf("=") + 1, multab_list[4].length()))));
                        uparLightAdtdMulTab.setV73016(new BigDecimal(changeType(multab_list[5].substring(multab_list[5].indexOf("=") + 1, multab_list[5].length()))));
                        uparLightAdtdMulTab.setV73023(new BigDecimal(changeType(multab_list[6].substring(multab_list[6].indexOf("=") + 1, multab_list[6].length()))));
                        uparLightAdtdMulTab.setV73011(new BigDecimal(changeType(multab_list[7].substring(multab_list[7].indexOf("=") + 1, multab_list[7].length()))));
                        uparLightAdtdMulTab.setV73110(changeType(multab_list[8].substring(multab_list[8].indexOf("：") + 1, multab_list[8].length())));
                        uparLightAdtdMulTab.setV010151("999999");
                        uparLightAdtdMulTab.setV010152("999999");
                        uparLightAdtdMulTab.setV010153("999999");
                        uparLightAdtdMulTab.setvBbb("999999");
                        uparLightAdtdMulTab.setdDatetime(multab_list[1] + " " + multab_list[2]);
                        parseResult.put(uparLightAdtdMulTab);
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
            parseResult.setSuccess(false);
            System.out.println("File is not Exit");
        }
        return parseResult;
    }

    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1  || dates.equals('"')) {
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
}
