package cma.cimiss2.dpc.indb.core.bean;


import java.io.File;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
/**
 * @Description: 多线程格式化数据返回值实体类
 * @Aouthor: xzh
 * @create: 2018-07-05 16:56
 */
public class StatFormate<T> {
    private List<Map<String, Object>> reports;
    
    /**
	 * 解码后返回的结果集
	 */
    private ParseResult<T> result;
    
    /**
	 * 解码文件
	 */
    private File file;

    public List<Map<String, Object>> getReports() {
        return reports;
    }

    public void setReports(List<Map<String, Object>> reports) {
        this.reports = reports;
    }

    public ParseResult<T> getResult() {
        return result;
    }

    public void setResult(ParseResult<T> result) {
        this.result = result;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
