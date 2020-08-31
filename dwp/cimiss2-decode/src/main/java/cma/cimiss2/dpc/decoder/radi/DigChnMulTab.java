package cma.cimiss2.dpc.decoder.radi;

import java.util.Date;

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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/29 0029 14:25
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class DigChnMulTab {
    /**
     * 结果集
     */
    private ParseResult<RadiDigChnMulTab> parseResult = new ParseResult<RadiDigChnMulTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public Map<String, Object> decode(File file) {
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

                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                List<RadiDigChnMulHourTab> radiDigChnMulHourTabList = new ArrayList<>();
                List<RadiDigChnMulDayTab> radiDigChnMulDayTabList = new ArrayList<>();
                String stationTyp=null;
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    String[] shijian = txtFileContent.get(0).split("\\s+");
                    String V01300 = changeType(shijian[0]);
                    String V01301 = changeType(shijian[0]);
                    String V05001 = shijian[1];
                    String V06001 = shijian[2];
                    String V07001 = shijian[3];
                    stationTyp=shijian[4];
                    String V04001 = shijian[6];
                    String V04002 = shijian[7];
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    List QhourList = new ArrayList();
                    List NhourList = new ArrayList();
                    List DhourList = new ArrayList();
                    List ShourList = new ArrayList();
                    List RhourList = new ArrayList();
                    List QdayList = new ArrayList<>();
                    List NdayList = new ArrayList<>();
                    List DdayList = new ArrayList();
                    List SdayList = new ArrayList<>();
                    List RdayList = new ArrayList<>();
                    int totalDay = 0;
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        if (txtFileContent.get(i).equals("Q")) {
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] message = txtFileContent.get(j).split("\\s+");
                                for (int h = 0; h < 24; h++) {
                                    QhourList.add(message[h]);
                                }
                                String listMesage = message[24] + "," + message[25] + "," + message[26]+","+message[27];
                                QdayList.add(listMesage);
                                if (message[27].endsWith("=") && txtFileContent.get(j + 1).length() < 10) {
                                    break;
                                }

                            }
                        }
                        if (txtFileContent.get(i).equals("N")) {
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] message = txtFileContent.get(j).split("\\s+");

                                for (int h = 0; h < 24; h++) {
                                    NhourList.add(message[h]);
                                }
                                String listMesage = message[24] + "," + message[25] + "," + message[26] + "," + message[27] + "," + message[28];
                                NdayList.add(listMesage);
                                if (message[28].endsWith("=") && txtFileContent.get(j + 1).length() < 10) {
                                    break;
                                }

                            }
                        }
                        if (txtFileContent.get(i).equals("D")) {
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] message = txtFileContent.get(j).split("\\s+");

                                for (int h = 0; h < 24; h++) {
                                    DhourList.add(message[h]);
                                }
                                String listMesage = message[24] + "," + message[25] + "," + message[26];
                                DdayList.add(listMesage);
                                if (message[26].endsWith("=") && txtFileContent.get(j + 1).length() < 10) {
                                    break;
                                }

                            }
                        }
                        if (txtFileContent.get(i).equals("S")) {
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] message = txtFileContent.get(j).split("\\s+");

                                for (int h = 0; h < 24; h++) {
                                    ShourList.add(message[h]);
                                }
                                String listMesage = message[24] + "," + message[25] + "," + message[26];
                                SdayList.add(listMesage);
                                if (message[27].endsWith("=") && txtFileContent.get(j + 1).length() < 10) {
                                    break;
                                }

                            }
                        }
                        if (txtFileContent.get(i).equals("R")) {
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] message = txtFileContent.get(j).split("\\s+");

                                for (int h = 0; h < 24; h++) {
                                    RhourList.add(message[h]);
                                }
                                String listMesage = message[24] + "," + message[26] + "," + message[27];
                                RdayList.add(listMesage);
                                if (message[33].endsWith("=") && txtFileContent.get(j + 1).length() < 10) {
                                    break;
                                }

                            }
                        }
                    }
                    int hourday = 1;
                    int hourhour = 0;
                    int year=Integer.parseInt(shijian[6]);
                    int month=Integer.parseInt(shijian[7]);
                    int count=0;
                    totalDay= DateUtils.getDayCountOnMonth(month,year);
                    for (int h = 0; h < QhourList.size(); h++) {
                        count++;
                        if (count% 24 != 0) {
                            hourhour = hourhour + 1;
                        } else{
                            hourday = hourday + 1;
                            hourhour = 0;
                        }

                        if (hourday > totalDay) {
                            hourday = 1;
                            month+=1;
                            if(month>12){
                                year+=1;
                                month=1;
                            }
                            totalDay= DateUtils.getDayCountOnMonth(month,year);
                        }
                        String D_DATETIME =  year+ "-" + month + "-" + hourday + " " + hourhour + ":00:00";
                        RadiDigChnMulHourTab radiDigChnMulHourTab = new RadiDigChnMulHourTab();
                        radiDigChnMulHourTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));
                        radiDigChnMulHourTab.setdIymdhm(sdf.format(calendar.getTime()));
                        radiDigChnMulHourTab.setdRymdhm(sdf.format(calendar.getTime()));
                        radiDigChnMulHourTab.setdUpdateTime(sdf.format(calendar.getTime()));
                        radiDigChnMulHourTab.setdDatetime(D_DATETIME);
                        radiDigChnMulHourTab.setV01301(V01301);
                        radiDigChnMulHourTab.setV01300(Integer.valueOf(V01300));
                        String v0511=V05001.substring(0,V05001.length()-3);
                        String v0512=V05001.substring(V05001.length()-3,V05001.length()-1);
                        String v0611=V06001.substring(0,V06001.length()-3);
                        String v0612=V06001.substring(V06001.length()-3,V06001.length()-1);
                        BigDecimal v0501=new BigDecimal(Integer.parseInt(v0511)+Float.parseFloat(v0512)/60);
                        BigDecimal v0601=new BigDecimal(Integer.parseInt(v0611)+Float.parseFloat(v0612)/60);
                        radiDigChnMulHourTab.setV05001(v0501.setScale(4,BigDecimal.ROUND_HALF_UP));
                        radiDigChnMulHourTab.setV06001(v0601.setScale(4,BigDecimal.ROUND_HALF_UP));
                        radiDigChnMulHourTab.setV07001(new BigDecimal(Double.parseDouble(changeType(V07001))/10));
                        radiDigChnMulHourTab.setV04001(Short.parseShort(year+""));
                        radiDigChnMulHourTab.setV04002(Short.parseShort(month+""));
                        radiDigChnMulHourTab.setV04003(Short.parseShort(String.valueOf(hourday)));
                        radiDigChnMulHourTab.setV04004(Short.parseShort(String.valueOf(hourhour)));
                        radiDigChnMulHourTab.setV04005((short) 0);
                        radiDigChnMulHourTab.setV14311(999998);
                        radiDigChnMulHourTab.setV14312(999998);
                        radiDigChnMulHourTab.setV14313(999998);
                        radiDigChnMulHourTab.setV14314(999998);
                        radiDigChnMulHourTab.setV14315(999998);
                        radiDigChnMulHourTab.setV14316(999998);
                        try {
                            if (changeType(QhourList.get(h).toString()).equals("999999")) {
                                radiDigChnMulHourTab.setV14320(new BigDecimal(999999));
                            } else {
                                radiDigChnMulHourTab.setV14320(new BigDecimal(String.valueOf(Double.parseDouble(changeType(QhourList.get(h).toString())) / 100)));
                            }
                        } catch (Exception e) {
                            radiDigChnMulHourTab.setV14320(new BigDecimal(999999));
                        }
                        radiDigChnMulHourTab.setV1431105(999998);
                        radiDigChnMulHourTab.setV1402105052(999998);
                        try {
                            if (changeType(NhourList.get(h).toString()).equals("999999")) {
                                radiDigChnMulHourTab.setV14308(new BigDecimal(999999));
                            } else {
                                radiDigChnMulHourTab.setV14308(new BigDecimal(String.valueOf(Double.parseDouble(changeType(NhourList.get(h).toString())) / 100)));
                            }
                        } catch (Exception e) {
                            radiDigChnMulHourTab.setV14308(new BigDecimal(999999));
                        }
                        radiDigChnMulHourTab.setV1431205(999998);
                        radiDigChnMulHourTab.setV1431205052(999998);
                        radiDigChnMulHourTab.setV1431206(999998);
                        radiDigChnMulHourTab.setV1431206052(999998);
                        if("Z1".equals(stationTyp)){
                            radiDigChnMulHourTab.setV14322(new BigDecimal("999999"));
                        }else{
                            radiDigChnMulHourTab.setV14322(new BigDecimal("999998"));
                        }

                        radiDigChnMulHourTab.setV1431305(999998);
                        radiDigChnMulHourTab.setV1431305052(999998);
                        try {
                            if (changeType(DhourList.get(h).toString()).equals("999999")) {
                                if("Z1".equals(stationTyp)) {
                                    radiDigChnMulHourTab.setV14309(new BigDecimal(999999));
                                }else{
                                    radiDigChnMulHourTab.setV14309(new BigDecimal(999998));
                                }
                            } else {
                                radiDigChnMulHourTab.setV14309(new BigDecimal(String.valueOf(Double.parseDouble(changeType(DhourList.get(h).toString())) / 100)));
                            }
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulHourTab.setV14309(new BigDecimal(999999));
                            }else{
                                radiDigChnMulHourTab.setV14309(new BigDecimal(999998));
                            }
                        }

                        radiDigChnMulHourTab.setV1431405(999998);
                        radiDigChnMulHourTab.setV1431405052(999998);
                        try {
                            if (changeType(RhourList.get(h).toString()).equals("999999")) {
                                if("Z1".equals(stationTyp)){
                                    radiDigChnMulHourTab.setV14306(new BigDecimal(999999));
                                }else {
                                    radiDigChnMulHourTab.setV14306(new BigDecimal(999998));
                                }
                            } else {
                                radiDigChnMulHourTab.setV14306(new BigDecimal(String.valueOf(Double.parseDouble(changeType(RhourList.get(h).toString())) / 100)));
                            }
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulHourTab.setV14306(new BigDecimal(999999));
                            }else{
                                radiDigChnMulHourTab.setV14306(new BigDecimal(999998));
                            }
                        }

                        radiDigChnMulHourTab.setV1431505(999998);
                        radiDigChnMulHourTab.setV1431505052(999998);
                        if("Z1".equals(stationTyp)) {
                            radiDigChnMulHourTab.setV14307(new BigDecimal("999999"));
                        }else{
                            radiDigChnMulHourTab.setV14307(new BigDecimal("999998"));
                        }
                        radiDigChnMulHourTab.setV1431605(999998);
                        radiDigChnMulHourTab.setV1431605052(999998);
                        if("Z1".equals(stationTyp)){
                            radiDigChnMulHourTab.setV14032(new BigDecimal("999999"));
                            radiDigChnMulHourTab.setV15483(new BigDecimal("999999"));
                        }else{
                            radiDigChnMulHourTab.setV14032(new BigDecimal("999998"));
                            radiDigChnMulHourTab.setV15483(new BigDecimal("999998"));
                        }

                        radiDigChnMulHourTabList.add(radiDigChnMulHourTab);

                    }
                    int days = 0;
                   year=Integer.parseInt(shijian[6]);
                   month=Integer.parseInt(shijian[7]);
                    for (int h = 0; h < QdayList.size(); h++) {
                        days = days + 1;
                        if(days > totalDay){
                            days = 1;
                            month+=1;
                            if(month>12){
                                year+=1;
                                month=1;
                            }
                        }
                        totalDay= DateUtils.getDayCountOnMonth(month,year);
                        String D_DATETIME = year + "-" + month + "-" + days + " 00:00:00";

                        String[] QMessage = null;
                        String[] NMessage = null;
                        String[] DMessage = null;
                        String[] SMessage = null;
                        String[] RMessage = null;
                        if (QdayList.size() > 0) {
                            QMessage = QdayList.get(h).toString().split(",");
                        }
                        if (NdayList.size() > 0) {
                            NMessage = NdayList.get(h).toString().split(",");
                        }
                        if (DdayList.size() > 0) {
                            DMessage = DdayList.get(h).toString().split(",");
                        }
                        if (SdayList.size() > 0) {
                            SMessage = SdayList.get(h).toString().split(",");
                        }
                        if (RdayList.size() > 0) {
                            RMessage = RdayList.get(h).toString().split(",");
                        }
                        RadiDigChnMulDayTab radiDigChnMulDayTab = new RadiDigChnMulDayTab();
                        radiDigChnMulDayTab.setdRetainId(new BigDecimal(getOrderIdByUUId()));
                        radiDigChnMulDayTab.setdIymdhm(sdf.format(calendar.getTime()));
                        radiDigChnMulDayTab.setdRymdhm(sdf.format(calendar.getTime()));
                        radiDigChnMulDayTab.setdUpdateTime(sdf.format(calendar.getTime()));
                        radiDigChnMulDayTab.setdDatetime(D_DATETIME);
                        radiDigChnMulDayTab.setV01301(V01301);
                        radiDigChnMulDayTab.setV01300(Integer.valueOf(V01300));
                        String v0511=V05001.substring(0,V05001.length()-3);
                        String v0512=V05001.substring(V05001.length()-3,V05001.length()-1);
                        String v0611=V06001.substring(0,V06001.length()-3);
                        String v0612=V06001.substring(V06001.length()-3,V06001.length()-1);
                        BigDecimal v0501=new BigDecimal(Integer.parseInt(v0511)+Float.parseFloat(v0512)/60);
                        BigDecimal v0601=new BigDecimal(Integer.parseInt(v0611)+Float.parseFloat(v0612)/60);
                        radiDigChnMulDayTab.setV05001(v0501.setScale(4,BigDecimal.ROUND_HALF_UP));
                        radiDigChnMulDayTab.setV06001(v0601.setScale(4,BigDecimal.ROUND_HALF_UP));
                        radiDigChnMulDayTab.setV07001(new BigDecimal(Double.parseDouble(changeType(V07001))/10));
                        radiDigChnMulDayTab.setV04001(Short.parseShort(year+""));
                        radiDigChnMulDayTab.setV04002(Short.parseShort(month+""));
                        radiDigChnMulDayTab.setV04003(Short.parseShort(String.valueOf(days)));
                        radiDigChnMulDayTab.setV04004(Short.parseShort(String.valueOf(hourhour)));
                        radiDigChnMulDayTab.setV04005((short) 0);
                        try {
                            if (changeType(QMessage[0]).equals("999999")) {
                                radiDigChnMulDayTab.setV14320Day(new BigDecimal(999999));
                            } else {
                                radiDigChnMulDayTab.setV14320Day(new BigDecimal(String.valueOf(Double.parseDouble(changeType(QMessage[0])) / 100)));
                            }
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV14320Day(new BigDecimal(999999));
                        }
                        try {
                            radiDigChnMulDayTab.setV1431105Day(Integer.parseInt(changeType(QMessage[1])));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431105Day(999999);
                        }
                        try {
                            if(QMessage[2].endsWith("=")){
                                QMessage[2] = QMessage[2].replace("=","");
                            }
                            radiDigChnMulDayTab.setV1402105052Day(changeType(QMessage[2]));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1402105052Day("999999");
                        }
                        try {
                            if(QMessage[3].endsWith("=")){
                                QMessage[3] = QMessage[3].replace("=","");
                            }
                            BigDecimal decimal=new BigDecimal(Double.parseDouble(changeType(QMessage[3]))/10);
                            radiDigChnMulDayTab.setV14032(decimal.setScale(1,BigDecimal.ROUND_HALF_UP));
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)){
                                radiDigChnMulDayTab.setV14032(new BigDecimal(999999));
                            }else{
                                radiDigChnMulDayTab.setV14032(new BigDecimal(999998));
                            }

                        }
                        try {
                            if (changeType(NMessage[0]).equals("999999")) {
                                radiDigChnMulDayTab.setV14308Day(new BigDecimal(999999));
                            } else {
                                radiDigChnMulDayTab.setV14308Day(new BigDecimal(String.valueOf(Double.parseDouble(changeType(NMessage[0])) / 100)));
                            }
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV14308Day(new BigDecimal(999999));
                        }
                        try {
                            radiDigChnMulDayTab.setV1431205Day(Integer.parseInt(changeType(NMessage[1])));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431205Day(999999);
                        }
                        try {
                            radiDigChnMulDayTab.setV1431205052Day(changeType(NMessage[2]));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431205052Day("999999");
                        }
                        try {
                            radiDigChnMulDayTab.setV1431206Day(Integer.parseInt(changeType(NMessage[3])));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431206Day(999999);
                        }
                        try {
                            if(NMessage[4].endsWith("=")){
                                NMessage[4] = NMessage[4].replace("=","");
                            }
                            radiDigChnMulDayTab.setV1431206052Day(changeType(NMessage[4]));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431206052Day("999999");
                        }
                        try {
                            if (changeType(SMessage[0]).equals("999999")) {
                                if("Z1".equals(stationTyp)){
                                    radiDigChnMulDayTab.setV14322Day(new BigDecimal(999999));
                                }else{
                                    radiDigChnMulDayTab.setV14322Day(new BigDecimal(999998));
                                }
                            } else {
                                radiDigChnMulDayTab.setV14322Day(new BigDecimal(String.valueOf(Double.parseDouble(changeType(SMessage[0])) / 100)));
                            }
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulDayTab.setV14322Day(new BigDecimal(999999));
                            }else{
                                radiDigChnMulDayTab.setV14322Day(new BigDecimal(999998));
                            }
                        }
                        if("Z1".equals(stationTyp)) {
                            try {
                                radiDigChnMulDayTab.setV1431305Day(Integer.parseInt(changeType(SMessage[1])));
                            } catch (Exception e) {
                                radiDigChnMulDayTab.setV1431305Day(999999);

                            }
                        }else{
                            radiDigChnMulDayTab.setV1431305Day(999998);
                        }
                        try {
                            if(SMessage[2].endsWith("=")){
                                SMessage[2] = SMessage[2].replace("=","");
                            }
                            radiDigChnMulDayTab.setV1431305052Day(changeType(SMessage[2]));
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulDayTab.setV1431305052Day("999999");
                            }else{
                                radiDigChnMulDayTab.setV1431305052Day("999998");
                            }
                        }
                        try {
                            if (changeType(DMessage[0]).equals("999999")) {
                                if("Z1".equals(stationTyp)){
                                    radiDigChnMulDayTab.setV14309Day(new BigDecimal(999999));
                                }else {
                                    radiDigChnMulDayTab.setV14309Day(new BigDecimal(999998));
                                }

                            } else {
                                radiDigChnMulDayTab.setV14309Day(new BigDecimal(String.valueOf(Double.parseDouble(changeType(DMessage[0])) / 100)));
                            }
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulDayTab.setV14309Day(new BigDecimal(999999));
                            }else{
                                radiDigChnMulDayTab.setV14309Day(new BigDecimal(999998));
                            }
                        }
                        try {
                            radiDigChnMulDayTab.setV1431405Day(Integer.parseInt(changeType(DMessage[1])));
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulDayTab.setV1431405Day(999999);
                            }else{
                                radiDigChnMulDayTab.setV1431405Day(999998);
                            }
                        }
                        try {
                            if(DMessage[2].endsWith("=")){
                                DMessage[2] = DMessage[2].replace("=","");
                            }
                            radiDigChnMulDayTab.setV1431405052Day(changeType(DMessage[2]));
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulDayTab.setV1431405052Day("999999");
                            }else{
                                radiDigChnMulDayTab.setV1431405052Day("999998");
                            }
                        }

                        try {
                            if (changeType(RMessage[0]).equals("999999")) {
                                if("Z1".equals(stationTyp)) {
                                    radiDigChnMulDayTab.setV14306Day(new BigDecimal(999999));
                                }else{
                                    radiDigChnMulDayTab.setV14306Day(new BigDecimal(999998));
                                }
                            } else {
                                radiDigChnMulDayTab.setV14306Day(new BigDecimal(String.valueOf(Double.parseDouble(changeType(RMessage[0])) / 100)));
                            }
                        } catch (Exception e) {
                            if("Z1".equals(stationTyp)) {
                                radiDigChnMulDayTab.setV14306Day(new BigDecimal(999999));
                            }else{
                                radiDigChnMulDayTab.setV14306Day(new BigDecimal(999998));
                            }
                        }
                        try {
                            radiDigChnMulDayTab.setV1431505Day(Integer.parseInt(changeType(RMessage[1])));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431505Day(999998);
                        }
                        try {
                            if(RMessage[2].endsWith("=")){
                                RMessage[2] = RMessage[2].replace("=","");
                            }
                            radiDigChnMulDayTab.setV1431505052Day(changeType(RMessage[2]));
                        } catch (Exception e) {
                            radiDigChnMulDayTab.setV1431505052Day("999998");
                        }
                        if("Z1".equals(stationTyp)){
                            radiDigChnMulDayTab.setV14307(new BigDecimal("999999"));
                        }else{
                            radiDigChnMulDayTab.setV14307(new BigDecimal("999998"));
                        }

                        if("Z1".equals(stationTyp)){
                            radiDigChnMulDayTab.setV1431605Day(999999);
                        }else {
                            radiDigChnMulDayTab.setV1431605Day(999998);
                        }
                        if("Z1".equals(stationTyp)) {
                            radiDigChnMulDayTab.setV1431605052Day("999999");
                        }else{
                            radiDigChnMulDayTab.setV1431605052Day("999998");
                        }
                        radiDigChnMulDayTabList.add(radiDigChnMulDayTab);

                    }
                    if (!radiDigChnMulHourTabList.isEmpty()) {
                        resultMap.put("radiDigChnMulHourTabList", radiDigChnMulHourTabList);
                    }
                    if (!radiDigChnMulDayTabList.isEmpty()) {
                        resultMap.put("radiDigChnMulDayTabList", radiDigChnMulDayTabList);
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
        if("...".equals(dates)){
            dates="0";
        }
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.indexOf(".") > -1) {
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

    public static void main(String[] args) {
        File file=new File("E:\\filedecode\\digChnMul\\R53068-201804.txt");
        DigChnMulTab dct=new DigChnMulTab();
        dct.decode(file);
    }
}
