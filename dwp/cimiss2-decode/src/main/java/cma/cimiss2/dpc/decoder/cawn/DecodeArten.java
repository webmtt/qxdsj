package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Arten;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeArten {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Arten> parseResult = new ParseResult<Arten>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Arten> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForArten(file);
            // 循环读取文件的行
            for (ArrayList<String> items : itemList) {
                Arten arten = new Arten();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    arten.setDRecordId(id);
                    arten.setV04001(Short.parseShort(items.get(0).substring(0,2)));
                    arten.setV04002(Short.parseShort(items.get(0).substring(2,4)));
                    arten.setV04003(Short.parseShort(items.get(0).substring(4,6)));
                    arten.setV13011(new BigDecimal(items.get(1)));
                    arten.setV15532(new BigDecimal(items.get(2)));
                    arten.setV22381(new BigDecimal(items.get(3)));
                    arten.setNa(new BigDecimal(items.get(4)));
                    arten.setK(new BigDecimal(items.get(5)));
                    arten.setMg(new BigDecimal(items.get(6)));
                    arten.setCa(new BigDecimal(items.get(7)));
                    arten.setCl(new BigDecimal(items.get(8)));
                    arten.setNh4(new BigDecimal(items.get(9)));
                    arten.setNo3(new BigDecimal(items.get(10)));
                    arten.setSo4(new BigDecimal(items.get(11)));
                    arten.setF(new BigDecimal(items.get(12)));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(arten);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
