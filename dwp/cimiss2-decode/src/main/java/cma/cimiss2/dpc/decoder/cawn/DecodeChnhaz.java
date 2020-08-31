package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnhaz;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnhaz {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Chnhaz> parseResult = new ParseResult<Chnhaz>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Chnhaz> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readFile(file);
            // 循环读取文件的行
            for (String[] items : itemList) {
                Chnhaz chnhaz = new Chnhaz();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    chnhaz.setDRecordId(id);
                    chnhaz.setV01301(items[0]);
                    chnhaz.setV05001(new BigDecimal(items[1]));
                    chnhaz.setV06001(new BigDecimal(items[2]));
                    chnhaz.setV07001(new BigDecimal(items[3]));
                    chnhaz.setV04001(Short.parseShort(items[4]));
                    chnhaz.setV04002(Short.parseShort(items[5]));
                    chnhaz.setV04003(Short.parseShort(items[6]));
                    chnhaz.setV20302005(Integer.parseInt(items[7]));
                    chnhaz.setQ20302005(Short.parseShort(items[8]));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(chnhaz);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
