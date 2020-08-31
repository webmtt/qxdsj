package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiBmbDatTab;
import cma.cimiss2.dpc.decoder.bean.radi.RadiBmbHourDatTab;
import cma.cimiss2.dpc.decoder.bean.radi.RadiBmbMinDatTab;
import cma.cimiss2.dpc.decoder.fileDecode.util.CommonUtil;
import cma.cimiss2.dpc.decoder.fileDecode.util.DateUtils;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/31 0031 15:11
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class BmbDatTab {
    /**
     * 结果集
     */
    private ParseResult<RadiBmbDatTab> parseResult = new ParseResult<RadiBmbDatTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public Map<String, Object> decode(File file,String pathname) {
        Map<String, Object> resultMap = new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false);
        if (file != null && file.exists() && file.isFile()) {
            if(file.length() <= 0){
                decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                resultMap.put("decodingInfo", decodingInfo);
                return resultMap;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;

                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                List<RadiBmbHourDatTab> radiBmbHourDatTabList = new ArrayList<>();
                List<RadiBmbMinDatTab> radiBmbMinDatTabList = new ArrayList<>();

                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    if (pathname.contains("SR_M_R")) {
                        String messages = txtFileContent.get(0);
                        String V01301 = messages.substring(0,5);
                        String V04001 = messages.substring(5,10);
                        String V04002 = messages.substring(10,15);
                        String V04003 = messages.substring(15,20);
                        String V06001 = messages.substring(20,27);
                        String V05001 = messages.substring(28,34);
                        String V07001 = messages.substring(35,40);
                        String SITELEVEL = messages.substring(40,45);
                        String TRI_HEIGHT = messages.substring(45,50);
                        String ROTARY_HEIGHT = messages.substring(110,115);
                        String LATITUDE_POLYSILICON = messages.substring(165,170);
                        String LATITUDE_MONOCRYSTALLINE = messages.substring(170,175);
                        String LATITUDE_ALTITUDE = messages.substring(175,180);
                        for(int i = 1;i <txtFileContent.size();i++){
                            String msg = txtFileContent.get(i);
                            String HOURMIN = msg.substring(0,4);
                            String TRI_HP = msg.substring(4,8);
                            String TRE_HP = msg.substring(8,12);
                            String LATITUDE_TRI_HP = msg.substring(92,96);
                            String LATITUDE_TRE_HP = msg.substring(96,100);
                            String STRE_RFP = msg.substring(108,112);
                            String SCATRI_RFP = msg.substring(112,116);
                            String SCATRE_RFP = msg.substring(116,120);
                            String MIOTPR_RFP = msg.substring(120,124);
                            String MIOTPRT_RFP = msg.substring(124,128);
                            String MIOPDR_RFP = msg.substring(128,132);
                            String SUN_NUM = msg.substring(144,148);
                            String hours=msg.substring(0,2);
                            String days=changeType(V04003);
                            String month=changeType(V04002);
                            int count=DateUtils.getDayCountOnMonth(Integer.parseInt(changeType(month)),Integer.parseInt(changeType(V04001)));
                            if("24".equals(hours)){
                                //日+1
                                if(Integer.parseInt(days)+1>count){
                                    //月+1
                                    if(Integer.parseInt(V04002)+1>12){
                                        //年+1
                                        V04001=Integer.parseInt(V04001)+1+"";
                                        V04002="01";
                                    }else{
                                        V04002=Integer.parseInt(V04002)+1+"";
                                    }
                                    days="01";
                                }else {
                                    days = Integer.parseInt(days) + 1 + "";
                                }
                                hours="00";
                            }
                            String D_DATETIME = V04001 +"-"+ changeType(V04002) +"-"+ days+" "+ hours +":"+msg.substring(2,4)+":00";
                            RadiBmbMinDatTab radiBmbMinDatTab = new RadiBmbMinDatTab();
                            radiBmbMinDatTab.setdRetainId(UUID.randomUUID().toString());
//                            radiBmbMinDatTab.setdDataId("");
                            radiBmbMinDatTab.setdDatetime(D_DATETIME);
                            radiBmbMinDatTab.setV01301(V01301);
                            radiBmbMinDatTab.setV04001(Short.parseShort(changeType(V04001)));
                            radiBmbMinDatTab.setV04002(Short.parseShort(changeType(V04002)));
                            radiBmbMinDatTab.setV04003(Short.parseShort(changeType(V04003)));
                            radiBmbMinDatTab.setV04004(Short.parseShort(msg.substring(0,2)));
                            radiBmbMinDatTab.setV04005(Short.parseShort(msg.substring(2,4)));
                            radiBmbMinDatTab.setV06001(new BigDecimal(V06001));
                            radiBmbMinDatTab.setV05001(new BigDecimal(V05001));
                            radiBmbMinDatTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(V07001)/10)));
                            radiBmbMinDatTab.setSitelevel(Short.parseShort(changeType(SITELEVEL)));
                            radiBmbMinDatTab.setTriHeight(new BigDecimal(changeType(TRI_HEIGHT)));
                            try{
                                radiBmbMinDatTab.setTriHp(new BigDecimal(changeType(TRI_HP)));
                            }catch (Exception e){
                                radiBmbMinDatTab.setTriHp(new BigDecimal(999999));
                            }
                            if(changeType(TRE_HP).equals("999999")){
                                radiBmbMinDatTab.setTreHp(new BigDecimal(999999));
                            }else {
                                radiBmbMinDatTab.setTreHp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(TRE_HP))/100)));
                            }
                            radiBmbMinDatTab.setRotaryHeight(new BigDecimal(changeType(ROTARY_HEIGHT)));
                            radiBmbMinDatTab.setLatitudePolysilicon(new BigDecimal(changeType(LATITUDE_POLYSILICON)));
                            radiBmbMinDatTab.setLatitudeMonocrystalline(new BigDecimal(changeType(LATITUDE_MONOCRYSTALLINE)));
                            radiBmbMinDatTab.setLatitudeAltitude(new BigDecimal(changeType(LATITUDE_ALTITUDE)));
                            radiBmbMinDatTab.setLatitudeTriHp(new BigDecimal(changeType(LATITUDE_TRI_HP)));
                            if(changeType(LATITUDE_TRE_HP).equals("999999")){
                                radiBmbMinDatTab.setLatitudeTreHp(new BigDecimal(999999));
                            }else {
                                radiBmbMinDatTab.setLatitudeTreHp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(LATITUDE_TRE_HP))/100)));
                            }
                            radiBmbMinDatTab.setStreRfp(new BigDecimal(changeType(STRE_RFP)));
                            if(changeType(SCATRI_RFP).equals("999999")){
                                radiBmbMinDatTab.setScatriRfp(new BigDecimal(999999));
                            }else {
                                radiBmbMinDatTab.setScatriRfp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(SCATRI_RFP))/100)));
                            }
                            radiBmbMinDatTab.setScatreRfp(new BigDecimal(changeType(SCATRE_RFP)));
                            if(changeType(MIOTPR_RFP).equals("999999")){
                                radiBmbMinDatTab.setMiotprRfp(new BigDecimal(999999));
                            }else {
                                radiBmbMinDatTab.setMiotprRfp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MIOTPR_RFP))/100)));
                            }
                            radiBmbMinDatTab.setMiotprtRfp(new BigDecimal(changeType(MIOTPRT_RFP)));
                            if(changeType(MIOPDR_RFP).equals("999999")){
                                radiBmbMinDatTab.setMiopdrRfp(new BigDecimal(999999));
                            }else {
                                radiBmbMinDatTab.setMiopdrRfp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MIOPDR_RFP))/100)));
                            }
                            radiBmbMinDatTab.setSunNum(new BigDecimal(changeType(SUN_NUM)));
                            radiBmbMinDatTabList.add(radiBmbMinDatTab);
                        }
                    }else if (pathname.contains("SR_H_R")) {
                        String messages = txtFileContent.get(0);
                        String V01301 = messages.substring(0,5);
                        String V04001 = messages.substring(5,10);
                        String V04002 = messages.substring(10,15);
                        String V06001 = messages.substring(15,22);
                        String V05001 = messages.substring(23,29);
                        String V07001 = messages.substring(30,35);
                        String SITELEVEL = messages.substring(35,40);
                        String TRI_HEIGHT = messages.substring(40,45);
                        for(int i = 1;i <txtFileContent.size();i++){
                            String msg = txtFileContent.get(i);
                            String HOURMIN = msg.substring(0,4);
                            RadiBmbHourDatTab radiBmbHourDatTab = new RadiBmbHourDatTab();
                            String V04003 = msg.substring(0,2);
                            String V04004 = msg.substring(2,4);
                            String dataTime=V04001+"-"+V04002+"-"+V04003+" "+V04004;
                            String TRI_HP = msg.substring(4,8);
                            String TRE_HP = msg.substring(8,12);
                            String TRI_MAX_HP = msg.substring(12,16);
                            String TRI_HP_DATE = msg.substring(16,20);
                            String LATITUDE_ALTITUDE = msg.substring(180,184);
                            String LATITUDE_TRI_HP = msg.substring(184,188);
                            String LATITUDE_MAX_ALTITUDE = msg.substring(188,192);
                            String LATITUDE_ALTITUDE_DATE = msg.substring(192,196);
                            String STRE_RFP = msg.substring(212,216);
                            String MIOTPR_RFP = msg.substring(216,220);
                            String STRE_MAX_RFP = msg.substring(220,224);
                            String STRE_RFP_DATE = msg.substring(224,228);
                            String SCATRI_RFP = msg.substring(228,232);
                            String SCATRE_RFP = msg.substring(232,236);
                            String SCATRI_MAX_RFP = msg.substring(236,240);
                            String SCATRI_RFP_DATE = msg.substring(240,244);
                            String MIOTPRT_RFP =  msg.substring(244,248);
                            String MIOPDR_RFP = msg.substring(248,252);
                            String MIOTPRT_MAX_RFP = msg.substring(252,256);
                            String MIOTPRT_FP_DATE = msg.substring(256,260);
                            String SUN_NUM = msg.substring(280,284);
                            radiBmbHourDatTab.setdRetainId(UUID.randomUUID().toString());
                            radiBmbHourDatTab.setV01301(V01301);
                            radiBmbHourDatTab.setV04001(Short.parseShort(changeType(V04001)));
                            radiBmbHourDatTab.setV04002(Short.parseShort(changeType(V04002)));
                            radiBmbHourDatTab.setV04003(Short.parseShort(changeType(V04003)));
                            radiBmbHourDatTab.setV04004(Short.parseShort(changeType(V04004)));
                            radiBmbHourDatTab.setV04005((short)0);
                            radiBmbHourDatTab.setV06001(new BigDecimal(V06001));
                            radiBmbHourDatTab.setV05001(new BigDecimal(V05001));
                            radiBmbHourDatTab.setV07001(new BigDecimal(String.valueOf(Double.parseDouble(V07001)/10)));
                            radiBmbHourDatTab.setSitelevel(Short.parseShort(changeType(SITELEVEL)));
                            radiBmbHourDatTab.setdDatetime(dataTime);
                            radiBmbHourDatTab.setTriHp(new BigDecimal(changeType(TRI_HP)));
                            if(changeType(TRE_HP).equals("999999")){
                                radiBmbHourDatTab.setTreHp(new BigDecimal(999999));
                            }else {
                                radiBmbHourDatTab.setTreHp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(TRE_HP))/100)));
                            }
                            radiBmbHourDatTab.setTriMaxHp(new BigDecimal(changeType(TRI_MAX_HP)));
                            radiBmbHourDatTab.setTriHpDate(new BigDecimal(changeType(TRI_HP_DATE)));
                            radiBmbHourDatTab.setLatitudeAltitude(new BigDecimal(changeType(LATITUDE_ALTITUDE)));
                            if(changeType(LATITUDE_TRI_HP).equals("999999")){
                                radiBmbHourDatTab.setLatitudeTriHp(new BigDecimal(999999));
                            }else {
                                radiBmbHourDatTab.setLatitudeTriHp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(LATITUDE_TRI_HP))/100)));
                            }
                            radiBmbHourDatTab.setLatitudeMaxAltitude(new BigDecimal(changeType(LATITUDE_MAX_ALTITUDE)));
                            radiBmbHourDatTab.setLatitudeAltitudeDate(new BigDecimal(changeType(LATITUDE_ALTITUDE_DATE)));
                            radiBmbHourDatTab.setStreRfp(new BigDecimal(changeType(STRE_RFP)));
                            if(changeType(MIOTPR_RFP).equals("999999")){
                                radiBmbHourDatTab.setMiotprRfp(new BigDecimal(999999));
                            }else {
                                radiBmbHourDatTab.setMiotprRfp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MIOTPR_RFP))/100)));
                            }
                            radiBmbHourDatTab.setStreMaxRfp(new BigDecimal(changeType(STRE_MAX_RFP)));
                            radiBmbHourDatTab.setStreRfpDate(new BigDecimal(changeType(STRE_RFP_DATE)));
                            radiBmbHourDatTab.setScatriRfp(new BigDecimal(changeType(SCATRI_RFP)));
                            if(changeType(SCATRE_RFP).equals("999999")){
                                radiBmbHourDatTab.setScatreRfp(new BigDecimal(999999));
                            }else {
                                radiBmbHourDatTab.setScatreRfp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(SCATRE_RFP))/100)));
                            }
                            radiBmbHourDatTab.setScatriMaxRfp(new BigDecimal(changeType(SCATRI_MAX_RFP)));
                            radiBmbHourDatTab.setScatriRfpDate(new BigDecimal(changeType(SCATRI_RFP_DATE)));
                            radiBmbHourDatTab.setMiotprtRfp(new BigDecimal(changeType(MIOTPRT_RFP)));
                            if(changeType(MIOPDR_RFP).equals("999999")){
                                radiBmbHourDatTab.setMiopdrRfp(new BigDecimal(999999));
                            }else {
                                radiBmbHourDatTab.setMiopdrRfp(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MIOPDR_RFP))/100)));
                            }
                            radiBmbHourDatTab.setMiotprtMaxRfp(new BigDecimal(changeType(MIOTPRT_MAX_RFP)));
                            radiBmbHourDatTab.setMiotprtRfpDate(new BigDecimal(changeType(MIOTPRT_FP_DATE)));
                            radiBmbHourDatTab.setSunNum(new BigDecimal(String.valueOf(Double.parseDouble(changeType(SUN_NUM))/60)));
                            radiBmbHourDatTabList.add(radiBmbHourDatTab);
                        }

                    }
                    if (!radiBmbHourDatTabList.isEmpty()) {
                        resultMap.put("radiBmbHourDatTabList", radiBmbHourDatTabList);
                    }
                    if (!radiBmbMinDatTabList.isEmpty()) {
                        resultMap.put("radiBmbMinDatTabList", radiBmbMinDatTabList);
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
        decodingInfo.setSuccess(true);
        resultMap.put("decodingInfo", decodingInfo);
        return resultMap;
    }
    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf(".") > -1) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }
}
