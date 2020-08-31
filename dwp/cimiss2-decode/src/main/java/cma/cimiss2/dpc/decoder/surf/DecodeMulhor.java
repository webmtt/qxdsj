package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.Mulhor;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DecodeMulhor {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<Mulhor> parseResult = new ParseResult<Mulhor>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<Mulhor> decode(File file) {
        try {
            //获取表格数据
            ArrayList<ArrayList<String>> itemList = ReadCsv.readExcelForMul(file);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(itemList);
            String lineTxt = null;
            // 循环读取文件的行
            int positionx = 1;
            for (ArrayList<String> items : itemList) {
                Mulhor mulhor = new Mulhor();
                try {
                    mulhor.setDDatetime(items.get(1));
                    mulhor.setLayer(items.get(2));
                    mulhor.setV01301(items.get(3));
                    mulhor.setDRymdhm(new Timestamp(sf.parse(items.get(4)).getTime()));
                    mulhor.setH10300(items.get(5));
                    mulhor.setH10301(items.get(6));
                    mulhor.setH10301052(items.get(7));
                    mulhor.setH10302(items.get(8));
                    mulhor.setH10302052(items.get(9));
                    mulhor.setH10310(items.get(10));
                    mulhor.setH10311(items.get(11));
                    mulhor.setH10311052(items.get(12));
                    mulhor.setH10312(items.get(13));
                    mulhor.setH10312052(items.get(14));

                    mulhor.setH10320(items.get(15));
                    mulhor.setH10321(items.get(16));
                    mulhor.setH10321052(items.get(17));
                    mulhor.setH10322(items.get(18));
                    mulhor.setH10322052(items.get(19));
                    mulhor.setV11290(Double.parseDouble(items.get(20)));
                    mulhor.setV11291(Double.parseDouble(items.get(21)));
                    mulhor.setV11292(Double.parseDouble(items.get(22)));
                    mulhor.setV11293(Double.parseDouble(items.get(23)));
                    mulhor.setV11296(Double.parseDouble(items.get(24)));
                    mulhor.setV11042(Double.parseDouble(items.get(25)));
                    mulhor.setV11042052(items.get(26));
                    mulhor.setV11201(Double.parseDouble(items.get(27)));
                    mulhor.setV11202(Double.parseDouble(items.get(28)));

                    mulhor.setV11211(Double.parseDouble(items.get(29)));
                    mulhor.setV11046(items.get(30));
                    mulhor.setV11046052(items.get(31));
                    mulhor.setV13011(Double.parseDouble(items.get(32)));
                    mulhor.setV13012(items.get(33));
                    //mulhor.setV11290(Double.parseDouble(items.get(34)));
                    mulhor.setV12001(Double.parseDouble(items.get(34)));
                    mulhor.setV12011(Double.parseDouble(items.get(35)));
                    mulhor.setV12011052(items.get(36));
                    mulhor.setV130031(items.get(37));
                    mulhor.setV13003(Double.parseDouble(items.get(38)));
                    mulhor.setV13007(Double.parseDouble(items.get(39)));
                    mulhor.setV13007052(items.get(40));
                    mulhor.setV13004(Double.parseDouble(items.get(41)));

                    mulhor.setV12003(Double.parseDouble(items.get(42)));
                    mulhor.setV10004(Double.parseDouble(items.get(43)));
                    mulhor.setV10301(Double.parseDouble(items.get(44)));
                    mulhor.setV10301052(items.get(45));
                    mulhor.setV10302(Double.parseDouble(items.get(46)));
                    mulhor.setV10302052(items.get(47));
                    mulhor.setV12314(Double.parseDouble(items.get(48)));
                    mulhor.setV12315(Double.parseDouble(items.get(59)));
                    mulhor.setV12315052(items.get(50));
                    mulhor.setV12316(Double.parseDouble(items.get(51)));
                    mulhor.setV12316052(items.get(52));
                    mulhor.setV12120(Double.parseDouble(items.get(53)));
                    mulhor.setV12311(Double.parseDouble(items.get(54)));
                    mulhor.setV12311052(items.get(55));

                    mulhor.setV12121(Double.parseDouble(items.get(56)));
                    mulhor.setV12121052(items.get(57));
                    mulhor.setV12030040(Double.parseDouble(items.get(58)));
                    mulhor.setV12030050(Double.parseDouble(items.get(59)));
                    mulhor.setV12030080(Double.parseDouble(items.get(60)));
                    mulhor.setV12030160(Double.parseDouble(items.get(61)));
                    mulhor.setV12030320(Double.parseDouble(items.get(62)));
                    mulhor.setV14042(Double.parseDouble(items.get(63)));
                    mulhor.setV2000170101(Double.parseDouble(items.get(64)));
                    mulhor.setV20059(Double.parseDouble(items.get(65)));
                    mulhor.setV20059052(items.get(66));
                    mulhor.setV10051(Double.parseDouble(items.get(67)));
                    mulhor.setV13019(Double.parseDouble(items.get(68)));
                    mulhor.setV1301111(items.get(69));

                    mulhor.setV14052(items.get(70));
                    mulhor.setV130112(items.get(71));
                    mulhor.setV1301121(items.get(72));
                    mulhor.setV1301122(items.get(73));
                    mulhor.setV13013(Double.parseDouble(items.get(74)));
                    mulhor.setV12012052(items.get(75));
                    mulhor.setV12012(Double.parseDouble(items.get(76)));
                    mulhor.setDDataId(items.get(77));


                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    re.setSegment(lineTxt);
                    re.setPositionx(positionx);
                    parseResult.put(re);
                    continue;
                }
                positionx ++;
                parseResult.put(mulhor);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
