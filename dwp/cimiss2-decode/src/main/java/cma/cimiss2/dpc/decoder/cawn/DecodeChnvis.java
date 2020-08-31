package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnvis;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnvis {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Chnvis> parseResult = new ParseResult<Chnvis>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Chnvis> decode(File file, String fileName) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readFile(file);
            // 循环读取文件的行
            for (String[] items : itemList) {
                Chnvis chnvis = new Chnvis();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    chnvis.setDRecordId(id);
                    chnvis.setV01301(items[0]);
                    chnvis.setV05001(new BigDecimal(items[1]));
                    chnvis.setV06001(new BigDecimal(items[2]));
                    chnvis.setV07001(new BigDecimal(items[3]));
                    chnvis.setV04001(Short.parseShort(items[4]));
                    chnvis.setV04002(Short.parseShort(items[5]));
                    chnvis.setV04003(Short.parseShort(items[6]));
                    chnvis.setVisibitlity02(Integer.parseInt(items[7]));
                    chnvis.setVisibitlity08(Integer.parseInt(items[8]));
                    chnvis.setVisibitlity14(Integer.parseInt(items[9]));
                    chnvis.setVisibitlity20(Integer.parseInt(items[10]));
                    chnvis.setQVisibitlity02(Integer.parseInt(items[11]));
                    chnvis.setQVisibitlity08(Integer.parseInt(items[12]));
                    chnvis.setQVisibitlity14(items[13]);
                    chnvis.setQVisibitlity20(items[14]);
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(chnvis);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
