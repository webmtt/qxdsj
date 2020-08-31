package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.SandMove;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeSandMove {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<SandMove> parseResult = new ParseResult<SandMove>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<SandMove> decode(File file) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForSend(file);
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                SandMove sandMove = new SandMove();
                try {
                    int i = 0;
                    sandMove.setV01300(Double.parseDouble(items.get(0)));
                    sandMove.setV01301(items.get(1));
                    sandMove.setdUpdateTime(sf.parse(items.get(2)));
                    sandMove.setV06001(ReadCsv.Dms2D(items.get(3)));
                    sandMove.setV05001(ReadCsv.Dms2D(items.get(4)));
                    sandMove.setV07001(Double.parseDouble(items.get(5)));
                    sandMove.setA07002(Double.parseDouble(items.get(6)));
                    sandMove.setA00301(Double.parseDouble(items.get(7)));
                    sandMove.setA00303(Double.parseDouble(items.get(8)));
                    sandMove.setA00305(Double.parseDouble(items.get(9)));
                    sandMove.setA00306(items.get(10));
                    sandMove.setA00308(items.get(11));
                    sandMove.setA00310(items.get(12));
                    sandMove.setA00312(items.get(13));
                    if(items.get(14).equals("")){
                        sandMove.setA00314(0.0);
                    }else{
                        sandMove.setA00314(Double.parseDouble(items.get(14)));
                    }
                    sandMove.setA00007(items.get(15));
                    if (items.get(16).equals("")){
                        sandMove.setdDatetime(sf.parse("0000-00-00 00:00:00"));
                    }else{
                        sandMove.setdDatetime(sf.parse(items.get(16)));
                    }

                    sandMove.setA00009(items.get(17));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(sandMove);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
