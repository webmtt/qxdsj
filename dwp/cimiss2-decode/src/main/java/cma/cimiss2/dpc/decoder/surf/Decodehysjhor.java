package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Hysjhor;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.util.ArrayList;

public class Decodehysjhor {

    /**
     * 解码结构
     */
    private ParseResult<Hysjhor> parseResult = new ParseResult<Hysjhor>(false) ;



    public ParseResult<Hysjhor> decode(File file) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readTxt(file);
            System.out.println(itemList);
            String[] item = itemList.get(0);
            String[] items = itemList.get(1);
            Hysjhor hysjhor = new Hysjhor();
            hysjhor.setStationnum(item[0]);
            hysjhor.setLatitude(Double.parseDouble(item[1]));
            hysjhor.setLongitude(Double.parseDouble(item[2]));
            hysjhor.setGroundHeight(Integer.parseInt(item[3]));
            hysjhor.setBarometerHeight(Integer.parseInt(item[4]));
            hysjhor.setManualOrAutomatic(Integer.parseInt(item[5]));
            hysjhor.setObserveTime(items[0]);
            hysjhor.setF2Dir(Double.parseDouble(items[1]));
            hysjhor.setF2Speed(Double.parseDouble(items[2]));
            hysjhor.setF10Dir(Double.parseDouble(items[3]));
            hysjhor.setF10Speed(Double.parseDouble(items[4]));
            hysjhor.setFMaxDir(Double.parseDouble(items[5]));
            hysjhor.setFMaxSpeed(Double.parseDouble(items[6]));

            hysjhor.setFMaxSpeedTime(Integer.parseInt(items[7]));
            hysjhor.setF0Dir(Double.parseDouble(items[8]));
            hysjhor.setF0Speed(Double.parseDouble(items[9]));
            hysjhor.setFGreatDir(Double.parseDouble(items[10]));
            hysjhor.setFGreatSpeed(Double.parseDouble(items[11]));
            hysjhor.setFGreatSpeedTime(Integer.parseInt(items[12]));

            hysjhor.setR(Double.parseDouble(items[13]));
            hysjhor.setT(Double.parseDouble(items[14]));
            hysjhor.setTMax(Double.parseDouble(items[15]));
            hysjhor.setTMaxTime(Integer.parseInt(items[16]));
            hysjhor.setTMin(Double.parseDouble(items[17]));
            hysjhor.setTMinTime(Integer.parseInt(items[18]));

            hysjhor.setU(Double.parseDouble((items[19])));
            hysjhor.setUMin(Double.parseDouble(items[20]));
            hysjhor.setUMinTime(Integer.parseInt(items[21]));
            hysjhor.setE(Double.parseDouble(items[22]));
            hysjhor.setTd(Double.parseDouble(items[23]));
            hysjhor.setP(Double.parseDouble(items[24]));

            hysjhor.setPMax(Double.parseDouble((items[25])));
            hysjhor.setPMaxTime(Integer.parseInt(items[26]));
            hysjhor.setPMin(Double.parseDouble(items[27]));
            hysjhor.setPMinTime(Integer.parseInt(items[28]));
            hysjhor.setB(Double.parseDouble(items[29]));
            hysjhor.setBMax(Double.parseDouble(items[30]));

            hysjhor.setBMaxTime(Integer.parseInt((items[31])));
            hysjhor.setBMin(Double.parseDouble(items[32]));
            hysjhor.setBMinTime(Integer.parseInt(items[33]));
            hysjhor.setD0(Double.parseDouble(items[34]));
            hysjhor.setD0Max(Double.parseDouble(items[35]));
            hysjhor.setD0MaxTime(Integer.parseInt(items[36]));
            hysjhor.setD0Min(Double.parseDouble(items[37]));
            hysjhor.setD0MinTime(Integer.parseInt(items[38]));

            hysjhor.setD5(Double.parseDouble((items[39])));
            hysjhor.setD10(Double.parseDouble(items[40]));
            hysjhor.setD15(Double.parseDouble(items[41]));
            hysjhor.setD20(Double.parseDouble(items[42]));
            hysjhor.setD40(Double.parseDouble(items[43]));
            hysjhor.setD80(Double.parseDouble(items[44]));
            hysjhor.setD160(Double.parseDouble(items[45]));
            hysjhor.setD320(Double.parseDouble(items[46]));

            hysjhor.setL(Double.parseDouble((items[47])));
            hysjhor.setPSea(Double.parseDouble(items[48]));
            hysjhor.setV(Integer.parseInt(items[49]));
            hysjhor.setVTime(Integer.parseInt(items[50]));
            hysjhor.setVMinTime(Integer.parseInt(items[51]));
            parseResult.put(hysjhor);
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
