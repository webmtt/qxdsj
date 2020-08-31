package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnwin;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnwin {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Chnwin> parseResult = new ParseResult<Chnwin>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Chnwin> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readFile(file);
            // 循环读取文件的行
            for (String[] items : itemList) {
                Chnwin chnwin = new Chnwin();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    chnwin.setDRecordId(id);
                    chnwin.setV01301(items[0]);
                    chnwin.setV05001(new BigDecimal(items[1]));
                    chnwin.setV06001(new BigDecimal(items[2]));
                    chnwin.setV07001(new BigDecimal(items[3]));
                    chnwin.setV04001(Short.parseShort(items[4]));
                    chnwin.setV04002(Short.parseShort(items[5]));
                    chnwin.setV04003(Short.parseShort(items[6]));
                    chnwin.setV1100102(new BigDecimal(items[7]));
                    chnwin.setV1100202(new BigDecimal(items[8]));
                    chnwin.setV1100108(new BigDecimal(items[9]));
                    chnwin.setV1100208(new BigDecimal(items[10]));
                    chnwin.setV1100114(new BigDecimal(items[11]));
                    chnwin.setV1100214(new BigDecimal(items[12]));
                    chnwin.setV1100120(new BigDecimal(items[13]));
                    chnwin.setV1100220(new BigDecimal(items[14]));
                    chnwin.setQ1100102(new BigDecimal(items[15]));
                    chnwin.setQ1100202(new BigDecimal(items[16]));
                    chnwin.setQ1100108(new BigDecimal(items[17]));
                    chnwin.setQ1100208(new BigDecimal(items[18]));
                    chnwin.setQ1100114(new BigDecimal(items[19]));
                    chnwin.setQ1100214(new BigDecimal(items[20]));
                    chnwin.setQ1100120(new BigDecimal(items[21]));
                    chnwin.setQ1100220(new BigDecimal(items[22]));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(chnwin);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
