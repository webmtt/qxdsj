package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnrhu;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnrhu {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Chnrhu> parseResult = new ParseResult<Chnrhu>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Chnrhu> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readFile(file);
            // 循环读取文件的行
            for (String[] items : itemList) {
                Chnrhu chnrhu = new Chnrhu();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    chnrhu.setDRecordId(id);
                    chnrhu.setV01301(items[0]);
                    chnrhu.setV05001(new BigDecimal(items[1]));
                    chnrhu.setV06001(new BigDecimal(items[2]));
                    chnrhu.setV07001(new BigDecimal(items[3]));
                    chnrhu.setV04001(Short.parseShort(items[4]));
                    chnrhu.setV04002(Short.parseShort(items[5]));
                    chnrhu.setV04003(Short.parseShort(items[6]));
                    chnrhu.setV1300302(Integer.parseInt(items[7]));
                    chnrhu.setV1300308(Integer.parseInt(items[8]));
                    chnrhu.setV1300314(Integer.parseInt(items[9]));
                    chnrhu.setV1300320(Integer.parseInt(items[10]));
                    chnrhu.setQ1300302(items[11]);
                    chnrhu.setQ1300308(items[12]);
                    chnrhu.setQ1300314(items[13]);
                    chnrhu.setQ1300320(items[14]);
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(chnrhu);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
