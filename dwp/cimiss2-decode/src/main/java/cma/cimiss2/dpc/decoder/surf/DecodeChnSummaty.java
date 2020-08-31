package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Chnmrbc;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.util.UUID;

public class DecodeChnSummaty {

    /**
     * 解码结构
     */
    private ParseResult<Chnmrbc> parseResult = new ParseResult<Chnmrbc>(false) ;



    public ParseResult<Chnmrbc> decode(File file) {
        try {
            //获取表格数据
            String itemList = ReadCsv.readNote(file);
            System.out.println(itemList);
            String[] list = itemList.split("=#");
            String[] str = list[0].split("#");
            String[] strr = list[1].split("#");
            Chnmrbc chnmrbc = new Chnmrbc();
            String id = UUID.randomUUID().toString().replace("-","");
            chnmrbc.setDRecordId(id);
            for (int i = 0; i < str.length; i++) {
                str[i].replace(" ","");
            }
            String[] strings = str[1].split("/");
            chnmrbc.setV01301(strings[2]);
            chnmrbc.setSummatyMonth(strr[1].split("/")[0]);
            chnmrbc.setSummaryEvent(strr[2]);
            chnmrbc.setSummaryEventid(strr[0]);
            if (strr[2].substring(2).equals("88")){
                chnmrbc.setEventTypeIdentification1("00");
            }else{
                chnmrbc.setEventTypeIdentification1(strr[2].substring(0,2));
            }
            parseResult.put(chnmrbc);
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
