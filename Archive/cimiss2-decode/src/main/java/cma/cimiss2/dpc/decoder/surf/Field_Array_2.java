package cma.cimiss2.dpc.decoder.surf;

/**
 * 
 * <br>
 * @Title:  Field_Array_2.java   
 * @Package cma.cimiss2.dpc.decoder.surf.mwr_pre   
 * @Description:    TODO(水利部降水报文头结构体-2)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年6月20日 上午9:05:56   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public enum Field_Array_2 {
	
	ObservationTime(0, "时间"),
	RainFall(1, "雨量"),
	Latitude(2, "纬度"),
	Longtitude(3, "经度");
	
	
	public static int FIELD_ARRAY_SIZE = 4;
	private int idx;
	private String Description;

	/**
	 * @Title:  Field_Array_2   
	 * @Description:    TODO(构造函数)   
	 * @param:  idx 序号
	 * @param:  Description  名称
	 * @throws
	 */
	Field_Array_2 (int idx, String Description){
		this.setIdx(idx);
		this.setDescription(Description);
	}

	public static Field_Array_2 getFieldArray(int idx){
		for(Field_Array_2 field_Array : Field_Array_2.values()){
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
