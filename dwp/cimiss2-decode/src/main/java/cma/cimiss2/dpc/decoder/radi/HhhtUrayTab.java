package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.radi.RadiHhhtUrayTab;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/24 0024 13:34
 * @description：解码类
 * @modified By：
 * @version: 1.0$
 */
public class HhhtUrayTab {
    /**
     * 解码结构
     */
    private ParseResult<RadiHhhtUrayTab> parseResult = new ParseResult<RadiHhhtUrayTab>(false) ;

    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public ParseResult<RadiHhhtUrayTab> decode(byte[] data,String fileCode) {
        try {
            //获取表格数据
            ArrayList<String[]> itemList = ReadCsv.readCsv(data,fileCode);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (String[] items : itemList) {
                RadiHhhtUrayTab radiHhhtUrayTab = new RadiHhhtUrayTab();
                try {
                    String dates[] = items[0].split(" ");
                    String nian[] = dates[0].split("-");
                    String shifen[] = dates[1].split(":");
                    radiHhhtUrayTab.setdRetainId(String.valueOf(UUID.randomUUID()));
                    radiHhhtUrayTab.setdDatetime(items[0]);
                    radiHhhtUrayTab.setdIymdhm(sdf.format(calendar.getTime()));
                    radiHhhtUrayTab.setV04001(Short.parseShort(nian[0]));
                    radiHhhtUrayTab.setV04002(Short.parseShort(nian[1]));
                    radiHhhtUrayTab.setV04003(Short.parseShort(nian[2]));
                    radiHhhtUrayTab.setV04004(Short.parseShort(shifen[0]));
                    radiHhhtUrayTab.setV04005(Short.parseShort(shifen[1]));
                    radiHhhtUrayTab.setV01301("999999");
                    radiHhhtUrayTab.setV01300(999999);
                    radiHhhtUrayTab.setUva(items[1]);
                    radiHhhtUrayTab.setUvb(items[2]);
                    radiHhhtUrayTab.setCmaUvi(items[3]);
                    radiHhhtUrayTab.setInlUvi(items[4]);
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(radiHhhtUrayTab);
                parseResult.setSuccess(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
