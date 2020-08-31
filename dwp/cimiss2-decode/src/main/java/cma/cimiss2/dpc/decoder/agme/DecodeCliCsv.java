package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.CliCsv;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class DecodeCliCsv {

    /**
     * 缺测默认值
     */
    public String NANStr = "999999";

    /**
     * 解码结构
     */
    private ParseResult<CliCsv> parseResult = new ParseResult<CliCsv>(false) ;



    public ParseResult<CliCsv> decode(File file) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readCdp(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (String[] items : itemList) {
                CliCsv cliCsv = new CliCsv();
                try {
                    cliCsv.setV01301(items[0]);
                    cliCsv.setV01300(items[1]);
                    cliCsv.setProvinceNM(items[2]);
                    cliCsv.setV06001(Double.parseDouble(items[3]));
                    cliCsv.setV05001(Double.parseDouble(items[4]));
                    cliCsv.setV07001(Double.parseDouble(items[5]));
                    cliCsv.setCropNM(items[6]);
                    cliCsv.setdDatetime(sf.parse(items[7]));
                    cliCsv.setV10004(Double.parseDouble(items[8]));
                    cliCsv.setV1200130(items[9]);
                    cliCsv.setV1200160(items[10]);
                    cliCsv.setV12001150(Double.parseDouble(items[11]));
                    cliCsv.setV12001300(Double.parseDouble(items[12]));
                    cliCsv.setV12001C(items[13]);
                    cliCsv.setV13019(Double.parseDouble(items[14]));
                    cliCsv.setV1129130(Double.parseDouble(items[15]));
                    cliCsv.setV1129160(Double.parseDouble(items[16]));
                    cliCsv.setV11291150(Double.parseDouble(items[17]));
                    cliCsv.setV11291300(Double.parseDouble(items[18]));
                    cliCsv.setV11291600(Double.parseDouble(items[19]));
                    cliCsv.setV13003030(Double.parseDouble(items[20]));
                    cliCsv.setV13003060(Double.parseDouble(items[21]));
                    cliCsv.setV13003150(Double.parseDouble(items[22]));
                    cliCsv.setV13003300(Double.parseDouble(items[23]));
                    cliCsv.setV12030000(Double.parseDouble(items[24]));
                    cliCsv.setV12030005(Double.parseDouble(items[25]));
                    cliCsv.setV12030010(Double.parseDouble(items[26]));
                    cliCsv.setV12030015(Double.parseDouble(items[27]));
                    cliCsv.setV12030020(Double.parseDouble(items[28]));
                    cliCsv.setV12030030(Double.parseDouble(items[29]));
                    cliCsv.setV12030040(Double.parseDouble(items[30]));
                    cliCsv.setV12030080(Double.parseDouble(items[31]));
                    cliCsv.setV71105010(Double.parseDouble(items[32]));
                    cliCsv.setV71105020(Double.parseDouble(items[33]));
                    cliCsv.setV71105030(Double.parseDouble(items[34]));
                    cliCsv.setV71105040(Double.parseDouble(items[35]));
                    cliCsv.setV71105050(Double.parseDouble(items[36]));
                    cliCsv.setV71102010(Double.parseDouble(items[37]));
                    cliCsv.setV71102020(Double.parseDouble(items[38]));
                    cliCsv.setV71102030(Double.parseDouble(items[39]));
                    cliCsv.setV71102040(Double.parseDouble(items[40]));
                    cliCsv.setV71102050(Double.parseDouble(items[41]));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(cliCsv);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
