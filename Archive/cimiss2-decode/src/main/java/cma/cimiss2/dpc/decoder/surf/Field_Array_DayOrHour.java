



package cma.cimiss2.dpc.decoder.surf;

/**
 * 
 * <br>
 * @Title:  Field_Array_DayOrHour.java   
 * @Package cma.cimiss2.dpc.decoder.surf
 * @Description:    TODO(水利部降水报文头结构体-3)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年3月19日  liyamin   Initial creation.
 * </pre>
 * 
 * @author liyamin
 *
 *
 */
public enum Field_Array_DayOrHour {
	
	ObservationTime(0, "时间"),
	StationCode(1,"站号"),
	StationName(2, "站名"),
	RainFall(3, "雨量"),
	Latitude(4, "纬度"),
	Longtitude(5, "经度");
	
	
	public static int FIELD_ARRAY_SIZE = 6;
	private int idx;
	private String Description;

	/**
	 * @Title:  Field_Array_DayOrHour   
	 * @Description:    TODO(构造函数)   
	 * @param:  idx 序号
	 * @param:  Description  名称
	 * @throws
	 */
	Field_Array_DayOrHour (int idx, String Description){
		this.setIdx(idx);
		this.setDescription(Description);
	}

	public static Field_Array_DayOrHour getFieldArray(int idx){
		for(Field_Array_DayOrHour field_Array : Field_Array_DayOrHour.values()){
			if (field_Array.getIdx() == idx) {
				return field_Array;
			}
		}
		throw new IllegalArgumentException();	
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
}
