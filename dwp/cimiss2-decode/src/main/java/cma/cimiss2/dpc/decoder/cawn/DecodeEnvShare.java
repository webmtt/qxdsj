package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.EnvShare;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DecodeEnvShare {

    /**
     * The parse result.
     */
    /* 存放数据解析的结果集 */
    private ParseResult<EnvShare> parseResult = new ParseResult<EnvShare>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<EnvShare> decode(File file,String fileName) {
        try {
            //1.创建SAXBuilder对象
            SAXBuilder saxBuilder = new SAXBuilder();
            //2.创建输入流
            InputStream is = new FileInputStream(file);
            //3.将输入流加载到build中
            Document document = saxBuilder.build(is);
            //4.获取根节点
            Element rootElement = document.getRootElement();
            //5.获取子节点
            List<Element> children = rootElement.getChildren();
            ArrayList<ArrayList<String>> lists = new ArrayList<>();
            for (Element child : children) {
                List<Element> childrenList = child.getChildren();
                for (Element o : childrenList) {
                    List<Element> list = o.getChildren();
                    ArrayList<String> str = new ArrayList<>();
                    for (Element ob:list){
                        List<Attribute> att = ob.getAttributes();
                        str.add(att.get(2).getValue());
                    }
                    lists.add(str);
                }
            }
            System.out.println(lists);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
            for (ArrayList<String> items : lists) {
                EnvShare envShare = new EnvShare();
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    envShare.setDRecordId(id);
                    envShare.setCityname(items.get(0));
                    envShare.setCitycode(items.get(1));
                    envShare.setStationname(items.get(2));
                    envShare.setV01301(items.get(3));
                    envShare.setStationattribute(items.get(4));
                    envShare.setV06001(new BigDecimal(items.get(5)));
                    envShare.setV05001(new BigDecimal(items.get(6)));
                    envShare.setVAcode(items.get(7));
                    envShare.setV04001(Short.parseShort(items.get(8)));
                    envShare.setV04002(Short.parseShort(items.get(9)));
                    envShare.setV04003(Short.parseShort(items.get(10)));
                    envShare.setV04004(Short.parseShort(items.get(11)));
                    envShare.setSo2(items.get(12));
                    envShare.setNo(Integer.parseInt(items.get(13)));
                    envShare.setNo2(new BigDecimal(items.get(14)));
                    envShare.setNox(new BigDecimal(items.get(15)));
                    envShare.setCo(new BigDecimal(items.get(16)));
                    envShare.setO3(Integer.parseInt(items.get(17)));
                    envShare.setPm10(Integer.parseInt(items.get(18)));
                    envShare.setPm25(items.get(19));
                    envShare.setV11202(new BigDecimal(items.get(20)));
                    envShare.setV11201(new BigDecimal(items.get(21)));
                    envShare.setV12001(new BigDecimal(items.get(22)));
                    envShare.setV10004(new BigDecimal(items.get(23)));
                    envShare.setV13003(new BigDecimal(items.get(24)));
                    if ("".equals(items.get(25))||items.get(25)==null){
                        envShare.setV13019(new BigDecimal(0));
                    }else{
                        envShare.setV13019(new BigDecimal(items.get(25)));
                    }
                    envShare.setRemark(items.get(26));
                    envShare.setDDatetime(new Timestamp(sf.parse(items.get(8)+"-"+items.get(9)+"-"+items.get(10)+" "+items.get(11)).getTime()));
                } catch (NumberFormatException e) {
                    ReportError re = new ReportError();
                    re.setMessage("数字转换异常");
                    parseResult.put(re);
                    continue;
                }
                parseResult.put(envShare);
            }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
