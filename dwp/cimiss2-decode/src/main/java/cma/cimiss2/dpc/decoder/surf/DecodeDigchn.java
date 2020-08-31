package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.Digchn;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DecodeDigchn {

    /**
     * 解码结构
     */
    private ParseResult<Digchn> parseResult = new ParseResult<Digchn>(false) ;



    public ParseResult<Digchn> decode(File file) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readDat(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            int index = 0;
            for (int i= 0; i<itemList.size();i+=15) {
                if(index==itemList.size()){
                    break;
                }else{
                    try {
                        Digchn digchn = new Digchn();
                        if (itemList.get(i)[6].equals("////")){
                            itemList.get(i)[6]="0000";
                        }
                        if(itemList.get(i)[7].equals("//")){
                            itemList.get(i)[7]="00";
                        }
                        if(itemList.get(i)[8].equals("//")){
                            itemList.get(i)[8]="00";
                        }
                        if(itemList.get(i)[9].equals("//")){
                            itemList.get(i)[9]="00";
                        }
                        if(itemList.get(i)[10].equals("//")){
                            itemList.get(i)[10]="00";
                        }
                        digchn.setV01300(Integer.parseInt(itemList.get(i)[0]));
                        digchn.setV01301(itemList.get(i)[0]);
                        digchn.setV04001(Integer.parseInt(itemList.get(i)[1]));
                        digchn.setDDatetime(sf.parse(itemList.get(i)[1]+"01010000"));
                        digchn.setRmax05minutes(Double.parseDouble(itemList.get(i)[6])/10);
                        digchn.setTimeRmax05minutes(new Timestamp(sf.parse(itemList.get(i)[1]+itemList.get(i)[7]+itemList.get(i)[8]+itemList.get(i)[9]+itemList.get(i)[10]).getTime()));
                        digchn.setRmax10minutes(Double.parseDouble(itemList.get(i+1)[6])/10);
                        digchn.setTimeRmax10minutes(new Timestamp(sf.parse(itemList.get(i+1)[1]+itemList.get(i+1)[7]+itemList.get(i+1)[8]+itemList.get(i+1)[9]+itemList.get(i+1)[10]).getTime()));
                        digchn.setRmax15minutes(Double.parseDouble(itemList.get(i+2)[6])/10);
                        digchn.setTimeRmax15minutes(new Timestamp(sf.parse(itemList.get(i+2)[1]+itemList.get(i+2)[7]+itemList.get(i+2)[8]+itemList.get(i+2)[9]+itemList.get(i+2)[10]).getTime()));
                        digchn.setRmax20minutes(Double.parseDouble(itemList.get(i+3)[6])/10);
                        digchn.setTimeRmax20minutes(new Timestamp(sf.parse(itemList.get(i+3)[1]+itemList.get(i+3)[7]+itemList.get(i+3)[8]+itemList.get(i+3)[9]+itemList.get(i+3)[10]).getTime()));
                        digchn.setRmax30minutes(Double.parseDouble(itemList.get(i+4)[6])/10);
                        digchn.setTimeRmax30minutes(new Timestamp(sf.parse(itemList.get(i+4)[1]+itemList.get(i+4)[7]+itemList.get(i+4)[8]+itemList.get(i+4)[9]+itemList.get(i+4)[10]).getTime()));
                        digchn.setRmax45minutes(Double.parseDouble(itemList.get(i+5)[6])/10);
                        digchn.setTimeRmax45minutes(new Timestamp(sf.parse(itemList.get(i+5)[1]+itemList.get(i+5)[7]+itemList.get(i+5)[8]+itemList.get(i+5)[9]+itemList.get(i+5)[10]).getTime()));
                        digchn.setRmax60minutes(Double.parseDouble(itemList.get(i+6)[6])/10);
                        digchn.setTimeRmax60minutes(new Timestamp(sf.parse(itemList.get(i+6)[1]+itemList.get(i+6)[7]+itemList.get(i+6)[8]+itemList.get(i+6)[9]+itemList.get(i+6)[10]).getTime()));
                        digchn.setRmax90minutes(Double.parseDouble(itemList.get(i+7)[6])/10);
                        digchn.setTimeRmax90minutes(new Timestamp(sf.parse(itemList.get(i+7)[1]+itemList.get(i+7)[7]+itemList.get(i+7)[8]+itemList.get(i+7)[9]+itemList.get(i+7)[10]).getTime()));
                        digchn.setRmax120minutes(Double.parseDouble(itemList.get(i+8)[6])/10);
                        digchn.setTimeRmax120minutes(new Timestamp(sf.parse(itemList.get(i+8)[1]+itemList.get(i+8)[7]+itemList.get(i+8)[8]+itemList.get(i+8)[9]+itemList.get(i+8)[10]).getTime()));
                        digchn.setRmax180minutes(Double.parseDouble(itemList.get(i+9)[6])/10);
                        digchn.setTimeRmax180minutes(new Timestamp(sf.parse(itemList.get(i+9)[1]+itemList.get(i+9)[7]+itemList.get(i+9)[8]+itemList.get(i+9)[9]+itemList.get(i+9)[10]).getTime()));
                        digchn.setRmax240minutes(Double.parseDouble(itemList.get(i+10)[6])/10);
                        digchn.setTimeRmax240minutes(new Timestamp(sf.parse(itemList.get(i+10)[1]+itemList.get(i+10)[7]+itemList.get(i+10)[8]+itemList.get(i+10)[9]+itemList.get(i+10)[10]).getTime()));
                        digchn.setRmax360minutes(Double.parseDouble(itemList.get(i+11)[6])/10);
                        digchn.setTimeRmax360minutes(new Timestamp(sf.parse(itemList.get(i+11)[1]+itemList.get(i+11)[7]+itemList.get(i+11)[8]+itemList.get(i+11)[9]+itemList.get(i+11)[10]).getTime()));
                        digchn.setRmax540minutes(Double.parseDouble(itemList.get(i+12)[6])/10);
                        digchn.setTimeRmax540minutes(new Timestamp(sf.parse(itemList.get(i+12)[1]+itemList.get(i+12)[7]+itemList.get(i+12)[8]+itemList.get(i+12)[9]+itemList.get(i+12)[10]).getTime()));
                        digchn.setRmax720minutes(Double.parseDouble(itemList.get(i+13)[6])/10);
                        digchn.setTimeRmax720minutes(new Timestamp(sf.parse(itemList.get(i+13)[1]+itemList.get(i+13)[7]+itemList.get(i+13)[8]+itemList.get(i+13)[9]+itemList.get(i+13)[10]).getTime()));
                        digchn.setRmax1440minutes(Double.parseDouble(itemList.get(i+14)[6])/10);
                        digchn.setTimeRmax1440minutes(new Timestamp(sf.parse(itemList.get(i+14)[1]+itemList.get(i+14)[7]+itemList.get(i+14)[8]+itemList.get(i+14)[9]+itemList.get(i+14)[10]).getTime()));
                        parseResult.put(digchn);
                    } catch (NumberFormatException e) {
                        ReportError re = new ReportError();
                        re.setMessage("数字转换异常");
                        re.setSegment(lineTxt);
                        re.setPositionx(positionx);
                        parseResult.put(re);
                        continue;
                    }
                }
                positionx ++;
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
