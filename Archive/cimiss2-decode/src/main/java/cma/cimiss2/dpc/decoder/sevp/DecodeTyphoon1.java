package cma.cimiss2.dpc.decoder.sevp;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.sevp.Typhoon1;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ************************************
 * @ClassName: DecodeTyphoon
 * @Auther: dangjinhu
 * @Date: 2019/3/11 10:37
 * @Description: 台风实况和台风预测解析结果集
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DecodeTyphoon1 {

    /**
     * 对外接口
     * @param fileName
     * @return
     */
    public ParseResult<Typhoon1> decodeFile(String fileName) {
        ParseResult<Typhoon1> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.getName().contains("NULL") || file.length() <= 0) {
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
            } else {
                readFileInfo(file, parseResult);
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
    private void readFileInfo(File file, ParseResult<Typhoon1> parseResult) {
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
    private void bufferReader(File file, String fileCode, ParseResult<Typhoon1> parseResult) {
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), fileCode);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                // 过滤空行中占有空字符
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                String[] values = line.trim().split(",");
                if (values.length == 0 && values == null) {
                    continue;
                }
                // 数组长度检验
                // 预报 values.length 为 8
                if (file.getName().contains("forecast")) {
                    if (values.length != 8) {
                        ReportError error = new ReportError();
                        error.setMessage("预报数据格式异常");
                        error.setSegment(file.getPath());
                        parseResult.put(error);
                        continue; // 读下一行
                    }
                } else if (file.getName().contains("active")) {
                    // 实况 values.length 为 7
                    if (values.length != 7) {
                        ReportError error = new ReportError();
                        error.setMessage("实况数据格式异常");
                        error.setSegment(file.getPath());
                        parseResult.put(error);
                        continue; // 读下一行
                    }
                } else {
                    ReportError error = new ReportError();
                    error.setMessage("文件名异常");
                    error.setSegment(file.getPath());
                    parseResult.put(error);
                    break; // 文件设定为forecast,active,捕获文件名可能出错
                }
                try {
                    Typhoon1 typhoon = new Typhoon1();
                    int i = 0;
                    // 区域
                    typhoon.setArea(values[i++]);
                    // 台风编号
                    typhoon.setTyphoonNumber(values[i++]);
                    // 台风名
                    typhoon.setTyphoonName(values[i++]);
                    // 观测时间
                    typhoon.setDate(values[i++].trim());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
				    Date dataTime=simpleDateFormat.parse(typhoon.getDate());
                    if(!TimeCheckUtil.checkTime(dataTime)){
						ReportError re = new ReportError();
						re.setMessage("DataTime out of range：time:"+dataTime);
						re.setSegment(line);
						parseResult.put(re);
						continue;
					}
                    // 预报资料设置预报时效
                    if (file.getName().contains("forecast")) {
                        typhoon.setForestTime(values[i++].trim());
                    }
                    // 纬度
                    typhoon.setLatitude(values[i++].trim());
                    // 经度
                    typhoon.setLongitude(values[i++].trim());
                    // 强度
                    String Strength=values[i++].trim();
                    if(!"999999".equals(Strength)){
	                    try{
	                    	 Strength=String.valueOf(Double.parseDouble(Strength)*0.51);
	                    }catch (Exception e) {
							// TODO: handle exception
						}
                    }
//                  String Strength=String.valueOf(Double.parseDouble(Strength0)*0.51);
                    typhoon.setStrength(Strength);
                    parseResult.put(typhoon);
                    // 一行站一条数据,有一条数据成功设置true
                    parseResult.setSuccess(true);
                } catch (Exception e) {
                    ReportError error = new ReportError();
                    error.setMessage("测站数据解析异常" + e.getMessage());
                    error.setSegment(line);
                    parseResult.put(error);
                }
            }
        } catch (IOException e) {
            ReportError error = new ReportError(); // 文件异常捕获
            error.setMessage("读取文件异常" + e.getMessage());
            error.setSegment(file.getPath()); // 加文件路径
            parseResult.put(error);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("BufferedReader流关闭异常" + e.getMessage());
                    parseResult.put(error);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("InputStreamReader流关闭异常" + e.getMessage());
                    parseResult.put(error);
                }
            }
        }
    }

    public static void main(String[] args) {

       /* String fileNameActive = "E:\\CIMISS2\\data\\M\\M.0004.0002.R001\\201901\\2019010708\\active_SouthernHemisphere_201901070000_NOAA_SH082019_20190107154532.txt";
        String fileNameForecast = "E:\\CIMISS2\\data\\M\\M.0004.0003.R001\\201901\\2019010709\\forecast_SouthernHemisphere_201901070600_NOAA_SH082019_20190107174510.txt";
        DecodeTyphoon decodeTyphoon = new DecodeTyphoon();
        ParseResult<Typhoon> parseResult = decodeTyphoon.decodeFile(fileNameForecast);
        List<Typhoon> ty = parseResult.getData();*/
    }
}
