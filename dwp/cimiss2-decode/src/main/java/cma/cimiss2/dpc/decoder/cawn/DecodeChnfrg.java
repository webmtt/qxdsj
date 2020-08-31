package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnfrg;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnfrg {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Chnfrg> parseResult = new ParseResult<Chnfrg>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Chnfrg> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readFile(file);
            // 循环读取文件的行
            for (String[] items : itemList) {
                Chnfrg chnfrg = new Chnfrg();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    chnfrg.setDRecordId(id);
                    chnfrg.setV01301(items[0]);
                    chnfrg.setV05001(new BigDecimal(items[1]));
                    chnfrg.setV06001(new BigDecimal(items[2]));
                    chnfrg.setV07001(new BigDecimal(items[3]));
                    chnfrg.setV04001(Short.parseShort(items[4]));
                    chnfrg.setV04002(Short.parseShort(items[5]));
                    chnfrg.setV04003(Short.parseShort(items[6]));
                    chnfrg.setV20302010(Integer.parseInt(items[7]));
                    chnfrg.setV20302042(Integer.parseInt(items[8]));
                    chnfrg.setQ20302010(Short.parseShort(items[9]));
                    chnfrg.setQ20302042(Short.parseShort(items[10]));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(chnfrg);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
