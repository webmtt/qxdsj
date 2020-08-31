package cma.cimiss2.dpc.decoder.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cma.cimiss2.dpc.decoder.tools.enumeration.Quality;


// TODO: Auto-generated Javadoc
/**
 * 将要素值和质控码包裹在一起.
 *
 * @param <T>            为要素值类型
 */
public class QCElement<T> implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** 要素值. */
	T value;
	
	/** 质控码，可能是1级或多级，如国省县3级. */
	List<Quality> quality;
	
	/**
	 * Instantiates a new QC element.
	 *
	 * @param value the value
	 * @param quality the quality
	 */
	public QCElement(T value, List<Quality> quality) {
		this.value = value;
		this.quality = quality;
	}
	
	/**
	 * Instantiates a new QC element.
	 *
	 * @param value the value
	 * @param quality the quality
	 */
	public QCElement(T value, Quality... quality) {
		this.value = value;
		this.quality = Arrays.asList(quality);
	}

	/**
	 * Instantiates a new QC element.
	 *
	 * @param value the value
	 * @param qualityCode the quality code
	 */
	public QCElement(T value, int... qualityCode) {
		this.value = value;
		this.quality = new ArrayList<Quality>();
		for (int c : qualityCode) {
			this.quality.add(Quality.getQualityByCode(c));
		}
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Gets the quality.
	 *
	 * @return the quality
	 */
	public List<Quality> getQuality() {
		return quality;
	}

	/**
	 * Sets the quality.
	 *
	 * @param quality the new quality
	 */
	public void setQuality(List<Quality> quality) {
		this.quality = quality;
	}

}