package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Chnmrbc;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class DecodeChnClimatc {

    /**
     * 解码结构
     */
    private ParseResult<Chnmrbc> parseResult = new ParseResult<Chnmrbc>(false) ;



    public ParseResult<Chnmrbc> decode(File file) {
        try {
            //获取表格数据
            String itemList = ReadCsv.readNote(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
            String[] list = itemList.split("=#");
            String[] str = list[0].split("#");
            String[] strv = list[2].split("#");
            Chnmrbc chnmrbc = new Chnmrbc();
            String id = UUID.randomUUID().toString().replace("-","");
            chnmrbc.setDRecordId(id);
            for (int i = 0; i < str.length; i++) {
                str[i].replace(" ","");
            }
            String[] strings = str[1].split("/");
            chnmrbc.setDDatetime(new Timestamp(sf.parse(strings[0]).getTime()));
            chnmrbc.setV01301(strings[2]);
            chnmrbc.setV04001(Integer.parseInt(strings[0].substring(0,4)));
            chnmrbc.setV04002(Integer.parseInt(strings[0].substring(4,6)));
            if (strv[2].split("/")[0].equals("01")){
                chnmrbc.setClimatcid(strv[2].split("/")[0]);
                chnmrbc.setClimateCharacterisitics(strv[2].split("/")[1]);
                chnmrbc.setOverallMerit(strv[strv.length-1].split("/")[1]);
            }else if(strv[2].split("/")[0].equals("02")){
                chnmrbc.setMajorDisaster(strv[2].split("/")[1]);
                chnmrbc.setClimatcid(strv[2].split("/")[0]);
                chnmrbc.setClimateCharacterisitics("-");
                chnmrbc.setOverallMerit(strv[strv.length-1].split("/")[1]);
            }else if(strv[2].split("/")[0].equals("03")){
                chnmrbc.setWeatherProcess(strv[2].split("/")[1]);
                chnmrbc.setClimatcid(strv[2].split("/")[0]);
                chnmrbc.setClimateCharacterisitics("-");
                chnmrbc.setOverallMerit(strv[strv.length-1].split("/")[1]);
            }else if(strv[2].split("/")[0].equals("04")){
                chnmrbc.setLongAdverseWeather(strv[2].split("/")[1]);
                chnmrbc.setClimatcid(strv[2].split("/")[0]);
                chnmrbc.setClimateCharacterisitics("-");
                chnmrbc.setOverallMerit(strv[strv.length-1].split("/")[1]);
            }else if(strv[2].split("/")[0].equals("05")){
                chnmrbc.setOverallMerit(strv[2].split("/")[1]);
                chnmrbc.setClimatcid(strv[2].split("/")[0]);
                chnmrbc.setClimateCharacterisitics("-");
            }
            parseResult.put(chnmrbc);
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
