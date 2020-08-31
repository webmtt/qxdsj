package cma.cimiss2.dpc.decoder.grib.grib2;

import java.io.IOException;

import org.cimiss.core.bean.GridData;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.grib.DataAttr;
import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.GribDecoderConfigureHelper;
import ucar.nc2.grib.GdsHorizCoordSys;
import ucar.nc2.grib.grib2.Grib2Pds;
import ucar.nc2.grib.grib2.Grib2Record;
import ucar.nc2.grib.grib2.Grib2RecordScanner;
import ucar.nc2.grib.grib2.Grib2SectionGridDefinition;
import ucar.nc2.grib.grib2.Grib2SectionIdentification;
import ucar.nc2.grib.grib2.Grib2SectionIndicator;
import ucar.unidata.io.RandomAccessFile;
/**
 * 解析GRIB2文件
 * @author wufeng
 *
 */
public class DecodeGrib2File {
	// 解码结果集
	public static ParseResult<GridData> parseResult = new ParseResult<>(false);
	
	/**
	 * GRIB2 解析函数
	 * @param d_data_id 资料四级编码
	 * @param filename 文件的绝对路径
	 */
	public static void decodeGrib2File(String d_data_id, String filename) {
		RandomAccessFile randomAccessFile = null;
		try {
			// 打开文件
			randomAccessFile = new RandomAccessFile(filename, "r" );
			// 创建 Grib2RecordScanner 遍历数据集
			Grib2RecordScanner grib2RecordScanner = new Grib2RecordScanner(randomAccessFile);
			
		
			while (grib2RecordScanner.hasNext()) {
				// 获取数据集
				Grib2Record grib2Record = grib2RecordScanner.next();
			
				//0段：指示符段
				Grib2SectionIndicator grib2SectionIndicator = grib2Record.getIs(); 
				//1段：产品标识段
				Grib2SectionIdentification grib2SectionIdentification = grib2Record.getId(); 
				//3段：网格定义段
				Grib2SectionGridDefinition grib2SectionGridDefinition = grib2Record.getGDSsection(); 
				
				//4段：产品定义段
				Grib2Pds grib2Pds = grib2Record.getPDS(); 					
				
				GridData gridData = new GridData();
				
				GdsHorizCoordSys hcs = grib2SectionGridDefinition.getGDS().makeHorizCoordSys();
				System.out.println(grib2SectionGridDefinition.getGDS().getNameShort());
				// 设置数据开始点的经纬度，及左下角经纬度
				gridData.setStartX((float)hcs.getStartX());
				gridData.setStartY((float)hcs.getStartY());
				
				// 设置数据结束点经纬度，及右上角经纬度
				gridData.setEndX((float)hcs.getEndX());
				gridData.setEndY((float)hcs.getEndY());
				
				// 设置数据径向格点数
				gridData.setXCount(hcs.nx);
				// 设置数据纬向格点数
				gridData.setYCount(hcs.ny);
				gridData.setXStep((float)hcs.dx);
				gridData.setYStep((float)hcs.dy);
				
				float[] data = grib2Record.readData(randomAccessFile);
				// 起报时间
				gridData.setTime(grib2SectionIdentification.getReferenceDate().toDate());
				// 预报时效
				gridData.setValidtime(grib2Pds.getForecastTime());
				gridData.setValue(data);
				
				//获取层次类型
				int level_type1 = grib2Pds.getLevelType1(); //层次类型1
				float level1 = (float)grib2Pds.getLevelValue1(); //层次1
				if(level_type1==100||level_type1==101||level_type1==108) //把原始数据的单位pa转化为hpa
				{
					level1 = level1/100;
				}
				else if(level_type1==106) //把单位进行转化，比如：0.28变为28
				{
					level1 = level1*100;
				}
				gridData.setLevel((int)level1);
				
				int discipline = grib2SectionIndicator.getDiscipline();
				int parameterCategory = grib2Pds.getParameterCategory() ;
				int parameterNumber = grib2Pds.getParameterNumber();
				
				GribDecoderConfigureHelper decoderConfigureHelper = GribDecoderConfigureHelper.instance();
				String element = decoderConfigureHelper.getGrib2ElementShortName(d_data_id, discipline, parameterCategory, parameterNumber, level_type1);	
				gridData.setProdCode(element);
				
				DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
				gridData.setProductDescription(data_attributes.getdescription() + " " + element);
				
				gridData.setGridUnits(0);
				gridData.setValueByteNum(4);
				gridData.setValuePrecision(10);
				
				//格点投影类型：Grib2:第3段的模板号
				int TemplateNumber = grib2SectionGridDefinition.getGDSTemplateNumber();
				gridData.setGridProject(TemplateNumber);
				
				//加工中心
				int GeneratingCentrer = grib2SectionIdentification.getCenter_id();
				gridData.setProductCenter(GeneratingCentrer+"");
				
				gridData.setHeightType(level_type1);
				gridData.setHeightCount(1);
				float[] heights={0};
				gridData.setHeights(heights);
				gridData.setProductMethod("0");
				
				parseResult.put(gridData);
				parseResult.setSuccess(true);
				System.out.println(grib2Record);
			}
		} catch (IOException e) {
			if(e.getClass() == IOException.class) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}
		}finally {
			if(randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		 
	}
	
	public static void main(String[] args) {
		DecodeGrib2File.decodeGrib2File("","D:\\cmadass\\testdata\\h500_2017122412_000.grb2");
	}

}
