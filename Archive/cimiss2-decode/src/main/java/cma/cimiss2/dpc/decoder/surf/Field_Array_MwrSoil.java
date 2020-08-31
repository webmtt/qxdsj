package cma.cimiss2.dpc.decoder.surf;
/**
 * 
 * <br>
 * @Title:  Field_Array_MwrPre.java   
 * @Package org.cimiss2.decode.z_surf_mwr_soil   
 * @Description:    TODO(水利部土壤墒情报头结构体)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月2日 上午10:49:38   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public enum Field_Array_MwrSoil {
	
	StationCode(0, "测站编码"),
	StationName(1, "测站名称"),
	ObservationTime(2, "时间"),
	WaterContent10(3, "深度含水率10cm"),
	WaterContent20(4, "深度含水率20cm"),
	WaterContent30(5, "深度含水率30cm"),
	WaterContent40(6, "深度含水率40cm"),
	WaterContent60(7, "深度含水率60cm"),
	WaterContetn80(8, "深度含水率80cm"),
	WaterContent100(9, "深度含水率100cm");
	
	
	public static int FIELD_ARRAY_SIZE = 10;
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
	Field_Array_MwrSoil (int idx, String Description){
		this.setIdx(idx);
		this.setDescription(Description);
	}
	/**
	 * 
	 * @Title: getFieldArray   
	 * @Description: TODO(获得某个字段的名称)   
	 * @param idx  序号  
	 * @return: Field_Array_MwrPre      
	 * @throws:
	 */
	public static Field_Array_MwrSoil getFieldArray(int idx){
		for(Field_Array_MwrSoil field_Array : Field_Array_MwrSoil.values()){
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
