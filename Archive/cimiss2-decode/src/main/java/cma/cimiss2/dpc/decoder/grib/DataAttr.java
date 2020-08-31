package cma.cimiss2.dpc.decoder.grib;

public class DataAttr
{
	private String data_id; //四级编码
	private String description; //资料简称
	private String precision; //高分或低分
	private boolean isEnsemble;//是否为集合预报
	private String grib_version; //grib版本：1,2或12（同时有两种版本号）
	private String field_organize; //多场文件的组织方式:common表示一般的GRIB...7777...GRIB...7777组织方式,other表示循环嵌套的组织方式	
	private int time_index_of_filename; //时间字段在原始文件名的第几段，如在第5个：Z_NAFP_C_BABJ_20180725060000_P_CNPC-T639-GMFS-HNEHE-02100.grib2
	private String data_dir; //资料的数据目录，可多级，放在根目录后，如：/根目录/data_dir/YYYYMMDD
	private int index_type; //索引解析类型，0为走拆分入库，1为解析文件名+入Cassandra(智能网格GRIB、nc产品)，2为解析文件名(jpg文件等)
	private String filename_match; //文件名匹配规则
	private String table_id; //表名标识
	
	//入M4表
	private boolean m4Cassandra; //是否入M4的Cassandra
	private String m4Area; //入M4的Cassandra区域
	private String m4TableName; //入M4的Cassandra表名
	
	//区域裁剪相关
	private String area_src; //原始区域
	private String area_des; //裁剪后区域
	private String Lat_Lon; //裁剪后的经纬度范围
	
	public DataAttr(String data_id,String description,String precision,String grib_version,String field_organize, String index_type)
	{
		//this( data_id, description, precision, grib_version, field_organize,  index_type,false);//默认为非集合预报
	}
	
	public DataAttr(String data_id,String description,String precision,String grib_version,String field_organize, 
			int time_index_of_filename,boolean isEnsemble, String data_dir,int index_type,String filename_match,
			String table_id)
	{
		this.data_id = data_id; 
		this.description = description; 
		this.precision = precision;
		this.grib_version = grib_version;
		this.field_organize = field_organize;	
		this.time_index_of_filename = time_index_of_filename;
		this.isEnsemble = isEnsemble;
		this.data_dir = data_dir;
		
		this.index_type = index_type;
		this.filename_match = filename_match;
		
		this.table_id = table_id;		
		
	}
	
	public DataAttr(String data_id,boolean m4Cassandra,String m4Area,String m4TableName)
	{
		this.data_id = data_id; 
		this.m4Cassandra = m4Cassandra;
		this.m4Area = m4Area;
		this.m4TableName = m4TableName;
	}
	
	//区域裁剪相关
	public DataAttr(String data_id,String area_src,String area_des,String Lat_Lon)
	{
		this.data_id = data_id;
		this.area_src = area_src;
		this.area_des = area_des;
		this.Lat_Lon = Lat_Lon;
	}
	
	public String getdataid() 
	{
		return data_id;
	}
	
	public String getdescription()
	{
		return description;
	}
	
	public String getprecision()
	{
		return precision;
	}
	
	public String getgribversion()
	{
		return grib_version;
	}
	
	public String getfieldorganize()
	{
		return field_organize;
	}
	
	public int gettime_index_of_filename()
	{
		return time_index_of_filename;
	}
	
	public boolean getIsEnsemble() {
		return isEnsemble;
	}
	
	public String getdata_dir() {
		return data_dir;
	}
	
	public int getindex_type() {
		return index_type;
	}
	
	public String getfilename_match() {
		return filename_match;
	}
	
	public String gettable_id() {
		return table_id;
	}
	
	public boolean getM4Cassandra() {
		return m4Cassandra;
	}
	
	public String getm4Area() {
		return m4Area;
	}
	
	public String getm4TableName() {
		return m4TableName;
	}
	
	public String getarea_src() {
		return area_src;
	}
	
	public String getarea_des() {
		return area_des;
	}
	
	public String getLat_Lon() {
		return Lat_Lon;
	}
}