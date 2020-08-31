package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.SoilObserve;
import cma.cimiss2.dpc.decoder.tools.utils.ReadExecl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DecodeSoilObserve {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<SoilObserve> parseResult = new ParseResult<SoilObserve>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<SoilObserve> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> values = ReadExecl.readExcelToObj(file);
            DecimalFormat df = new DecimalFormat("0.00");
            String str = file.getName();
            String nullNum="999";
            if(str.contains("肥力")){
                for (int i = 2; i <values.size() ; i++) {
                    if (values.get(i).size()>7){
                        if (!"备注".equals(values.get(i).get(0))){
                            SoilObserve soilObserve = new SoilObserve();
                            soilObserve.setCname(values.get(i).get(0));
                            if (" ".equals(values.get(i).get(1))||"".equals(values.get(i).get(1))||values.get(i).get(1)==null){
                                soilObserve.setA30000(nullNum);
                            }else {
                                soilObserve.setA30000(values.get(i).get(1));
                            }
                            if (" ".equals(values.get(i).get(2))||"".equals(values.get(i).get(2))||values.get(i).get(2)==null){
                                soilObserve.setA30006(nullNum);
                            }else{
                                soilObserve.setA30006(df.format(new BigDecimal(values.get(i).get(2))));
                            }
                            if (" ".equals(values.get(i).get(3))||"".equals(values.get(i).get(3))||values.get(i).get(3)==null){
                                soilObserve.setA30001(nullNum);
                            }else {
                                soilObserve.setA30001(df.format(new BigDecimal(values.get(i).get(3))));
                            }
                            if (" ".equals(values.get(i).get(4))||"".equals(values.get(i).get(4))||values.get(i).get(4)==null){
                                soilObserve.setA30002(nullNum);
                            }else {
                                soilObserve.setA30002(df.format(new BigDecimal(values.get(i).get(4))));
                            }
                            if (" ".equals(values.get(i).get(5))||"".equals(values.get(i).get(5))||values.get(i).get(5)==null){
                                soilObserve.setA30003(nullNum);
                            }else {
                                soilObserve.setA30003(df.format(new BigDecimal(values.get(i).get(5))));
                            }
                            if (" ".equals(values.get(i).get(6))||"".equals(values.get(i).get(6))||values.get(i).get(6)==null){
                                soilObserve.setA30004(nullNum);
                            }else {
                                soilObserve.setA30004(df.format(new BigDecimal(values.get(i).get(6))));
                            }
                            if (" ".equals(values.get(i).get(7))||"".equals(values.get(i).get(7))||values.get(i).get(7)==null){
                                soilObserve.setA30005(nullNum);
                            }else {
                                soilObserve.setA30005(df.format(new BigDecimal(values.get(i).get(7))));
                            }
                            soilObserve.setV04001(Short.parseShort(str.substring(0,4)));
                            soilObserve.setDDatetime(new Timestamp(Long.parseLong(str.substring(0,4))));
                            parseResult.put(soilObserve);
                        }

                    }else if(values.get(i).size()>1&&values.get(i).size()<=6){
                        if (!"备注".equals(values.get(i).get(0))) {
                            SoilObserve soilObserve = new SoilObserve();
                            soilObserve.setCname(values.get(i).get(0));
                            if (" ".equals(values.get(i).get(1))||"".equals(values.get(i).get(1))||values.get(i).get(1)==null) {
                                soilObserve.setA30000(nullNum);
                            } else {
                                soilObserve.setA30000(values.get(i).get(1));
                            }
                            if (" ".equals(values.get(i).get(2))||"".equals(values.get(i).get(2))||values.get(i).get(2)==null) {
                                soilObserve.setA30006(nullNum);
                            } else {
                                soilObserve.setA30006(df.format(new BigDecimal(values.get(i).get(2))));
                            }
                            if (" ".equals(values.get(i).get(3))||"".equals(values.get(i).get(3))||values.get(i).get(3)==null) {
                                soilObserve.setA30001(nullNum);
                            } else {
                                soilObserve.setA30001(df.format(new BigDecimal(values.get(i).get(3))));
                            }
                            if (" ".equals(values.get(i).get(4))||"".equals(values.get(i).get(4))||values.get(i).get(4)==null) {
                                soilObserve.setA30002(nullNum);
                            } else {
                                soilObserve.setA30002(df.format(new BigDecimal(values.get(i).get(4))));
                            }
                            if (" ".equals(values.get(i).get(5))||"".equals(values.get(i).get(5))||values.get(i).get(5)==null) {
                                soilObserve.setA30003(nullNum);
                            } else {
                                soilObserve.setA30003(df.format(new BigDecimal(values.get(i).get(5))));
                            }
                            soilObserve.setA30004(nullNum);
                            soilObserve.setA30005(nullNum);
                            soilObserve.setV04001(Short.parseShort(str.substring(0, 4)));
                            soilObserve.setDDatetime(new Timestamp(Long.parseLong(str.substring(0,4))));
                            parseResult.put(soilObserve);
                        }
                    }

                }
            }else{
                for (int i = 2; i <values.size() ; i++) {
                    if (values.get(i).size()>1) {
                        SoilObserve soilObserve = new SoilObserve();
                        soilObserve.setCname(values.get(i).get(1));
                        soilObserve.setA30004(values.get(i).get(3).replace("us", "").replace("ms", ""));
                        soilObserve.setA30005(values.get(i).get(2));
                        soilObserve.setA30000(nullNum);
                        soilObserve.setA30001(nullNum);
                        soilObserve.setA30002(nullNum);
                        soilObserve.setA30003(nullNum);
                        soilObserve.setA30006(nullNum);
                        soilObserve.setV04001(Short.parseShort(str.substring(0, 4)));
                        soilObserve.setDDatetime(new Timestamp(Long.parseLong(str.substring(0,4))));
                        parseResult.put(soilObserve);
                    }
                }
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
