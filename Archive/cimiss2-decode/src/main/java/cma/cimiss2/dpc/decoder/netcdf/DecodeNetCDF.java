package cma.cimiss2.dpc.decoder.netcdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.cimiss.core.bean.GridData;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis2D;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.geoloc.LatLonRect;
/**
 * 解析NETCDF文件
 * @author wufeng
 *
 */
public class DecodeNetCDF {
	// 返回的结果集
	public static ParseResult<GridData> parseResult = new ParseResult<>(false);
	/**
	 * 读取NetCDF文件
	 * @param filename
	 */
	public static void decodeNetCDFFile(String filename){
		NetcdfDataset dataset = null;
		GridDataset gridDataset = null;
		try {
			dataset = NetcdfDataset.openDataset(filename);
			gridDataset = new GridDataset(dataset);
			//获取变量
			List<Variable> variables = dataset.getVariables();
			
			
			for (int i = 0; i < variables.size(); i++) {
				Variable variable = variables.get(i);
				// 判断是否为二维变量
				if(variable.getClass() == VariableDS.class || variable.getClass() == CoordinateAxis2D.class) {
					// 获取数据的范围  及  最大经纬度点的坐标和最小经纬度点的坐标
					LatLonRect latLonRect = getLatLonRect(gridDataset, variable);
					
					@SuppressWarnings("deprecation")
					GridDatatype gridDatatype = gridDataset.findGridDatatype(variable.getName());
					GridCoordSystem gridCoordSystem = gridDatatype.getCoordinateSystem();
					int[] startIndex = gridCoordSystem.findXYindexFromLatLonBounded(latLonRect.getLatMin(), latLonRect.getLonMin(), null);
					int[] endIndex = gridCoordSystem.findXYindexFromLatLonBounded(latLonRect.getLatMax(), latLonRect.getLonMax(), null);
					Range colRange = new Range(startIndex[0], endIndex[0]);
					Range rowRange = new Range(startIndex[1], endIndex[1]);
					//获取格点数据
					Array array = variable.read(Arrays.asList(rowRange, colRange));
					
					CoordinateAxis xHorizAxis = gridCoordSystem.getXHorizAxis();
					
					CoordinateAxis yHorizAxis = gridCoordSystem.getYHorizAxis();
					// try {
					Array xArray = xHorizAxis.read(); // 经度数组
					float[] xs = (float[]) xArray.copyTo1DJavaArray();
					Array yArray = yHorizAxis.read();
					float[] ys = (float[]) yArray.copyTo1DJavaArray();
					// 创建  GridData
					GridData gridData = new GridData();
					
					float[] datas = (float[]) array.copyTo1DJavaArray();
					gridData.setValue(datas);
					//西经 startX 浮点型 必填 
					gridData.setStartX((float)latLonRect.getLonMin());
					//南纬 startY 浮点型 必填 
					gridData.setStartY((float)latLonRect.getLatMin());
					//东经 endX 浮点型 必填 
					gridData.setEndX((float)latLonRect.getLonMax());
					//北纬 endY 浮点型 必填 
					gridData.setEndY((float)latLonRect.getLatMax());
					
					//经向格距 xStep 浮点型 必填 
					gridData.setXStep(xs[1] - xs[0]);
					//纬向格距 yStep 浮点型 必填 
					gridData.setYStep(ys[1] - ys[0]);
					//经向格数 xCount 整型 必填 
					gridData.setXCount(xs.length);
					//纬向格数 yCount 整型 必填 
					gridData.setYCount(ys.length);
					parseResult.put(gridData);
					
				}
			}
		} catch (IOException | InvalidRangeException e) {
			if(e.getClass() == IOException.class) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}else {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}finally {
			if(gridDataset != null) {
				try {
					gridDataset.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(dataset != null) {
				try {
					dataset.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
	}
	
	/**
	 * 获取网格数据的矩形区域
	 * @param gridDataset  网格数据集
	 * @param variable 变量
	 * @return LatLonRect 网格数据的矩形区域
	 */
	private static LatLonRect getLatLonRect(GridDataset gridDataset , Variable variable) {
		return gridDataset
				.findGridByName(variable.getFullName())
				.getCoordinateSystem()
				.getLatLonBoundingBox();
	}
	
	
	public static void main(String[] args) {
		DecodeNetCDF.decodeNetCDFFile("D:\\CIMISS2\\Z_NAFP_C_BABJ_20181030052947_P_HRCLDAS_RT_CHN_0P01_HOR-DPT-2018103005.nc");
		
	}

}
