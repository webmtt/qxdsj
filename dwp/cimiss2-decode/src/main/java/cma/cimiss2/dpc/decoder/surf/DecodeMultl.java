package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.Muldy;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeMultl {

    /**
     * 解码结构
     */
    private ParseResult<Muldy> parseResult = new ParseResult<Muldy>(false) ;



    public ParseResult<Muldy> decode(byte[] data,String fileCode) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readCsv(data,fileCode);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (String[] items : itemList) {
                Muldy multl = new Muldy();
                try {
                    multl.setObservationDataDate(new java.sql.Timestamp(sf.parse(items[0]).getTime()));
                    multl.setStationNumber(items[1]);
                    multl.setLongitudeFv(Double.parseDouble(items[2]));
                    multl.setLatitudeFv(Double.parseDouble(items[3]));
                    multl.setObservationPlaceEvaluation(Double.parseDouble(items[4]));
                    if (items[5].equals("NULL")){
                        items[5]="0.0";
                    }
                    multl.setTemp(Double.parseDouble(items[5]));
                    if (items[6].equals("NULL")){
                        items[6]="0.0";
                    }
                    multl.setPres(Double.parseDouble(items[6]));
                    if (items[7].equals("NULL")){
                        items[7]="0.0";
                    }
                    multl.setAveWd2min(Double.parseDouble(items[7]));
                    if (items[8].equals("NULL")){
                        items[8]="0.0";
                    }
                    multl.setAveWs2min(Double.parseDouble(items[8]));
                    if (items[9].equals("NULL")){
                        items[9]="0.0";
                    }
                    multl.setWeightAccRainCount(Double.parseDouble(items[9]));
                    if (items[10].equals("NULL")){
                        items[10]="0.0";
                    }
                    multl.setRelaHumi(Double.parseDouble(items[10]));
                    if (items[11].equals("NULL")){
                        items[11]="0.0";
                    }
                    multl.setVisibility(Double.parseDouble(items[11]));
                    multl.setSnowDepth(items[12]);
                    if (items[13].equals("NULL")){
                        items[13]="0.0";
                    }
                    multl.setLand5cmTemp(Double.parseDouble(items[13]));
                    if (items[14].equals("NULL")){
                        items[14]="0.0";
                    }
                    multl.setLand10cmTemp(Double.parseDouble(items[14]));
                    multl.setGrI(items[15]);
                    multl.setT1(items[16]);
                    multl.setT4(items[17]);
                    multl.setT5(items[18]);
                    multl.setT6(items[19]);
                    if (items[20].equals("NULL")){
                        items[20]="0.0";
                    }
                    multl.setAveWd10min(Double.parseDouble(items[20]));
                    if (items[21].equals("NULL")){
                        items[21]="0.0";
                    }
                    multl.setAveWs10min(Double.parseDouble(items[21]));
                    multl.setAveWs2min15(items[22]);
                    multl.setAveWs10min15(items[23]);
                    if (items[24].equals("NULL")){
                        items[24]="0.0";
                    }
                    multl.setLandIrTemp(Double.parseDouble(items[24]));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(multl);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
