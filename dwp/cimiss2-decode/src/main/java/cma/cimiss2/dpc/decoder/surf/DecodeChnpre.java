package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.ChnPre;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;
import org.nutz.dao.impl.sql.callback.FetchTimestampCallback;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DecodeChnpre {

    /**
     * 缺测默认值
     */
    public String NANStr = "9999";

    /**
     * 解码结构
     */
    private ParseResult<ChnPre> parseResult = new ParseResult<ChnPre>(false) ;


    public ParseResult<ChnPre> decode(File file) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readDat(file);
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm");
            SimpleDateFormat sdd = new SimpleDateFormat("HHmm");
            System.out.println(itemList);
            String lineTxt = null;
            String fileName = file.getName();
            int etTime = 0;
            // 循环读取文件的行
            int positionx = 1;
            for (String[] items : itemList) {
                try {
                    if(fileName.contains("R01")) {
                        if (!"2000".equals(items[7])) {
                            for (int i = 0; i < items.length; i++) {
                                if ("0".equals(items[i])) {
                                    int startTime = Integer.parseInt(items[i + 1]);
                                    int endTime = Integer.parseInt(items[i + 2]);
                                    if (startTime - endTime == 0) {
                                        ChnPre chnPre = new ChnPre();
                                        chnPre.setDRECORDID(UUID.randomUUID().toString().replace("-", ""));
                                        chnPre.setDIYMDHM(new Timestamp(new Date().getTime()));
                                        //chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + items[2]+items[i + 1]).getTime()));
                                        chnPre.setDUPDATETIME(new Timestamp(new Date().getTime()));
                                        //chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + items[2]+items[i + 1]).getTime()));
                                        chnPre.setV01300(Integer.parseInt(fileName.substring(3, 8)));
                                        chnPre.setV01301(fileName.substring(3, 8));
                                        chnPre.setV05001(49.58);
                                        chnPre.setV06001(117.32);
                                        chnPre.setV07001(661.8);
                                        chnPre.setV02001(0);
                                        chnPre.setV02301(14);
                                        chnPre.setVACODE(150000);
                                        chnPre.setV04001(Integer.parseInt(items[0]));
                                        chnPre.setV04002(Integer.parseInt(items[1]));
                                        //chnPre.setV04003(Integer.parseInt(items[2]));
                                        chnPre.setQ13011(Integer.parseInt(items[3]));
                                        if(Integer.parseInt(items[i + 1].substring(0, 2))>=20){
                                            chnPre.setV04003(Integer.parseInt(items[2])-1);
                                            chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + String.valueOf(Integer.parseInt(items[2])-1)+items[i + 1]).getTime()));
                                            chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + String.valueOf(Integer.parseInt(items[2])-1)+items[i + 1]).getTime()));
                                        }else{
                                            chnPre.setV04003(Integer.parseInt(items[2]));
                                            chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + items[2]+items[i + 1]).getTime()));
                                            chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + items[2]+items[i + 1]).getTime()));
                                        }
                                        chnPre.setV04004(Integer.parseInt(items[i + 1].substring(0, 2)));
                                        chnPre.setV04005(Integer.parseInt(items[i + 1].substring(2, 4)));
                                        chnPre.setV13011(Double.parseDouble(items[i + 3])/10);
                                        chnPre.setVBBB("000");
                                        parseResult.put(chnPre);
                                    } else if (endTime - startTime > 0) {
                                        etTime = (Integer.parseInt(items[i + 2].substring(0, 2)) * 60 + Integer.parseInt(items[i + 2].substring(2, 4)))
                                                - (Integer.parseInt(items[i + 1].substring(0, 2)) * 60 + Integer.parseInt(items[i + 1].substring(2, 4)));
                                        //int aa = (int)(sdd.parse(items[i + 2]).getTime()-sdd.parse(items[i + 1]).getTime())/(1000*60);
                                        for (int j = 0; j < etTime+1; j++) {
                                            ChnPre chnPre = new ChnPre();
                                            chnPre.setDRECORDID(UUID.randomUUID().toString().replace("-", ""));
                                            chnPre.setDIYMDHM(new Timestamp(new Date().getTime()));
                                            chnPre.setDUPDATETIME(new Timestamp(new Date().getTime()));
                                            chnPre.setV01300(Integer.parseInt(fileName.substring(3, 8)));
                                            chnPre.setV01301(fileName.substring(3, 8));
                                            chnPre.setV05001(49.58);
                                            chnPre.setV06001(117.32);
                                            chnPre.setV07001(661.8);
                                            chnPre.setV02001(0);
                                            chnPre.setV02301(14);
                                            chnPre.setVACODE(150000);
                                            chnPre.setV04001(Integer.parseInt(items[0]));
                                            chnPre.setV04002(Integer.parseInt(items[1]));
                                            chnPre.setQ13011(Integer.parseInt(items[3]));
                                            Calendar calendar = Calendar.getInstance();
                                            long dd= sd.parse(items[0]+items[1]+items[2]+items[i + 1]).getTime()+60000*(j);
                                            calendar.setTimeInMillis(dd);
                                            String time = sd.format(calendar.getTime());
                                            if(Integer.parseInt(time.substring(8, 10))>=20){
                                                chnPre.setV04003(Integer.parseInt(items[2])-1);
                                                chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + String.valueOf(Integer.parseInt(items[2])-1)+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                                chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + String.valueOf(Integer.parseInt(items[2])-1)+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                            }else{
                                                chnPre.setV04003(Integer.parseInt(items[2]));
                                                chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + items[2]+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                                chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + items[2]+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                            }
                                            chnPre.setV04004(Integer.parseInt(time.substring(8, 10)));
                                            chnPre.setV04005(Integer.parseInt(time.substring(10, 12)));
                                            chnPre.setV13011(Double.parseDouble(items[i + 3 + j])/10);
                                            chnPre.setVBBB("000");
                                            parseResult.put(chnPre);
                                        }
                                    } else if (endTime - startTime < 0){
                                        etTime = (Integer.parseInt(items[i + 2].substring(0, 2) + 24) * 60 + Integer.parseInt(items[i + 2].substring(2, 4)))
                                                - (Integer.parseInt(items[i + 1].substring(0, 2)) * 60 + Integer.parseInt(items[i + 1].substring(2, 4)));
                                        for (int z = 0; z < etTime+1; z++) {
                                            ChnPre chnPre = new ChnPre();
                                            chnPre.setDRECORDID(UUID.randomUUID().toString().replace("-", ""));
                                            chnPre.setDIYMDHM(new Timestamp(new Date().getTime()));
                                            chnPre.setDUPDATETIME(new Timestamp(new Date().getTime()));
                                            chnPre.setV01300(Integer.parseInt(fileName.substring(3, 8)));
                                            chnPre.setV01301(fileName.substring(3, 8));
                                            chnPre.setV05001(49.58);
                                            chnPre.setV06001(117.32);
                                            chnPre.setV07001(661.8);
                                            chnPre.setV02001(0);
                                            chnPre.setV02301(14);
                                            chnPre.setVACODE(150000);
                                            chnPre.setV04001(Integer.parseInt(items[0]));
                                            chnPre.setV04002(Integer.parseInt(items[1]));
                                            chnPre.setQ13011(Integer.parseInt(items[3]));
                                            Calendar calendar = Calendar.getInstance();
                                            long dd= sd.parse(items[0]+items[1]+items[2]+items[i + 1]).getTime()+60000*(z);
                                            calendar.setTimeInMillis(dd);
                                            String time = sd.format(calendar.getTime());
                                            if(Integer.parseInt(time.substring(8, 10))>=20){
                                                chnPre.setV04003(Integer.parseInt(items[2])-1);
                                                chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + String.valueOf(Integer.parseInt(items[2])-1)+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                                chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + String.valueOf(Integer.parseInt(items[2])-1)+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                            }else{
                                                chnPre.setV04003(Integer.parseInt(items[2]));
                                                chnPre.setDRYMDHM(new Timestamp(sd.parse(items[0] + items[1] + items[2]+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                                chnPre.setDDATETIME(new Timestamp(sd.parse(items[0] + items[1] + items[2]+time.substring(8, 10)+time.substring(10, 12)).getTime()));
                                            }
                                            chnPre.setV04004(Integer.parseInt(time.substring(8, 10)));
                                            chnPre.setV04005(Integer.parseInt(time.substring(10, 12)));
                                            chnPre.setV13011(Double.parseDouble(items[i + 3 + z])/10);
                                            chnPre.setVBBB("000");
                                            parseResult.put(chnPre);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
