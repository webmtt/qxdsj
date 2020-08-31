package cma.cimiss2.dpc.indb.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 格式化数据返回实体类
 * @Aouthor: xzh
 * @create: 2018-04-16 11:15
 */
public class StatTF implements Serializable {

	private static final long serialVersionUID = 1L;
	//小时数据list
    private List<Map<String, Object>> horList = new ArrayList<>();
    //分钟数据list
    private List<Map<String, Object>> minList = new ArrayList<>();
    //分钟降水list
    private List<Map<String, Object>> minPreList = new ArrayList<>();
    //累计降水list
    private List<Map<String, Object>> cumPreList = new ArrayList<>();
    //处理后报文集合
    private List<Map<String, Object>> repList = new ArrayList<>();
    
    // 全球小时表
    private List<Map<String, Object>> glbHorList = new ArrayList<>();
    
    // 2020-3-3 chy 重要天气表
    private List<Map<String, Object>> weatherList = new ArrayList<>();
    
    //日志
    private StringBuffer buffer = new StringBuffer();

    public List<Map<String, Object>> getHorList() {
        return horList;
    }

    public void setHorList(List<Map<String, Object>> horList) {
        this.horList = horList;
    }

    public List<Map<String, Object>> getMinList() {
        return minList;
    }

    public void setMinList(List<Map<String, Object>> minList) {
        this.minList = minList;
    }

    public List<Map<String, Object>> getRepList() {
        return repList;
    }

    public void setRepList(List<Map<String, Object>> repList) {
        this.repList = repList;
    }


    public StringBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuffer buffer) {
        this.buffer = buffer;
    }

	public List<Map<String, Object>> getMinPreList() {
		return minPreList;
	}

	public void setMinPreList(List<Map<String, Object>> minPreList) {
		this.minPreList = minPreList;
	}

	public List<Map<String, Object>> getCumPreList() {
		return cumPreList;
	}

	public void setCumPreList(List<Map<String, Object>> cumPreList) {
		this.cumPreList = cumPreList;
	}

	public List<Map<String, Object>> getGlbHorList() {
		return glbHorList;
	}

	public void setGlbHorList(List<Map<String, Object>> glbHorList) {
		this.glbHorList = glbHorList;
	}

	public List<Map<String, Object>> getWeatherList() {
		return weatherList;
	}

	public void setWeatherList(List<Map<String, Object>> weatherList) {
		this.weatherList = weatherList;
	}
}
