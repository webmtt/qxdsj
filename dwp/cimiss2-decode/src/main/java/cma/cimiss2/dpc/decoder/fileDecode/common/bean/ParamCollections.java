package cma.cimiss2.dpc.decoder.fileDecode.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */
@Data
public class ParamCollections implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日值
     */
    private ViewParamDayEntity dayEntity=new ViewParamDayEntity();
    /**
     * key：hours，Value：小时值
     */
    private Map<String, ViewParamsHourEntity> mapHour =
            new ConcurrentHashMap<>();
    private TimeEntity timeEntity=null;
}
