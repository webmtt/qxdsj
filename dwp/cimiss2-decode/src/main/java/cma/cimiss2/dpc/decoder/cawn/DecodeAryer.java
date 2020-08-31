package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Aryer;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeAryer {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Aryer> parseResult = new ParseResult<Aryer>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Aryer> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForArmon(file);
            // 循环读取文件的行
            for (ArrayList<String> items : itemList) {
                Aryer aryer = new Aryer();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    aryer.setDRecordId(id);
                    if (items.get(0).contains(",")){
                        aryer.setV13011Year(new BigDecimal(items.get(0).replace(",","")));
                    }else{
                        aryer.setV13011Year(new BigDecimal(items.get(0)));
                    }
                    aryer.setTotalV13011Num(Integer.parseInt(items.get(1).replace(".0","")));
                    aryer.setAvgYearV15532(new BigDecimal(items.get(2)));
                    aryer.setMaxYearV15532(new BigDecimal(items.get(3)));
                    aryer.setMinYearV15532(new BigDecimal(items.get(4)));
                    aryer.setV15532Rate56(new BigDecimal(items.get(5)));
                    aryer.setV15532Rate45(new BigDecimal(items.get(6)));
                    aryer.setAvgYearV22381(new BigDecimal(items.get(7)));
                    if (items.get(8).contains(",")){
                        aryer.setMaxYearV22381(new BigDecimal(items.get(8).replace(",","")));
                    }else{
                        aryer.setMaxYearV22381(new BigDecimal(items.get(8)));
                    }
                    aryer.setMinYearV22381(new BigDecimal(items.get(9)));
                    aryer.setV01300(Integer.parseInt(items.get(10).replace(".0","")));
                    aryer.setCname(items.get(11));
                    aryer.setV04002(items.get(12));
                    if (items.get(13).contains(",")){
                        aryer.setV15532Rain(new BigDecimal(items.get(13).replace(",","")));
                    }else{
                        aryer.setV15532Rain(new BigDecimal(items.get(13)));
                    }
                    aryer.setV15532Num(Integer.parseInt(items.get(15).replace(".0","")));
                    aryer.setV22381Num(Integer.parseInt(items.get(16).replace(".0","")));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(aryer);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
