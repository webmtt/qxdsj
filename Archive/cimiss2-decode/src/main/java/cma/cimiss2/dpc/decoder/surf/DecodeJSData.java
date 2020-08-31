package cma.cimiss2.dpc.decoder.surf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.JapanStationData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;

/**
 * ************************************
 * @ClassName:DecodeJSData
 * @Auther: liuyingshuang
 * @Date:2019年4月6日
 * @Description:日本站点资料解析
 * @Copyright: All rights reserver.
 * ************************************
 */
public class DecodeJSData {

    /**
     * 对外接口
     * @param fileName
     * @return
     */
    public ParseResult<JapanStationData> decodeFile(String fileName) {
        ParseResult<JapanStationData> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.length() <= 0) {
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
    private void readFileInfo(File file, ParseResult<JapanStationData> parseResult) {
        // 获取文件编码
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
        bufferReader(file, fileCode, parseResult);
    }

    /**
     * BufferedReader读文件
     * @param file
     * @param fileCode
     */
    private void bufferReader(File file, String fileCode, ParseResult<JapanStationData> pr) {
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), fileCode);
            reader = new BufferedReader(is);
            String line; 
            List<JapanStationData> japanStationData = new ArrayList<JapanStationData>();
         
            while ((line = reader.readLine()) != null) {
                try {
                    // 过滤空行中占有空字符
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
					 line=line.replace(")", "");
                    line=line.replace("]", "");
                    String[] values = line.trim().split(",");
                    NullVauleOper(values);
                      //读数据
                    if(values.length==22) {
                    try {
                    	   readDate(values,japanStationData);
                    	   
                    	   //2019-7-16 cuihongyuan
                    	   try {
	                   			if(!TimeCheckUtil.checkTime(
	                   					(new SimpleDateFormat( "yyyyMMddHH")).parse(japanStationData.get(japanStationData.size() - 1).getDataTime())
	                   				)
	                   			  ){
	                   				ReportError reportError = new ReportError();
	                   				reportError.setMessage("time check error!");
	                   				reportError.setSegment(values[6] + "\t" + values[0] + "\t" + values[1] + "\t" + values[2]);
	                   				pr.put(reportError);
	                   				continue;
	                   			}
                   			//以上 2019-7-16
                   			
                   		} catch (ParseException e) {
                   			// TODO Auto-generated catch block
                   			e.printStackTrace();
                   		}
                    	   
                    	   
                    	   // 若有一个站点数据读取成功,设置success为true
                    	   pr.setSuccess(true);
                    } catch (Exception e) {
                        ReportError error = new ReportError();
                        error.setMessage("日本站点数据格式转换异常");
                        error.setSegment(line);
                        pr.put(error);
                    }
                    }else {
                    	 ReportError error = new ReportError();
                         error.setMessage("读取日本数据基本参数异常");
                         error.setSegment(line);
                         pr.put(error);    
                    }
                    
                } catch (Exception e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("读取文件异常!");
                    error.setSegment(line);
                    pr.put(error);
                }
                
            }
            // 数据读取成功
            pr.setData(japanStationData);
        } catch (IOException e) {
            ReportError error = new ReportError(); // 文件异常捕获
            error.setMessage("异常" + e.getMessage());
            pr.put(error);
        } finally {

            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("流关闭异常" + e.getMessage());
                    pr.put(error);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    ReportError error = new ReportError(); // 文件异常捕获
                    error.setMessage("流关闭异常" + e.getMessage());
                    pr.put(error);
                }
            }
        }
    }

	/**缺测值处理
	 * @param values
	 */
	private void NullVauleOper(String[] values) {
		// TODO Auto-generated method stub

		for (int i=0;i<values.length;i++) {
					
			if(values[i].toLowerCase().equals("null")) {
				values[i]="999999";
			}
			if(values[i].toLowerCase().equals("abeyance")) {
				values[i]="999999";
			}
			if(values[i].toLowerCase().equals("x")) {
				values[i]="999999";
			}
			if(values[i].toLowerCase().equals("")) {
				values[i]="999999";
			}
		}
	}

	/**
	 * @param values
	 * @param jaPanStationData
	 */
	private void readDate(String[] values, List<JapanStationData> japanStationData) {
		// TODO Auto-generated method stub
		JapanStationData jsd=new JapanStationData();
		
		//省
		jsd.setCityName(values[0]);
		//市
		jsd.setCountyName(values[1]);
		//县
		jsd.setVillageName(values[2]);
		//台站
		jsd.setStation(values[2].trim());
		//经度
		jsd.setLatitude(transDouble(values[3].trim()));
		//纬度
		jsd.setLongitude(transDouble(values[4].trim()));
		//海拔
		jsd.setAltitude(transDouble(values[5].trim()));
		//时间
		jsd.setDataTime(values[6]);
		
		//温度
		jsd.setTemperature(transDouble(values[7].trim()));
		//降水
		jsd.setPrecipitation(transDouble(values[8].trim()));
		//风向
		jsd.setWindDirection(values[9]);
		//风速
		jsd.setWindSpeed(transDouble(values[10]));
		//日照时间
		jsd.setSunshineDuration(transDouble(values[11].trim()));
		//雪深
		jsd.setSnowDepth(transDouble(values[12].trim()));
		//相对湿度
		jsd.setHumidity(transDouble(values[13].trim()));
		//气压
		jsd.setPressure(transDouble(values[14].trim()));
		//24小时最高温
		jsd.setMaxTEP24(transDouble(values[15].trim()));
		//24小时最高温时间
		jsd.setMaxTEP24Time(values[16]);
		//24小时最低温
		jsd.setMinTEP24(transDouble(values[17].trim()));
		//24小时最低温时间
		jsd.setMinTEP24Time(values[18]);
		////24小时最大风速
		jsd.setMaxWindSpeed24(transDouble(values[19].trim()));
		//24小时最大风向
		jsd.setMaxWindDirection24(values[20]);
		//24小时最大风时间
		jsd.setMaxWind24Time(values[21]);

		japanStationData.add(jsd);

	}
	public double transDouble(String value){
		double val=0.0;
		try {
			val= Double.parseDouble(value);
			return val;
		} catch (Exception e) {
			return 999999.0;
		}
	}

}

