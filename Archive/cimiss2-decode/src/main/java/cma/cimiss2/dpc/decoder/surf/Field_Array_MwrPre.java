package cma.cimiss2.dpc.decoder.surf;
/**
 * 
 * <br>
 * @Title:  Field_Array_MwrPre.java   
 * @Package org.cimiss2.decode.z_surf_mwr_pre   
 * @Description:    TODO(水利部降水报文头结构体)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月2日 上午10:45:07   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public enum Field_Array_MwrPre {
	
	StationCode(0, "站号"),
	ObservationTime(1, "日期"),
	RainFall(2, "雨量");
	
	
	public static int FIELD_ARRAY_SIZE = 3;
	private int idx;
	private String Description;
	/**
	 * 
	 * @Title:  Field_Array_MwrPre   
	 * @Description:    TODO(构造函数)   
	 * @param  idx 序号
	 * @param  Description  名称
	 * @throws:
	 */
	Field_Array_MwrPre (int idx, String Description){
		this.setIdx(idx);
		this.setDescription(Description);
	}
	/**
	 * 
	 * @Title: getFieldArray   
	 * @Description: TODO(获得某个字段的名称)   
	 * @param idx 序号 
	 * @return: Field_Array_MwrPre      
	 * @throws:
	 */
	public static Field_Array_MwrPre getFieldArray(int idx){
		for(Field_Array_MwrPre field_Array : Field_Array_MwrPre.values()){
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
