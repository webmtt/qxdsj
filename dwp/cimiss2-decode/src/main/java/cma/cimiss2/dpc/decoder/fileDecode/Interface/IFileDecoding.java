package cma.cimiss2.dpc.decoder.fileDecode.Interface;

import cma.cimiss2.dpc.decoder.bean.radi.RadiDigChnMulTab;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/21
 */
public interface IFileDecoding {
    /**
     * 组装数据
     * @param file
     * @param dayCtsCode 日值四级编码
     * @param hourCts 小时值四级编码
     * @param monthCts 月值四级编码
     * @param filename 文件名称
     * @return
     */
     Map<String,Object> assemblyData(File file, String sunlightCts,String dayCtsCode, String hourCts, String monthCts, String meadowCts, String filename, List<RadiDigChnMulTab> Rlist);
}
