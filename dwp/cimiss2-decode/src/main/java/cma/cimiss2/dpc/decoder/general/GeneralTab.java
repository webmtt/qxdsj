package cma.cimiss2.dpc.decoder.general;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
import cma.cimiss2.dpc.decoder.tools.utils.GetPropertiesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/27 0027 10:46
 * @description：通用读取文件
 * @modified By：
 * @version: 1.0$
 */
public class GeneralTab {
    /**
     * 结果集
     */

    private ParseResult parseResult = new ParseResult(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult decode(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if(file.length() <= 0){
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                return parseResult;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;

                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    String field_name = GetPropertiesUtil.getMessage("field_name");
                    String empty_field = GetPropertiesUtil.getMessage("empty_field");
                    String[] ef = empty_field.split(",");
                    for(int i = 0; i < ef.length; i++){
                        field_name = field_name.replace(ef[i]+",","");
                    }
                    String[] fn = field_name.split(",");
                    for (int i = 0; i < txtFileContent.size(); i++) {
                        Map map = new HashMap();
                        String[] multab_list = txtFileContent.get(i).split("\\s+");
                        String uuid = UUID.randomUUID().toString();
                        map.put("D_RECORD_ID",uuid);
                        map.put("D_DATETIME",Timestamp.valueOf(multab_list[1]+"-"+multab_list[2]+"-"+multab_list[3]+" "+"00:00:00"));
                        for(int j = 0; j < fn.length; j++){
                            map.put(fn[j],multab_list[j]);
                        }
                        parseResult.put(map);
                        parseResult.setSuccess(true);
                    }

                } else {
                    if (txtFileContent == null || txtFileContent.size() == 0) {
                        // 空文件
                        parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                    } else {
                        // 数据只有一行，格式不正确
                        parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
            } catch (FileNotFoundException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // file not exsit
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        return parseResult;
    }
}
