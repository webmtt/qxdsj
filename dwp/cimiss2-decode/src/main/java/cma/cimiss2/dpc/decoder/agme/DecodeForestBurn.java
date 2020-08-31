package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.ForestBurn;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeForestBurn {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<ForestBurn> parseResult = new ParseResult<ForestBurn>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<ForestBurn> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForBurn(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                ForestBurn forestBurn = new ForestBurn();
                try {
                    forestBurn.setV01300(Double.parseDouble(items.get(0)));
                    forestBurn.setV01301(items.get(1));
                    forestBurn.setdDatetime(sf.parse(items.get(2)));
                    forestBurn.setA00201(Double.parseDouble(items.get(3)));
                    forestBurn.setV06001(items.get(4));
                    forestBurn.setV05001(items.get(5));
                    forestBurn.setV00200(items.get(6));
                    forestBurn.setA00202(items.get(7));
                    forestBurn.setA00203(Double.parseDouble(items.get(8)));
                    forestBurn.setA00204(Double.parseDouble(items.get(9)));
                    forestBurn.setA00205(Double.parseDouble(items.get(10)));
                    forestBurn.setA00212(Double.parseDouble(items.get(11)));
                    forestBurn.setA00213(Double.parseDouble(items.get(12)));
                    forestBurn.setA00214(Double.parseDouble(items.get(13)));
                    if(items.get(14).equals("")){
                        forestBurn.setA00206(0.0);
                    }else{
                        forestBurn.setA00206(Double.parseDouble(items.get(14)));
                    }
                    if(items.get(15).equals("")){
                        forestBurn.setA00211(0.0);
                    }else{
                        forestBurn.setA00211(Double.parseDouble(items.get(15)));
                    }
                    forestBurn.setA00009(items.get(16));
                    forestBurn.setA00007(items.get(17));
                    forestBurn.setdRymdhm(sf.parse(items.get(18)));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(forestBurn);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
