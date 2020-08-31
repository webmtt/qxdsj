package cma.cimiss2.dpc.decoder.agme;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.agme.NonsatMics;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
// TODO: Auto-generated Javadoc

/**
 * -------------------------------------------------------------------------------
 * <br>.
 *
 * @author zhaoxiaojun
 * ---------------------------------------------------------------------------------
 * @Title: DecodeNONSAT.java
 * @Package cma.cimiss2.dpc.decoder.agme
 * @Description: 非标准格式农田小气候（中环天仪）
 * <p>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年9月30日 下午2:42:42  Initial creation.
 * </pre>
 */
public class DecodeNONSAT {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<NonsatMics> parseResult = new ParseResult<NonsatMics>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<NonsatMics> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcel(file);
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                NonsatMics nonsatMics = new NonsatMics();
                    try {
                        nonsatMics.setdRymdhm(items.get(0));
                        nonsatMics.setdDatetime(items.get(1));
                        nonsatMics.setAirTU(new BigDecimal(items.get(2)));
                        nonsatMics.setAirHM(new BigDecimal(items.get(3)));
                        nonsatMics.setAirTD(new BigDecimal(items.get(4)));
                        nonsatMics.setV12030000(new BigDecimal(items.get(5)));
                        nonsatMics.setV12030005(items.get(6));
                        nonsatMics.setV12030010(new BigDecimal(items.get(7)));
                        nonsatMics.setRadi(new BigDecimal(items.get(8)));
                        nonsatMics.setCo2(new BigDecimal(items.get(9)));
                        nonsatMics.setParM(new BigDecimal(items.get(10)));
                        nonsatMics.setSurHU(new BigDecimal(items.get(11)));
                        nonsatMics.setSurHM(new BigDecimal(items.get(12)));
                        nonsatMics.setSurHD(new BigDecimal(items.get(13)));
                        if(items.get(14).equals("")){
                            nonsatMics.setCollectV(new BigDecimal("0.0"));
                        }else{
                            nonsatMics.setCollectV(new BigDecimal(items.get(14)));
                        }
                        if(items.get(15).equals("")){
                            nonsatMics.setCollectV(new BigDecimal("0.0"));
                        }else{
                            nonsatMics.setCollectV(new BigDecimal(items.get(15)));
                        }
                        nonsatMics.setMinuteHM(items.get(16));
                        nonsatMics.setV01301(items.get(17));
                    } catch (NumberFormatException e) {
                        ReportError re = new ReportError();
                        re.setMessage("数字转换异常");
                        re.setSegment(lineTxt);
                        re.setPositionx(positionx);
                        parseResult.put(re);
                        continue;
                    }
                positionx ++;
                parseResult.put(nonsatMics);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
