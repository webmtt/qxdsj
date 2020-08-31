package cma.cimiss2.dpc.decoder.tools.utils;

import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ywj
 * @title: ListUtil
 * @projectName storm-test
 * @description: TODO
 * @date 2019/6/18 18:22
 */
public class ListUtil {

    /**
     * byte[]转换为List<String>
     * @param byteFile 文件
     * @return 数据
     */
    public static List<String> getListByBytes(byte[] byteFile){
        List<String> readList = new ArrayList<>();
        // 获取文件的编码
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode = FileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(byteFile)];
        BufferedReader bufr = null;
        try {
            bufr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(byteFile), fileCode));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                if (!"".equals(line)) {
                    readList.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufr != null) {
                    bufr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return readList;
    }
    public static Workbook getExcelByBytes(byte[] byteFile, String filename){
        Workbook workbook = null;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(byteFile);
            workbook = ExcelUtil.getWorkbokByFilename(byteArrayInputStream, filename);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workbook;
    }
}
