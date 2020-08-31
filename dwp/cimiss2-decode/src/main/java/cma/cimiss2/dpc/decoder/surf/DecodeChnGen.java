package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Chnmrbc;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.util.UUID;

public class DecodeChnGen {

    /**
     * 解码结构
     */
    private ParseResult<Chnmrbc> parseResult = new ParseResult<Chnmrbc>(false) ;



    public ParseResult<Chnmrbc> decode(File file) {
        try {
            //获取表格数据
            String itemList = ReadCsv.readNote(file);
            String[] list = itemList.split("=#");
            String[] str = list[0].split("#");
            String[] strc = list[3].split("#");
            Chnmrbc chnmrbc = new Chnmrbc();
            String id = UUID.randomUUID().toString().replace("-","");
            chnmrbc.setDRecordId(id);
            for (int i = 0; i < str.length; i++) {
                str[i].replace(" ","");
            }
            String[] strings = str[1].split("/");
            chnmrbc.setV01301(strings[2]);
            chnmrbc.setNoteGeneralEventid("BB");
            chnmrbc.setNoteMonth(strings[0].substring(0,6));
            String string=new String();
            for (int i = 2; i <strc.length ; i++) {
                string += strc[i]+",";
            }
            chnmrbc.setNoteGeneralEvent(string);

            parseResult.put(chnmrbc);
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
