package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.ChnDst;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnDst {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<ChnDst> parseResult = new ParseResult<ChnDst>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<ChnDst> decode(File file,String fileName) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readFile(file);
            // 循环读取文件的行
            for (String[] items : itemList) {
                ChnDst chnDst = new ChnDst();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    chnDst.setDRecordId(id);
                    if(fileName.contains("DSS")){
                       chnDst.setV01300(Integer.parseInt(items[0]));
                       chnDst.setV06001(new BigDecimal(items[1]));
                       chnDst.setV05001(new BigDecimal(items[2]));
                       chnDst.setV07001(new BigDecimal(items[3]));
                       chnDst.setV04001(Short.parseShort(items[4]));
                       chnDst.setV04002(Short.parseShort(items[5]));
                       chnDst.setV04003(Short.parseShort(items[6]));
                       chnDst.setV20302031(Integer.parseInt(items[7]));
                       chnDst.setV20302007(Integer.parseInt(items[8]));
                       chnDst.setV20302006(Integer.parseInt(items[9]));
                       chnDst.setQ20302031(Short.parseShort(items[10]));
                       chnDst.setQ20302007(Short.parseShort(items[11]));
                       chnDst.setQ20302006(Short.parseShort(items[12]));
                    }
                    if(fileName.contains("VIS")){
                        chnDst.setV01300(Integer.parseInt(items[0]));
                        chnDst.setV06001(new BigDecimal(items[1]));
                        chnDst.setV05001(new BigDecimal(items[2]));
                        chnDst.setV07001(new BigDecimal(items[3]));
                        chnDst.setV04001(Short.parseShort(items[4]));
                        chnDst.setV04002(Short.parseShort(items[5]));
                        chnDst.setV04003(Short.parseShort(items[6]));
                        chnDst.setV05002(new BigDecimal(items[7]));
                        chnDst.setV06008(new BigDecimal(items[8]));
                        chnDst.setV07014(new BigDecimal(items[9]));
                        chnDst.setV02001(Integer.parseInt(items[10]));
                        chnDst.setQ05001(Short.parseShort(items[11]));
                        chnDst.setQ06001(Short.parseShort(items[12]));
                        chnDst.setQ07001(Short.parseShort(items[13]));
                        chnDst.setQ02001(Short.parseShort(items[14]));
                    }
                    if(fileName.contains("Wmax")){
                        chnDst.setV01300(Integer.parseInt(items[0]));
                        chnDst.setV06001(new BigDecimal(items[1]));
                        chnDst.setV05001(new BigDecimal(items[2]));
                        chnDst.setV07001(new BigDecimal(items[3]));
                        chnDst.setV04001(Short.parseShort(items[4]));
                        chnDst.setV04002(Short.parseShort(items[5]));
                        chnDst.setV04003(Short.parseShort(items[6]));
                        chnDst.setV11042(new BigDecimal(items[7]));
                        chnDst.setV11296(new BigDecimal(items[8]));
                        chnDst.setV11046(new BigDecimal(items[9]));
                        chnDst.setV11211(new BigDecimal(items[10]));
                        chnDst.setQ11042(Short.parseShort(items[11]));
                        chnDst.setQ11296(Short.parseShort(items[12]));
                        chnDst.setQ11046(Short.parseShort(items[13]));
                        chnDst.setQ11211(Short.parseShort(items[14]));
                    }
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(chnDst);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
