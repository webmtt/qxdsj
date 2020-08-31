package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.GrassNutrition;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;
import cma.cimiss2.dpc.decoder.tools.utils.ReadExecl;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DecodeGrassNutrition {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<GrassNutrition> parseResult = new ParseResult<GrassNutrition>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<GrassNutrition> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadExecl.readExcelToObj(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            String iymdhm ="";
            for (int j = 0; j < itemList.get(0).size(); j++) {
                if (!"".equals(itemList.get(0).get(j))){
                    iymdhm = itemList.get(0).get(j);
                }
            }
            System.out.println(itemList.get(0).size());
            iymdhm = iymdhm.substring(iymdhm.length()-4);
            for (int i = 3;i<itemList.size();i+=2) {
                GrassNutrition grassNutrition = new GrassNutrition();
                String id = UUID.randomUUID().toString().replace("-","");
                try {
                    grassNutrition.setDDataId(id);
                    grassNutrition.setDIymdhm(new Timestamp(new Date().getTime()));
                    grassNutrition.setDDatetime(new Timestamp(sf.parse(iymdhm).getTime()));
                    grassNutrition.setV04001(Integer.parseInt(iymdhm));
                    grassNutrition.setV01301(itemList.get(i).get(1));
                    grassNutrition.setV71501(itemList.get(i).get(2));
                    grassNutrition.setA00101(Double.parseDouble(itemList.get(i).get(3)));
                    grassNutrition.setA00100(Double.parseDouble(itemList.get(i).get(4)));
                    grassNutrition.setA00103(Double.parseDouble(itemList.get(i).get(5)));
                    grassNutrition.setA00106(Double.parseDouble(itemList.get(i).get(6)));
                    grassNutrition.setA00107(Double.parseDouble(itemList.get(i).get(7)));
                    grassNutrition.setA00105(Double.parseDouble(itemList.get(i).get(8)));
                    grassNutrition.setA00102(Double.parseDouble(itemList.get(i).get(9)));
                    grassNutrition.setA00108(100-Double.parseDouble(itemList.get(i).get(9))
                            -Double.parseDouble(itemList.get(i).get(8))-Double.parseDouble(itemList.get(i).get(5))
                            -Double.parseDouble(itemList.get(i).get(4))-Double.parseDouble(itemList.get(i).get(3)));
                    if(itemList.get(itemList.size()-1).get(0).equals("备注")&&!itemList.get(i).get(1).equals("")){
                        grassNutrition.setA00009(itemList.get(itemList.size()-1).get(1));
                    }else{
                        grassNutrition.setA00009("");
                    }
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(grassNutrition);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
