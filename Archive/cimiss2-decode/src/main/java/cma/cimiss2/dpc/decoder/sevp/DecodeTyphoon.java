package cma.cimiss2.dpc.decoder.sevp;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sevp.Typhoon;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

import java.io.*;
import java.util.List;

/**
 * ************************************
 * @ClassName: DecodeTyphoon
 * @Auther: dangjinhu
 * @Date: 2019/3/11 10:37
 * @Description: 台风实况和台风预测解析结果集
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DecodeTyphoon {

    /**
     * 对外接口
     * @param fileName 文件名
     * @return ParseResult<Typhoon> 数据解码结果集
     */
    public ParseResult<Typhoon> decodeFile(String fileName) {
        ParseResult<Typhoon> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.getName().contains("NULL") || file.length() <= 0) {
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
            } else {
                readFileInfo(file, parseResult);
                parseResult.setSuccess(true);
            }
        } else {
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        return parseResult;
    }

    /**
     * 读取文件信息
     * @param file
     * @return
     */
    private void readFileInfo(File file, ParseResult<Typhoon> parseResult) {
        // 获取文件编码
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
        bufferReader(file, fileCode, parseResult);
    }

    /**
     * BufferedReader读文件
     * @param file
     * @param fileCode
     */
    private void bufferReader(File file, String fileCode, ParseResult<Typhoon> parseResult) {

        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), fileCode);
            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(",");
                Typhoon typhoon = new Typhoon();
                int i = 0;
                typhoon.setArea(strs[i++]);
                typhoon.setTyphoonNumber(strs[i++]);
                typhoon.setTyphoonName(strs[i++]);
                typhoon.setDate(strs[i++]);
                if (file.getName().contains("forecast")){
                    typhoon.setForestTime(strs[i++]);
                }
                typhoon.setLatitude(strs[i++]);
                typhoon.setLongitude(strs[i++]);
                typhoon.setStrength(strs[i++]);
                parseResult.put(typhoon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {

        String fileNameActive = "E:\\CIMISS2\\data\\M\\M.0004.0002.R001\\201901\\2019010708\\active_SouthernHemisphere_201901070000_NOAA_SH082019_20190107154532.txt";
        String fileNameForecast = "E:\\CIMISS2\\data\\M\\M.0004.0003.R001\\201901\\2019010709\\forecast_SouthernHemisphere_201901070600_NOAA_SH082019_20190107174510.txt";
        DecodeTyphoon decodeTyphoon = new DecodeTyphoon();
        ParseResult<Typhoon> parseResult = decodeTyphoon.decodeFile(fileNameForecast);
        List<Typhoon> ty = parseResult.getData();
        for (int i = 0; i < ty.size(); i++) {
            Typhoon hk = ty.get(i);
        }

    }

}
