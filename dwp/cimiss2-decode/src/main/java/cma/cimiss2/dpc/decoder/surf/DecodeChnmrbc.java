package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Chnmrbc;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DecodeChnmrbc {

    /**
     * 解码结构
     */
    private ParseResult<Chnmrbc> parseResult = new ParseResult<Chnmrbc>(false) ;



    public ParseResult<Chnmrbc> decode(File file) {
        try {
            //获取表格数据
            String itemList = ReadCsv.readNote(file);
            //new String(itemList.getBytes("UTF-8"), "ISO-8859-1");
            System.out.println(itemList);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
            String[] list = itemList.split("=#");
            String[] str = list[0].split("#");
            Chnmrbc chnmrbc = new Chnmrbc();
            String id = UUID.randomUUID().toString().replace("-","");
            chnmrbc.setDRecordId(id);
            for (int i = 0; i < str.length; i++) {
                str[i].replace(" ","");
            }
            chnmrbc.setDIymdhm(new Timestamp(new Date().getTime()));
            String[] strings = str[1].split("/");
            System.out.println(strings[0].substring(6));
            chnmrbc.setDDatetime(new Timestamp(sf.parse(strings[0]).getTime()));
            chnmrbc.setV04001(Integer.parseInt(strings[0].substring(0,4)));
            chnmrbc.setV04002(Integer.parseInt(strings[0].substring(4,6)));
            chnmrbc.setArchivesnum(strings[1]);
            chnmrbc.setV01301(strings[2]);
            chnmrbc.setV01300(Integer.parseInt(strings[2]));
            chnmrbc.setV05001(Double.parseDouble(str[6].substring(0,str[6].length()-1))/100);
            chnmrbc.setV06001(Double.parseDouble(str[7].substring(0,str[7].length()-1))/100);
            chnmrbc.setV07001(Double.parseDouble(str[8])/10);
            chnmrbc.setV07031(Double.parseDouble(str[9])/10);
            chnmrbc.setV0703204(Double.parseDouble(str[10])/10);
            chnmrbc.setStationname(str[12]);
            chnmrbc.setInputmes(str[13]);
            chnmrbc.setProofreading(str[14]);
            chnmrbc.setPretrial(str[15]);
            chnmrbc.setTransmission(str[17]);
            chnmrbc.setTransmissiontime(new Timestamp(sfd.parse(str[18]).getTime()));
            chnmrbc.setAddress(str[4]);
            chnmrbc.setGeoenvironment(str[5]);
            chnmrbc.setV02001(0);
            chnmrbc.setV02301(14);
            chnmrbc.setVAcode("150000");
            parseResult.put(chnmrbc);
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
