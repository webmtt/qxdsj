package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.SandWind;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeSandWind {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<SandWind> parseResult = new ParseResult<SandWind>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<SandWind> decode(File file) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForWind(file);
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                SandWind sndWind = new SandWind();
                try {
                    int i = 0;
                    sndWind.setV01300(Double.parseDouble(items.get(0)));
                    sndWind.setV01301(items.get(1));
                    sndWind.setdUpdateTime(sf.parse(items.get(2)));
                    if("".equals(items.get(3))){
                        sndWind.setA00401(0.0);
                    }else{
                        sndWind.setA00401(Double.parseDouble(items.get(3)));
                    }
                    if("".equals(items.get(4))){
                        sndWind.setA00403(0.0);
                    }else{
                        sndWind.setA00403(Double.parseDouble(items.get(4)));
                    }
                    if("".equals(items.get(5))){
                        sndWind.setA00402(0.0);
                    }else{
                        sndWind.setA00402(Double.parseDouble(items.get(5)));
                    }
                    if("".equals(items.get(6))){
                        sndWind.setA00404(0.0);
                    }else{
                        sndWind.setA00404(Double.parseDouble(items.get(6)));
                    }
                    sndWind.setA00007(items.get(7));
                    sndWind.setdDatetime(sf.parse(items.get(8)));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(sndWind);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
