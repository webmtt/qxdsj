package cma.cimiss2.dpc.quickqc.bean;

import cma.cimiss2.dpc.quickqc.bean.enums.Quality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:将要素值和质控码包裹在一起. param <T> 要素值类型
 * @author: When6passBye
 * @create: 2019-07-23 19:17
 **/
public class QCEle<T> implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 要素值.
     */
    T value;

    /**
     * 质控码，可能是1级或多级，如国省县3级.
     */
    List<Quality> quality;

    /**
     * 5步质控记录
     */
    private Quality[] records=new Quality[]{Quality.Z_QC_NO_QC,Quality.Z_QC_NO_QC,Quality.Z_QC_NO_QC,Quality.Z_QC_NO_QC,Quality.Z_QC_NO_QC};

    /**
     * Instantiates a new QC element.
     * @param value   the value
     */
    public QCEle(T value){
        this.value = value;
        this.quality = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            quality.add(Quality.Z_QC_NO_QC);
        }

    }

    /**
     * Instantiates a new QC element.
     * @param value   the value
     * @param quality the quality
     */
    public QCEle(T value, List<Quality> quality) {
        this.value = value;
        this.quality = quality;
    }

    /**
     * Instantiates a new QC element.
     * @param value   the value
     * @param quality the quality
     */
    public QCEle(T value, Quality... quality) {
        this.value = value;
        this.quality = Arrays.asList(quality);

    }

    /**
     * Instantiates a new QC element.
     * @param value       the value
     * @param qualityCode the quality code
     */
    public QCEle(T value, int... qualityCode) {
        this.value = value;
        this.quality = new ArrayList<Quality>();
        for (int c : qualityCode) {
            this.quality.add(Quality.getQualityByCode(c));
        }
    }

    /**
     * Gets the value.
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Gets the quality.
     * @return the quality
     */
    public List<Quality> getQuality() {
        return quality;
    }

    /**
     * Sets the quality.
     * @param quality the new quality
     */
    public void setQuality(List<Quality> quality) {
        this.quality = quality;
    }


    public Quality[] getRecords() {
        return records;
    }

    public void setRecords(Quality[] records) {
        this.records = records;
    }


}
