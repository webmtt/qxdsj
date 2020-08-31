package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Arday;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeArday {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Arday> parseResult = new ParseResult<Arday>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Arday> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForAr(file);
            // 循环读取文件的行
            for (ArrayList<String> items : itemList) {
                Arday arday = new Arday();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    arday.setDRecordId(id);
                    arday.setV01300(Integer.parseInt(items.get(0).substring(0,1)));
                    arday.setCname(items.get(1));
                    arday.setV04002(Short.parseShort(items.get(2).substring(0,1)));
                    if (items.get(5).contains(".")){
                        items.get(5).replace(".","");
                    }
                    arday.setV04307(items.get(2).substring(0,1)+items.get(3).substring(0,1)+items.get(5));
                    if (items.get(6).contains(".")){
                        items.get(6).replace(".",":");
                    }
                    arday.setV04308(items.get(2)+"月"+items.get(4)+"日"+items.get(6)+"时");
                    arday.setV15532(new BigDecimal(items.get(7)));
                    arday.setV22381(new BigDecimal(items.get(8)));
                    arday.setWaterV12001(new BigDecimal(items.get(9)));
                    arday.setV13011(new BigDecimal(items.get(10)));
                    if (items.size()<13){
                        arday.setRemarks(0);
                    }else{
                        arday.setRemarks(Integer.parseInt(items.get(12)));
                    }
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(arday);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
