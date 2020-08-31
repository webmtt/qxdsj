package cma.cimiss2.dpc.decoder.grib;

import java.util.Date;

public class Grib_Struct_Data
{
	//入Cassandra
	private String element; //要素代码
	private String Time; //资料时次:yyyyMMddHHmmss
	private String ValidTime; //预报时效
	private int levelType; //层次类型
	private int level1; //层次1
	private int level1_orig; //层次1，原始的：不进行单位转换
	private int level2; //层次2
	private int level2_orig; //层次2，原始的：不进行单位转换
	private int ValueByteNum = 4; //每个格点场数据占的字节
	private int ValuePrecision = 10; //格点场数据的精度
	private int GridUnits = 0; //格点单位类型，默认为：0-经纬度
	private int GridProject; //格点投影类型，即Grib2:第3段的模板号,Grib1:第2段的数据表示类型
	private float StartX; //开始经度
	private float StartY; //开始维度
	private float EndX; //结束经度 
	private float EndY; //结束维度
	private float XStep; //经度间隔（分辨率）
	private float YStep; //维度间隔（分辨率）	
	private int XCount; //经度格点数
	private int YCount; //维度格点数
	private int HeightCount; //高度数，模式数据为单层，为1
	private float[] Heights; //高度值，模式数据取{0}
	private String ProductCenter = "0"; //加工中心（预报中心），如无时用字符串0
	private String ProductMethod = "0"; //制作方法（预报方法），如无时用字符串0
	private String ProductDescription = ""; //产品描述
	private String data_id = ""; //四级编码
	private String IYMDHM; //入库时间
	private Date event_time; //
	private float data[]; //格点场数据
	
	//入mysql索引表需要字段
	//主表：NAS大文件总表
	private String D_SOURCE_ID;
	private String D_FILE_ID; //模式主表记录编号：四级编码+要素+资料时间+区域	
	private String D_RYMDHM; //第一个场文件收到时间
	private String D_UPDATETIME; //最后一次更新时间
	private String DATETIME; //资料时次，格式：YYYY-MM-DD HH24:MI:SS 
	private int V04001; //年
	private int V04002; //月
	private int V04003; //日
	private int V04004; //时
	private int V04005; //分
	private int V04006; //秒
	private int V_CCCC_N; //加工中心代码，(1段中，第6-7个八位组编码值)
	private int V_CCCC_SN; //子中心标识，(1段中，第8-9个八位组编码值)
	private int V_GENPRO_TYPE; //资料加工过程（模式）标识，(4段中，第14个八位组编码值)
	private String V_AREACODE; //区域
	private String D_STORAGE_SITE; //数据存储路径：完整路径(包含文件名)
	private String V_FILE_NAME; //NAS存储文件名
	private long D_FILE_SIZE; //文件大小
	private int D_ARCHIVE_FLAG = 0; //归档标识，0：未归档 ，1：已归档
	private String V_FILE_FORMAT; //文件格式：GRIB1，GRIB2，NETCDF3，NETCDF4，HDF5
	private int D_FILE_SAVE_HIERARCHY; //存储状态: 0：高端；1：大容量；2：磁带；3：回迁;4: 实时存储;
	
	//子表：每个场数据表
	private int V04320; //预报时效
	private int V_FIELD_TYPE; //场类型，加工数据类型（1段中，第21个8位组）
	private String V_FILE_NAME_SOURCE; //通信系统发来的原始文件名
	private String V_FIELD_FILE_NAME_SOURCE; //拆分后的单场文件名
	private int V_RETAIN1; //保留字段1
	private int V_RETAIN2; //保留字段2
	private int V_RETAIN3; //保留字段3
	private int typeOfGeneratingProcess; /*加工过程类型 (4段中，第12个八位组编码值)*/
	private int grib_version; /*grib版本号*/
	
	//其它
	private int grid_template_num;  //网格定义模板号
	private int prod_template_num;  //产品定义模板号
	private int datarep_template_num; //数据定义模板号
	
	private Date rcv_time; //场文件的原始文件收到时间
	private Date start_time; //场文件开始处理时间
	private Date end_time; //场文件处理结束时间
	private int insert_ret; //入索引表状态
	private int insert_radb_ret; //入实时库（Cassandra）状态
	
	//C3E集合预报的成员属性
	private String member; //集合预报成员变量
	
	//带层次类型的要素服务代码
	private String element_serv; //要素服务代码+层次类型，如：TEM_100
	
	private String area_fieldType_genProcessType; //区域+场类型+加工过程类型
	
	//预报参数标识
	private String forecast_parameter;
	
	//智能网格、实况产品入索引表（NAFP_PRODUCT_FILE_TAB）字段
	private String v_cccc; //编报中心cccc
	private String maketime; //制作时间
	private String area; //区域
	private String prodSort; //产品种类
	private String prodContent; //产品要素
	private String V_PROD_SYSTEM; //生产系统
	private String fntime; //资料时次（字符型）
	private String v04320_c; //时效
	private String internal; //时效间隔
	private String density; //分辨率
	private String retain1; //保留字段1
	private String retain2; //保留字段2
	private String retain3; //保留字段3
	
	//add,2020.4.19:入M4的Cassandra存储字段
	private float isolineStartValue = 0; //等值线开始值
	private float isolineEndValue = 0; //等值线结束值
	private float isolineSpace = 0; //等值线间隔
	private String m4Element; //要素代码
		
	public Grib_Struct_Data()
	{		
	}
	
	public String getElement()
	{
		return element;
	}
	public void setElement(String element)
	{
		this.element = element;
	}
	
	public String getValidTime()
	{
		return ValidTime;
	}
	public void setValidTime(String ValidTime)
	{
		this.ValidTime = ValidTime;
	}
	
	public String getTime()
	{
		return Time;
	}
	public void setTime(String Time)
	{
		this.Time = Time;
	}
	
	public int getLevelType()
	{
		return levelType;
	}
	public void setLevelType(int levelType)
	{
		this.levelType = levelType;
	}
	
	public int getLevel1()
	{
		return level1;
	}
	public void setLevel1(int level1)
	{
		this.level1 = level1;
	}
	
	public int getlevel1_orig()
	{
		return level1_orig;
	}
	public void setlevel1_orig(int level1_orig)
	{
		this.level1_orig = level1_orig;
	}
	
	public int getLevel2()
	{
		return level2;
	}
	public void setLevel2(int level2)
	{
		this.level2 = level2;
	}
	
	public int getlevel2_orig()
	{
		return level2_orig;
	}
	public void setlevel2_orig(int level2_orig)
	{
		this.level2_orig = level2_orig;
	}
	
	public int getValueByteNum()
	{
		return ValueByteNum;
	}
	public void setValueByteNum(int ValueByteNum)
	{
		this.ValueByteNum = ValueByteNum;
	}
	
	public int getValuePrecision()
	{
		return ValuePrecision;
	}
	public void setValuePrecision(int ValuePrecision)
	{
		this.ValuePrecision = ValuePrecision;
	}
	
	public int getGridUnits()
	{
		return GridUnits;
	}
	public void setGridUnits(int GridUnits)
	{
		this.GridUnits = GridUnits;
	}
	
	public int getGridProject()
	{
		return GridProject;
	}
	public void setGridProject(int GridProject)
	{
		this.GridProject = GridProject;
	}
	
	public float getStartX()
	{
		return StartX;
	}
	public void setStartX(float StartX)
	{
		this.StartX = StartX;
	}
	
	public float getStartY()
	{
		return StartY;
	}
	public void setStartY(float StartY)
	{
		this.StartY = StartY;
	}
	
	public float getEndX()
	{
		return EndX;
	}
	public void setEndX(float EndX)
	{
		this.EndX = EndX;
	}
	
	public float getEndY()
	{
		return EndY;
	}
	public void setEndY(float EndY)
	{
		this.EndY = EndY;
	}
	
	public float getXStep()
	{
		return XStep;
	}
	public void setXStep(float XStep)
	{
		this.XStep = XStep;
	}
	
	public float getYStep()
	{
		return YStep;
	}
	public void setYStep(float YStep)
	{
		this.YStep = YStep;
	}
	
	public int getXCount()
	{
		return XCount;
	}
	public void setXCount(int XCount)
	{
		this.XCount = XCount;
	}
	
	public int getYCount()
	{
		return YCount;
	}
	public void setYCount(int YCount)
	{
		this.YCount = YCount;
	}
	
	public int getHeightCount()
	{
		return HeightCount;
	}
	public void setHeightCount(int HeightCount)
	{
		this.HeightCount = HeightCount;
	}
	
	public float[] getHeights()
	{
		return Heights;
	}
	public void setHeights(float[] Heights)
	{
		this.Heights = Heights;
	}
	

	
	public String getProductCenter()
	{
		return ProductCenter;
	}
	public void setProductCenter(String ProductCenter)
	{
		this.ProductCenter = ProductCenter;
	}
	
	public String getProductMethod()
	{
		return ProductMethod;
	}
	public void setProductMethod(String ProductMethod)
	{
		this.ProductMethod = ProductMethod;
	}
	
	public String getProductDescription()
	{
		return ProductDescription;
	}
	public void setProductDescription(String ProductDescription)
	{
		this.ProductDescription = ProductDescription;
	}
	
	public String getdata_id()
	{
		return data_id;
	}
	public void setdata_id(String data_id)
	{
		this.data_id = data_id;
	}
	
	public String getIYMDHM()
	{
		return IYMDHM;
	}
	public void setIYMDHM(String IYMDHM)
	{
		this.IYMDHM = IYMDHM;
	}
	
	public Date getevent_time()
	{
		return event_time;
	}
	public void setevent_time(Date event_time)
	{
		this.event_time = event_time;
	}
	
	public float[] getdata()
	{
		return data;
	}
	public void setdata(float[] data)
	{
		this.data = data;
	}
	
	public String getD_FILE_ID()
	{
		return D_FILE_ID;
	}
	public void setD_FILE_ID(String D_FILE_ID)
	{
		this.D_FILE_ID = D_FILE_ID;
	}
	
	public String getD_RYMDHM()
	{
		return D_RYMDHM;
	}
	public void setD_RYMDHM(String D_RYMDHM)
	{
		this.D_RYMDHM = D_RYMDHM;
	}
	
	public String getD_UPDATETIME()
	{
		return D_UPDATETIME;
	}
	public void setD_UPDATETIME(String D_UPDATETIME)
	{
		this.D_UPDATETIME = D_UPDATETIME;
	}
	
	public String getDATETIME()
	{
		return DATETIME;
	}
	public void setDATETIME(String DATETIME)
	{
		this.DATETIME = DATETIME;
	}
	
	public int getV04001()
	{
		return V04001;
	}
	public void setV04001(int V04001)
	{
		this.V04001 = V04001;
	}
	
	public int getV04002()
	{
		return V04002;
	}
	public void setV04002(int V04002)
	{
		this.V04002 = V04002;
	}
	
	public int getV04003()
	{
		return V04003;
	}
	public void setV04003(int V04003)
	{
		this.V04003 = V04003;
	}
	
	public int getV04004()
	{
		return V04004;
	}
	public void setV04004(int V04004)
	{
		this.V04004 = V04004;
	}
	
	public int getV04005()
	{
		return V04005;
	}
	public void setV04005(int V04005)
	{
		this.V04005 = V04005;
	}
	
	public int getV04006()
	{
		return V04006;
	}
	public void setV04006(int V04006)
	{
		this.V04006 = V04006;
	}
	
	public int getV_CCCC_N()
	{
		return V_CCCC_N;
	}
	public void setV_CCCC_N(int V_CCCC_N)
	{
		this.V_CCCC_N = V_CCCC_N;
	}
	
	public int getV_CCCC_SN()
	{
		return V_CCCC_SN;
	}
	public void setV_CCCC_SN(int V_CCCC_SN)
	{
		this.V_CCCC_SN = V_CCCC_SN;
	}
	
	public int getV_GENPRO_TYPE()
	{
		return V_GENPRO_TYPE;
	}
	public void setV_GENPRO_TYPE(int V_GENPRO_TYPE)
	{
		this.V_GENPRO_TYPE = V_GENPRO_TYPE;
	}
	
	public String getV_AREACODE()
	{
		return V_AREACODE;
	}
	public void setV_AREACODE(String V_AREACODE)
	{
		this.V_AREACODE = V_AREACODE;
	}
	
	public String getD_STORAGE_SITE()
	{
		return D_STORAGE_SITE;
	}
	public void setD_STORAGE_SITE(String D_STORAGE_SITE)
	{
		this.D_STORAGE_SITE = D_STORAGE_SITE;
	}
	
	public String getV_FILE_NAME()
	{
		return V_FILE_NAME;
	}
	public void setV_FILE_NAME(String V_FILE_NAME)
	{
		this.V_FILE_NAME = V_FILE_NAME;
	}
	
	public long getD_FILE_SIZE()
	{
		return D_FILE_SIZE;
	}
	public void setD_FILE_SIZE(long D_FILE_SIZE)
	{
		this.D_FILE_SIZE = D_FILE_SIZE;
	}
	
	public int getD_ARCHIVE_FLAG()
	{
		return D_ARCHIVE_FLAG;
	}
	public void setD_ARCHIVE_FLAG(int D_ARCHIVE_FLAG)
	{
		this.D_ARCHIVE_FLAG = D_ARCHIVE_FLAG;
	}
	
	public String getV_FILE_FORMAT()
	{
		return V_FILE_FORMAT;
	}
	public void setV_FILE_FORMAT(String V_FILE_FORMAT)
	{
		this.V_FILE_FORMAT = V_FILE_FORMAT;
	}
	
	public int getD_FILE_SAVE_HIERARCHY()
	{
		return D_FILE_SAVE_HIERARCHY;
	}
	public void setD_FILE_SAVE_HIERARCHY(int D_FILE_SAVE_HIERARCHY)
	{
		this.D_FILE_SAVE_HIERARCHY = D_FILE_SAVE_HIERARCHY;
	}
	
	public int getV04320()
	{
		return V04320;
	}
	public void setV04320(int V04320)
	{
		this.V04320 = V04320;
	}
	
	public int getV_FIELD_TYPE()
	{
		return V_FIELD_TYPE;
	}
	public void setV_FIELD_TYPE(int V_FIELD_TYPE)
	{
		this.V_FIELD_TYPE = V_FIELD_TYPE;
	}
	
	public String getV_FILE_NAME_SOURCE()
	{
		return V_FILE_NAME_SOURCE;
	}
	public void setV_FILE_NAME_SOURCE(String V_FILE_NAME_SOURCE)
	{
		this.V_FILE_NAME_SOURCE = V_FILE_NAME_SOURCE;
	}
	
	public String getV_FIELD_FILE_NAME_SOURCE()
	{
		return V_FIELD_FILE_NAME_SOURCE;
	}
	public void setV_FIELD_FILE_NAME_SOURCE(String V_FIELD_FILE_NAME_SOURCE)
	{
		this.V_FIELD_FILE_NAME_SOURCE = V_FIELD_FILE_NAME_SOURCE;
	}
	
	public int getV_RETAIN1()
	{
		return V_RETAIN1;
	}
	public void setV_RETAIN1(int V_RETAIN1)
	{
		this.V_RETAIN1 = V_RETAIN1;
	}
	
	public int getV_RETAIN2()
	{
		return V_RETAIN2;
	}
	public void setV_RETAIN2(int V_RETAIN2)
	{
		this.V_RETAIN2 = V_RETAIN2;
	}
	
	public int getV_RETAIN3()
	{
		return V_RETAIN3;
	}
	public void setV_RETAIN3(int V_RETAIN3)
	{
		this.V_RETAIN3 = V_RETAIN3;
	}	
	
	public int gettypeOfGeneratingProcess()
	{
		return typeOfGeneratingProcess;
	}
	public void settypeOfGeneratingProcess(int typeOfGeneratingProcess)
	{
		this.typeOfGeneratingProcess = typeOfGeneratingProcess;
	}
	
	public int getgrib_version()
	{
		return grib_version;
	}
	public void setgrib_version(int grib_version)
	{
		this.grib_version = grib_version;
	}
	
	public int getgrid_template_num()
	{
		return grid_template_num;
	}
	public void setgrid_template_num(int grid_template_num)
	{
		this.grid_template_num = grid_template_num;
	}
	
	public int getprod_template_num()
	{
		return prod_template_num;
	}
	public void setprod_template_num(int prod_template_num)
	{
		this.prod_template_num = prod_template_num;
	}
	
	public int getdatarep_template_num()
	{
		return datarep_template_num;
	}
	public void setdatarep_template_num(int datarep_template_num)
	{
		this.datarep_template_num = datarep_template_num;
	}
	
	public Date getrcv_time()
	{
		return rcv_time;
	}
	public void setrcv_time(Date rcv_time)
	{
		this.rcv_time = rcv_time;
	}
	
	public Date getstart_time()
	{
		return start_time;
	}
	public void setstart_time(Date start_time)
	{
		this.start_time = start_time;
	}
	
	public Date getend_time()
	{
		return end_time;
	}
	public void setend_time(Date end_time)
	{
		this.end_time = end_time;
	}
	
	public int getinsert_ret()
	{
		return insert_ret;
	}
	public void setinsert_ret(int insert_ret)
	{
		this.insert_ret = insert_ret;
	}
	
	public int getinsert_radb_ret()
	{
		return insert_radb_ret;
	}
	public void setinsert_radb_ret(int insert_radb_ret)
	{
		this.insert_radb_ret = insert_radb_ret;
	}	
	
	public String getmember()
	{
		return member;
	}
	public void setmember(String member)
	{
		this.member = member;
	}
	
	public String getelement_serv()
	{
		return element_serv;
	}
	public void setelement_serv(String element_serv)
	{
		this.element_serv = element_serv;
	}	
	
	public String getarea_fieldType_genProcessType()
	{
		return area_fieldType_genProcessType;
	}
	public void setarea_fieldType_genProcessType(String area_fieldType_genProcessType)
	{
		this.area_fieldType_genProcessType = area_fieldType_genProcessType;
	}
	
	public String getforecast_parameter()
	{
		return forecast_parameter;
	}
	public void setforecast_parameter(String forecast_parameter)
	{
		this.forecast_parameter = forecast_parameter;
	}
	
	public String getv_cccc()
	{
		return v_cccc;
	}
	public void setv_cccc(String v_cccc)
	{
		this.v_cccc = v_cccc;
	}
	
	public String getmaketime()
	{
		return maketime;
	}
	public void setmaketime(String maketime)
	{
		this.maketime = maketime;
	}
	
	public String getprodSort()
	{
		return prodSort;
	}
	public void setprodSort(String prodSort)
	{
		this.prodSort = prodSort;
	}
	
	public String getprodContent()
	{
		return prodContent;
	}
	public void setprodContent(String prodContent)
	{
		this.prodContent = prodContent;
	}	
	
	public String getV_PROD_SYSTEM()
	{
		return V_PROD_SYSTEM;
	}
	public void setV_PROD_SYSTEM(String V_PROD_SYSTEM)
	{
		this.V_PROD_SYSTEM = V_PROD_SYSTEM;
	}
	
	public String getfntime()
	{
		return fntime;
	}
	public void setfntime(String fntime)
	{
		this.fntime = fntime;
	}
	
	public String getv04320_c()
	{
		return v04320_c;
	}
	public void setv04320_c(String v04320_c)
	{
		this.v04320_c = v04320_c;
	}
	
	public String getinternal()
	{
		return internal;
	}
	public void setinternal(String internal)
	{
		this.internal = internal;
	}
	
	public String getdensity()
	{
		return density;
	}
	public void setdensity(String density)
	{
		this.density = density;
	}
	
	public String getretain1()
	{
		return retain1;
	}
	public void setretain1(String retain1)
	{
		this.retain1 = retain1;
	}
	
	public String getretain2()
	{
		return retain2;
	}
	public void setretain2(String retain2)
	{
		this.retain2 = retain2;
	}
	
	public String getretain3()
	{
		return retain3;
	}
	public void setretain3(String retain3)
	{
		this.retain3 = retain3;
	}

	public String getD_SOURCE_ID()
	{
		return D_SOURCE_ID;
	}
	public void setD_SOURCE_ID(String D_SOURCE_ID)
	{
		this.D_SOURCE_ID = D_SOURCE_ID;
	}	
	
	public float getIsolineStartValue()
	{
		return isolineStartValue;
	}
	public void setIsolineStartValue(float isolineStartValue)
	{
		this.isolineStartValue = isolineStartValue;
	}
	
	public float getIsolineEndValue()
	{
		return isolineEndValue;
	}
	public void setIsolineEndValue(float isolineEndValue)
	{
		this.isolineEndValue = isolineEndValue;
	}
	
	public float getIsolineSpace()
	{
		return isolineSpace;
	}
	public void setIsolineSpace(float isolineSpace)
	{
		this.isolineSpace = isolineSpace;
	}	
	
	public String getM4Element()
	{
		return m4Element;
	}
	public void setM4Element(String m4Element)
	{
		this.m4Element = m4Element;
	}
}