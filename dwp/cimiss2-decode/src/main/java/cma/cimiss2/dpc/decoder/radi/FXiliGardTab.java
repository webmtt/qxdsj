package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliGardHourTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliGardMinTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliGardTab;
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
 * @date ：Created in 2019/11/6 0006 17:05
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class FXiliGardTab {
    /**
     * 结果集
     */
    private ParseResult<SurfXiliGardTab> parseResult = new ParseResult<SurfXiliGardTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public Map<String,Object> decode(File file, String pathname) {
        Map<String,Object> resultMap=new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false) ;
        if (file != null && file.exists() && file.isFile()) {
            if(file.length() <= 0){
                decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                resultMap.put("decodingInfo",decodingInfo);
                return resultMap;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                List<SurfXiliGardMinTab> surfXiliGardMinTabList = new ArrayList<>();
                List<SurfXiliGardHourTab> surfXiliGardHourTabList = new ArrayList<>();
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    if(pathname.contains("PBL_VG_M")){
                        String messages = txtFileContent.get(0).split("-")[0];
                        String V01301 = messages.substring(0,5);
                        String V04001 = messages.substring(5,9);
                        String V04002 = messages.substring(9,11);
                        String V04003 = messages.substring(11,13);
                        String V06001 = messages.substring(13,20);
                        String V05001 = messages.substring(20,26);
                        String V07001 = messages.substring(26,33);
                        String V07001_0001 = messages.substring(33,38);
                        String V07001_0002 = messages.substring(38,43);
                        String V07001_0003 = messages.substring(43,48);
                        String V07001_0004 = messages.substring(48,53);
                        String V07001_0005 = messages.substring(53,58);
                        String V01300 = messages.substring(58,63);
                        String GROUND_DISTANCE = messages.substring(63,68);
                        String INFRA_RED = messages.substring(68,73);
                        String VERSION = txtFileContent.get(0).substring(txtFileContent.get(0).length() - 5,txtFileContent.get(0).length());
                        for(int i = 1; i < txtFileContent.size(); i++){
                            String post_list = txtFileContent.get(i);
                            String dDataId = "A.0006.0001.S0001";
                            String HOURMIN = changeType(post_list.substring(0,5));
                            String V02001 = changeType(post_list.substring(5,9));
                            String V02301 = changeType(post_list.substring(9,13));
                            String V_ACODE = changeType(post_list.substring(13,17));
                            String V_BBB = changeType(post_list.substring(17,21));
                            String V08010 = changeType(post_list.substring(21,25));
                            String V07031 = changeType(post_list.substring(25,29));
                            String V07032_02 = changeType(post_list.substring(29,33));
                            String V12120 = changeType(post_list.substring(33,37));
                            String V12030_005 = changeType(post_list.substring(37,41));
                            String V12030_010 = changeType(post_list.substring(41,45));
                            String V12030_015 = changeType(post_list.substring(45,49));
                            String V12030_020 = changeType(post_list.substring(49,53));
                            String V12030_040 = changeType(post_list.substring(53,57));
                            String V71105_010 = changeType(post_list.substring(57,61));
                            String V71105_020 = changeType(post_list.substring(61,65));
                            String V71105_050 = changeType(post_list.substring(65,69));
                            String V71105_100 = changeType(post_list.substring(69,73));
                            String V71105_180 = changeType(post_list.substring(73,77));
                            String SHFLUX1 = changeType(post_list.substring(77,81));
                            String SHFLUX2 = changeType(post_list.substring(81,85));
                            String SHFLUX3 = changeType(post_list.substring(85,89));
                            String AWSPEED1M1L = changeType(post_list.substring(89,93));
                            String AWSPEED10M1L = changeType(post_list.substring(93,97));
                            String MAXWSPEED1L = changeType(post_list.substring(97,101));
                            String AWSPEED1M2L = changeType(post_list.substring(101,105));
                            String AWSPEED10M2L = changeType(post_list.substring(105,109));
                            String MAXWSPEED2L = changeType(post_list.substring(109,113));
                            String AWSPEED1M10MH = changeType(post_list.substring(113,117));
                            String AWDIRECTION1M10MH = changeType(post_list.substring(117,120));
                            String AWSPEED10M10MH = changeType(post_list.substring(120,124));
                            String AWDIRECTION10M10MH = changeType(post_list.substring(124,127));
                            String MAXWSPEED10MH = changeType(post_list.substring(127,131));
                            String MAXWIND10M=changeType(post_list.substring(131,134));
                            String AVGSPEED1M4L=changeType(post_list.substring(134,138));
                            String AVGSPEED10M4L=changeType(post_list.substring(138,142));
                            String MAXWIND4L=changeType(post_list.substring(142,146));
                            String AWSPEED1M5L = changeType(post_list.substring(146,150));
                            String AWSPEED10M5L = changeType(post_list.substring(150,154));
                            String MAXWSPEED5L = changeType(post_list.substring(154,158));
                            String ATEMP1M1L = changeType(post_list.substring(158,162));
                            String ARELAHUM1M1L = changeType(post_list.substring(162,165));
                            String AWVPRE1M1L = changeType(post_list.substring(165,168));
                            String ATEMP1M2L = changeType(post_list.substring(168,172));
                            String ARELAHUM1M2L = changeType(post_list.substring(172,175));
                            String AWVPRE1M2L = changeType(post_list.substring(175,178));
                            String ATEMP1M10MH = changeType(post_list.substring(178,182));
                            String ARELAHUM1M10MH = changeType(post_list.substring(182,185));
                            String AWVPRE1M10MH = changeType(post_list.substring(185,188));
                            String ATEMP1M4L = changeType(post_list.substring(188,192));
                            String ARELAHUM1M4L = changeType(post_list.substring(192,195));
                            String AWVPRE1M4L = changeType(post_list.substring(195,198));
                            String ATEMP1M5L = changeType(post_list.substring(198,202));
                            String ARELAHUM1M5L = changeType(post_list.substring(202,205));
                            String AWVPRE1M5L = changeType(post_list.substring(205,208));
                            String ALPRESSURE1M = changeType(post_list.substring(208,213));
                            String CUMPRE1M = "999999";
                            String CUMEVA1M = "999999";
                            SurfXiliGardMinTab surfXiliGardMinTab = new SurfXiliGardMinTab();
                            String uuid = UUID.randomUUID().toString();
                            surfXiliGardMinTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));
                            surfXiliGardMinTab.setdDataId(dDataId);
                            surfXiliGardMinTab.setV01301(V01301);
                            String[] m=HOURMIN.split(":");
                            String v04004=m[0];
                            String v04005=m[1];
                            String dDatetime =null;
                            if(i<240){
                                //日-1
                                int newv04003=Integer.parseInt(V04003)-1;
                                int newV04002=Integer.parseInt(V04002);
                                int newV04001=Integer.parseInt(V04001);
                                if(newv04003<1){
                                    newV04002=newV04002-1;
                                    int maxMonth= DateUtils.getDayCountOnMonth(newV04002,newV04001);
                                    newv04003=maxMonth;
                                    if(newV04002<1){
                                        newV04001=newV04001-1;
                                        newV04002=1;
                                        newv04003=1;
                                    }
                                }
                                dDatetime = newV04001 + "-" + newV04002 + "-" + newv04003 + " " + v04004+":"+v04005;
                                surfXiliGardMinTab.setV04001(newV04001+"");
                                surfXiliGardMinTab.setV04002(newV04002+"");
                                surfXiliGardMinTab.setV04003(newv04003+"");
                            }else{
                                dDatetime=V04001+"-"+V04002+"-"+V04003+" "+v04004+":"+v04005;
                                surfXiliGardMinTab.setV04001(changeType(V04001));
                                surfXiliGardMinTab.setV04002(changeType(V04002));
                                surfXiliGardMinTab.setV04003(changeType(V04003));
                            }

                            surfXiliGardMinTab.setV04004(changeType(v04004));

                            surfXiliGardMinTab.setV04005(changeType(v04005));

                            surfXiliGardMinTab.setdDatetime(dDatetime);
                            surfXiliGardMinTab.setV06001(V06001);
                            surfXiliGardMinTab.setV05001(V05001);
                            surfXiliGardMinTab.setV07001(V07001);
                            surfXiliGardMinTab.setV070010001(V07001_0001);
                            surfXiliGardMinTab.setV070010002(V07001_0002);
                            surfXiliGardMinTab.setV070010003(V07001_0003);
                            surfXiliGardMinTab.setV070010004(V07001_0004);
                            surfXiliGardMinTab.setV070010005(V07001_0005);
                            surfXiliGardMinTab.setV01300(V01300);
                            surfXiliGardMinTab.setGroundDistance(new BigDecimal(GROUND_DISTANCE));
                            surfXiliGardMinTab.setInfraRed(new BigDecimal(INFRA_RED));

                            surfXiliGardMinTab.setHourmin(HOURMIN);

                            surfXiliGardMinTab.setV02001(Integer.parseInt(V02001));

                            surfXiliGardMinTab.setV02301(Integer.parseInt(V02301));

                            surfXiliGardMinTab.setvAcode(V_ACODE);

                            if(changeType(V_BBB).equals("999999")){
                                surfXiliGardMinTab.setvBbb("999999");
                            }else {
                                surfXiliGardMinTab.setvBbb(String.valueOf(Double.parseDouble(changeType(V_BBB))/10));
                            }

                            surfXiliGardMinTab.setV08010(new BigDecimal(V08010));

//                            surfXiliGardMinTab.setV07031(new BigDecimal(V07031));

                            if(changeType(V07031).equals("999999")){
                                surfXiliGardMinTab.setV07031(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV07031(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V07031))/10)));
                            }

                            surfXiliGardMinTab.setV0703202(new BigDecimal(V07032_02));

//                            surfXiliGardMinTab.setV12120(new BigDecimal(V12120));

                            if(changeType(V12120).equals("999999")){
                                surfXiliGardMinTab.setV12120(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV12120(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V12120))/10)));
                            }
                            if(changeType(V12030_005).equals("999999")){
                                surfXiliGardMinTab.setV12030005(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV12030005(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V12030_005))/10)));
                            }
                            if(changeType(V12030_010).equals("999999")){
                                surfXiliGardMinTab.setV12030010(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV12030010(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V12030_010))/10)));
                            }
                            if(changeType(V12030_015).equals("999999")){
                                surfXiliGardMinTab.setV12030015(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV12030015(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V12030_015))/10)));
                            }
                            if(changeType(V12030_020).equals("999999")){
                                surfXiliGardMinTab.setV12030020(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV12030020(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V12030_020))/10)));
                            }
                            if(changeType(V12030_040).equals("999999")){
                                surfXiliGardMinTab.setV12030040(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV12030040(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V12030_040))/10)));
                            }

//                            surfXiliGardMinTab.setV71105010(new BigDecimal(V71105_010));
//
//                            surfXiliGardMinTab.setV71105020(new BigDecimal(V71105_020));
//
//                            surfXiliGardMinTab.setV71105050(new BigDecimal(V71105_050));
//
//                            surfXiliGardMinTab.setV71105100(new BigDecimal(V71105_100));
//
//                            surfXiliGardMinTab.setV71105180(new BigDecimal(V71105_180));
                            if(changeType(V71105_010).equals("999999")){
                                surfXiliGardMinTab.setV71105010(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV71105010(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V71105_010))/10)));
                            }
                            if(changeType(V71105_020).equals("999999")){
                                surfXiliGardMinTab.setV71105020(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV71105020(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V71105_020))/10)));
                            }
                            if(changeType(V71105_050).equals("999999")){
                                surfXiliGardMinTab.setV71105050(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV71105050(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V71105_050))/10)));
                            }
                            if(changeType(V71105_100).equals("999999")){
                                surfXiliGardMinTab.setV71105100(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV71105100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V71105_100))/10)));
                            }

                            if(changeType(V71105_180).equals("999999")){
                                surfXiliGardMinTab.setV71105180(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setV71105180(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V71105_180))/10)));
                            }

                            surfXiliGardMinTab.setShflux1(SHFLUX1);

                            surfXiliGardMinTab.setShflux2(SHFLUX2);

                            surfXiliGardMinTab.setShflux3(SHFLUX3);

                            if(changeType(AWSPEED1M1L).equals("999999")){
                                surfXiliGardMinTab.setAwspeed1m1l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed1m1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED1M1L))/10)));
                            }
                            if(changeType(AWSPEED10M1L).equals("999999")){
                                surfXiliGardMinTab.setAwspeed10m1l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed10m1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED10M1L))/10)));
                            }
//                            surfXiliGardMinTab.setAwspeed1m1l(new BigDecimal(AWSPEED1M1L));
//
//                            surfXiliGardMinTab.setAwspeed10m1l(new BigDecimal(AWSPEED10M1L));

//                            surfXiliGardMinTab.setMaxwspeed1l(new BigDecimal(MAXWSPEED1L));
                            if(changeType(MAXWSPEED1L).equals("999999")){
                                surfXiliGardMinTab.setMaxwspeed1l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setMaxwspeed1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEED1L))/10)));
                            }

                            if(changeType(AWSPEED1M2L).equals("999999")){
                                surfXiliGardMinTab.setAwspeed1m2l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed1m2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED1M2L))/10)));
                            }

                            if(changeType(AWSPEED10M2L).equals("999999")){
                                surfXiliGardMinTab.setAwspeed10m2l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed10m2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED10M2L))/10)));
                            }

                            if(changeType(MAXWSPEED2L).equals("999999")){
                                surfXiliGardMinTab.setMaxwspeed2l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setMaxwspeed2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEED2L))/10)));
                            }

                            if(changeType(AWSPEED1M10MH).equals("999999")){
                                surfXiliGardMinTab.setAwspeed1m10mh(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed1m10mh(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED1M10MH))/10)));
                            }

                            surfXiliGardMinTab.setAwdirection1m10mh(new BigDecimal(AWDIRECTION1M10MH));

                            if(changeType(AWSPEED10M10MH).equals("999999")){
                                surfXiliGardMinTab.setAwspeed10m10mh(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed10m10mh(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED10M10MH))/10)));
                            }

                            surfXiliGardMinTab.setAwdirection10m10mh(new BigDecimal(AWDIRECTION10M10MH));

                            if(changeType(MAXWSPEED10MH).equals("999999")){
                                surfXiliGardMinTab.setMaxwspeed10mh(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setMaxwspeed10mh(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEED10MH))/10)));
                            }

                            surfXiliGardMinTab.setMaxwind10m(new BigDecimal(changeType(MAXWIND10M)));

                            if(changeType(AVGSPEED1M4L).equals("999999")){
                                surfXiliGardMinTab.setAvgspeed1m4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAvgspeed1m4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AVGSPEED1M4L))/10)));
                            }

                            if(changeType(AVGSPEED10M4L).equals("999999")){
                                surfXiliGardMinTab.setAvgspeed10m4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAvgspeed10m4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AVGSPEED10M4L))/10)));
                            }

                            if(changeType(AVGSPEED10M4L).equals("999999")){
                                surfXiliGardMinTab.setAvgspeed10m4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAvgspeed10m4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AVGSPEED10M4L))/10)));
                            }

                            if(changeType(MAXWIND4L).equals("999999")){
                                surfXiliGardMinTab.setMaxwind4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setMaxwind4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWIND4L))/10)));
                            }

                            if(changeType(AWSPEED1M5L).equals("999999")){
                                surfXiliGardMinTab.setAwspeed1m5l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed1m5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED1M5L))/10)));
                            }


                            if(changeType(AWSPEED10M5L).equals("999999")){
                                surfXiliGardMinTab.setAwspeed10m5l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwspeed10m5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEED10M5L))/10)));
                            }

                            if(changeType(MAXWSPEED5L).equals("999999")){
                                surfXiliGardMinTab.setMaxwspeed5l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setMaxwspeed5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEED5L))/10)));
                            }

                            if(changeType(ATEMP1M1L).equals("999999")){
                                surfXiliGardMinTab.setAtemp1m1l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAtemp1m1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMP1M1L))/10)));
                            }

                            surfXiliGardMinTab.setArelahum1m1l(new BigDecimal(ARELAHUM1M1L));

                            try{
                                if(changeType(AWVPRE1M1L).equals("999999")){
                                    surfXiliGardMinTab.setAwvpre1m1l(new BigDecimal(999999));
                                }else {
                                    surfXiliGardMinTab.setAwvpre1m1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPRE1M1L))/10)));
                                }
                            }catch (Exception e){
                                surfXiliGardMinTab.setAwvpre1m1l(new BigDecimal("999999"));
                            }

                            if(changeType(ATEMP1M2L).equals("999999")){
                                surfXiliGardMinTab.setAtemp1m2l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAtemp1m2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMP1M2L))/10)));
                            }

                            surfXiliGardMinTab.setArelahum1m2l(new BigDecimal(ARELAHUM1M2L));

                            if(changeType(AWVPRE1M2L).equals("999999")){
                                surfXiliGardMinTab.setAwvpre1m2l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwvpre1m2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPRE1M2L))/10)));
                            }

                            if(changeType(ATEMP1M10MH).equals("999999")){
                                surfXiliGardMinTab.setAtemp1m10mh(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAtemp1m10mh(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMP1M10MH))/10)));
                            }


                            surfXiliGardMinTab.setArelahum1m10mh(new BigDecimal(ARELAHUM1M10MH));

                            if(changeType(AWVPRE1M10MH).equals("999999")){
                                surfXiliGardMinTab.setAwvpre1m10mh(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwvpre1m10mh(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPRE1M10MH))/10)));
                            }

                            if(changeType(ATEMP1M4L).equals("999999")){
                                surfXiliGardMinTab.setAtemp1m4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAtemp1m4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMP1M4L))/10)));
                            }

                            surfXiliGardMinTab.setArelahum1m4l(new BigDecimal(ARELAHUM1M4L));

                            if(changeType(AWVPRE1M4L).equals("999999")){
                                surfXiliGardMinTab.setAwvpre1m4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwvpre1m4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPRE1M4L))/10)));
                            }

                            if(changeType(ATEMP1M5L).equals("999999")){
                                surfXiliGardMinTab.setAtemp1m5l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAtemp1m5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMP1M5L))/10)));
                            }
                            surfXiliGardMinTab.setArelahum1m5l(new BigDecimal(ARELAHUM1M5L));
                            if(changeType(AWVPRE1M5L).equals("999999")){
                                surfXiliGardMinTab.setAwvpre1m5l(new BigDecimal(999999));
                            }else {
                                surfXiliGardMinTab.setAwvpre1m5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPRE1M5L))/10)));
                            }

                            surfXiliGardMinTab.setAlpressure1m(new BigDecimal(ALPRESSURE1M));

                            surfXiliGardMinTab.setCumpre1m(new BigDecimal(CUMPRE1M));

                            surfXiliGardMinTab.setCumeva1m(new BigDecimal(CUMEVA1M));

                            surfXiliGardMinTabList.add(surfXiliGardMinTab);
                        }

                    }else if(pathname.contains("PBL_VG_FT")) {
                        String messages = txtFileContent.get(0).split("-")[0];
                        String V01301 = messages.substring(0,5);
                        String V04001 = messages.substring(5,9);
                        String V04002 = messages.substring(9,11);
                        String V06001 = messages.substring(11,18);
                        String V05001 = messages.substring(18,24);
                        String V07001 = messages.substring(24,31);
                        String V07001_0001 = messages.substring(31,36);
                        String V07001_0002 = messages.substring(36,41);
                        String V07001_0003 = messages.substring(41,46);
                        String V07001_0004 = messages.substring(46,51);
                        String V07001_0005 = messages.substring(51,56);
                        String V01300 = messages.substring(56,61);
                        if(V07001.startsWith("0")){
                            V07001 = V07001.substring(1,V07001.length());
                            if(V07001.startsWith("0")){
                                V07001 = V07001.substring(1,V07001.length());
                            }
                        }
                        if(V07001_0001.startsWith("0")){
                            V07001_0001 = V07001_0001.substring(1,V07001_0001.length());
                            if(V07001_0001.startsWith("0")){
                                V07001_0001 = V07001_0001.substring(1,V07001_0001.length());
                            }
                        }
                        if(V07001_0002.startsWith("0")){
                            V07001_0002 = V07001_0002.substring(1,V07001_0002.length());
                            if(V07001_0002.startsWith("0")){
                                V07001_0002 = V07001_0002.substring(1,V07001_0002.length());
                            }
                        }
                        if(V07001_0003.startsWith("0")){
                            V07001_0003 = V07001_0003.substring(1,V07001_0003.length());
                            if(V07001_0003.startsWith("0")){
                                V07001_0003 = V07001_0003.substring(1,V07001_0003.length());
                            }
                        }
                        if(V07001_0004.startsWith("0")){
                            V07001_0004 = V07001_0004.substring(1,V07001_0004.length());
                            if(V07001_0004.startsWith("0")){
                                V07001_0004 = V07001_0004.substring(1,V07001_0004.length());
                            }
                        }
                        if(V07001_0005.startsWith("0")){
                            V07001_0005 = V07001_0005.substring(1,V07001_0005.length());
                            if(V07001_0005.startsWith("0")){
                                V07001_0005 = V07001_0005.substring(1,V07001_0005.length());
                            }
                        }
                        if(V01300.startsWith("0")){
                            V01300 = V01300.substring(1,V01300.length());
                            if(V01300.startsWith("0")){
                                V01300 = V01300.substring(1,V01300.length());
                            }                        }
                        String GROUND_DISTANCE = messages.substring(61,66);
                        String INFRA_RED = messages.substring(66,71);
                        String DSRSENSORIDEN = messages.substring(71,72);
                        String USRSENSORIDEN = messages.substring(72,73);
                        String DLWRSENSORIDEN = messages.substring(73,74);
                        String ULWRSENSORIDEN = messages.substring(74,75);
                        String PARSENSORIDEN = messages.substring(75,76);
                        String GTEMPSENSORIDEN = messages.substring(76,77);
                        String GTEMPSENSORIDEN5 = messages.substring(77,78);
                        String GTEMPSENSORIDEN10 = messages.substring(78,79);
                        String GTEMPSENSORIDEN15 = messages.substring(79,80);
                        String GTEMPSENSORIDEN20 = messages.substring(80,81);
                        String GTEMPSENSORIDEN40 = messages.substring(81,82);
                        String SMSENSORIDEN10 = messages.substring(82,83);
                        String SMSENSORIDEN20 = messages.substring(83,84);
                        String SMSENSORIDEN50 = messages.substring(84,85);
                        String SMSENSORIDEN100 = messages.substring(85,86);
                        String GTEMPSENSORIDEN180 = messages.substring(86,87);
                        String SHFLUX1 = messages.substring(87,88);
                        String SHFLUX2 = messages.substring(88,89);
                        String SHFLUX3 = messages.substring(89,90);
                        String WSSENSORIDEN1L = messages.substring(90,91);
                        String TAHSENSORIDEN1L = messages.substring(91,92);
                        String WSSENSORIDEN2L = messages.substring(92,93);
                        String TAHSENSORIDEN2L = messages.substring(93,94);
                        String WSSENSORIDEN10MH = messages.substring(94,95);
                        String WDSENSORIDEN10MH = messages.substring(95,96);
                        String TAHSENSORIDEN10MH = messages.substring(96,97);
                        String WSSENSORIDEN4L = messages.substring(97,98);
                        String TAHSENSORIDEN4L = messages.substring(98,99);
                        String WSSENSORIDEN5L = messages.substring(99,100);
                        String TAHSENSORIDEN5L = messages.substring(100,101);
                        String AIRPSENSORIDEN = messages.substring(101,102);
                        String EVASENSORIDEN = messages.substring(102,103);
                        String VERSION = txtFileContent.get(0).substring(txtFileContent.get(0).length() - 5,txtFileContent.get(0).length());
                        for(int i = 1; i < txtFileContent.size(); i++) {
                            String post_list = txtFileContent.get(i);
                            String HOURMIN = post_list.substring(11, 16);
                            String dDataId = "A.0006.0002.S0001";
                            String dDatetime = post_list.substring(0, 16);
                            String[] dtimes=dDatetime.split("-");
                             V04001 = dtimes[0];
                            V04002 = dtimes[1];
                            String[] dd=dtimes[2].split(" ");
                            String[] mm=dd[1].split(":");
                            String V04003=dd[0];
                            String V04004=mm[0];
                            String V04005=mm[1];
                            String MIDSRADIATIONHAH = post_list.substring(16, 20);
                            String MAXHDSRADIATION = post_list.substring(20, 24);
                            String TMAXOWHDSWRADIATION = post_list.substring(24, 28);
                            String EHDSWRADIATION = post_list.substring(28, 32);
                            String AWSWRADIATIONIHAH = post_list.substring(32, 36);
                            String MAXIHOUSRADIATION = post_list.substring(36, 40);//向上短波辐射辐照度小时内的最大值

                            String MAXTOUSRADIATIONHI = post_list.substring(40, 44);//向上短波辐射辐照度小时内的最大值出现时间

                            String EHUSWRADIATION = post_list.substring(44, 48);//向上短波辐射小时内的曝辐量

                            String MIDLWRADIATIONHAH = post_list.substring(48, 52);//半小时内向下长波辐射辐照度平均值

                            String MAXIHDLWRADIATION = post_list.substring(52, 56);//向下长波辐射辐照度小时内的最大值

                            String MAXTOWHDLWRADIATIONI = post_list.substring(56, 60);//向下长波辐射辐照度小时内的最大值出现时间

                            String EWHDLWRADIATION = post_list.substring(60, 64);//向下长波辐射小时内的曝辐量

                            String MIULWRADIATIONHAH = post_list.substring(64, 68);//半小时内向上长波辐射辐照度平均值

                            String MAXIHULWRADIATION = post_list.substring(68, 72);//向上长波辐射辐照度小时内的最大值

                            String MAXTOWHULWRADIATIONI = post_list.substring(72, 76);//向上长波辐射辐照度小时内的最大值出现时间

                            String EIHULWRADIATION = post_list.substring(76, 80);//向上长波辐射小时内的曝辐量

                            String ANRADIAIHAH = changeType(post_list.substring(80, 84));//半小时内净辐射辐照度平均值

                            String MAXIHNRADIAI = changeType(post_list.substring(84, 88));//净辐射辐照度小时内的最大值

                            String MAXTOWHNRADIAI = post_list.substring(88, 92);//净辐射辐照度小时内的最大值出现时间

                            String MINHNRADIAI = changeType(post_list.substring(92, 96));//净辐射辐照度小时内的最小值

                            String MINTOWHNRADIAI = post_list.substring(96, 100);//净辐射辐照度小时内的最小值出现时间

                            String EWHNRADIATION = changeType(post_list.substring(100, 104));//净辐射小时内的曝辐量

                            String AIPARADIAHAH = post_list.substring(104, 108);//半小时内光合有效辐射辐照度平均值

                            String V14340_05_60 = post_list.substring(108, 112);//光合有效辐射辐照度小时内的最大值

                            String V14340_05_052 = post_list.substring(112, 116);//光合有效辐射辐照度小时内的最大值出现时间

                            String EPARADIATIONIH = post_list.substring(116, 120);//光合有效辐射小时内的曝辐量

                            String AGTEMPHAH = changeType(post_list.substring(120, 124));//半小时内平均地面温度（红外）

                            String MAXGTEMPH = changeType(post_list.substring(124, 128));//小时内地面最高温度（红外）

                            String MAXOTGH = changeType(post_list.substring(128, 132));//小时内地面最高（红外）出现时间

                            String MINGTEMPH = changeType(post_list.substring(132, 136));//小时内地面最低温度（红外）

                            String MINOTGH = changeType(post_list.substring(136, 140));//小时内地面最低（红外）出现时间

                            String AGTEMP5CMHAH = changeType(post_list.substring(140, 144));//半小时内平均5cm地温

                            String AGTEMP10CMHAH = changeType(post_list.substring(144, 148));//半小时内平均10cm地温

                            String AGTEMP15CMHAH = changeType(post_list.substring(148, 152));//半小时内平均15cm地温

                            String AGTEMP20CMHAH = changeType(post_list.substring(152, 156));//半小时内平均20cm地温

                            String AGTEMP40CMHAH = changeType(post_list.substring(156, 160));//半小时内平均40cm地温

                            String A10CMSVWCHAH = changeType(post_list.substring(160, 164));//半小时内平均10 cm土壤体积含水量

                            String A20CMSVWCHAH = changeType(post_list.substring(164, 168));//半小时内平均20 cm土壤体积含水量

                            String A50CMSVWCHAH = changeType(post_list.substring(168, 172));//半小时内平均50 cm土壤体积含水量

                            String A100CMSVWCHAH = changeType(post_list.substring(172, 176));//半小时内平均100 cm土壤体积含水量

                            String A180CMSVWCHAH = changeType(post_list.substring(176, 180));//半小时内平均180 cm土壤体积含水量

                            String ASHFLUXHAH = changeType(post_list.substring(180, 184));//半小时内平均土壤热通量

                            String AWSPEEDHAH1L = post_list.substring(184, 188);//第1层高度半小时内平均风速

                            String EXWSPEEDH1L = post_list.substring(188, 192);//第1层高度小时内极大风速

                            String MAXWSPEEDTIMEH1L = post_list.substring(192, 196);//第1层高度小时内极大风速出现时间

                            String MAXWSPEEDH1L = post_list.substring(196, 200);//第1层高度小时内最大风速

                            String TIMEMAXWVH1L = post_list.substring(200, 204);//第1层高度小时内最大风速出现时间

                            String AWSPEEDHAH2L = post_list.substring(204, 208);//第2层高度半小时内平均风速

                            String EXWSPEEDH2L = post_list.substring(208, 212);//第2层高度小时内极大风速

                            String MAXWSPEEDTIMEH2L = post_list.substring(212, 216);//第2层高度小时内极大风速出现时间

                            String MAXWSPEEDH2L = post_list.substring(216, 220);//第2层高度小时内最大风速

                            String TIMEMAXWVH2L = post_list.substring(220, 224);//第2层高度小时内最大风速出现时间

                            String AWSPEEDHAH10M = post_list.substring(224, 228);//10米高度半小时内平均风速

                            String AVSHWV10MHAH = post_list.substring(228, 232);//10米高度半小时内平均矢量合成水平风速

                            String AVCHWDIR10MHAH = post_list.substring(232, 236);//10米高度半小时内平均矢量合成水平风向

                            String SDWDIRECTION = post_list.substring(236, 243);//风向标准差

                            String EXWSPEEDH10M = post_list.substring(243, 247);//10米高度小时内极大风速

                            String MAXWDIRWSPEED10DH = post_list.substring(247, 250);//10米高度小时内极大风速的风向

                            String MAXWSPEEDT10DH = post_list.substring(250, 254);//10米高度小时内极大风速出现时间

                            String MAXWSPEEDH10M = post_list.substring(254, 258);//10米高度小时内最大风速

                            String MAXWDIRWSPEED10MH = post_list.substring(258, 261);//10米高度小时内最大风速的风向

                            String MAXWSPEEDT10MH = post_list.substring(261, 265);//10米高度小时内最大风速出现时间

                            String AWSPEEDHAH4L = post_list.substring(265, 269);//第4层高度半小时内平均风速

                            String EXWSPEEDH4L = post_list.substring(269, 273);//第4层高度小时内极大风速

                            String MAXWSPEEDTIMEH4L = post_list.substring(273, 277);//第4层高度小时内极大风速出现时间

                            String MAXWSPEEDH4L = post_list.substring(277, 281);//第4层高度小时内最大风速

                            String TIMEMAXWVH4L = post_list.substring(281, 285);//第4层高度小时内最大风速出现时间

                            String AWSPEEDHAH5L = post_list.substring(285, 289);//第5层高度半小时内平均风速

                            String EXWSPEEDH5L = post_list.substring(289, 293);//第5层高度小时内极大风速

                            String MAXWSPEEDTIMEH5L = post_list.substring(293, 297);//第5层高度小时内极大风速出现时间

                            String MAXWSPEEDH5L = changeType(post_list.substring(297, 301));//第5层高度小时内最大风速

                            String TIMEMAXWVH5L = changeType(post_list.substring(301, 305));//第5层高度小时内最大风速出现时间

                            String ATEMPHAH1L = changeType(post_list.substring(305, 309));//第1层高度半小时平均气温

                            String MAXTEMPH1L = changeType(post_list.substring(309, 313));//第1层高度小时内最高气温

                            String TMAXTEMPAH1L = changeType(post_list.substring(313, 317));//第1层高度小时内最高气温出现时间

                            String MINTEMPH1L = changeType(post_list.substring(317, 321));//第1层高度小时内最低气温

                            String TMINTEMPAH1L = changeType(post_list.substring(321, 325));//第1层高度小时内最低气温出现时间

                            String ARHHAH1L = post_list.substring(325, 328);//第1层高度半小时平均相对湿度

                            String MINRHH1L = post_list.substring(328, 331);//第1层高度小时内最小相对湿度

                            String MINRHOTH1L = post_list.substring(331, 335);//第1层高度小时内最小相对湿度出现时间

                            String AWVPHAH1L = post_list.substring(335, 338);//第1层高度半小时平均水汽压

                            String ATEMPHAH2L = post_list.substring(338, 342);//第2层高度半小时平均气温

                            String MAXTEMPH2L = post_list.substring(342, 346);//第2层高度小时内最高气温

                            String TMAXTEMPAH2L = post_list.substring(346, 350);//第2层高度小时内最高气温出现时间

                            String MINTEMPH2L = post_list.substring(350, 354);//第2层高度小时内最低气温

                            String TMINTEMPAH2L = post_list.substring(354, 358);//第2层高度小时内最低气温出现时间

                            String ARHHAH2L = post_list.substring(358, 361);//第2层高度半小时平均相对湿度

                            String MINRHH2L = post_list.substring(361, 364);//第2层高度小时内最小相对湿度

                            String MINRHOTH2L = post_list.substring(364, 368);//第2层高度小时内最小相对湿度出现时间

                            String AWVPHAH2L = post_list.substring(368, 371);//第2层高度半小时平均水汽压

                            String ATEMPHAH10L = post_list.substring(371, 375);//10米高度半小时平均气温

                            String MAXTEMPH10L = post_list.substring(375, 379);//10米高度小时内最高气温

                            String TMAXTEMPAH10L = post_list.substring(379, 383);//10米高度小时内最高气温出现时间

                            String MINTEMPH10L = post_list.substring(383, 387);//10米高度小时内最低气温

                            String TMINTEMPAH10L = post_list.substring(387, 391);//10米高度小时内最低气温出现时间

                            String ARHHAH10L = post_list.substring(391, 394);//10米高度半小时平均相对湿度

                            String MINRHH10L = post_list.substring(394, 397);//10米高度小时内最小相对湿度

                            String MINRHOTH10L = post_list.substring(397, 401);//10米高度小时内最小相对湿度出现时间

                            String AWVPHAH10L = post_list.substring(401, 404);//10米高度半小时平均水汽压

                            String ATEMPHAH4L = post_list.substring(404, 408);//第4层高度半小时平均气温

                            String MAXTEMPH4L = post_list.substring(408, 412);//第4层高度小时内最高气温

                            String TMAXTEMPAH4L = post_list.substring(412, 416);//第4层高度小时内最高气温出现时间

                            String MINTEMPH4L = post_list.substring(416, 420);//第4层高度小时内最低气温

                            String TMINTEMPAH4L = post_list.substring(420, 424);//第4层高度小时内最低气温出现时间

                            String ARHHAH4L = post_list.substring(424, 427);//第4层高度半小时平均相对湿度

                            String MINRHH4L = post_list.substring(427, 430);//第4层高度小时内最小相对湿度

                            String MINRHOTH4L = post_list.substring(430, 434);//第4层高度小时内最小相对湿度出现时间

                            String AWVPHAH4L = post_list.substring(434, 437);//第4层高度半小时平均水汽压

                            String ATEMPHAH5L = post_list.substring(437, 441);//第5层高度半小时平均气温

                            String MAXTEMPH5L = post_list.substring(441, 445);//第5层高度小时内最高气温

                            String TMAXTEMPAH5L = post_list.substring(445, 449);//第5层高度小时内最高气温出现时间

                            String MINTEMPH5L = post_list.substring(449, 453);//第5层高度小时内最低气温

                            String TMINTEMPAH5L = post_list.substring(453, 457);//第5层高度小时内最低气温出现时间

                            String ARHHAH5L = post_list.substring(457, 460);//第5层高度半小时平均相对湿度

                            String MINRHH5L = post_list.substring(460, 463);//第5层高度小时内最小相对湿度

                            String MINRHOTH5L = post_list.substring(463, 467);//第5层高度小时内最小相对湿度出现时间

                            String AWVPHAH5L = post_list.substring(467, 470);//第5层高度半小时平均水汽压

                            String ALPRESSUREHAH = post_list.substring(470, 475);//半小时内平均本站气压

                            String V10301 = post_list.substring(475, 480);//小时内最高本站气压

                            String V10301_052 = post_list.substring(480, 484);//小时内最高本站气压出现时间

                            String V10302 = post_list.substring(484, 489);//小时内最低本站气压

                            String V10302_052 = post_list.substring(489, 493);//小时内最低本站气压出现时间

                            String APRECIPITATIONH = post_list.substring(493, 497);//小时内累积降水量

                            String CUMEVAH = post_list.substring(497, 500);//小时内累积蒸发量

                            SurfXiliGardHourTab surfXiliGardHourTab = new SurfXiliGardHourTab();

                            surfXiliGardHourTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));

                            surfXiliGardHourTab.setdDataId(dDataId);

                            surfXiliGardHourTab.setdDatetime(dDatetime);

                            surfXiliGardHourTab.setV01301(changeType(V01301));

                            surfXiliGardHourTab.setV04001(changeType(V04001));

                            surfXiliGardHourTab.setV04002(changeType(V04002));

                            surfXiliGardHourTab.setV04003(changeType(V04003));

                            surfXiliGardHourTab.setV04004(changeType(V04004));

                            surfXiliGardHourTab.setV04005(changeType(V04005));

                            surfXiliGardHourTab.setV06001(changeType(V06001));

                            surfXiliGardHourTab.setV05001(changeType(V05001));

                            surfXiliGardHourTab.setV07001(changeType(V07001));

                            surfXiliGardHourTab.setV070010001(changeType(V07001_0001));

                            surfXiliGardHourTab.setV070010002(changeType(V07001_0002));

                            surfXiliGardHourTab.setV070010003(changeType(V07001_0003));

                            surfXiliGardHourTab.setV070010004(changeType(V07001_0004));

                            surfXiliGardHourTab.setV070010005(changeType(V07001_0005));

                            surfXiliGardHourTab.setV01300(changeType(V01300));

                            surfXiliGardHourTab.setGroundDistance(new BigDecimal(changeType(GROUND_DISTANCE)));

                            surfXiliGardHourTab.setInfraRed(new BigDecimal(changeType(INFRA_RED)));

                            surfXiliGardHourTab.setDsrsensoriden(new BigDecimal(changeType(DSRSENSORIDEN)));

                            surfXiliGardHourTab.setUsrsensoriden(new BigDecimal(changeType(USRSENSORIDEN)));

                            surfXiliGardHourTab.setDlwrsensoriden(new BigDecimal(changeType(DLWRSENSORIDEN)));

                            surfXiliGardHourTab.setUlwrsensoriden(new BigDecimal(changeType(ULWRSENSORIDEN)));

                            surfXiliGardHourTab.setParsensoriden(new BigDecimal(changeType(PARSENSORIDEN)));

                            surfXiliGardHourTab.setGtempsensoriden(new BigDecimal(changeType(GTEMPSENSORIDEN)));

                            surfXiliGardHourTab.setGtempsensoriden5(new BigDecimal(changeType(GTEMPSENSORIDEN5)));

                            surfXiliGardHourTab.setGtempsensoriden10(new BigDecimal(changeType(GTEMPSENSORIDEN10)));

                            surfXiliGardHourTab.setGtempsensoriden15(new BigDecimal(changeType(GTEMPSENSORIDEN15)));

                            surfXiliGardHourTab.setGtempsensoriden20(new BigDecimal(changeType(GTEMPSENSORIDEN20)));

                            surfXiliGardHourTab.setGtempsensoriden40(new BigDecimal(changeType(GTEMPSENSORIDEN40)));

                            surfXiliGardHourTab.setSmsensoriden10(new BigDecimal(changeType(SMSENSORIDEN10)));

                            surfXiliGardHourTab.setSmsensoriden20(new BigDecimal(changeType(SMSENSORIDEN20)));

                            surfXiliGardHourTab.setSmsensoriden50(new BigDecimal(changeType(SMSENSORIDEN50)));

                            surfXiliGardHourTab.setSmsensoriden100(new BigDecimal(changeType(SMSENSORIDEN100)));

                            surfXiliGardHourTab.setGtempsensoriden180(new BigDecimal(changeType(GTEMPSENSORIDEN180)));

                            surfXiliGardHourTab.setShflux1(changeType(SHFLUX1));

                            surfXiliGardHourTab.setShflux2(changeType(SHFLUX2));

                            surfXiliGardHourTab.setShflux3(changeType(SHFLUX3));

                            surfXiliGardHourTab.setWssensoriden1l(new BigDecimal(changeType(WSSENSORIDEN1L)));

                            surfXiliGardHourTab.setTahsensoriden1l(new BigDecimal(changeType(TAHSENSORIDEN1L)));

                            surfXiliGardHourTab.setWssensoriden2l(new BigDecimal(changeType(WSSENSORIDEN2L)));

                            surfXiliGardHourTab.setTahsensoriden2l(new BigDecimal(changeType(TAHSENSORIDEN2L)));

                            surfXiliGardHourTab.setWssensoriden10mh(new BigDecimal(changeType(WSSENSORIDEN10MH)));

                            surfXiliGardHourTab.setWdsensoriden10mh(new BigDecimal(changeType(WDSENSORIDEN10MH)));

                            surfXiliGardHourTab.setTahsensoriden10mh(new BigDecimal(changeType(TAHSENSORIDEN10MH)));

                            surfXiliGardHourTab.setWssensoriden4l(new BigDecimal(changeType(WSSENSORIDEN4L)));

                            surfXiliGardHourTab.setTahsensoriden4l(new BigDecimal(changeType(TAHSENSORIDEN4L)));

                            surfXiliGardHourTab.setWssensoriden5l(new BigDecimal(changeType(WSSENSORIDEN5L)));

                            surfXiliGardHourTab.setTahsensoriden5l(new BigDecimal(changeType(TAHSENSORIDEN5L)));

                            surfXiliGardHourTab.setAirpsensoriden(new BigDecimal(changeType(AIRPSENSORIDEN)));

                            surfXiliGardHourTab.setEvasensoriden(new BigDecimal(changeType(EVASENSORIDEN)));

                            surfXiliGardHourTab.setHourmin(HOURMIN);

                            surfXiliGardHourTab.setMidsradiationhah(changeType(MIDSRADIATIONHAH));

                            surfXiliGardHourTab.setMaxhdsradiation(changeType(MAXHDSRADIATION));

                            surfXiliGardHourTab.setTmaxowhdswradiation(changeType(TMAXOWHDSWRADIATION));

                            if (changeType(EHDSWRADIATION).equals("999999")) {
                                surfXiliGardHourTab.setEhdswradiation("999999");
                            } else {
                                surfXiliGardHourTab.setEhdswradiation(String.valueOf(Double.parseDouble(changeType(EHDSWRADIATION)) / 100));
                            }

                            surfXiliGardHourTab.setAwswradiationihah(changeType(AWSWRADIATIONIHAH));

                            surfXiliGardHourTab.setMaxihousradiation(new BigDecimal(changeType(MAXIHOUSRADIATION)));

                            surfXiliGardHourTab.setMaxtousradiationhi(new BigDecimal(changeType(MAXTOUSRADIATIONHI)));

                            surfXiliGardHourTab.setEhuswradiation(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EHUSWRADIATION)) / 100)));

                            surfXiliGardHourTab.setMidlwradiationhah(new BigDecimal(changeType(MIDLWRADIATIONHAH)));

                            surfXiliGardHourTab.setMaxihdlwradiation(new BigDecimal(changeType(MAXIHDLWRADIATION)));

                            surfXiliGardHourTab.setMaxtowhdlwradiationi(new BigDecimal(changeType(MAXTOWHDLWRADIATIONI)));

                            surfXiliGardHourTab.setEwhdlwradiation(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EWHDLWRADIATION)) / 100)));

                            surfXiliGardHourTab.setMiulwradiationhah(new BigDecimal(changeType(MIULWRADIATIONHAH)));

                            surfXiliGardHourTab.setMaxihulwradiation(new BigDecimal(changeType(MAXIHULWRADIATION)));

                            surfXiliGardHourTab.setMaxtowhulwradiationi(new BigDecimal(changeType(MAXTOWHULWRADIATIONI)));

                            surfXiliGardHourTab.setEihulwradiation(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EIHULWRADIATION)) / 100)));

                            surfXiliGardHourTab.setAnradiaihah(new BigDecimal(changeType(ANRADIAIHAH)));

                            surfXiliGardHourTab.setMaxihnradiai(new BigDecimal(changeType(MAXIHNRADIAI)));

                            surfXiliGardHourTab.setMaxtowhnradiai(new BigDecimal(changeType(MAXTOWHNRADIAI)));

                            surfXiliGardHourTab.setMinhnradiai(new BigDecimal(changeType(MINHNRADIAI)));

                            surfXiliGardHourTab.setMintowhnradiai(new BigDecimal(changeType(MINTOWHNRADIAI)));

                            surfXiliGardHourTab.setEwhnradiation(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EWHNRADIATION)) / 100)));

                            surfXiliGardHourTab.setAiparadiahah(new BigDecimal(changeType(AIPARADIAHAH)));

                            surfXiliGardHourTab.setV143400560(new BigDecimal(changeType(V14340_05_60)));

                            surfXiliGardHourTab.setV1434005052(Integer.parseInt(changeType(V14340_05_052)));

                            surfXiliGardHourTab.setEparadiationih(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EPARADIATIONIH)) / 100)));

                            surfXiliGardHourTab.setAgtemphah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AGTEMPHAH)) / 10)));

                            surfXiliGardHourTab.setMaxgtemph(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXGTEMPH)) / 10)));

                            surfXiliGardHourTab.setMaxotgh(Integer.parseInt(changeType(MAXOTGH)));

                            surfXiliGardHourTab.setMingtemph(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MINGTEMPH)) / 10)));

                            surfXiliGardHourTab.setMinotgh(Integer.parseInt(changeType(MINOTGH)));

                            if ("999999".equals(changeType(AGTEMP5CMHAH))) {
                                surfXiliGardHourTab.setAgtemp5cmhah(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAgtemp5cmhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AGTEMP5CMHAH)) / 10)));

                            }

                            if ("999999".equals(changeType(AGTEMP10CMHAH))) {
                                surfXiliGardHourTab.setAgtemp10cmhah(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAgtemp10cmhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AGTEMP10CMHAH)) / 10)));

                            }

                            if ("999999".equals(changeType(AGTEMP15CMHAH))) {
                                surfXiliGardHourTab.setAgtemp15cmhah(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAgtemp15cmhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AGTEMP15CMHAH)) / 10)));

                            }

                            if ("999999".equals(changeType(AGTEMP20CMHAH))) {
                                surfXiliGardHourTab.setAgtemp20cmhah(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAgtemp20cmhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AGTEMP20CMHAH)) / 10)));

                            }

                            if ("999999".equals(changeType(AGTEMP40CMHAH))) {
                                surfXiliGardHourTab.setAgtemp40cmhah(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAgtemp40cmhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AGTEMP40CMHAH)) / 10)));

                            }

                            surfXiliGardHourTab.setA10cmsvwchah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(A10CMSVWCHAH))/10)));

                            surfXiliGardHourTab.setA20cmsvwchah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(A20CMSVWCHAH))/10)));

                            surfXiliGardHourTab.setA50cmsvwchah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(A50CMSVWCHAH))/10)));
                            surfXiliGardHourTab.setA100cmsvwchah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(A100CMSVWCHAH))/10)));

                            surfXiliGardHourTab.setA180cmsvwchah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(A180CMSVWCHAH))/10)));

                            surfXiliGardHourTab.setAshfluxhah(Integer.parseInt(changeType(ASHFLUXHAH)));

                            surfXiliGardHourTab.setAwspeedhah1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEEDHAH1L))/10)));

                            surfXiliGardHourTab.setExwspeedh1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EXWSPEEDH1L))/10)));

                            surfXiliGardHourTab.setMaxwspeedtimeh1l(Integer.parseInt(changeType(MAXWSPEEDTIMEH1L)));

                            surfXiliGardHourTab.setMaxwspeedh1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEEDH1L))/10)));

                            surfXiliGardHourTab.setTimemaxwvh1l(Integer.parseInt(changeType(TIMEMAXWVH1L)));

                            surfXiliGardHourTab.setAwspeedhah2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEEDHAH2L))/10)));

                            surfXiliGardHourTab.setExwspeedh2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EXWSPEEDH2L))/10)));

                            surfXiliGardHourTab.setMaxwspeedtimeh2l(Integer.parseInt(changeType(MAXWSPEEDTIMEH2L)));

                            surfXiliGardHourTab.setMaxwspeedh2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEEDH2L))/10)));

                            surfXiliGardHourTab.setTimemaxwvh2l(Integer.parseInt(changeType(TIMEMAXWVH2L)));

                            surfXiliGardHourTab.setAwspeedhah10m(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEEDHAH10M))/10)));

                            surfXiliGardHourTab.setAvshwv10mhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AVSHWV10MHAH))/10)));

                            surfXiliGardHourTab.setAvchwdir10mhah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AVCHWDIR10MHAH))/10)));

                            surfXiliGardHourTab.setSdwdirection(changeType(SDWDIRECTION));

                            surfXiliGardHourTab.setExwspeedh10m(String.valueOf(Double.parseDouble(changeType(EXWSPEEDH10M))/10));

                            surfXiliGardHourTab.setMaxwdirwspeed10dh(changeType(MAXWDIRWSPEED10DH));

                            surfXiliGardHourTab.setMaxwspeedt10dh(Integer.parseInt(changeType(MAXWSPEEDT10DH)));

                            surfXiliGardHourTab.setMaxwspeedh10m(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEEDH10M))/10)));

                            surfXiliGardHourTab.setMaxwdirwspeed10mh(Integer.parseInt(changeType(MAXWDIRWSPEED10MH)));

                            surfXiliGardHourTab.setMaxwspeedt10mh(Integer.parseInt(changeType(MAXWSPEEDT10MH)));

                            if ("999999".equals(changeType(AWSPEEDHAH4L))) {
                                surfXiliGardHourTab.setAwspeedhah4l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAwspeedhah4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEEDHAH4L)) / 10)));

                            }

                            surfXiliGardHourTab.setExwspeedh4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EXWSPEEDH4L))/10)));

                            surfXiliGardHourTab.setMaxwspeedtimeh4l(Integer.parseInt(changeType(MAXWSPEEDTIMEH4L)));

                            surfXiliGardHourTab.setMaxwspeedh4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEEDH4L))/10)));

                            surfXiliGardHourTab.setTimemaxwvh4l(Integer.parseInt(changeType(TIMEMAXWVH4L)));

                            surfXiliGardHourTab.setAwspeedhah5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWSPEEDHAH5L))/10)));

                            surfXiliGardHourTab.setExwspeedh5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(EXWSPEEDH5L))/10)));

                            surfXiliGardHourTab.setMaxwspeedtimeh5l(Integer.parseInt(changeType(MAXWSPEEDTIMEH5L)));

                            surfXiliGardHourTab.setMaxwspeedh5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXWSPEEDH5L))/10)));

                            surfXiliGardHourTab.setTimemaxwvh5l(Integer.parseInt(changeType(TIMEMAXWVH5L)));

                            if ("999999".equals(changeType(ATEMPHAH1L))) {
                                surfXiliGardHourTab.setAtemphah1l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAtemphah1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMPHAH1L)) / 10)));

                            }

                            if ("999999".equals(changeType(MAXTEMPH1L))) {
                                surfXiliGardHourTab.setMaxtemph1l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMaxtemph1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXTEMPH1L)) / 10)));

                            }

                            surfXiliGardHourTab.setTmaxtempah1l(Integer.parseInt(changeType(TMAXTEMPAH1L)));

                            if ("999999".equals(changeType(MINTEMPH1L))) {
                                surfXiliGardHourTab.setMintemph1l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMintemph1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MINTEMPH1L)) / 10)));
                            }

                            surfXiliGardHourTab.setTmintempah1l(Integer.parseInt(changeType(TMINTEMPAH1L)));

                            surfXiliGardHourTab.setArhhah1l(Integer.parseInt(changeType(ARHHAH1L)));

                            surfXiliGardHourTab.setMinrhh1l(Integer.parseInt(changeType(MINRHH1L)));

                            surfXiliGardHourTab.setMinrhoth1l(Integer.parseInt(changeType(MINRHOTH1L)));
                            try{
                                if(changeType(AWVPHAH1L).equals("999999")){
                                    surfXiliGardHourTab.setAwvphah1l(new BigDecimal(999999));
                                }else {
                                    surfXiliGardHourTab.setAwvphah1l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPHAH1L))/10)));
                                }

                            }catch (Exception e){
                                surfXiliGardHourTab.setAwvphah1l(new BigDecimal(999999));
                            }

                            if ("999999".equals(changeType(ATEMPHAH2L))) {
                                surfXiliGardHourTab.setAtemphah2l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAtemphah2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMPHAH2L)) / 10)));

                            }

                            if ("999999".equals(changeType(MAXTEMPH2L))) {
                                surfXiliGardHourTab.setMaxtemph2l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMaxtemph2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXTEMPH2L)) / 10)));

                            }

                            surfXiliGardHourTab.setTmaxtempah2l(Integer.parseInt(changeType(TMAXTEMPAH2L)));

                            if ("999999".equals(changeType(MINTEMPH2L))) {
                                surfXiliGardHourTab.setMintemph2l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMintemph2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MINTEMPH2L)) / 10)));

                            }


                            surfXiliGardHourTab.setTmintempah2l(Integer.parseInt(changeType(TMINTEMPAH2L)));

                            surfXiliGardHourTab.setArhhah2l(Integer.parseInt(changeType(ARHHAH2L)));

                            surfXiliGardHourTab.setMinrhh2l(Integer.parseInt(changeType(MINRHH2L)));

                            surfXiliGardHourTab.setMinrhoth2l(Integer.parseInt(changeType(MINRHOTH2L)));
                            try{
                                if(changeType(AWVPHAH2L).equals("999999")){
                                    surfXiliGardHourTab.setAwvphah2l(new BigDecimal(999999));
                                }else{
                                    surfXiliGardHourTab.setAwvphah2l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPHAH2L))/10)));
                                }

                            }catch (Exception e){
                                surfXiliGardHourTab.setAwvphah2l(new BigDecimal(999999));
                            }

                            if(changeType(ATEMPHAH10L).equals("999999")){
                                surfXiliGardHourTab.setAtemphah10l(new BigDecimal(999999));
                            }else{
                                surfXiliGardHourTab.setAtemphah10l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMPHAH10L))/10)));
                            }
                            if("999999".equals(changeType(MAXTEMPH10L))){
                                surfXiliGardHourTab.setMaxtemph10l(new BigDecimal(999999));
                            }else{
                                surfXiliGardHourTab.setMaxtemph10l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXTEMPH10L))/10)));
                            }

                            surfXiliGardHourTab.setTmaxtempah10l(Integer.parseInt(changeType(TMAXTEMPAH10L)));

                            if("999999".equals(changeType(MINTEMPH10L))){
                                surfXiliGardHourTab.setMintemph10l(new BigDecimal(999999));
                            }else{
                                surfXiliGardHourTab.setMintemph10l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MINTEMPH10L))/10)));
                            }

                            try{
                                surfXiliGardHourTab.setTmintempah10l(Integer.parseInt(changeType(TMINTEMPAH10L)));
                            }catch (Exception e){
                                surfXiliGardHourTab.setTmintempah10l(999999);
                            }
                            try{
                                surfXiliGardHourTab.setArhhah10l(Integer.parseInt(changeType(ARHHAH10L)));
                            }catch (Exception e){
                                surfXiliGardHourTab.setArhhah10l(999999);
                            }

                            try{
                                surfXiliGardHourTab.setMinrhh10l(Integer.parseInt(changeType(MINRHH10L)));
                            }catch (Exception e){
                                surfXiliGardHourTab.setMinrhh10l(999999);
                            }
                            try{
                                surfXiliGardHourTab.setMinrhoth10l(Integer.parseInt(changeType(MINRHOTH10L)));
                            }catch (Exception e){
                                surfXiliGardHourTab.setMinrhoth10l(999999);
                            }

                            try{
                                if(changeType(AWVPHAH10L).equals("999999")){
                                    surfXiliGardHourTab.setAwvphah10l(new BigDecimal(999999));
                                }else {
                                    surfXiliGardHourTab.setAwvphah10l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPHAH10L))/10)));
                                }

                            }catch (Exception e){
                                surfXiliGardHourTab.setAwvphah10l(new BigDecimal(999999));
                            }

                            if("999999".equals(changeType(ATEMPHAH4L))){
                                surfXiliGardHourTab.setAtemphah4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardHourTab.setAtemphah4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMPHAH4L))/10)));
                            }

                            if ("999999".equals(changeType(MAXTEMPH4L))) {
                                surfXiliGardHourTab.setMaxtemph4l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMaxtemph4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXTEMPH4L)) / 10)));

                            }

                            surfXiliGardHourTab.setTmaxtempah4l(Integer.parseInt(changeType(TMAXTEMPAH4L)));

                            if ("999999".equals(changeType(MINTEMPH4L))) {
                                surfXiliGardHourTab.setMintemph4l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMintemph4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MINTEMPH4L)) / 10)));

                            }

                            surfXiliGardHourTab.setTmintempah4l(Integer.parseInt(changeType(TMINTEMPAH4L)));

                            surfXiliGardHourTab.setArhhah4l(Integer.parseInt(changeType(ARHHAH4L)));

                            surfXiliGardHourTab.setMinrhh4l(Integer.parseInt(changeType(MINRHH4L)));

                            surfXiliGardHourTab.setMinrhoth4l(Integer.parseInt(changeType(MINRHOTH4L)));
                            if(changeType(AWVPHAH4L).equals("999999")){
                                surfXiliGardHourTab.setAwvphah4l(new BigDecimal(999999));
                            }else {
                                surfXiliGardHourTab.setAwvphah4l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPHAH4L))/10)));
                            }

                            if ("999999".equals(changeType(ATEMPHAH5L))) {
                                surfXiliGardHourTab.setAtemphah5l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setAtemphah5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ATEMPHAH5L)) / 10)));

                            }

                            if ("999999".equals(changeType(MAXTEMPH5L))) {
                                surfXiliGardHourTab.setMaxtemph5l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMaxtemph5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MAXTEMPH5L)) / 10)));
                            }

                            surfXiliGardHourTab.setTmaxtempah5l(Integer.parseInt(changeType(TMAXTEMPAH5L)));

                            if ("999999".equals(changeType(MINTEMPH5L))) {
                                surfXiliGardHourTab.setMintemph5l(new BigDecimal("999999"));
                            } else {
                                surfXiliGardHourTab.setMintemph5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(MINTEMPH5L)) / 10)));
                            }

                            surfXiliGardHourTab.setTmintempah5l(Integer.parseInt(changeType(TMINTEMPAH5L)));

                            surfXiliGardHourTab.setArhhah5l(Integer.parseInt(changeType(ARHHAH5L)));

                            surfXiliGardHourTab.setMinrhh5l(Integer.parseInt(changeType(MINRHH5L)));

                            surfXiliGardHourTab.setMinrhoth5l(Integer.parseInt(changeType(MINRHOTH5L)));
                            try{
                                if(changeType(AWVPHAH5L).equals("999999")){
                                    surfXiliGardHourTab.setAwvphah5l(new BigDecimal(999999));
                                }else {
                                    surfXiliGardHourTab.setAwvphah5l(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AWVPHAH5L))/10)));
                                }
                            }catch (Exception e){
                                surfXiliGardHourTab.setAwvphah5l(new BigDecimal(999999));
                            }

                            surfXiliGardHourTab.setAlpressurehah(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ALPRESSUREHAH))/10)));

                            surfXiliGardHourTab.setV10301(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V10301))/10)));

                            surfXiliGardHourTab.setV10301052(Integer.parseInt(changeType(V10301_052)));

                            surfXiliGardHourTab.setV10302(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V10302))/10)));

                            surfXiliGardHourTab.setV10302052(Integer.parseInt(changeType(V10302_052)));
                            if("////".equals(APRECIPITATIONH)){
                                surfXiliGardHourTab.setAprecipitationh(999999);
                            }else {
                                surfXiliGardHourTab.setAprecipitationh(Integer.parseInt(changeType(APRECIPITATIONH)));
                            }

                            if("///".equals(CUMEVAH)){
                                surfXiliGardHourTab.setCumevah(999999);
                            }else {
                                surfXiliGardHourTab.setCumevah(Integer.parseInt(changeType(CUMEVAH)));
                            }


                            surfXiliGardHourTabList.add(surfXiliGardHourTab);
                        }
                    }
                    if(!surfXiliGardMinTabList.isEmpty()){
                        resultMap.put("surfXiliGardMinTabList",surfXiliGardMinTabList);
                    }
                    if(!surfXiliGardHourTabList.isEmpty()){
                        resultMap.put("surfXiliGardHourTabList",surfXiliGardHourTabList);
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
        resultMap.put("decodingInfo",decodingInfo);
        return resultMap;
    }
    public static String changeType(String dates) {
        if(dates==null || dates=="" || dates.equals("///")  || dates.indexOf("/") > -1 || dates.equals("////") || dates.equals("/////") || dates.equals("//////") || dates.equals("////////") || dates.equals("----") || dates.equals("---") || dates.equals("--") ||  dates.equals("-----") || dates.equals("------")){
            dates="999999";
        }
        dates = dates.replaceAll(" ","");
        return dates;
    }
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public static void main(String[] args) {
        File file=new File("C:\\Users\\Administrator\\Desktop\\修改后入库数据\\近地面通量-梯度\\分钟\\PBL_VG_M_54102_20090301.TXT");
        FXiliGardTab fXiliGardTab=new FXiliGardTab();
        fXiliGardTab.decode(file,"PBL_VG_M");
    }
}
