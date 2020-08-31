package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.GressWeight;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeGressWeight {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<GressWeight> parseResult = new ParseResult<GressWeight>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<GressWeight> decode(File file) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForPress(file);
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                GressWeight gressWeight = new GressWeight();
                try {
                    int i = 0;
                    gressWeight.setPrefectureName(items.get(i));
                    gressWeight.setV01301(items.get(++i));
                    gressWeight.setV01300(Double.parseDouble(items.get(++i)));
                    gressWeight.setV04001(Integer.parseInt(items.get(++i).substring(0,4)));
                    gressWeight.setV04002(Integer.parseInt(items.get(++i).substring(0,1)));
                    gressWeight.setA02500(items.get(++i));
                    gressWeight.setA02501(items.get(++i));
                    gressWeight.setA02502(items.get(++i));
                    gressWeight.setA02503(items.get(++i));
                    gressWeight.setA02504(items.get(++i));
                    gressWeight.setA02505(items.get(++i));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(gressWeight);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }

}
