package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.Multl;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeMuldy {

    /**
     * 解码结构
     */
    private ParseResult<Multl> parseResult = new ParseResult<Multl>(false) ;



    public ParseResult<Multl> decode(byte[] data,String fileCode) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readCsv(data,fileCode);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (String[] items : itemList) {
                Multl multl = new Multl();
                try {
                    multl.setId(items[0]);
                    multl.setObservationDataDate(new java.sql.Timestamp(sf.parse(items[1]).getTime()));
                    multl.setLayer(items[2]);
                    multl.setStationNumber(items[3]);
                    multl.setCommandName(items[4]);
                    multl.setCreateDate(new java.sql.Timestamp(sf.parse(items[5]).getTime()));
                    multl.setAveWd2min(Integer.parseInt(items[6]));
                    multl.setAveWs2min(Double.parseDouble(items[7]));
                    multl.setAveWd10min(Integer.parseInt(items[8]));
                    multl.setAveWs10min(Double.parseDouble(items[9]));
                    multl.setMostWd(Integer.parseInt(items[10]));
                    multl.setMostWs(Double.parseDouble(items[11]));
                    multl.setMostWsDate(new java.sql.Timestamp(sf.parse(items[12]).getTime()));
                    multl.setInstWd(Integer.parseInt(items[13]));
                    multl.setInstWs(Double.parseDouble(items[14]));
                    multl.setMaxWd(Integer.parseInt(items[15]));
                    multl.setMaxWs(Double.parseDouble(items[16]));
                    multl.setMaxWsDate(new java.sql.Timestamp(sf.parse(items[17]).getTime()));
                    multl.setBucketRainCount(Integer.parseInt(items[18]));
                    multl.setWeightRainCount(items[19]);
                    multl.setTemp(Double.parseDouble(items[20]));
                    multl.setMostTemp(Double.parseDouble(items[21]));
                    multl.setMostTempDate(new java.sql.Timestamp(sf.parse(items[22]).getTime()));
                    multl.setLeastTemp(Double.parseDouble(items[23]));
                    multl.setLeastTempDate(new java.sql.Timestamp(sf.parse(items[24]).getTime()));
                    multl.setWetBallTemp(items[25]);
                    multl.setRelaHumi(items[26]);
                    multl.setLeastRelaHumi(items[27]);
                    multl.setLeastRelaHumiDate(items[28]);
                    multl.setVaporPres(items[29]);
                    multl.setExpoPointTemp(items[30]);
                    multl.setPres(items[31]);
                    multl.setMostPres(items[32]);
                    multl.setMostPresDate(items[33]);
                    multl.setLeastPres(items[34]);
                    multl.setLeastPresDate(items[35]);
                    multl.setGrassTemp(items[36]);
                    multl.setMostGrassTemp(items[37]);

                    multl.setMostGrassTempDate(items[38]);
                    multl.setLeastGrassTemp(items[39]);
                    multl.setLeastGrassTempDate(items[40]);
                    multl.setLandPtTemp(items[41]);
                    multl.setMostLandPtTemp(items[42]);
                    multl.setMostLandPtTempDate(items[43]);
                    multl.setLeastLandPtTemp(items[44]);
                    multl.setLeastLandPtTempDate(items[45]);
                    multl.setLandIrTemp(items[46]);
                    multl.setMostLandIrTemp(items[47]);
                    multl.setMostLandIrTempDate(items[48]);
                    multl.setLeastLandIrTemp(items[49]);
                    multl.setLeastLandIrTempDate(items[50]);

                    multl.setLand5cmTemp(items[51]);
                    multl.setLand10cmTemp(items[52]);
                    multl.setLand15cmTemp(items[53]);
                    multl.setLand20cmTemp(items[54]);
                    multl.setLand30cmTemp(items[55]);
                    multl.setLand40cmTemp(items[56]);
                    multl.setLand50cmTemp(items[57]);
                    multl.setLand80cmTemp(items[58]);
                    multl.setLand160cmTemp(items[59]);
                    multl.setLand320cmTemp(items[60]);

                    multl.setEvaporation(items[61]);
                    multl.setSunExposure(items[62]);
                    multl.setVisibility(items[63]);
                    multl.setLeastVisibility(items[64]);
                    multl.setLeastVisibilityDate(items[65]);
                    multl.setCloudLevel(items[66]);
                    multl.setTotalCloudCoverage(items[67]);
                    multl.setCurrentMeteoCode(items[68]);

                    multl.setExt1(items[69]);
                    multl.setExt2(items[70]);
                    multl.setExt3(items[71]);
                    multl.setExt4(items[72]);
                    multl.setExt5(items[73]);
                    multl.setSeaLevelPres(items[74]);
                    multl.setBucketAccRainCount(items[75]);
                    multl.setAccEvaporation(items[76]);

                    multl.setAccSunExposureTime(items[77]);
                    multl.setWeightAccRainCount(items[78]);
                    multl.setBucketMinRainCnt(items[79]);
                    multl.setWeightMinRainCnt(items[80]);
                    multl.setAveWd1min(items[81]);
                    multl.setAveWs1min(items[82]);
                    multl.setAccMeteoCode(items[83]);
                    multl.setAccMeteoCode15(items[84]);

                    multl.setDayAccRainCnt(items[85]);
                    multl.setBucketRainCount2(items[86]);
                    multl.setBucketAccRainCount2(items[87]);
                    multl.setBucketMinRainCnt2(items[88]);
                    multl.setEvaWl(items[89]);
                    multl.setLowCc(items[90]);
                    multl.setSnowDepth(items[91]);
                    multl.setFrozenRain(items[92]);

                    multl.setWireIat(items[93]);
                    multl.setFrozenSd(items[94]);
                    multl.setLightningFreq(items[95]);
                    multl.setCtemp(items[96]);
                    multl.setMostCtemp(items[97]);
                    multl.setMostCtempDate(items[98]);
                    multl.setLeastCtemp(items[99]);
                    multl.setLeastCtempDate(items[100]);

                    multl.setWsVentilation(items[101]);
                    multl.setAveWs2min15(items[102]);
                    multl.setAveWs10min15(items[103]);
                    multl.setMostWs15(items[104]);
                    multl.setMostWsDate15(items[105]);
                    multl.setMaxWs1min15(items[106]);
                    multl.setMaxWs15(items[107]);
                    multl.setMaxWsDate15(items[108]);

                    multl.setAve10minVisi(items[109]);
                    multl.setQcStrBr(items[110]);
                    multl.setQcStrB2r(items[111]);
                    multl.setQcStrWr(items[112]);
                    multl.setLeastVisi10(items[113]);
                    multl.setLeastVisi10Date(items[114]);
                    multl.setInstW1d(Integer.parseInt(items[115]));
                    multl.setInstW1s(Double.parseDouble(items[116]));
                    multl.setNegOions(items[117]);
                    multl.setPosOions(items[118]);
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
