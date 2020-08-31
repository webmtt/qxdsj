package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.ZslObserve;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DecodeZslObserve {


    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<ZslObserve> parseResult = new ParseResult<ZslObserve>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<ZslObserve> decode(File file) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForZsl(file);
            System.out.println(itemList);
            DecimalFormat df = new DecimalFormat("0");
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (int i = 0; i <itemList.size(); i++) {
                try {
                    for (int j = 0; j <10 ; j++) {
                        ZslObserve zslObserve = new ZslObserve();
                        zslObserve.setdRecordId(new BigDecimal(ReadCsv.getOrderNo()));
                        zslObserve.setV01300(Double.parseDouble(itemList.get(i).get(0)));
                        zslObserve.setV01301(df.format(Double.parseDouble(itemList.get(i).get(0))));
                        zslObserve.setStationName(itemList.get(i).get(1));
                        zslObserve.setdDatetime(new Date(sf.parse(itemList.get(i).get(2)).getTime()-(10-j) * 24 * 60 * 60 * 1000));
                        String[] str = sf.format(new Date(sf.parse(itemList.get(i).get(2)).getTime()-(10-j) * 24 * 60 * 60 * 1000)).split("-");
                        zslObserve.setV04001(Integer.parseInt(str[0]));
                        zslObserve.setV04002(Integer.parseInt(str[1]));
                        zslObserve.setV04003(Integer.parseInt(str[2]));
                        zslObserve.setA28000(Double.parseDouble(itemList.get(i).get(3+j)));
                        zslObserve.setA00007(itemList.get(i).get(13));
                        parseResult.put(zslObserve);
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
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
