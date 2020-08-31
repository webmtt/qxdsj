package cma.cimiss2.dpc.decoder.agme;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.Chnsoil;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class DecodeChnsoil {

    /**
     * 解码结构
     */
    private ParseResult<Chnsoil> parseResult = new ParseResult<Chnsoil>(false) ;



    public ParseResult<Chnsoil> decode(File data, String fileName) {
        try {
                if (fileName.contains("SWP")){
                    ArrayList<String[]> swplist = ReadCsv.readFile(data);
                    for (String[] items : swplist) {
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setV04002(Short.parseShort(items[2]));
                        chnsoil.setV04003(Short.parseShort(items[3]));
                        chnsoil.setDDatetime(new BigDecimal(items[4]));
                        chnsoil.setV71104010(new BigDecimal(items[5]));
                        chnsoil.setV71104020(new BigDecimal(items[6]));
                        chnsoil.setV71104030(new BigDecimal(items[7]));
                        chnsoil.setV71104040(new BigDecimal(items[8]));
                        chnsoil.setV71104050(new BigDecimal(items[9]));
                        chnsoil.setV71104060(new BigDecimal(items[10]));
                        chnsoil.setV71104070(new BigDecimal(items[11]));
                        chnsoil.setV71104080(new BigDecimal(items[12]));
                        chnsoil.setV71104090(new BigDecimal(items[13]));
                        chnsoil.setV71104100(new BigDecimal(items[14]));

                        chnsoil.setQ05data(new BigDecimal(items[15]));
                        chnsoil.setQ0510data(new BigDecimal(items[16]));
                        chnsoil.setQ1020data(new BigDecimal(items[17]));
                        chnsoil.setQ2030data(new BigDecimal(items[18]));
                        chnsoil.setQ3040data(new BigDecimal(items[19]));
                        chnsoil.setQ4050data(new BigDecimal(items[20]));
                        chnsoil.setQ5060data(new BigDecimal(items[21]));
                        chnsoil.setQ6070data(new BigDecimal(items[22]));
                        chnsoil.setQ7080data(new BigDecimal(items[23]));
                        chnsoil.setQ8090data(new BigDecimal(items[24]));
                        chnsoil.setQ90100data(new BigDecimal(items[25]));
                        parseResult.put(chnsoil);
                    }
                }
                if (fileName.contains("WT")){
                    ArrayList<String[]> wtlist = ReadCsv.readFile(data);
                    for (String[] items:wtlist){
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setV04002(Short.parseShort(items[2]));
                        chnsoil.setV04003(Short.parseShort(items[3]));
                        chnsoil.setV07061(new BigDecimal(items[4]));
                        chnsoil.setQV07061(new BigDecimal(items[5]));
                        parseResult.put(chnsoil);
                    }
                }
                if (fileName.contains("FWC")){
                    ArrayList<String[]> fwclist = ReadCsv.readFile(data);
                    for (String[] items:fwclist){
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setV711100005(new BigDecimal(items[2]));
                        chnsoil.setV711100510(new BigDecimal(items[3]));
                        chnsoil.setV711101020(new BigDecimal(items[4]));
                        chnsoil.setV711102030(new BigDecimal(items[5]));
                        chnsoil.setV711103040(new BigDecimal(items[6]));
                        chnsoil.setV711104050(new BigDecimal(items[7]));
                        chnsoil.setV711105060(new BigDecimal(items[8]));
                        chnsoil.setV711106070(new BigDecimal(items[9]));
                        chnsoil.setV711107080(new BigDecimal(items[10]));
                        chnsoil.setV711108090(new BigDecimal(items[11]));
                        chnsoil.setV7111090100(new BigDecimal(items[12]));

                        chnsoil.setParamenapdate(new BigDecimal(items[13]));
                        chnsoil.setQ05data(new BigDecimal(items[14]));
                        chnsoil.setQ0510data(new BigDecimal(items[15]));
                        chnsoil.setQ1020data(new BigDecimal(items[16]));
                        chnsoil.setQ2030data(new BigDecimal(items[17]));
                        chnsoil.setQ3040data(new BigDecimal(items[18]));
                        chnsoil.setQ4050data(new BigDecimal(items[19]));
                        chnsoil.setQ5060data(new BigDecimal(items[20]));
                        chnsoil.setQ6070data(new BigDecimal(items[21]));
                        chnsoil.setQ7080data(new BigDecimal(items[22]));
                        chnsoil.setQ8090data(new BigDecimal(items[23]));
                        chnsoil.setQ90100data(new BigDecimal(items[24]));
                        chnsoil.setQParamenapdate(new BigDecimal(items[25]));
                        parseResult.put(chnsoil);
                    }
                }
                if (fileName.contains("SVW")){
                    ArrayList<String[]> svwlist = ReadCsv.readFile(data);
                    for (String[] items:svwlist){
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setV711090005(new BigDecimal(items[2]));
                        chnsoil.setV711090005(new BigDecimal(items[3]));
                        chnsoil.setV711091020(new BigDecimal(items[4]));
                        chnsoil.setV711092030(new BigDecimal(items[5]));
                        chnsoil.setV711093040(new BigDecimal(items[6]));
                        chnsoil.setV711094050(new BigDecimal(items[7]));
                        chnsoil.setV711095060(new BigDecimal(items[8]));
                        chnsoil.setV711096070(new BigDecimal(items[9]));
                        chnsoil.setV711097080(new BigDecimal(items[10]));
                        chnsoil.setV711098090(new BigDecimal(items[11]));
                        chnsoil.setV7110990100(new BigDecimal(items[12]));

                        chnsoil.setParamenapdate(new BigDecimal(items[13]));
                        chnsoil.setQ05data(new BigDecimal(items[14]));
                        chnsoil.setQ0510data(new BigDecimal(items[15]));
                        chnsoil.setQ1020data(new BigDecimal(items[16]));
                        chnsoil.setQ2030data(new BigDecimal(items[17]));
                        chnsoil.setQ3040data(new BigDecimal(items[18]));
                        chnsoil.setQ4050data(new BigDecimal(items[19]));
                        chnsoil.setQ5060data(new BigDecimal(items[20]));
                        chnsoil.setQ6070data(new BigDecimal(items[21]));
                        chnsoil.setQ7080data(new BigDecimal(items[22]));
                        chnsoil.setQ8090data(new BigDecimal(items[23]));
                        chnsoil.setQ90100data(new BigDecimal(items[24]));
                        chnsoil.setQParamenapdate(new BigDecimal(items[25]));
                        parseResult.put(chnsoil);
                    }
                }
                if (fileName.contains("FH")){
                    ArrayList<String[]> fhlist = ReadCsv.readFile(data);
                    for (String[] items:fhlist){
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setV711080005(new BigDecimal(items[2]));
                        chnsoil.setV711080510(new BigDecimal(items[3]));
                        chnsoil.setV711081020(new BigDecimal(items[4]));
                        chnsoil.setV711082030(new BigDecimal(items[5]));
                        chnsoil.setV711083040(new BigDecimal(items[6]));
                        chnsoil.setV711084050(new BigDecimal(items[7]));
                        chnsoil.setV711085060(new BigDecimal(items[8]));
                        chnsoil.setV711086070(new BigDecimal(items[9]));
                        chnsoil.setV711087080(new BigDecimal(items[10]));
                        chnsoil.setV711088090(new BigDecimal(items[11]));
                        chnsoil.setV7110890100(new BigDecimal(items[12]));

                        chnsoil.setParamenapdate(new BigDecimal(items[13]));
                        chnsoil.setQ05data(new BigDecimal(items[14]));
                        chnsoil.setQ0510data(new BigDecimal(items[15]));
                        chnsoil.setQ1020data(new BigDecimal(items[16]));
                        chnsoil.setQ2030data(new BigDecimal(items[17]));
                        chnsoil.setQ3040data(new BigDecimal(items[18]));
                        chnsoil.setQ4050data(new BigDecimal(items[19]));
                        chnsoil.setQ5060data(new BigDecimal(items[20]));
                        chnsoil.setQ6070data(new BigDecimal(items[21]));
                        chnsoil.setQ7080data(new BigDecimal(items[22]));
                        chnsoil.setQ8090data(new BigDecimal(items[23]));
                        chnsoil.setQ90100data(new BigDecimal(items[24]));
                        chnsoil.setQParamenapdate(new BigDecimal(items[25]));
                        parseResult.put(chnsoil);
                    }
                }
                if (fileName.contains("PAI")){
                    ArrayList<String[]> pailist = ReadCsv.readFile(data);
                    for (String[] items:pailist){
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setV04002(Short.parseShort(items[2]));
                        chnsoil.setV04003(Short.parseShort(items[3]));
                        chnsoil.setPrecipitioncode(new BigDecimal(items[4]));
                        chnsoil.setPrecipitiondata(new BigDecimal(items[5]));
                        chnsoil.setQPrecipitiondata(new BigDecimal(items[6]));
                        parseResult.put(chnsoil);
                    }
                }
                if (fileName.contains("TFAT")){
                    ArrayList<String[]> tfatlist = ReadCsv.readFile(data);
                    for (String[] items:tfatlist){
                        Chnsoil chnsoil = new Chnsoil();
                        String id = UUID.randomUUID().toString().replace("-","");
                        chnsoil.setDRecordId(id);
                        chnsoil.setV01301(items[0]);
                        chnsoil.setV04001(Short.parseShort(items[1]));
                        chnsoil.setFreezemark(new BigDecimal(items[2]));
                        chnsoil.setFreezedate(new BigDecimal(items[3]));
                        chnsoil.setFreezedate10(new BigDecimal(items[4]));
                        chnsoil.setFreezedate20(new BigDecimal(items[5]));
                        chnsoil.setFreezedate0(new BigDecimal(items[6]));
                        chnsoil.setQFreezedate10(new BigDecimal(items[7]));
                        chnsoil.setQFreezedate20(new BigDecimal(items[8]));
                        parseResult.put(chnsoil);
                    }
                }
            parseResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
    }
}
