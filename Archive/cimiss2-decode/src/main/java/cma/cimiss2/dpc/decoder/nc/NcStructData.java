package cma.cimiss2.dpc.decoder.nc;

import java.util.Date;

public class NcStructData {

	/**
	 * 要素代码
	 */
	private String element;
	/**
	 * 资料时次:yyyyMMddHHmmss
	 */
	private Date time;
	/**
	 * 预报时效
	 */
	private String validTime;
	/**
	 * 层次类型
	 */
	private int levelType;
	/**
	 * 层次1
	 */
	private int level1;
	/**
	 * 层次2
	 */
	private int level2;
	/**
	 * 每个格点场数据占的字节
	 */
	private int valueByteNum = 4;
	/**
	 * 格点场数据的精度
	 */
	private int valuePrecision = 10;
	/**
	 * 格点单位类型，默认为：0-经纬度
	 */
	private int gridUnits = 0;
	/**
	 * 格点投影类型，即Grib2:第3段的模板号,Grib1:第2段的数据表示类型
	 */
	private int gridProject;
	/**
	 * 开始经度
	 */
	private float startX;
	/**
	 * 开始纬度
	 */
	private float startY;
	/**
	 * 结束经度
	 */
	private float endX;
	/**
	 * 结束纬度
	 */
	private float endY;
	/**
	 * 经度间隔（分辨率）
	 */
	private float xStep;
	/**
	 * 纬度间隔（分辨率）
	 */
	private float yStep;
	/**
	 * 经度格点数
	 */
	private int xCount;
	/**
	 * 纬度格点数
	 */
	private int yCount;
	/**
	 * 高度数，模式数据为单层，为1
	 */
	private int heightCount;
	/**
	 * 高度值，模式数据取{0}
	 */
	private float[] heights;
	/**
	 * 加工中心（预报中心），如无时用字符串0
	 */
	private String productCenter = "0";
	/**
	 * 制作方法（预报方法），如无时用字符串0
	 */
	private String productMethod = "0";
	/**
	 * 产品描述
	 */
	private String productDescription = "";
	/**
	 * 四级编码
	 */
	private String data_id = "";
	/**
	 * 入库时间
	 */
	private String IYMDHM;
	private Date event_time;
	/**
	 * 格点场数据
	 */
	private float[] data;

	// 入mysql索引表需要字段
	// 主表：NAS大文件总表
	/**
	 * 模式主表记录编号：四级编码+要素+资料时间+区域
	 */
	private String D_FILE_ID;
	/**
	 * 第一个场文件收到时间
	 */
	private String D_RYMDHM;
	/**
	 * 最后一次更新时间
	 */
	private String D_UPDATETIME;
	/**
	 * 资料时次，格式：YYYY-MM-DD HH24:MI:SS
	 */
	private String DATETIME;
	/**
	 * 年
	 */
	private int V04001;
	/**
	 * 月
	 */
	private int V04002;
	/**
	 * 日
	 */
	private int V04003;
	/**
	 * 时
	 */
	private int V04004;
	/**
	 * 分
	 */
	private int V04005;
	/**
	 * 秒
	 */
	private int V04006;
	/**
	 * 加工中心代码
	 */
	private int V01033;
	/**
	 * 子中心标识
	 */
	private int V01034;
	/**
	 * 资料加工过程（模式）标识
	 */
	private int V_GENPRO_TYPE;
	/**
	 * 区域
	 */
	private String V_AREACODE;
	/**
	 * 数据存储路径：完整路径(包含文件名)
	 */
	private String D_STORAGE_SITE;
	/**
	 * NAS存储文件名
	 */
	private String V_FILE_NAME;
	/**
	 * 文件大小
	 */
	private long D_FILE_SIZE;
	/**
	 * 归档标识，0：未归档 ，1：已归档
	 */
	private int D_ARCHIVE_FLAG = 0;
	/**
	 * 文件格式：GRIB1，GRIB2，NETCDF3，NETCDF4，HDF5
	 */
	private String V_FILE_FORMAT;
	/**
	 * 存储状态: 0：高端；1：大容量；2：磁带；3：回迁;4: 实时存储;
	 */
	private int D_FILE_SAVE_HIERARCHY;

	// 子表：每个场数据表
	/**
	 * 预报时效
	 */
	private int V04320;
	/**
	 * 场类型
	 */
	private int V_FIELD_TYPE;
	/**
	 * 通信系统发来的原始文件名
	 */
	private String V_FILE_NAME_SOURCE;
	/**
	 * 拆分后的单场文件名
	 */
	private String V_FIELD_FILE_NAME_SOURCE;
	/**
	 * 保留字段1
	 */
	private int V_RETAIN1;
	/**
	 * 保留字段2
	 */
	private int V_RETAIN2;
	/**
	 * 保留字段3
	 */
	private int V_RETAIN3;
	
	/**
	 * 资料制作时间
	 */
	private String makeTime;

	/**
	 * @Title:  getElement <BR>
	 * @Description: 要素代码 getter <BR>
	 * @return element 要素代码 <BR>
	 */
	public String getElement() {
		return element;
	}

	/**
	 * @Title:  setElement <BR>
	 * @Description: 要素代码 setter <BR>
	 * @param element 要素代码 <BR>
	 */
	public void setElement(String element) {
		this.element = element;
	}

	/**
	 * @Title:  getTime <BR>
	 * @Description: 资料时次:yyyyMMddHHmmss getter <BR>
	 * @return time 资料时次:yyyyMMddHHmmss <BR>
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @Title:  setTime <BR>
	 * @Description: 资料时次:yyyyMMddHHmmss setter <BR>
	 * @param time 资料时次:yyyyMMddHHmmss <BR>
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @Title:  getValidTime <BR>
	 * @Description: 预报时效 getter <BR>
	 * @return validTime 预报时效 <BR>
	 */
	public String getValidTime() {
		return validTime;
	}

	/**
	 * @Title:  setValidTime <BR>
	 * @Description: 预报时效 setter <BR>
	 * @param validTime 预报时效 <BR>
	 */
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	/**
	 * @Title:  getLevelType <BR>
	 * @Description: 层次类型 getter <BR>
	 * @return levelType 层次类型 <BR>
	 */
	public int getLevelType() {
		return levelType;
	}

	/**
	 * @Title:  setLevelType <BR>
	 * @Description: 层次类型 setter <BR>
	 * @param levelType 层次类型 <BR>
	 */
	public void setLevelType(int levelType) {
		this.levelType = levelType;
	}

	/**
	 * @Title:  getLevel1 <BR>
	 * @Description: 层次1 getter <BR>
	 * @return level1 层次1 <BR>
	 */
	public int getLevel1() {
		return level1;
	}

	/**
	 * @Title:  setLevel1 <BR>
	 * @Description: 层次1 setter <BR>
	 * @param level1 层次1 <BR>
	 */
	public void setLevel1(int level1) {
		this.level1 = level1;
	}

	/**
	 * @Title:  getLevel2 <BR>
	 * @Description: 层次2 getter <BR>
	 * @return level2 层次2 <BR>
	 */
	public int getLevel2() {
		return level2;
	}

	/**
	 * @Title:  setLevel2 <BR>
	 * @Description: 层次2 setter <BR>
	 * @param level2 层次2 <BR>
	 */
	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	/**
	 * @Title:  getValueByteNum <BR>
	 * @Description: 每个格点场数据占的字节 getter <BR>
	 * @return valueByteNum 每个格点场数据占的字节 <BR>
	 */
	public int getValueByteNum() {
		return valueByteNum;
	}

	/**
	 * @Title:  setValueByteNum <BR>
	 * @Description: 每个格点场数据占的字节 setter <BR>
	 * @param valueByteNum 每个格点场数据占的字节 <BR>
	 */
	public void setValueByteNum(int valueByteNum) {
		this.valueByteNum = valueByteNum;
	}

	/**
	 * @Title:  getValuePrecision <BR>
	 * @Description: 格点场数据的精度 getter <BR>
	 * @return valuePrecision 格点场数据的精度 <BR>
	 */
	public int getValuePrecision() {
		return valuePrecision;
	}

	/**
	 * @Title:  setValuePrecision <BR>
	 * @Description: 格点场数据的精度 setter <BR>
	 * @param valuePrecision 格点场数据的精度 <BR>
	 */
	public void setValuePrecision(int valuePrecision) {
		this.valuePrecision = valuePrecision;
	}

	/**
	 * @Title:  getGridUnits <BR>
	 * @Description: 格点单位类型，默认为：0-经纬度 getter <BR>
	 * @return gridUnits 格点单位类型，默认为：0-经纬度 <BR>
	 */
	public int getGridUnits() {
		return gridUnits;
	}

	/**
	 * @Title:  setGridUnits <BR>
	 * @Description: 格点单位类型，默认为：0-经纬度 setter <BR>
	 * @param gridUnits 格点单位类型，默认为：0-经纬度 <BR>
	 */
	public void setGridUnits(int gridUnits) {
		this.gridUnits = gridUnits;
	}

	/**
	 * @Title:  getGridProject <BR>
	 * @Description: 格点投影类型，即Grib2:第3段的模板号,Grib1:第2段的数据表示类型 getter <BR>
	 * @return gridProject 格点投影类型，即Grib2:第3段的模板号,Grib1:第2段的数据表示类型 <BR>
	 */
	public int getGridProject() {
		return gridProject;
	}

	/**
	 * @Title:  setGridProject <BR>
	 * @Description: 格点投影类型，即Grib2:第3段的模板号,Grib1:第2段的数据表示类型 setter <BR>
	 * @param gridProject 格点投影类型，即Grib2:第3段的模板号,Grib1:第2段的数据表示类型 <BR>
	 */
	public void setGridProject(int gridProject) {
		this.gridProject = gridProject;
	}

	/**
	 * @Title:  getStartX <BR>
	 * @Description: 开始经度 getter <BR>
	 * @return startX 开始经度 <BR>
	 */
	public float getStartX() {
		return startX;
	}

	/**
	 * @Title:  setStartX <BR>
	 * @Description: 开始经度 setter <BR>
	 * @param startX 开始经度 <BR>
	 */
	public void setStartX(float startX) {
		this.startX = startX;
	}

	/**
	 * @Title:  getStartY <BR>
	 * @Description: 开始纬度 getter <BR>
	 * @return startY 开始纬度 <BR>
	 */
	public float getStartY() {
		return startY;
	}

	/**
	 * @Title:  setStartY <BR>
	 * @Description: 开始纬度 setter <BR>
	 * @param startY 开始纬度 <BR>
	 */
	public void setStartY(float startY) {
		this.startY = startY;
	}

	/**
	 * @Title:  getEndX <BR>
	 * @Description: 结束经度 getter <BR>
	 * @return endX 结束经度 <BR>
	 */
	public float getEndX() {
		return endX;
	}

	/**
	 * @Title:  setEndX <BR>
	 * @Description: 结束经度 setter <BR>
	 * @param endX 结束经度 <BR>
	 */
	public void setEndX(float endX) {
		this.endX = endX;
	}

	/**
	 * @Title:  getEndY <BR>
	 * @Description: 结束纬度 getter <BR>
	 * @return endY 结束纬度 <BR>
	 */
	public float getEndY() {
		return endY;
	}

	/**
	 * @Title:  setEndY <BR>
	 * @Description: 结束纬度 setter <BR>
	 * @param endY 结束纬度 <BR>
	 */
	public void setEndY(float endY) {
		this.endY = endY;
	}

	/**
	 * @Title:  getXStep <BR>
	 * @Description: 经度间隔（分辨率） getter <BR>
	 * @return xStep 经度间隔（分辨率） <BR>
	 */
	public float getXStep() {
		return xStep;
	}

	/**
	 * @Title:  setxStep <BR>
	 * @Description: 经度间隔（分辨率） setter <BR>
	 * @param XStep 经度间隔（分辨率） <BR>
	 */
	public void setXStep(float XStep) {
		this.xStep = XStep;
	}

	/**
	 * @Title:  getyStep <BR>
	 * @Description: 纬度间隔（分辨率） getter <BR>
	 * @return yStep 纬度间隔（分辨率） <BR>
	 */
	public float getYStep() {
		return yStep;
	}

	/**
	 * @Title:  setyStep <BR>
	 * @Description: 纬度间隔（分辨率） setter <BR>
	 * @param yStep 纬度间隔（分辨率） <BR>
	 */
	public void setYStep(float yStep) {
		this.yStep = yStep;
	}

	/**
	 * @Title:  getxCount <BR>
	 * @Description: 经度格点数 getter <BR>
	 * @return xCount 经度格点数 <BR>
	 */
	public int getXCount() {
		return xCount;
	}

	/**
	 * @Title:  setxCount <BR>
	 * @Description: 经度格点数 setter <BR>
	 * @param xCount 经度格点数 <BR>
	 */
	public void setXCount(int xCount) {
		this.xCount = xCount;
	}

	/**
	 * @Title:  getyCount <BR>
	 * @Description:  纬度格点数 getter <BR>
	 * @return yCount  纬度格点数 <BR>
	 */
	public int getYCount() {
		return yCount;
	}

	/**
	 * @Title:  setyCount <BR>
	 * @Description:  纬度格点数 setter <BR>
	 * @param yCount  纬度格点数 <BR>
	 */
	public void setYCount(int yCount) {
		this.yCount = yCount;
	}

	/**
	 * @Title:  getHeightCount <BR>
	 * @Description: 高度数，模式数据为单层，为1 getter <BR>
	 * @return heightCount 高度数，模式数据为单层，为1 <BR>
	 */
	public int getHeightCount() {
		return heightCount;
	}

	/**
	 * @Title:  setHeightCount <BR>
	 * @Description: 高度数，模式数据为单层，为1 setter <BR>
	 * @param heightCount 高度数，模式数据为单层，为1 <BR>
	 */
	public void setHeightCount(int heightCount) {
		this.heightCount = heightCount;
	}

	/**
	 * @Title:  getHeights <BR>
	 * @Description: 高度值，模式数据取{0} getter <BR>
	 * @return heights 高度值，模式数据取{0} <BR>
	 */
	public float[] getHeights() {
		return heights;
	}

	/**
	 * @Title:  setHeights <BR>
	 * @Description: 高度值，模式数据取{0} setter <BR>
	 * @param heights 高度值，模式数据取{0} <BR>
	 */
	public void setHeights(float[] heights) {
		this.heights = heights;
	}

	/**
	 * @Title:  getProductCenter <BR>
	 * @Description: 加工中心（预报中心），如无时用字符串0 getter <BR>
	 * @return productCenter 加工中心（预报中心），如无时用字符串0 <BR>
	 */
	public String getProductCenter() {
		return productCenter;
	}

	/**
	 * @Title:  setProductCenter <BR>
	 * @Description: 加工中心（预报中心），如无时用字符串0 setter <BR>
	 * @param productCenter 加工中心（预报中心），如无时用字符串0 <BR>
	 */
	public void setProductCenter(String productCenter) {
		this.productCenter = productCenter;
	}

	/**
	 * @Title:  getProductMethod <BR>
	 * @Description: 制作方法（预报方法），如无时用字符串0 getter <BR>
	 * @return productMethod 制作方法（预报方法），如无时用字符串0 <BR>
	 */
	public String getProductMethod() {
		return productMethod;
	}

	/**
	 * @Title:  setProductMethod <BR>
	 * @Description: 制作方法（预报方法），如无时用字符串0 setter <BR>
	 * @param productMethod 制作方法（预报方法），如无时用字符串0 <BR>
	 */
	public void setProductMethod(String productMethod) {
		this.productMethod = productMethod;
	}

	/**
	 * @Title:  getProductDescription <BR>
	 * @Description: 产品描述 getter <BR>
	 * @return productDescription 产品描述 <BR>
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * @Title:  setProductDescription <BR>
	 * @Description: 产品描述 setter <BR>
	 * @param productDescription 产品描述 <BR>
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * @Title:  getData_id <BR>
	 * @Description: 四级编码 getter <BR>
	 * @return data_id 四级编码 <BR>
	 */
	public String getData_id() {
		return data_id;
	}

	/**
	 * @Title:  setData_id <BR>
	 * @Description: 四级编码 setter <BR>
	 * @param data_id 四级编码 <BR>
	 */
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}

	/**
	 * @Title:  getIYMDHM <BR>
	 * @Description: 入库时间 getter <BR>
	 * @return iYMDHM 入库时间 <BR>
	 */
	public String getIYMDHM() {
		return IYMDHM;
	}

	/**
	 * @Title:  setIYMDHM <BR>
	 * @Description: 入库时间 setter <BR>
	 * @param iYMDHM 入库时间 <BR>
	 */
	public void setIYMDHM(String iYMDHM) {
		IYMDHM = iYMDHM;
	}

	/**
	 * @Title:  getEvent_time <BR>
	 * @Description: bare_field_comment getter <BR>
	 * @return event_time {bare_field_comment} <BR>
	 */
	public Date getEvent_time() {
		return event_time;
	}

	/**
	 * @Title:  setEvent_time <BR>
	 * @Description: #{bare_field_comment} setter <BR>
	 * @param event_time #{bare_field_comment} <BR>
	 */
	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}

	/**
	 * @Title:  getData <BR>
	 * @Description: 格点场数据 getter <BR>
	 * @return data 格点场数据 <BR>
	 */
	public float[] getData() {
		return data;
	}

	/**
	 * @Title:  setData <BR>
	 * @Description: 格点场数据 setter <BR>
	 * @param data 格点场数据 <BR>
	 */
	public void setData(float[] data) {
		this.data = data;
	}

	/**
	 * @Title:  getD_FILE_ID <BR>
	 * @Description: 模式主表记录编号：四级编码+要素+资料时间+区域 getter <BR>
	 * @return D_FILE_ID 模式主表记录编号：四级编码+要素+资料时间+区域 <BR>
	 */
	public String getD_FILE_ID() {
		return D_FILE_ID;
	}

	/**
	 * @Title:  setD_FILE_ID <BR>
	 * @Description: 模式主表记录编号：四级编码+要素+资料时间+区域 setter <BR>
	 * @param d_FILE_ID 模式主表记录编号：四级编码+要素+资料时间+区域 <BR>
	 */
	public void setD_FILE_ID(String D_FILE_ID) {
		this.D_FILE_ID = D_FILE_ID;
	}

	/**
	 * @Title:  getD_RYMDHM <BR>
	 * @Description: 第一个场文件收到时间 getter <BR>
	 * @return D_RYMDHM 第一个场文件收到时间 <BR>
	 */
	public String getD_RYMDHM() {
		return D_RYMDHM;
	}

	/**
	 * @Title:  setD_RYMDHM <BR>
	 * @Description: 第一个场文件收到时间 setter <BR>
	 * @param D_RYMDHM 第一个场文件收到时间 <BR>
	 */
	public void setD_RYMDHM(String D_RYMDHM) {
		this.D_RYMDHM = D_RYMDHM;
	}

	/**
	 * @Title:  getD_UPDATETIME <BR>
	 * @Description: 最后一次更新时间 getter <BR>
	 * @return D_UPDATETIME 最后一次更新时间 <BR>
	 */
	public String getD_UPDATETIME() {
		return D_UPDATETIME;
	}

	/**
	 * @Title:  setD_UPDATETIME <BR>
	 * @Description: 最后一次更新时间 setter <BR>
	 * @param D_UPDATETIME 最后一次更新时间 <BR>
	 */
	public void setD_UPDATETIME(String D_UPDATETIME) {
		this.D_UPDATETIME = D_UPDATETIME;
	}

	/**
	 * @Title:  getDATETIME <BR>
	 * @Description: 资料时次，格式：YYYY-MM-DD HH24:MI:SS getter <BR>
	 * @return DATETIME 资料时次，格式：YYYY-MM-DD HH24:MI:SS <BR>
	 */
	public String getDATETIME() {
		return DATETIME;
	}

	/**
	 * @Title:  setDATETIME <BR>
	 * @Description: 资料时次，格式：YYYY-MM-DD HH24:MI:SS setter <BR>
	 * @param DATETIME 资料时次，格式：YYYY-MM-DD HH24:MI:SS <BR>
	 */
	public void setDATETIME(String DATETIME) {
		this.DATETIME = DATETIME;
	}

	/**
	 * @Title:  getV04001 <BR>
	 * @Description: 年 getter <BR>
	 * @return V04001 年 <BR>
	 */
	public int getV04001() {
		return V04001;
	}

	/**
	 * @Title:  setV04001 <BR>
	 * @Description: 年 setter <BR>
	 * @param v04001 年 <BR>
	 */
	public void setV04001(int V04001) {
		this.V04001 = V04001;
	}

	/**
	 * @Title:  getV04002 <BR>
	 * @Description: 月 getter <BR>
	 * @return V04002 月 <BR>
	 */
	public int getV04002() {
		return V04002;
	}

	/**
	 * @Title:  setV04002 <BR>
	 * @Description: 月 setter <BR>
	 * @param V04002 月 <BR>
	 */
	public void setV04002(int V04002) {
		this.V04002 = V04002;
	}

	/**
	 * @Title:  getV04003 <BR>
	 * @Description: 日 getter <BR>
	 * @return V04003 日 <BR>
	 */
	public int getV04003() {
		return V04003;
	}

	/**
	 * @Title:  setV04003 <BR>
	 * @Description: 日 setter <BR>
	 * @param v04003 日 <BR>
	 */
	public void setV04003(int V04003) {
		this.V04003 = V04003;
	}

	/**
	 * @Title:  getV04004 <BR>
	 * @Description: 时 getter <BR>
	 * @return V04004 时 <BR>
	 */
	public int getV04004() {
		return V04004;
	}

	/**
	 * @Title:  setV04004 <BR>
	 * @Description: 时 setter <BR>
	 * @param V04004 时 <BR>
	 */
	public void setV04004(int V04004) {
		this.V04004 = V04004;
	}

	/**
	 * @Title:  getV04005 <BR>
	 * @Description: 分 getter <BR>
	 * @return V04005 分 <BR>
	 */
	public int getV04005() {
		return V04005;
	}

	/**
	 * @Title:  setV04005 <BR>
	 * @Description: 分 setter <BR>
	 * @param v04005 分 <BR>
	 */
	public void setV04005(int V04005) {
		this.V04005 = V04005;
	}

	/**
	 * @Title:  getV04006 <BR>
	 * @Description: 秒 getter <BR>
	 * @return V04006 秒 <BR>
	 */
	public int getV04006() {
		return V04006;
	}

	/**
	 * @Title:  setV04006 <BR>
	 * @Description: 秒 setter <BR>
	 * @param v04006 秒 <BR>
	 */
	public void setV04006(int V04006) {
		this.V04006 = V04006;
	}

	/**
	 * @Title:  getV01033 <BR>
	 * @Description: 加工中心代码 getter <BR>
	 * @return V01033 加工中心代码 <BR>
	 */
	public int getV01033() {
		return V01033;
	}

	/**
	 * @Title:  setV01033 <BR>
	 * @Description: 加工中心代码 setter <BR>
	 * @param V01033 加工中心代码 <BR>
	 */
	public void setV01033(int V01033) {
		this.V01033 = V01033;
	}

	/**
	 * @Title:  getV01034 <BR>
	 * @Description: 子中心标识 getter <BR>
	 * @return V01034 子中心标识 <BR>
	 */
	public int getV01034() {
		return V01034;
	}

	/**
	 * @Title:  setV01034 <BR>
	 * @Description: 子中心标识 setter <BR>
	 * @param V01034 子中心标识 <BR>
	 */
	public void setV01034(int V01034) {
		this.V01034 = V01034;
	}

	/**
	 * @Title:  getV_GENPRO_TYPE <BR>
	 * @Description: 资料加工过程（模式）标识 getter <BR>
	 * @return V_GENPRO_TYPE 资料加工过程（模式）标识 <BR>
	 */
	public int getV_GENPRO_TYPE() {
		return V_GENPRO_TYPE;
	}

	/**
	 * @Title:  setV_GENPRO_TYPE <BR>
	 * @Description: 资料加工过程（模式）标识 setter <BR>
	 * @param V_GENPRO_TYPE 资料加工过程（模式）标识 <BR>
	 */
	public void setV_GENPRO_TYPE(int V_GENPRO_TYPE) {
		this.V_GENPRO_TYPE = V_GENPRO_TYPE;
	}

	/**
	 * @Title:  getV_AREACODE <BR>
	 * @Description: 区域 getter <BR>
	 * @return V_AREACODE 区域 <BR>
	 */
	public String getV_AREACODE() {
		return V_AREACODE;
	}

	/**
	 * @Title:  setV_AREACODE <BR>
	 * @Description: 区域 setter <BR>
	 * @param V_AREACODE 区域 <BR>
	 */
	public void setV_AREACODE(String V_AREACODE) {
		this.V_AREACODE = V_AREACODE;
	}

	/**
	 * @Title:  getD_STORAGE_SITE <BR>
	 * @Description: 数据存储路径：完整路径(包含文件名) getter <BR>
	 * @return D_STORAGE_SITE 数据存储路径：完整路径(包含文件名) <BR>
	 */
	public String getD_STORAGE_SITE() {
		return D_STORAGE_SITE;
	}

	/**
	 * @Title:  setD_STORAGE_SITE <BR>
	 * @Description: 数据存储路径：完整路径(包含文件名) setter <BR>
	 * @param D_STORAGE_SITE 数据存储路径：完整路径(包含文件名) <BR>
	 */
	public void setD_STORAGE_SITE(String D_STORAGE_SITE) {
		this.D_STORAGE_SITE = D_STORAGE_SITE;
	}

	/**
	 * @Title:  getV_FILE_NAME <BR>
	 * @Description: NAS存储文件名 getter <BR>
	 * @return V_FILE_NAME NAS存储文件名 <BR>
	 */
	public String getV_FILE_NAME() {
		return V_FILE_NAME;
	}

	/**
	 * @Title:  setV_FILE_NAME <BR>
	 * @Description: NAS存储文件名 setter <BR>
	 * @param V_FILE_NAME NAS存储文件名 <BR>
	 */
	public void setV_FILE_NAME(String V_FILE_NAME) {
		this.V_FILE_NAME = V_FILE_NAME;
	}

	/**
	 * @Title:  getD_FILE_SIZE <BR>
	 * @Description: 文件大小 getter <BR>
	 * @return D_FILE_SIZE 文件大小 <BR>
	 */
	public long getD_FILE_SIZE() {
		return D_FILE_SIZE;
	}

	/**
	 * @Title:  setD_FILE_SIZE <BR>
	 * @Description: 文件大小 setter <BR>
	 * @param D_FILE_SIZE 文件大小 <BR>
	 */
	public void setD_FILE_SIZE(long D_FILE_SIZE) {
		this.D_FILE_SIZE = D_FILE_SIZE;
	}

	/**
	 * @Title:  getD_ARCHIVE_FLAG <BR>
	 * @Description: 归档标识，0：未归档 ，1：已归档 getter <BR>
	 * @return D_ARCHIVE_FLAG 归档标识，0：未归档 ，1：已归档 <BR>
	 */
	public int getD_ARCHIVE_FLAG() {
		return D_ARCHIVE_FLAG;
	}

	/**
	 * @Title:  setD_ARCHIVE_FLAG <BR>
	 * @Description: 归档标识，0：未归档 ，1：已归档 setter <BR>
	 * @param D_ARCHIVE_FLAG 归档标识，0：未归档 ，1：已归档 <BR>
	 */
	public void setD_ARCHIVE_FLAG(int D_ARCHIVE_FLAG) {
		this.D_ARCHIVE_FLAG = D_ARCHIVE_FLAG;
	}

	/**
	 * @Title:  getV_FILE_FORMAT <BR>
	 * @Description: 文件格式：GRIB1，GRIB2，NETCDF3，NETCDF4，HDF5 getter <BR>
	 * @return V_FILE_FORMAT 文件格式：GRIB1，GRIB2，NETCDF3，NETCDF4，HDF5 <BR>
	 */
	public String getV_FILE_FORMAT() {
		return V_FILE_FORMAT;
	}

	/**
	 * @Title:  setV_FILE_FORMAT <BR>
	 * @Description: 文件格式：GRIB1，GRIB2，NETCDF3，NETCDF4，HDF5 setter <BR>
	 * @param V_FILE_FORMAT 文件格式：GRIB1，GRIB2，NETCDF3，NETCDF4，HDF5 <BR>
	 */
	public void setV_FILE_FORMAT(String V_FILE_FORMAT) {
		this.V_FILE_FORMAT = V_FILE_FORMAT;
	}

	/**
	 * @Title:  getD_FILE_SAVE_HIERARCHY <BR>
	 * @Description: 存储状态: 0：高端；1：大容量；2：磁带；3：回迁;4: 实时存储; getter <BR>
	 * @return D_FILE_SAVE_HIERARCHY 存储状态: 0：高端；1：大容量；2：磁带；3：回迁;4: 实时存储; <BR>
	 */
	public int getD_FILE_SAVE_HIERARCHY() {
		return D_FILE_SAVE_HIERARCHY;
	}

	/**
	 * @Title:  setD_FILE_SAVE_HIERARCHY <BR>
	 * @Description: 存储状态: 0：高端；1：大容量；2：磁带；3：回迁;4: 实时存储; setter <BR>
	 * @param D_FILE_SAVE_HIERARCHY 存储状态: 0：高端；1：大容量；2：磁带；3：回迁;4: 实时存储; <BR>
	 */
	public void setD_FILE_SAVE_HIERARCHY(int D_FILE_SAVE_HIERARCHY) {
		this.D_FILE_SAVE_HIERARCHY = D_FILE_SAVE_HIERARCHY;
	}

	/**
	 * @Title:  getV04320 <BR>
	 * @Description: 预报时效 getter <BR>
	 * @return V04320 预报时效 <BR>
	 */
	public int getV04320() {
		return V04320;
	}

	/**
	 * @Title:  setV04320 <BR>
	 * @Description: 预报时效 setter <BR>
	 * @param V04320 预报时效 <BR>
	 */
	public void setV04320(int V04320) {
		this.V04320 = V04320;
	}

	/**
	 * @Title:  getV_FIELD_TYPE <BR>
	 * @Description: 场类型 getter <BR>
	 * @return V_FIELD_TYPE 场类型 <BR>
	 */
	public int getV_FIELD_TYPE() {
		return V_FIELD_TYPE;
	}

	/**
	 * @Title:  setV_FIELD_TYPE <BR>
	 * @Description: 场类型 setter <BR>
	 * @param V_FIELD_TYPE 场类型 <BR>
	 */
	public void setV_FIELD_TYPE(int V_FIELD_TYPE) {
		this.V_FIELD_TYPE = V_FIELD_TYPE;
	}

	/**
	 * @Title:  getV_FILE_NAME_SOURCE <BR>
	 * @Description: 通信系统发来的原始文件名 getter <BR>
	 * @return V_FILE_NAME_SOURCE 通信系统发来的原始文件名 <BR>
	 */
	public String getV_FILE_NAME_SOURCE() {
		return V_FILE_NAME_SOURCE;
	}

	/**
	 * @Title:  setV_FILE_NAME_SOURCE <BR>
	 * @Description: 通信系统发来的原始文件名 setter <BR>
	 * @param V_FILE_NAME_SOURCE 通信系统发来的原始文件名 <BR>
	 */
	public void setV_FILE_NAME_SOURCE(String V_FILE_NAME_SOURCE) {
		this.V_FILE_NAME_SOURCE = V_FILE_NAME_SOURCE;
	}

	/**
	 * @Title:  getV_FIELD_FILE_NAME_SOURCE <BR>
	 * @Description: 拆分后的单场文件名 getter <BR>
	 * @return V_FIELD_FILE_NAME_SOURCE 拆分后的单场文件名 <BR>
	 */
	public String getV_FIELD_FILE_NAME_SOURCE() {
		return V_FIELD_FILE_NAME_SOURCE;
	}

	/**
	 * @Title:  setV_FIELD_FILE_NAME_SOURCE <BR>
	 * @Description: 拆分后的单场文件名 setter <BR>
	 * @param V_FIELD_FILE_NAME_SOURCE 拆分后的单场文件名 <BR>
	 */
	public void setV_FIELD_FILE_NAME_SOURCE(String V_FIELD_FILE_NAME_SOURCE) {
		this.V_FIELD_FILE_NAME_SOURCE = V_FIELD_FILE_NAME_SOURCE;
	}

	/**
	 * @Title:  getV_RETAIN1 <BR>
	 * @Description: 保留字段1 getter <BR>
	 * @return V_RETAIN1 保留字段1 <BR>
	 */
	public int getV_RETAIN1() {
		return V_RETAIN1;
	}

	/**
	 * @Title:  setV_RETAIN1 <BR>
	 * @Description: 保留字段1 setter <BR>
	 * @param V_RETAIN1 保留字段1 <BR>
	 */
	public void setV_RETAIN1(int V_RETAIN1) {
		this.V_RETAIN1 = V_RETAIN1;
	}

	/**
	 * @Title:  getV_RETAIN2 <BR>
	 * @Description: 保留字段2 getter <BR>
	 * @return V_RETAIN2 保留字段2 <BR>
	 */
	public int getV_RETAIN2() {
		return V_RETAIN2;
	}

	/**
	 * @Title:  setV_RETAIN2 <BR>
	 * @Description:保留字段2 setter <BR>
	 * @param V_RETAIN2 保留字段2 <BR>
	 */
	public void setV_RETAIN2(int V_RETAIN2) {
		this.V_RETAIN2 = V_RETAIN2;
	}

	/**
	 * @Title:  getV_RETAIN3 <BR>
	 * @Description: 保留字段3 getter <BR>
	 * @return V_RETAIN3 保留字段3 <BR>
	 */
	public int getV_RETAIN3() {
		return V_RETAIN3;
	}

	/**
	 * @Title:  setV_RETAIN3 <BR>
	 * @Description: 保留字段3 setter <BR>
	 * @param V_RETAIN3 保留字段3 <BR>
	 */
	public void setV_RETAIN3(int V_RETAIN3) {
		this.V_RETAIN3 = V_RETAIN3;
	}

	/**
	 * @Title:  getMakeTime <BR>
	 * @Description: 资料制作时间 getter <BR>
	 * @return makeTime 资料制作时间 <BR>
	 */
	public String getMakeTime() {
		return makeTime;
	}

	/**
	 * @Title:  setMakeTime <BR>
	 * @Description: 资料制作时间 setter <BR>
	 * @param makeTime 资料制作时间 <BR>
	 */
	public void setMakeTime(String makeTime) {
		this.makeTime = makeTime;
	}
	
	

}
