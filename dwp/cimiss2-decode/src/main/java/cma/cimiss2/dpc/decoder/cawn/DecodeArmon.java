package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Armon;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;


import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeArmon {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Armon> parseResult = new ParseResult<Armon>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Armon> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForArmon(file);
            DecimalFormat df = new DecimalFormat("#");
            // 循环读取文件的行
            for (ArrayList<String> items : itemList) {
                Armon armon = new Armon();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    armon.setDRecordId(id);
                    armon.setV01300(Integer.parseInt(items.get(0).replace(".0","")));
                    armon.setCname(items.get(1));
                    armon.setV04002(Short.parseShort(items.get(2).replace(".0","")));
                    armon.setTotalV13011(new BigDecimal(items.get(3)));
                    armon.setTotalV13011Num(new BigDecimal(items.get(4)));
                    armon.setAvgMonthhV15532(new BigDecimal(items.get(5)));
                    armon.setMaxV15532(new BigDecimal(items.get(6)));
                    armon.setMinV15532(new BigDecimal(items.get(7)));
                    armon.setV15532Num45(new BigDecimal(items.get(8)));
                    armon.setV15532Rate45(new BigDecimal(items.get(9)));
                    armon.setV15532Num56(new BigDecimal(items.get(10)));
                    armon.setV15532Rate56(new BigDecimal(items.get(11)));
                    armon.setV15532Num70(new BigDecimal(items.get(12)));
                    armon.setV15532Rate10(new BigDecimal(items.get(13)));
                    armon.setV15532NumM70(new BigDecimal(items.get(14)));
                    armon.setV15532RateM70(new BigDecimal(items.get(15)));
                    armon.setV15532ForecatsRate(new BigDecimal(items.get(16)));
                    armon.setAvgMonthV22381(new BigDecimal(items.get(17)));
                    armon.setMaxV22381(new BigDecimal(items.get(18)));
                    armon.setMinV22381(new BigDecimal(items.get(19)));
                    armon.setV22381Rate(new BigDecimal(items.get(20)));
                    armon.setV15532Num40(new BigDecimal(items.get(21)));
                    armon.setV15532Rate40(new BigDecimal(items.get(22)));
                    armon.setV15532Rain(new BigDecimal(items.get(23)));
                    armon.setV22381Rain(new BigDecimal(items.get(24)));
                    armon.setV15532Num(Integer.parseInt(items.get(25).replace(".0","")));
                    armon.setV22381Num(Integer.parseInt(items.get(26).replace(".0","")));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(armon);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
