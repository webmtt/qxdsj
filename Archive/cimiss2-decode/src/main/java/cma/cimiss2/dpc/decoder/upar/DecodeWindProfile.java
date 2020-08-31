package cma.cimiss2.dpc.decoder.upar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.WindProfileData;
import cma.cimiss2.dpc.decoder.bean.upar.WindProfileRada;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;

/**
 * ************************************
 * @ClassName: DecodeWindRada
 * @Auther: dangjinhu
 * @Date: 2019/3/20 11:29
 * @Description: 实时、半小时平均、一小时平均的采样高度产品数据文件解析
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DecodeWindProfile {

    /**
     * 对外接口
     * @param fileName
     * @return
     */
    public ParseResult<WindProfileRada> decodeFile(String fileName) {
        ParseResult<WindProfileRada> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.length() <= 0) {
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
    private void readFileInfo(File file, ParseResult<WindProfileRada> parseResult) {
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
    private void bufferReader(File file, String fileCode, ParseResult<WindProfileRada> pr) {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), fileCode);
            BufferedReader reader = new BufferedReader(is);
          
            String line;
            int count = 0; // 记录值
            boolean flag = false;
            WindProfileRada rs = new WindProfileRada();
            List<WindProfileRada> wRatas = new ArrayList<>();
            List<WindProfileData> wDatas = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split(" ");
                cleanNullValue(values);
                if (count == 0 && values.length == 2) { // 数据格式和文件版本号
                    readFirstData(rs, values);
                    count++;
                    continue;
                }
                if (count == 1 && values.length == 6) { // 测站基本参数
                    readSecondData(rs, values);
                    count++;
                    continue;
                }
                if (count == 2) { // 数据标识
                    rs.set_OBS(values[0]);
                    count++;
                    flag = true;
                    continue;
                }
                // 若出现NNNN结束标志,退出循环
                if (WindProfileRada.NNNN.equals(values[0])) break;
                // 产品数据实体,多组数据,采样高度最多只有一条数据
                if (flag) {
                    // 产品数据出现‘/’,位缺测值,用999999替换
                    readThirdData(rs, wDatas, values);
                }
            }
            rs.setwData(wDatas);
            wRatas.add(rs);
            pr.setData(wRatas);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取第三段数据
     * @param rs
     * @param wDatas
     * @param values
     */
    private void readThirdData(WindProfileRada rs, List<WindProfileData> wDatas, String[] values) {
        WindProfileData wData = new WindProfileData();
        wData.setHeight(Double.parseDouble(values[0])); // 采样高度
        wData.setWindDirection(Double.parseDouble(values[1])); // 水平风向
        wData.setWindSpeed(Double.parseDouble(values[2])); // 水平风速
        // 垂直风速,有符号位
        if (values[3].charAt(0) == '0') {
            wData.setVwindSpeed(Double.parseDouble(values[3].substring(1)));
        } else if (values[3].charAt(0) == '-') {
            wData.setVwindSpeed(-Double.parseDouble(values[3].substring(1)));
        } else {
            wData.setVwindSpeed(Double.parseDouble(values[3]));
        }
        wData.setHcredibility(Integer.parseInt(values[4])); // 水平方向可信度
        wData.setVcredibility(Integer.parseInt(values[5])); // 垂直方向可信度
        wData.setCn2(values[6]); // 垂直方向cn2
        wDatas.add(wData);
    }

    /**
     * 缺测值处理
     * @param items
     */
    private void cleanNullValue(String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].contains("/")) {
                items[i] = WindProfileRada.NINE;
            }
        }
    }

    /**
     * 读取第二行数据
     * @param rs
     * @param values
     */
    private void readSecondData(WindProfileRada rs, String[] values) {
        rs.setStaionId(values[0]);
        // 经度
        if (values[1].charAt(0) == '0') {
            rs.setLongitude(Double.parseDouble(values[1].substring(1)));
        } else if (values[1].charAt(0) == '-') {
            rs.setLongitude(-Double.parseDouble(values[1].substring(1)));
        }
        // 纬度
        if (values[2].charAt(0) == '0') {
            rs.setLatitude(Double.parseDouble(values[2].substring(1)));
        } else if (values[2].charAt(0) == '-') {
            rs.setLatitude(-Double.parseDouble(values[2].substring(1)));
        }
        // 海拔
        if (values[3].charAt(0) == '0') {
            rs.setAltitude(Double.parseDouble(values[3].substring(1)));
        } else if (values[3].charAt(0) == '-') {
            rs.setAltitude(-Double.parseDouble(values[3].substring(1)));
        }
        // 雷达版本号
        rs.setRadaModel(values[4]);
        // 观测结束时间
        rs.setObeTime(values[5]);
    }

    /**
     * 读取第一行数据
     * @param rs
     * @param values
     */
    private void readFirstData(WindProfileRada rs, String[] values) {
        rs.setWND_OBS(values[0]);
        rs.setVersionNumber(values[1]);
    }


}

