package cma.cimiss2.dpc.quickqc.bean;

import java.util.Calendar;

/**
 * The type Max month para info.
 * @author :when6passBye
 */
public class MaxMonthParaInfo extends BaseStationInfo{
	private double jan_max_value;
	private double feb_max_value;
	private double mar_max_value;
	private double apr_max_value;
	private double may_max_value;
	private double jun_max_value;
	private double jul_max_value;
	private double aug_max_value;
	private double sep_max_value;
	private double oct_max_value;
	private double nov_max_value;
	private double dec_max_value;

	/**
	 * Gets jan max value.
	 *
	 * @return the jan max value
	 */
	public double getJan_max_value() {
		return jan_max_value;
	}

	/**
	 * Sets jan max value.
	 *
	 * @param jan_max_value the jan max value
	 */
	public void setJan_max_value(double jan_max_value) {
		this.jan_max_value = jan_max_value;
	}

	/**
	 * Gets feb max value.
	 *
	 * @return the feb max value
	 */
	public double getFeb_max_value() {
		return feb_max_value;
	}

	/**
	 * Sets feb max value.
	 *
	 * @param feb_max_value the feb max value
	 */
	public void setFeb_max_value(double feb_max_value) {
		this.feb_max_value = feb_max_value;
	}

	/**
	 * Gets mar max value.
	 *
	 * @return the mar max value
	 */
	public double getMar_max_value() {
		return mar_max_value;
	}

	/**
	 * Sets mar max value.
	 *
	 * @param mar_max_value the mar max value
	 */
	public void setMar_max_value(double mar_max_value) {
		this.mar_max_value = mar_max_value;
	}

	/**
	 * Gets apr max value.
	 *
	 * @return the apr max value
	 */
	public double getApr_max_value() {
		return apr_max_value;
	}

	/**
	 * Sets apr max value.
	 *
	 * @param apr_max_value the apr max value
	 */
	public void setApr_max_value(double apr_max_value) {
		this.apr_max_value = apr_max_value;
	}

	/**
	 * Gets may max value.
	 *
	 * @return the may max value
	 */
	public double getMay_max_value() {
		return may_max_value;
	}

	/**
	 * Sets may max value.
	 *
	 * @param may_max_value the may max value
	 */
	public void setMay_max_value(double may_max_value) {
		this.may_max_value = may_max_value;
	}

	/**
	 * Gets jun max value.
	 *
	 * @return the jun max value
	 */
	public double getJun_max_value() {
		return jun_max_value;
	}

	/**
	 * Sets jun max value.
	 *
	 * @param jun_max_value the jun max value
	 */
	public void setJun_max_value(double jun_max_value) {
		this.jun_max_value = jun_max_value;
	}

	/**
	 * Gets jul max value.
	 *
	 * @return the jul max value
	 */
	public double getJul_max_value() {
		return jul_max_value;
	}

	/**
	 * Sets jul max value.
	 *
	 * @param jul_max_value the jul max value
	 */
	public void setJul_max_value(double jul_max_value) {
		this.jul_max_value = jul_max_value;
	}

	/**
	 * Gets aug max value.
	 *
	 * @return the aug max value
	 */
	public double getAug_max_value() {
		return aug_max_value;
	}

	/**
	 * Sets aug max value.
	 *
	 * @param aug_max_value the aug max value
	 */
	public void setAug_max_value(double aug_max_value) {
		this.aug_max_value = aug_max_value;
	}

	/**
	 * Gets sep max value.
	 *
	 * @return the sep max value
	 */
	public double getSep_max_value() {
		return sep_max_value;
	}

	/**
	 * Sets sep max value.
	 *
	 * @param sep_max_value the sep max value
	 */
	public void setSep_max_value(double sep_max_value) {
		this.sep_max_value = sep_max_value;
	}

	/**
	 * Gets oct max value.
	 *
	 * @return the oct max value
	 */
	public double getOct_max_value() {
		return oct_max_value;
	}

	/**
	 * Sets oct max value.
	 *
	 * @param oct_max_value the oct max value
	 */
	public void setOct_max_value(double oct_max_value) {
		this.oct_max_value = oct_max_value;
	}

	/**
	 * Gets nov max value.
	 *
	 * @return the nov max value
	 */
	public double getNov_max_value() {
		return nov_max_value;
	}

	/**
	 * Sets nov max value.
	 *
	 * @param nov_max_value the nov max value
	 */
	public void setNov_max_value(double nov_max_value) {
		this.nov_max_value = nov_max_value;
	}

	/**
	 * Gets dec max value.
	 *
	 * @return the dec max value
	 */
	public double getDec_max_value() {
		return dec_max_value;
	}

	/**
	 * Sets dec max value.
	 *
	 * @param dec_max_value the dec max value
	 */
	public void setDec_max_value(double dec_max_value) {
		this.dec_max_value = dec_max_value;
	}

	/**
	 * Get mon max value double.
	 *
	 * @param mon the mon
	 * @return the double
	 */
	public double getMonMaxValue(int mon){
		if(mon<1||mon>12){
			throw new RuntimeException("not have this month:"+mon);
		}
		switch (mon-1){
			case Calendar.JANUARY:
				return getJan_max_value();
			case Calendar.FEBRUARY:
				return getFeb_max_value();
			case Calendar.MARCH:
				return getMar_max_value();
			case Calendar.APRIL:
				return getApr_max_value();
			case Calendar.MAY:
				return getMay_max_value();
			case Calendar.JUNE:
				return getJun_max_value();
			case Calendar.JULY:
				return getJul_max_value();
			case Calendar.AUGUST:
				return getAug_max_value();
			case Calendar.SEPTEMBER:
				return getSep_max_value();
			case Calendar.OCTOBER:
				return getOct_max_value();
			case Calendar.NOVEMBER:
				return getNov_max_value();
			case Calendar.DECEMBER:
				return getDec_max_value();
				default:
					break;
		}
		return 0;
	}

	/**
	 * Set mon max value.
	 *
	 * @param mon   the mon
	 * @param value the value
	 */
	public void setMonMaxValue(int mon,double value){
        if(mon<1||mon>12){
            throw new RuntimeException("not have this month:"+mon);
        }
        switch (mon-1){
            case Calendar.JANUARY:
                setJan_max_value(value);
                break;
            case Calendar.FEBRUARY:
                setFeb_max_value(value);
				break;
            case Calendar.MARCH:
                setMar_max_value(value);
				break;
            case Calendar.APRIL:
                setApr_max_value(value);
				break;
            case Calendar.MAY:
                setMay_max_value(value);
				break;
            case Calendar.JUNE:
                setJun_max_value(value);
				break;
            case Calendar.JULY:
                setJul_max_value(value);
				break;
            case Calendar.AUGUST:
                setAug_max_value(value);
				break;
            case Calendar.SEPTEMBER:
                setSep_max_value(value);
				break;
            case Calendar.OCTOBER:
                setOct_max_value(value);
				break;
            case Calendar.NOVEMBER:
                setNov_max_value(value);
				break;
            case Calendar.DECEMBER:
                setDec_max_value(value);
				break;
                default:
                	break;
        }
    }
	

}
