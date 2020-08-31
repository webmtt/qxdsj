package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.GrassSpecies;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeGrassSpecies {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<GrassSpecies> parseResult = new ParseResult<GrassSpecies>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<GrassSpecies> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForGress(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                GrassSpecies grassSpecies = new GrassSpecies();
                try {
                    grassSpecies.setV01300(Double.parseDouble(items.get(0)));
                    grassSpecies.setdDatetime(sf.parse(items.get(1)));
                    grassSpecies.setA00002(items.get(2));
                    grassSpecies.setV71501(items.get(3));
                    if(items.get(4).equals("√")||items.get(4).equals("有")){
                        grassSpecies.setA00006(1);
                    }else if(items.get(4).equals("")){
                        grassSpecies.setA00006(0);
                    }
                    grassSpecies.setdRymdhm(sf.parse(items.get(5)));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(grassSpecies);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
