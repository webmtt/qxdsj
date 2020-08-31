package cma.cimiss2.dpc.indb.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;


/**
 * @Description:数据入库返回值实体类
 * @Aouthor: xzh
 * @create: 2018-04-16 13:34
 */
public class StatInsert implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 解码后返回的di集合
	 */
	List<RestfulInfo> diInfo = new ArrayList<>();
	/**
	 * 解码后返回的ei集合
	 */
	List<RestfulInfo> eiInfo = new ArrayList<>();
	/**
	 * 解码后返回的日志信息
	 */
    StringBuffer buffer = new StringBuffer();
    /**
	 * 解码正确个数
	 */
    int correctNum = 0;
    /**
	 * 解码错误个数
	 */
    int errorNum = 0;
    int action = 0;//0 成功 1 失败

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<RestfulInfo> getDiInfo() {
        return diInfo;
    }

    public void setDiInfo(List<RestfulInfo> diInfo) {
        this.diInfo = diInfo;
    }

    public List<RestfulInfo> getEiInfo() {
        return eiInfo;
    }

    public void setEiInfo(List<RestfulInfo> eiInfo) {
        this.eiInfo = eiInfo;
    }

    public StringBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuffer buffer) {
        this.buffer = buffer;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }
}
