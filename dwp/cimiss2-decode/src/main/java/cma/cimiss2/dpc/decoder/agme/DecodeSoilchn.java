package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.Soilchn;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DecodeSoilchn {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Soilchn> parseResult = new ParseResult<Soilchn>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Soilchn> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForXls(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(itemList);
            String lineTxt = null;
            try {
                String id = UUID.randomUUID().toString().replace("-","");
                String[] str = itemList.get(1).get(0).replace(" ","").split("：");
                String cname = str[1].replace("监测时间","");
                String year = str[2].substring(0,4);
                String mouth = str[2].substring(5,6);
                String day = str[2].substring(7,9);
                Timestamp time = new Timestamp(sf.parse(year+"-"+mouth+"-"+day).getTime());
                Soilchn soilchn = new Soilchn();
                soilchn.setDRecordId(id);
                soilchn.setCname(cname);
                soilchn.setDIymdhm(new Timestamp(new Date().getTime()));
                soilchn.setDDatetime(time);
                soilchn.setDRymdhm(time);
                soilchn.setV71655010(new BigDecimal(itemList.get(4).get(1)));
                soilchn.setV71655020(new BigDecimal(itemList.get(5).get(1)));
                soilchn.setV71655030(new BigDecimal(itemList.get(6).get(1)));
                soilchn.setV71655040(new BigDecimal(itemList.get(7).get(1)));
                soilchn.setV71655050(new BigDecimal(itemList.get(8).get(1)));
                soilchn.setV04001(Short.parseShort(year));
                soilchn.setV04002(Short.parseShort(mouth));
                soilchn.setV04003(Short.parseShort(day));
                parseResult.put(soilchn);
            } catch (NumberFormatException e) {
                ReportError re = new ReportError();
                re.setMessage("数字转换异常");
                re.setSegment(lineTxt);
                parseResult.put(re);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
