package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.*;
import cma.cimiss2.dpc.decoder.fileDecode.util.DateUtils;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/18 0018 9:15
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class XiliWindenerTab {
    /**
     * 结果集
     */
    private ParseResult<SurfXiliWindenerTab> parseResult = new ParseResult<SurfXiliWindenerTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public Map<String, Object> decode(File file, String pathname) {
        Map<String, Object> resultMap = new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false);
        if (file != null && file.exists() && file.isFile()) {
            if (file.length() <= 0) {
                decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                resultMap.put("decodingInfo", decodingInfo);
                return resultMap;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
                List<String> txtFileContent = null;
                txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                List<SurfXiliWindenerHfsTab> surfXiliWindenerHfsTabList = new ArrayList<>();
                List<SurfXiliWindenerMdeTab> surfXiliWindenerMdeTabList = new ArrayList<>();
                List<SurfXiliWindenerMdoTab> surfXiliWindenerMdoTabList = new ArrayList<>();
                List<SurfXiliWindenerTimTab> surfXiliWindenerTimTabList = new ArrayList<>();
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    if (pathname.contains("PBL_WE_USO")) {
                        String messages = txtFileContent.get(0).split("\\s+")[0];
                        String v01301 = messages.substring(0, 5);
                        String v04001 = messages.substring(5, 9);
                        String v04002 = messages.substring(9, 11);
                        String v04003 = messages.substring(11, 13);
                        String v04004 = messages.substring(13, 15);
                        String v06001 = messages.substring(15, 22);
                        String v05001 = messages.substring(22, 28);
                        String v07001 = messages.substring(28, 35);
                        String theins50 = messages.substring(35, 38);
                        String theins70 = messages.substring(38, 41);
                        String theins100 = messages.substring(41, 44);
                        String v02320 = txtFileContent.get(0).split("\\s+")[1].split("-")[0];
                        for (int i = 1; i < txtFileContent.size(); i++) {
                            String post_list = txtFileContent.get(i);
                            String dDataId = "A.0006.0003.S0001";
                            String dDatetime = v04001 + "-" + v04002 + "-" + v04003 + " " + post_list.substring(0, 10);
                            String horiwindspeed50x = post_list.substring(10, 19);
                            if ("---------".equals(horiwindspeed50x)) {
                                continue;
                            }
                            String horiwindspeed50y = post_list.substring(19, 28);
                            String horiwindspeed50z = post_list.substring(28, 37);
                            String ultvirtem50 = post_list.substring(37, 45);
                            String diavalue50 = post_list.substring(45, 46);
                            String horiwindspeed70x = post_list.substring(46, 55);
                            String horiwindspeed70y = post_list.substring(55, 64);
                            String horiwindspeed70z = post_list.substring(64, 73);
                            String ultvirtem70 = post_list.substring(73, 81);
                            String diavalue70 = post_list.substring(81, 82);
                            String horiwindspeed100x = post_list.substring(82, 91);
                            String horiwindspeed100y = post_list.substring(91, 100);
                            String horiwindspeed100z = post_list.substring(100, 109);
                            String ultvirtem100 = post_list.substring(109, 117);
                            String diavalue100 = post_list.substring(117, 118);
                            SurfXiliWindenerHfsTab surfXiliWindenerHfsTab = new SurfXiliWindenerHfsTab();

                            surfXiliWindenerHfsTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));//记录标识

                            surfXiliWindenerHfsTab.setV01301(v01301);//区站号

                            surfXiliWindenerHfsTab.setdDataId(dDataId);

                            surfXiliWindenerHfsTab.setdDatetime(dDatetime);

                            surfXiliWindenerHfsTab.setV04001(Short.parseShort(v04001));//年

                            surfXiliWindenerHfsTab.setV04002(Short.parseShort(v04002));//月

                            surfXiliWindenerHfsTab.setV04003(Short.parseShort(v04003));//日

                            surfXiliWindenerHfsTab.setV06001(new BigDecimal(v06001));//铁塔所在位置经度

                            surfXiliWindenerHfsTab.setV05001(new BigDecimal(v05001));//铁塔所在位置纬度

                            surfXiliWindenerHfsTab.setV07001(new BigDecimal(v07001));//梯度塔所处地（湖、海）面拔海高度

                            surfXiliWindenerHfsTab.setTheins50(Short.parseShort(theins50));//50米超声风温仪安装角度

                            surfXiliWindenerHfsTab.setTheins70(Short.parseShort(theins70));//70米超声风温仪安装角度

                            surfXiliWindenerHfsTab.setTheins100(Short.parseShort(theins100));//100米超声风温仪安装角度

                            surfXiliWindenerHfsTab.setV02320(v02320);//采集器型号

                            surfXiliWindenerHfsTab.setHoriwindspeed50x(new BigDecimal(horiwindspeed50x));//50米高度水平风速（x轴）

                            surfXiliWindenerHfsTab.setHoriwindspeed50y(new BigDecimal(changeType(horiwindspeed50y)));//50米高度水平风速（y轴）

                            surfXiliWindenerHfsTab.setHoriwindspeed50z(new BigDecimal(changeType(horiwindspeed50z)));//50米高度垂向风速（z轴）

                            surfXiliWindenerHfsTab.setUltvirtem50(new BigDecimal(changeType(ultvirtem50)));//50米高度超声虚温

                            surfXiliWindenerHfsTab.setDiavalue50(new BigDecimal(diavalue50));//50米高度传感器诊断值

                            surfXiliWindenerHfsTab.setHoriwindspeed70x(horiwindspeed70x);//70米高度水平风速（x轴）

                            surfXiliWindenerHfsTab.setHoriwindspeed70y(horiwindspeed70y);//70米高度水平风速（y轴）

                            surfXiliWindenerHfsTab.setHoriwindspeed70z(new BigDecimal(changeType(horiwindspeed70z)));//70米高度垂向风速（z轴）

                            surfXiliWindenerHfsTab.setUltvirtem70(new BigDecimal(changeType(ultvirtem70)));//70米高度超声虚温

                            surfXiliWindenerHfsTab.setDiavalue70(new BigDecimal(changeType(diavalue70)));//70米高度传感器诊断值

                            surfXiliWindenerHfsTab.setHoriwindspeed100x(new BigDecimal(changeType(horiwindspeed100x)));//100米高度水平风速（x轴）

                            surfXiliWindenerHfsTab.setHoriwindspeed100y(new BigDecimal(changeType(horiwindspeed100y)));//100米高度水平风速（y轴）

                            surfXiliWindenerHfsTab.setHoriwindspeed100z(new BigDecimal(changeType(horiwindspeed100z)));//100米高度垂向风速（z轴）

                            surfXiliWindenerHfsTab.setUltvirtem100(new BigDecimal(ultvirtem100));//100米高度超声虚温

                            surfXiliWindenerHfsTab.setDiavalue100(new BigDecimal(diavalue100));//100米高度传感器诊断值

                            surfXiliWindenerHfsTabList.add(surfXiliWindenerHfsTab);
                        }
                    } else if (pathname.contains("PBL_WE_USM")) {
                        String messages = txtFileContent.get(0).split("\\s+")[0];
                        String v01301 = messages.substring(0, 5);
                        String v04001 = messages.substring(5, 9);
                        String v04002 = messages.substring(9, 11);
                        String v04003 = messages.substring(11, 13);
                        String v06001 = messages.substring(13, 20);
                        String v05001 = messages.substring(20, 26);
                        String v07001 = messages.substring(26, 33);
                        String THEINS50 = messages.substring(33, 36);
                        String THEINS70 = messages.substring(36, 39);
                        String THEINS100 = messages.substring(39,42);
                        for (int i = 1; i < txtFileContent.size(); i++) {
                            String post_list = txtFileContent.get(i);
                            String dDataId = "A.0006.0004.S0001";
                            String avehowinspe10mihywi50x = changeType(post_list.substring(5, 10));
                            String avehowinspe10mihywi50y = changeType(post_list.substring(10, 15));
                            String avehowinspe10mihywi50z = changeType(post_list.substring(15, 20));
                            String avehosywispe10mihywi50 = changeType(post_list.substring(20, 23));
                            String miavehovesywispehywi50 = changeType(post_list.substring(23, 26));
                            String miavehovewidirsyhywi50 = changeType(post_list.substring(26, 29));
                            String stade10minmehosywidirhy50 = post_list.substring(29, 36);
                            String vahowispeuxhigsuwi50 = post_list.substring(36, 44);
                            String vahowispeuyhigsuwi50 = post_list.substring(44, 52);
                            String vahowispeuzhigsuwi50 = post_list.substring(52, 60);
                            String ahwsp10mhwind70x = changeType(post_list.substring(60, 65));
                            String ahwsp10mhwind70y = changeType(post_list.substring(65, 70));
                            String ahwsp10mhwind70z = changeType(post_list.substring(70, 75));
                            String avehosywispe10mihywi70 = changeType(post_list.substring(75, 78));
                            String miavehovewidirsyhywi70 = changeType(post_list.substring(78, 81));
                            String miavehovewidirsyhywi71 = changeType(post_list.substring(81, 84));
                            String stade10minmehosywidirhy70 = changeType(post_list.substring(84, 91));
                            String vahowispeuxhigsuwi70 = changeType(post_list.substring(91, 99));
                            String vahowispeuyhigsuwi70 = changeType(post_list.substring(99, 107));
                            String vahowispeuzhigsuwi70 = changeType(post_list.substring(107, 115));
                            String ahwsp10mhwind100x = changeType(post_list.substring(115, 120));
                            String ahwsp10mhwind100y = changeType(post_list.substring(120, 125));
                            String ahwsp10mhwind100z = changeType(post_list.substring(125, 130));
                            String avehosywispe10mihywi100 = changeType(post_list.substring(130, 133));
                            String miavehovewidirsyhywi100 = changeType(post_list.substring(133, 136));
                            String miavehovewidirsyhywi101 = changeType(post_list.substring(136, 139));
                            String stade10minmehosywidirhy100 = changeType(post_list.substring(139, 146));
                            String vahowispeuxhigsuwi100 = post_list.substring(146, 154);
                            String vahowispeuyhigsuwi100 = post_list.substring(154, 162);
                            String vahowispeuzhigsuwi100 = post_list.substring(162, 170);
                            SurfXiliWindenerMdoTab surfXiliWindenerMdoTab = new SurfXiliWindenerMdoTab();

                            surfXiliWindenerMdoTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));//记录标识

                            surfXiliWindenerMdoTab.setdDataId(dDataId);

                            surfXiliWindenerMdoTab.setV01301(changeType(v01301));

                            String houmin=post_list.substring(0, 5);

                            surfXiliWindenerMdoTab.setHourmin(changeType(houmin));

                            String[] m=houmin.split(":");
                            String v04004=m[0];
                            String v04005=m[1];
                            String dDatetime =null;
                            if(i<24){
                                //日-1
                                int newv04003=Integer.parseInt(v04003)-1;
                                int newV04002=Integer.parseInt(v04002);
                                int newV04001=Integer.parseInt(v04001);
                                if(newv04003<1){
                                    newV04002=newV04002-1;
                                    int maxMonth=DateUtils.getDayCountOnMonth(newV04002,newV04001);
                                    newv04003=maxMonth;
                                    if(newV04002<1){
                                        newV04001=newV04001-1;
                                        newV04002=12;
                                        newv04003=31;
                                    }

                                }
                                dDatetime = newV04001 + "-" + newV04002 + "-" + newv04003 + " " + v04004+":"+v04005;
                                surfXiliWindenerMdoTab.setV04001(Short.parseShort(newV04001+""));
                                surfXiliWindenerMdoTab.setV04002(Short.parseShort(newV04002+""));
                                surfXiliWindenerMdoTab.setV04003(Short.parseShort(newv04003+""));
                            }else{
                                dDatetime=v04001+"-"+v04002+"-"+v04003+" "+v04004+":"+v04005;
                                surfXiliWindenerMdoTab.setV04001(Short.parseShort(changeType(v04001)));
                                surfXiliWindenerMdoTab.setV04002(Short.parseShort(changeType(v04002)));
                                surfXiliWindenerMdoTab.setV04003(Short.parseShort(changeType(v04003)));
                            }

                            surfXiliWindenerMdoTab.setV04004(Short.parseShort(changeType(v04004)));

                            surfXiliWindenerMdoTab.setV04005(Short.parseShort(changeType(v04005)));

                            surfXiliWindenerMdoTab.setdDatetime(dDatetime);

                            surfXiliWindenerMdoTab.setV06001(new BigDecimal(changeType(v06001)));

                            surfXiliWindenerMdoTab.setV05001(new BigDecimal(changeType(v05001)));

                            surfXiliWindenerMdoTab.setV07001(new BigDecimal(changeType(v07001)));

                            surfXiliWindenerMdoTab.setTheins50(Short.parseShort(THEINS50));

                            surfXiliWindenerMdoTab.setTheins70(Short.parseShort(THEINS70));

                            surfXiliWindenerMdoTab.setTheins100(Short.parseShort(THEINS100));

//                            surfXiliWindenerMdoTab.setAwssensoriden50(new BigDecimal("999999"));
//
//                            surfXiliWindenerMdoTab.setAwdsensoriden50(new BigDecimal("999999"));
//
//                            surfXiliWindenerMdoTab.setAwssensoriden70(new BigDecimal("999999"));
//
//                            surfXiliWindenerMdoTab.setAwdsensoriden70(new BigDecimal("999999"));
//
//                            surfXiliWindenerMdoTab.setAwssensoriden100(new BigDecimal("999999"));
//
//                            surfXiliWindenerMdoTab.setAwdsensoriden100(new BigDecimal("999999"));

                            if(changeType(avehowinspe10mihywi50x).equals("999999")){
                                surfXiliWindenerMdoTab.setAvehowinspe10mihywi50x(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAvehowinspe10mihywi50x(new BigDecimal(String.valueOf(Double.parseDouble(changeType(avehowinspe10mihywi50x))/10)));
                            }
                            if(changeType(avehowinspe10mihywi50y).equals("999999")){
                                surfXiliWindenerMdoTab.setAvehowinspe10mihywi50y(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAvehowinspe10mihywi50y(new BigDecimal(String.valueOf(Double.parseDouble(changeType(avehowinspe10mihywi50y))/10)));
                            }
                            if(changeType(avehowinspe10mihywi50z).equals("999999")){
                                surfXiliWindenerMdoTab.setAvehowinspe10mihywi50z(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAvehowinspe10mihywi50z(new BigDecimal(String.valueOf(Double.parseDouble(changeType(avehowinspe10mihywi50z))/10)));
                            }
                            if(changeType(avehosywispe10mihywi50).equals("999999")){
                                surfXiliWindenerMdoTab.setAvehosywispe10mihywi50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAvehosywispe10mihywi50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(avehosywispe10mihywi50))/10)));
                            }
                            if(changeType(miavehovesywispehywi50).equals("999999")){
                                surfXiliWindenerMdoTab.setMiavehovesywispehywi50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setMiavehovesywispehywi50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(miavehovesywispehywi50))/10)));
                            }


                            surfXiliWindenerMdoTab.setMiavehovewidirsyhywi50(new BigDecimal(changeType(miavehovewidirsyhywi50)));

                            surfXiliWindenerMdoTab.setStade10minmehosywidirhy50(new BigDecimal(changeType(stade10minmehosywidirhy50)));

                            surfXiliWindenerMdoTab.setVahowispeuxhigsuwi50(new BigDecimal(changeType(vahowispeuxhigsuwi50)));

                            surfXiliWindenerMdoTab.setVahowispeuyhigsuwi50(new BigDecimal(changeType(vahowispeuyhigsuwi50)));

                            surfXiliWindenerMdoTab.setVahowispeuzhigsuwi50(new BigDecimal(changeType(vahowispeuzhigsuwi50)));

                            if(changeType(ahwsp10mhwind70x).equals("999999")){
                                surfXiliWindenerMdoTab.setAhwsp10mhwind70x(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAhwsp10mhwind70x(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahwsp10mhwind70x))/10)));
                            }
                            if(changeType(ahwsp10mhwind70y).equals("999999")){
                                surfXiliWindenerMdoTab.setAhwsp10mhwind70y(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAhwsp10mhwind70y(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahwsp10mhwind70y))/10)));
                            }
                            if(changeType(ahwsp10mhwind70z).equals("999999")){
                                surfXiliWindenerMdoTab.setAhwsp10mhwind70z(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAhwsp10mhwind70z(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahwsp10mhwind70z))/10)));
                            }
                            if(changeType(avehosywispe10mihywi70).equals("999999")){
                                surfXiliWindenerMdoTab.setAvehosywispe10mihywi70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAvehosywispe10mihywi70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(avehosywispe10mihywi70))/10)));
                            }

                            if(changeType(miavehovewidirsyhywi70).equals("999999")){
                                surfXiliWindenerMdoTab.setMiavehovewidirsyhywi70s(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setMiavehovewidirsyhywi70s(new BigDecimal(String.valueOf(Double.parseDouble(changeType(miavehovewidirsyhywi70))/10)));
                            }

                            surfXiliWindenerMdoTab.setMiavehovewidirsyhywi70d(new BigDecimal(changeType(miavehovewidirsyhywi71)));

                            surfXiliWindenerMdoTab.setStade10minmehosywidirhy70(new BigDecimal(changeType(stade10minmehosywidirhy70)));

                            surfXiliWindenerMdoTab.setVahowispeuxhigsuwi70(new BigDecimal(changeType(vahowispeuxhigsuwi70)));

                            surfXiliWindenerMdoTab.setVahowispeuyhigsuwi70(new BigDecimal(changeType(vahowispeuyhigsuwi70)));
                            try {
                                surfXiliWindenerMdoTab.setVahowispeuzhigsuwi70(new BigDecimal(changeType(vahowispeuzhigsuwi70)));
                            } catch (Exception e) {
                                surfXiliWindenerMdoTab.setVahowispeuzhigsuwi70(new BigDecimal("999999"));

                            }
                            if(changeType(ahwsp10mhwind100x).equals("999999")){
                                surfXiliWindenerMdoTab.setAhwsp10mhwind100x(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAhwsp10mhwind100x(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahwsp10mhwind100x))/10)));
                            }
                            if(changeType(ahwsp10mhwind100y).equals("999999")){
                                surfXiliWindenerMdoTab.setAhwsp10mhwind100y(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAhwsp10mhwind100y(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahwsp10mhwind100y))/10)));
                            }
                            if(changeType(ahwsp10mhwind100z).equals("999999")){
                                surfXiliWindenerMdoTab.setAhwsp10mhwind100z(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAhwsp10mhwind100z(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahwsp10mhwind100z))/10)));
                            }
                            if(changeType(avehosywispe10mihywi100).equals("999999")){
                                surfXiliWindenerMdoTab.setAvehosywispe10mihywi100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setAvehosywispe10mihywi100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(avehosywispe10mihywi100))/10)));
                            }
                            if(changeType(miavehovewidirsyhywi100).equals("999999")){
                                surfXiliWindenerMdoTab.setMiavehovewidirsyhywi100s(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdoTab.setMiavehovewidirsyhywi100s(new BigDecimal(String.valueOf(Double.parseDouble(changeType(miavehovewidirsyhywi100))/10)));
                            }

                            surfXiliWindenerMdoTab.setMiavehovewidirsyhywi100d(new BigDecimal(changeType(miavehovewidirsyhywi101)));

                            surfXiliWindenerMdoTab.setStade10minmehosywidirhy100(new BigDecimal(changeType(stade10minmehosywidirhy100)));
                            try {
                                surfXiliWindenerMdoTab.setVahowispeuxhigsuwi100(new BigDecimal(changeType(vahowispeuxhigsuwi100)));
                            } catch (Exception e) {
                                surfXiliWindenerMdoTab.setVahowispeuxhigsuwi100(new BigDecimal("999999"));
                            }
                            try {
                                surfXiliWindenerMdoTab.setVahowispeuyhigsuwi100(new BigDecimal(changeType(vahowispeuyhigsuwi100)));
                            } catch (Exception e) {
                                surfXiliWindenerMdoTab.setVahowispeuyhigsuwi100(new BigDecimal("999999"));
                            }
                            try {
                                surfXiliWindenerMdoTab.setVahowispeuzhigsuwi100(new BigDecimal(changeType(vahowispeuzhigsuwi100)));
                            } catch (Exception e) {
                                surfXiliWindenerMdoTab.setVahowispeuzhigsuwi100(new BigDecimal("999999"));
                            }
                            surfXiliWindenerMdoTabList.add(surfXiliWindenerMdoTab);
                        }
                    } else if (pathname.contains("PBL_WE_M")) {
                        String messages = txtFileContent.get(0).split("-")[0];
                        String v01301 = messages.substring(0, 5);
                        String v04001 = messages.substring(5, 9);
                        String v04002 = messages.substring(9, 11);
                        String v04003 = messages.substring(11, 13);
                        String v06001 = messages.substring(13, 20);
                        String v05001 = messages.substring(20, 26);
                        String v07001 = messages.substring(26, 33);
                        String awssensoriden50 = messages.substring(33, 34);
                        String awdsensoriden50 = messages.substring(34, 35);
                        String awssensoriden70 = messages.substring(35, 36);
                        String awdsensoriden70 = messages.substring(36, 37);
                        String awssensoriden100 = messages.substring(37, 38);
                        String awdsensoriden100 = messages.substring(38, 39);
                        for (int i = 1; i < txtFileContent.size(); i++) {
                            String post_list = txtFileContent.get(i);
                            String dDataId = "A.0006.0005.S0001";
                            String a1mawspeed50 = post_list.substring(5, 9);
                            String a1mawdirection50 = post_list.substring(9, 12);
                            String a10mawspeed50 = post_list.substring(12, 16);
                            String a10mawdirection50 = post_list.substring(16, 19);
                            String ahmaxwspeed50 = post_list.substring(19, 23);
                            String ahmaxwspeedwdir50 = post_list.substring(23, 26);
                            String ahmaxwspeedt50 = post_list.substring(26, 30);
                            String hcfmiwspeed50 = post_list.substring(30, 34);
                            String hcfmiwdir50 = post_list.substring(34, 37);
                            String ahexwspeed50 = post_list.substring(37, 41);
                            String ehmaxwspeedwdir50 = post_list.substring(41, 44);
                            String hhmaxwspeedt50 = post_list.substring(44, 48);
                            String a1mawspeed70 = post_list.substring(48, 52);
                            String a1mawdirection70 = post_list.substring(52, 55);
                            String a10mawspeed70 = post_list.substring(55, 59);
                            String a10mawdirection70 = post_list.substring(59, 62);
                            String ahmaxwspeed70 = post_list.substring(62, 66);
                            String ahmaxwspeedwdir70 = post_list.substring(66, 69);
                            String ahmaxwspeedt70 = post_list.substring(69, 73);
                            String hcfmiwspeed70 = post_list.substring(73, 77);
                            String hcfmiwdir70 = post_list.substring(77, 80);
                            String ahexwspeed70 = post_list.substring(80, 84);
                            String ehmaxwspeedwdir70 = post_list.substring(84, 87);
                            String hhmaxwspeedt70 = post_list.substring(87, 91);
                            String a1mawspeed100 = post_list.substring(91, 95);
                            String a1mawdirection100 = post_list.substring(95, 98);
                            String a10mawspeed100 = post_list.substring(98, 102);
                            String a10mawdirection100 = post_list.substring(102, 105);
                            String ahmaxwspeed100 = post_list.substring(105, 109);
                            String ahmaxwspeedwdir100 = post_list.substring(109, 112);
                            String ahmaxwspeedt100 = post_list.substring(112, 116);
                            String hcfmiwspeed100 = post_list.substring(116, 120);
                            String hcfmiwdir100 = post_list.substring(120, 123);
                            String ahexwspeed100 = post_list.substring(123, 127);
                            String ehmaxwspeedwdir100 = post_list.substring(127, 130);
                            String hhmaxwspeedt100 = post_list.substring(130, 134);
                            SurfXiliWindenerMdeTab surfXiliWindenerMdeTab = new SurfXiliWindenerMdeTab();

                            surfXiliWindenerMdeTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));

                            surfXiliWindenerMdeTab.setdDataId(dDataId);

                            surfXiliWindenerMdeTab.setV01301(changeType(v01301));

                            String houmin=post_list.substring(0, 5);

                            surfXiliWindenerMdeTab.setHourmin(changeType(houmin));

                            String[] m=houmin.split(":");
                            String v04004=m[0];
                            String v04005=m[1];
                            String dDatetime =null;
                            if(i<240){
                                //日-1
                                int newv04003=Integer.parseInt(v04003)-1;
                                int newV04002=Integer.parseInt(v04002);
                                int newV04001=Integer.parseInt(v04001);
                                if(newv04003<1){
                                    newV04002=newV04002-1;
                                    int maxMonth=DateUtils.getDayCountOnMonth(newV04002,newV04001);
                                    newv04003=maxMonth;
                                    if(newV04002<1){
                                        newV04001=newV04001-1;
                                        newV04002=12;
                                        newv04003=31;
                                    }
                                }
                                dDatetime = newV04001 + "-" + newV04002 + "-" + newv04003 + " " + v04004+":"+v04005;
                                surfXiliWindenerMdeTab.setV04001(Short.parseShort(newV04001+""));
                                surfXiliWindenerMdeTab.setV04002(Short.parseShort(newV04002+""));
                                surfXiliWindenerMdeTab.setV04003(Short.parseShort(newv04003+""));
                            }else{
                                dDatetime=v04001+"-"+v04002+"-"+v04003+" "+v04004+":"+v04005;
                                surfXiliWindenerMdeTab.setV04001(Short.parseShort(changeType(v04001)));
                                surfXiliWindenerMdeTab.setV04002(Short.parseShort(changeType(v04002)));
                                surfXiliWindenerMdeTab.setV04003(Short.parseShort(changeType(v04003)));
                            }

                            surfXiliWindenerMdeTab.setV04004(Short.parseShort(changeType(v04004)));

                            surfXiliWindenerMdeTab.setV04005(Short.parseShort(changeType(v04005)));

                            surfXiliWindenerMdeTab.setdDatetime(dDatetime);

                            surfXiliWindenerMdeTab.setV06001(new BigDecimal(changeType(v06001)));

                            surfXiliWindenerMdeTab.setV05001(new BigDecimal(changeType(v05001)));

                            surfXiliWindenerMdeTab.setV07001(new BigDecimal(changeType(v07001)));

                            surfXiliWindenerMdeTab.setAwssensoriden50(new BigDecimal(changeType(awssensoriden50)));

                            surfXiliWindenerMdeTab.setAwdsensoriden50(new BigDecimal(changeType(awdsensoriden50)));

                            surfXiliWindenerMdeTab.setAwssensoriden70(new BigDecimal(changeType(awssensoriden70)));

                            surfXiliWindenerMdeTab.setAwdsensoriden70(new BigDecimal(changeType(awdsensoriden70)));

                            surfXiliWindenerMdeTab.setAwssensoriden100(new BigDecimal(changeType(awssensoriden100)));

                            surfXiliWindenerMdeTab.setAwdsensoriden100(new BigDecimal(changeType(awdsensoriden100)));



                            if(changeType(a1mawspeed50).equals("999999")){
                                surfXiliWindenerMdeTab.setA1mawspeed50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setA1mawspeed50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(a1mawspeed50))/10)));
                            }


                            surfXiliWindenerMdeTab.setA1mawdirection50(new BigDecimal(changeType(a1mawdirection50)));

                            if(changeType(a10mawspeed50).equals("999999")){
                                surfXiliWindenerMdeTab.setA10mawspeed50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setA10mawspeed50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(a10mawspeed50))/10)));
                            }


                            surfXiliWindenerMdeTab.setA10mawdirection50(new BigDecimal(changeType(a10mawdirection50)));

                            if(changeType(ahmaxwspeed50).equals("999999")){
                                surfXiliWindenerMdeTab.setAhmaxwspeed50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setAhmaxwspeed50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahmaxwspeed50))/10)));
                            }


                            surfXiliWindenerMdeTab.setAhmaxwspeedwdir50(new BigDecimal(changeType(ahmaxwspeedwdir50)));

                            surfXiliWindenerMdeTab.setAhmaxwspeedt50(new BigDecimal(changeType(ahmaxwspeedt50)));



                            if(changeType(hcfmiwspeed50).equals("999999")){
                                surfXiliWindenerMdeTab.setHcfmiwspeed50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setHcfmiwspeed50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(hcfmiwspeed50))/10)));
                            }

                            surfXiliWindenerMdeTab.setHcfmiwdir50(new BigDecimal(changeType(hcfmiwdir50)));

                            if(changeType(ahexwspeed50).equals("999999")){
                                surfXiliWindenerMdeTab.setAhexwspeed50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setAhexwspeed50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahexwspeed50))/10)));
                            }

                            surfXiliWindenerMdeTab.setEhmaxwspeedwdir50(new BigDecimal(changeType(ehmaxwspeedwdir50)));

                            surfXiliWindenerMdeTab.setHhmaxwspeedt50(new BigDecimal(changeType(hhmaxwspeedt50)));


                            if(changeType(a1mawspeed70).equals("999999")){
                                surfXiliWindenerMdeTab.setA1mawspeed70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setA1mawspeed70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(a1mawspeed70))/10)));
                            }

                            surfXiliWindenerMdeTab.setA1mawdirection70(new BigDecimal(changeType(a1mawdirection70)));

                            if(changeType(a10mawspeed70).equals("999999")){
                                surfXiliWindenerMdeTab.setA10mawspeed70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setA10mawspeed70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(a10mawspeed70))/10)));
                            }
                            surfXiliWindenerMdeTab.setA10mawdirection70(new BigDecimal(changeType(a10mawdirection70)));


                            if(changeType(ahmaxwspeed70).equals("999999")){
                                surfXiliWindenerMdeTab.setAhmaxwspeed70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setAhmaxwspeed70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahmaxwspeed70))/10)));
                            }
                            surfXiliWindenerMdeTab.setAhmaxwspeedwdir70(new BigDecimal(changeType(ahmaxwspeedwdir70)));

                            surfXiliWindenerMdeTab.setAhmaxwspeedt70(new BigDecimal(changeType(ahmaxwspeedt70)));

                            if(changeType(hcfmiwspeed70).equals("999999")){
                                surfXiliWindenerMdeTab.setHcfmiwspeed70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setHcfmiwspeed70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(hcfmiwspeed70))/10)));
                            }
                            surfXiliWindenerMdeTab.setHcfmiwdir70(new BigDecimal(changeType(hcfmiwdir70)));

                            if(changeType(ahexwspeed70).equals("999999")){
                                surfXiliWindenerMdeTab.setAhexwspeed70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setAhexwspeed70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahexwspeed70))/10)));
                            }
                            surfXiliWindenerMdeTab.setEhmaxwspeedwdir70(new BigDecimal(changeType(ehmaxwspeedwdir70)));

                            surfXiliWindenerMdeTab.setHhmaxwspeedt70(new BigDecimal(changeType(hhmaxwspeedt70)));

                            if(changeType(a1mawspeed100).equals("999999")){
                                surfXiliWindenerMdeTab.setA1mawspeed100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setA1mawspeed100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(a1mawspeed100))/10)));
                            }
                            surfXiliWindenerMdeTab.setA1mawdirection100(new BigDecimal(changeType(a1mawdirection100)));

                            if(changeType(a10mawspeed100).equals("999999")){
                                surfXiliWindenerMdeTab.setA10mawspeed100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setA10mawspeed100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(a10mawspeed100))/10)));
                            }
                            surfXiliWindenerMdeTab.setA10mawdirection100(new BigDecimal(changeType(a10mawdirection100)));

                            if(changeType(ahmaxwspeed100).equals("999999")){
                                surfXiliWindenerMdeTab.setAhmaxwspeed100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setAhmaxwspeed100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahmaxwspeed100))/10)));
                            }
                            surfXiliWindenerMdeTab.setAhmaxwspeedwdir100(new BigDecimal(changeType(ahmaxwspeedwdir100)));

                            surfXiliWindenerMdeTab.setAhmaxwspeedt100(new BigDecimal(changeType(ahmaxwspeedt100)));

                            if(changeType(hcfmiwspeed100).equals("999999")){
                                surfXiliWindenerMdeTab.setHcfmiwspeed100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setHcfmiwspeed100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(hcfmiwspeed100))/10)));
                            }
                            surfXiliWindenerMdeTab.setHcfmiwdir100(new BigDecimal(changeType(hcfmiwdir100)));

                            if(changeType(ahexwspeed100).equals("999999")){
                                surfXiliWindenerMdeTab.setAhexwspeed100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerMdeTab.setAhexwspeed100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(ahexwspeed100))/10)));
                            }
                            surfXiliWindenerMdeTab.setEhmaxwspeedwdir100(new BigDecimal(changeType(ehmaxwspeedwdir100)));

                            surfXiliWindenerMdeTab.setHhmaxwspeedt100(new BigDecimal(changeType(hhmaxwspeedt100)));

                            surfXiliWindenerMdeTabList.add(surfXiliWindenerMdeTab);
                        }
                    } else if (pathname.contains("PBL_WE_FT")) {
                        String messages = txtFileContent.get(0).split("-")[0];
                        String v01301 = messages.substring(0, 5);
                        String v04001 = messages.substring(5, 9);
                        String v04002 = messages.substring(9, 11);
                        String v06001 = messages.substring(11, 18);
                        String v05001 = messages.substring(18, 24);
                        String v07001 = messages.substring(24, 31);
                        String awssensoriden50 = messages.substring(31, 32);
                        String awdsensoriden50 = messages.substring(32, 33);
                        String awssensoriden70 = messages.substring(33, 34);
                        String awdsensoriden70 = messages.substring(34, 35);
                        String awssensoriden100 = messages.substring(35, 36);
                        String awdsensoriden100 = messages.substring(36, 37);
                        for (int i = 1; i < txtFileContent.size(); i++) {
                            String post_list = txtFileContent.get(i);
                            String dDataId = "A.0006.0006.S0001";
                            String dDatetime = post_list.substring(0, 16) + ":00";
                            if (post_list.contains("------")) {
                                continue;
                            }
                            String[] d=dDatetime.split("-");
                            v04001=d[0];
                            v04002=d[1];
                            String v04003 = post_list.substring(8, 10);
                            String v04004 = post_list.substring(11, 13);
                            String HOURMIN = post_list.substring(11, 16);

                            String AVSIWMAH50 = post_list.substring(16, 20);
                            String MEWDAH50 = post_list.substring(20, 23);
                            String W10MINAVSIWMAH50 = post_list.substring(23, 27);
                            String W10MINMAWISARHO50 = post_list.substring(27, 30);
                            String WHOURAVSIWMAH50 = post_list.substring(30, 34);
                            String WHOURMAWISARHO50 = post_list.substring(34, 37);
                            String WHOURMAWISARHO50TIME = post_list.substring(37, 41);
                            String WHOURMAWISARHO50MAXS = post_list.substring(41, 45);
                            String WHOURMAWISARHO50MAXF = post_list.substring(45, 48);
                            String WHOURMAWISARHO50MAXT = post_list.substring(48, 52);
                            String WAVSIWMAH70 = post_list.substring(52, 56);
                            String WMEWDAH70 = post_list.substring(56, 59);
                            String W10MINAVSIWMAH70 = post_list.substring(59, 63);
                            String W10MINMAWISARHO70 = post_list.substring(63, 66);
                            String WHOURAVSIWMAH70 = post_list.substring(66, 70);
                            String WHOURMAWISARHO70 = post_list.substring(70, 73);
                            String WHOURMAWISARHO70TIME = post_list.substring(73, 77);
                            String WHOURMAWISARHO70MAXS = post_list.substring(77, 81);
                            String WHOURMAWISARHO70MAXF = post_list.substring(81, 84);
                            String WHOURMAWISARHO70MAXT = post_list.substring(84, 88);
                            String WAVSIWMAH100 = post_list.substring(88, 92);
                            String WMEWDAH100 = post_list.substring(92, 95);
                            String W10MINAVSIWMAH100 = post_list.substring(95, 99);
                            String W10MINMAWISARHO100 = post_list.substring(99, 102);

                            String WHOURAVSIWMAH100 = post_list.substring(102, 106);

                            String WHOURMAWISARHO100 = post_list.substring(106, 109);

                            String WHOURMAWISARHO100TIME = post_list.substring(109, 113);

                            String WHOURMAWISARHO100MAXS = post_list.substring(113, 117);

                            String WHOURMAWISARHO100MAXF = post_list.substring(117, 120);

                            String WHOURMAWISARHO100MAXT = post_list.substring(120, 124);

                            SurfXiliWindenerTimTab surfXiliWindenerTimTab = new SurfXiliWindenerTimTab();

                            surfXiliWindenerTimTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));

                            surfXiliWindenerTimTab.setdDataId(dDataId);

                            surfXiliWindenerTimTab.setdDatetime(dDatetime);

                            surfXiliWindenerTimTab.setV01301(changeType(v01301));

                            surfXiliWindenerTimTab.setV04001(Short.parseShort(changeType(v04001)));

                            surfXiliWindenerTimTab.setV04002(Short.parseShort(changeType(v04002)));

                            surfXiliWindenerTimTab.setV04003(Short.parseShort(changeType(v04003)));

                            surfXiliWindenerTimTab.setV04004(Short.parseShort(changeType(v04004)));

                            surfXiliWindenerTimTab.setV06001(new BigDecimal(changeType(v06001)));

                            surfXiliWindenerTimTab.setV05001(new BigDecimal(changeType(v05001)));

                            surfXiliWindenerTimTab.setV07001(new BigDecimal(changeType(v07001)));

//                            surfXiliWindenerTimTab.setTheins50(Short.parseShort(changeType("99")));
//
//                            surfXiliWindenerTimTab.setTheins70(Short.parseShort(changeType("99")));
//
//                            surfXiliWindenerTimTab.setTheins100(Short.parseShort(changeType("99")));

                            surfXiliWindenerTimTab.setAwssensoriden50(new BigDecimal(changeType(awssensoriden50)));

                            surfXiliWindenerTimTab.setAwdsensoriden50(new BigDecimal(changeType(awdsensoriden50)));

                            surfXiliWindenerTimTab.setAwssensoriden70(new BigDecimal(changeType(awssensoriden70)));

                            surfXiliWindenerTimTab.setAwdsensoriden70(new BigDecimal(changeType(awdsensoriden70)));

                            surfXiliWindenerTimTab.setAwssensoriden100(new BigDecimal(changeType(awssensoriden100)));

                            surfXiliWindenerTimTab.setAwdsensoriden100(new BigDecimal(changeType(awdsensoriden100)));

                            surfXiliWindenerTimTab.setV02320(changeType("999999"));

                            surfXiliWindenerTimTab.setHourmin(changeType(HOURMIN));

                            if(changeType(AVSIWMAH50).equals("999999")){
                                surfXiliWindenerTimTab.setAvsiwmah50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setAvsiwmah50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(AVSIWMAH50))/10)));
                            }

                            surfXiliWindenerTimTab.setMewdah50(new BigDecimal(changeType(MEWDAH50)));

                            if(changeType(W10MINAVSIWMAH50).equals("999999")){
                                surfXiliWindenerTimTab.setW10minavsiwmah50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setW10minavsiwmah50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(W10MINAVSIWMAH50))/10)));
                            }

                            surfXiliWindenerTimTab.setW10minmawisarho50(new BigDecimal(changeType(W10MINMAWISARHO50)));

                            if(changeType(WHOURAVSIWMAH50).equals("999999")){
                                surfXiliWindenerTimTab.setWhouravsiwmah50(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWhouravsiwmah50(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WHOURAVSIWMAH50))/10)));
                            }
                            surfXiliWindenerTimTab.setWhourmawisarho50(new BigDecimal(changeType(WHOURMAWISARHO50)));

                            surfXiliWindenerTimTab.setWhourmawisarho50time(new BigDecimal(changeType(WHOURMAWISARHO50TIME)));

                            if(changeType(WHOURMAWISARHO50MAXS).equals("999999")){
                                surfXiliWindenerTimTab.setWhourmawisarho50maxs(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWhourmawisarho50maxs(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WHOURMAWISARHO50MAXS))/10)));
                            }
                            surfXiliWindenerTimTab.setWhourmawisarho50maxf(new BigDecimal(changeType(WHOURMAWISARHO50MAXF)));

                            surfXiliWindenerTimTab.setWhourmawisarho50maxt(new BigDecimal(changeType(WHOURMAWISARHO50MAXT)));

                            if(changeType(WAVSIWMAH70).equals("999999")){
                                surfXiliWindenerTimTab.setWavsiwmah70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWavsiwmah70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WAVSIWMAH70))/10)));
                            }
                            surfXiliWindenerTimTab.setWmewdah70(new BigDecimal(changeType(WMEWDAH70)));

                            if(changeType(W10MINAVSIWMAH70).equals("999999")){
                                surfXiliWindenerTimTab.setW10minavsiwmah70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setW10minavsiwmah70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(W10MINAVSIWMAH70))/10)));
                            }
                            surfXiliWindenerTimTab.setW10minmawisarho70(new BigDecimal(changeType(W10MINMAWISARHO70)));


                            if(changeType(WHOURAVSIWMAH70).equals("999999")){
                                surfXiliWindenerTimTab.setWhouravsiwmah70(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWhouravsiwmah70(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WHOURAVSIWMAH70))/10)));
                            }
                            surfXiliWindenerTimTab.setWhourmawisarho70(new BigDecimal(changeType(WHOURMAWISARHO70)));
                            try{
                                surfXiliWindenerTimTab.setWhourmawisarho70time(new BigDecimal(changeType(WHOURMAWISARHO70TIME)));
                            }catch (Exception e){
                                surfXiliWindenerTimTab.setWhourmawisarho70time(new BigDecimal("999999"));
                            }


                            if(changeType(WHOURMAWISARHO70MAXS).equals("999999")){
                                surfXiliWindenerTimTab.setWhourmawisarho70maxs(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWhourmawisarho70maxs(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WHOURMAWISARHO70MAXS))/10)));
                            }
                            surfXiliWindenerTimTab.setWhourmawisarho70maxf(new BigDecimal(changeType(WHOURMAWISARHO70MAXF)));

                            surfXiliWindenerTimTab.setWhourmawisarho70maxt(new BigDecimal(changeType(WHOURMAWISARHO70MAXT)));

                            if(changeType(WAVSIWMAH100).equals("999999")){
                                surfXiliWindenerTimTab.setWavsiwmah100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWavsiwmah100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WAVSIWMAH100))/10)));
                            }
                            surfXiliWindenerTimTab.setWmewdah100(new BigDecimal(changeType(WMEWDAH100)));

                            if(changeType(W10MINAVSIWMAH100).equals("999999")){
                                surfXiliWindenerTimTab.setW10minavsiwmah100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setW10minavsiwmah100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(W10MINAVSIWMAH100))/10)));
                            }
                            surfXiliWindenerTimTab.setW10minmawisarho100(new BigDecimal(changeType(W10MINMAWISARHO100)));

                            if(changeType(WHOURAVSIWMAH100).equals("999999")){
                                surfXiliWindenerTimTab.setWhouravsiwmah100(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWhouravsiwmah100(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WHOURAVSIWMAH100))/10)));
                            }
                            surfXiliWindenerTimTab.setWhourmawisarho100(new BigDecimal(changeType(WHOURMAWISARHO100)));

                            surfXiliWindenerTimTab.setWhourmawisarho100time(new BigDecimal(changeType(WHOURMAWISARHO100TIME)));

                            if(changeType(WHOURMAWISARHO100MAXS).equals("999999")){
                                surfXiliWindenerTimTab.setWhourmawisarho100maxs(new BigDecimal(999999));
                            }else {
                                surfXiliWindenerTimTab.setWhourmawisarho100maxs(new BigDecimal(String.valueOf(Double.parseDouble(changeType(WHOURMAWISARHO100MAXS))/10)));
                            }
                            surfXiliWindenerTimTab.setWhourmawisarho100maxf(new BigDecimal(changeType(WHOURMAWISARHO100MAXF)));

                            surfXiliWindenerTimTab.setWhourmawisarho100maxt(new BigDecimal(changeType(WHOURMAWISARHO100MAXT)));

                            surfXiliWindenerTimTabList.add(surfXiliWindenerTimTab);
                        }
                    }
                    if (!surfXiliWindenerHfsTabList.isEmpty()) {
                        resultMap.put("surfXiliWindenerHfsTabList", surfXiliWindenerHfsTabList);
                    }
                    if (!surfXiliWindenerMdeTabList.isEmpty()) {
                        resultMap.put("surfXiliWindenerMdeTabList", surfXiliWindenerMdeTabList);
                    }
                    if (!surfXiliWindenerMdoTabList.isEmpty()) {
                        resultMap.put("surfXiliWindenerMdoTabList", surfXiliWindenerMdoTabList);
                    }
                    if (!surfXiliWindenerTimTabList.isEmpty()) {
                        resultMap.put("surfXiliWindenerTimTabList", surfXiliWindenerTimTabList);
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

    //    public static String changeType(String dates) {
//        if(dates.contains("0-")){
//            dates = dates.substring(dates.indexOf("-"),dates.length());
//        }
//        return dates;
//    }
    public static java.sql.Timestamp strToSqlDate(String strDate) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = sf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Timestamp dateSQL = new java.sql.Timestamp(date.getTime());
        return dateSQL;
    }

    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.equals("///") || dates.indexOf("/") > -1 || dates.equals('"') || dates.equals("////") || dates.equals("/////") || dates.equals("//////") || dates.equals("////////") || dates.equals("---") ||  dates.equals("------") || dates.equals("-----")) {
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
