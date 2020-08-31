package cma.cimiss2.dpc.decoder.grib.grib1;
import java.io.IOException;
import org.cimiss.core.bean.GridData;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.grib.GribDecoderConfigureHelper;
import ucar.nc2.grib.GdsHorizCoordSys;
import ucar.nc2.grib.grib1.Grib1Record;
import ucar.nc2.grib.grib1.Grib1RecordScanner;
import ucar.nc2.grib.grib1.Grib1SectionGridDefinition;
import ucar.nc2.grib.grib1.Grib1SectionProductDefinition;
import ucar.unidata.io.RandomAccessFile;
/**
 * 解析Grib1类型的文件
 * @author wufeng
 *
 */
public class DecodeGrib1File {
	public static ParseResult<GridData> parseResult = new ParseResult<>(false);
	private static RandomAccessFile randomAccessFile;
	
	public static void decodeGrib1File(String d_data_id, String filename) {
		randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(filename, "r" );
			Grib1RecordScanner grib1RecordScanner = new Grib1RecordScanner(randomAccessFile);
			while (grib1RecordScanner.hasNext()) {
				Grib1Record grib1Record = grib1RecordScanner.next();
			
				Grib1SectionGridDefinition grib1SectionGridDefinition = grib1Record.getGDSsection();
				
				GdsHorizCoordSys hcs = grib1SectionGridDefinition.getGDS().makeHorizCoordSys();
				
				GridData gridData = new GridData();
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
				
				// 获取数据
				float[] data = grib1Record.readData(randomAccessFile);
				gridData.setValue(data);
				
				Grib1SectionProductDefinition grib1SectionProductDefinition = grib1Record.getPDSsection();
				int levelType = grib1SectionProductDefinition.getLevelType();
				
				gridData.setHeightType(levelType);
				
				GribDecoderConfigureHelper decoderConfigureHelper = GribDecoderConfigureHelper.instance();	
				String d_data_table_id = d_data_id; //如果是EC高分：F.0010.0002.R001,F.0010.0003.R001,F.0010.0004.R001,用GRIB表格版本号作为入口
				int TableVersion = grib1SectionProductDefinition.getTableVersion();
				if(d_data_id.compareToIgnoreCase("F.0010.0002.R001")==0||d_data_id.compareToIgnoreCase("F.0010.0003.R001")==0||d_data_id.compareToIgnoreCase("F.0010.0004.R001")==0)
				{
					d_data_table_id = d_data_id.substring(0, 7) + "T" + TableVersion + ".R001"; //F.0010.T228.R001
				}
				//获取要素名称：由参数指示符决定
				int ParameterNumber = grib1SectionProductDefinition.getParameterNumber(); //参数指示符
				
				String element = decoderConfigureHelper.getGrib1ElementShortName(d_data_table_id, ParameterNumber, levelType);	
				
				gridData.setProdCode(element);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(randomAccessFile != null)
					randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
