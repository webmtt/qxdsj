package cma.cimiss2.dpc.decoder.nc;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis2D;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.geoloc.LatLonRect;

public class NcDecode {

	private NetcdfDataset dataset;
	private List<Variable> variables = null;
	private GridDataset gridDataset;
	private String fullFilePath;
	private String d_data_id;

	public NcDecode(String fullFilePath, String d_data_id) {
		this.fullFilePath = fullFilePath;
		this.d_data_id = d_data_id;
		try {
			dataset = NetcdfDataset.openDataset(fullFilePath);
			setVariables(dataset.getVariables());
			gridDataset = new GridDataset(dataset);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取变量的经纬度范围
	 * @param variable 变量名
	 * @return LatLonRect 经纬度的范围
	 */
	public LatLonRect getLatLonRect1(Variable variable) {
		GridDatatype gridDatatype = gridDataset.findGridDatatype(variable.getName());
		GridCoordSystem gridCoordSystem = gridDatatype.getCoordinateSystem();
		return gridCoordSystem.getLatLonBoundingBox();
	}

	private float[] xs = null;
	private float[] ys = null;

	public LatLonRect getLatLonRect(Variable variable) throws IOException {
		GeoGrid geoGrid = gridDataset.findGridByName(variable.getFullName());
		GridCoordSystem gridCoordSystem = geoGrid.getCoordinateSystem();
		CoordinateAxis xHorizAxis = gridCoordSystem.getXHorizAxis();
		
		CoordinateAxis yHorizAxis = gridCoordSystem.getYHorizAxis();
		// try {
		Array xArray = xHorizAxis.read(); // 经度数组
		xs = (float[]) xArray.copyTo1DJavaArray();
		// DataType dataType = xArray.getDataType();
		Array yArray = yHorizAxis.read();
		ys = (float[]) yArray.copyTo1DJavaArray();

		// System.out.println("x length: " + xs.length + "\r\n" + Arrays.toString(xs));
		// System.out.println("y length: " + ys.length + "\r\n" + Arrays.toString(ys));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		return gridCoordSystem.getLatLonBoundingBox();
	}

	/**
	 * 根据经纬度范围和变量来获取变量的值
	 * @param lon_min   最小经度
	 * @param lat_min   最小纬度
	 * @param lon_max   最大经度
	 * @param lat_max   最大纬度
	 * @param variable  变量
	 * @return  Array 数据数组
	 * @throws IOException
	 * @throws InvalidRangeException 
	 */
	public Array readData(double lon_min, double lat_min, double lon_max, double lat_max, Variable variable) throws IOException, InvalidRangeException {

		GridDatatype gridDatatype = gridDataset.findGridDatatype(variable.getName());
		GridCoordSystem gridCoordSystem = gridDatatype.getCoordinateSystem();
		int[] startIndex = gridCoordSystem.findXYindexFromLatLonBounded(lat_min, lon_min, null);
		int[] endIndex = gridCoordSystem.findXYindexFromLatLonBounded(lat_max, lon_max, null);
		Range colRange = new Range(startIndex[0], endIndex[0]);
		Range rowRange = new Range(startIndex[1], endIndex[1]);
		return variable.read(Arrays.asList(rowRange, colRange));
		

	}

	/**
	 * 关闭数据集
	 */
	public void close() {
		try {
			dataset.close();
			gridDataset.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<NcStructData> decode() throws IOException, InvalidRangeException {

		File file = new File(fullFilePath);
		long fileSize = file.length();
		String fileName = file.getName();
		String[] fileNameElements = fileName.split("_|\\.|-");
		if (fileNameElements.length != 14) {
			return null;
		}
		// Z_NAFP_C_BABJ_20180719001912_P_CLDAS_RT_ASI_0P0625_HOR-PRS-2018071900.nc
		// Z_NAFP_C_BABJ_20180719001422_P_HRCLDAS_RT_CHN_0P01_HOR-PRE-2018071900.nc
		String productCenter = fileNameElements[3]; // 预报中心
		String makeTime = fileNameElements[4]; // 资料制作时间
		String systemCode = fileNameElements[6]; // 业务系统
		String type = fileNameElements[7]; // 类别
		String area = fileNameElements[8]; // 区域
		String spaceDpi = fileNameElements[9]; // 空间分辨率
		String timeDpi = fileNameElements[10]; // 时间分辨率
		String element = fileNameElements[11]; // 要素
		String dDateTime = fileNameElements[12]; // 资料时间yyyyMMddHH
		String fileFormat = fileNameElements[13]; // 文件类型
		Date receiveTime = new Date(file.lastModified());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dSdf = new SimpleDateFormat("yyyyMMddHH");

		String fileReceiveTime = sdf.format(receiveTime);
		Date _dDateTime = null;
		String d_datetime = null;
		Calendar cal = Calendar.getInstance();
		try {
			_dDateTime = dSdf.parse(dDateTime);
			cal.setTime(_dDateTime);
			d_datetime = sdf.format(_dDateTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Date now = new Date(System.currentTimeMillis());

		// d_FILE_ID 模式主表记录编号：四级编码+要素+资料时间+区域
		String d_file_id = d_data_id + "_" + element + "_" + dDateTime + "0000_" + area;

		// 读取nc文件
		// NcDecode ncDecode = new NcDecode(fullFilePath);
		List<NcStructData> ncDataList = new ArrayList<NcStructData>();
		try {

			List<Variable> vs = this.variables;
			
			for (Variable variable : vs) {
				// 获取二维的数据信息
				if (variable.getClass() == CoordinateAxis2D.class || variable.getClass() == VariableDS.class) {

					NcStructData ncData = new NcStructData();

					ncData.setV_FILE_NAME(fileName);
					ncData.setD_FILE_SIZE(fileSize);
					ncData.setV_FILE_NAME_SOURCE(fileName);
					ncData.setD_FILE_ID(d_file_id);
					ncData.setD_RYMDHM(fileReceiveTime);
					ncData.setD_STORAGE_SITE(fullFilePath);
					// ncData.setD_UPDATETIME();
					ncData.setData_id(d_data_id);
					ncData.setDATETIME(d_datetime);
					ncData.setTime(_dDateTime);
					ncData.setMakeTime(makeTime);
					ncData.setElement(element);
					ncData.setIYMDHM(sdf.format(now));
					ncData.setProductCenter(productCenter);
					ncData.setProductDescription(type);
					// ncData.setV01033(); //加工中心代码
					// ncData.setV01034(); //子加工中心
					ncData.setV04001(cal.get(Calendar.YEAR));
					ncData.setV04002(cal.get(Calendar.MONTH) + 1);
					ncData.setV04003(cal.get(Calendar.DAY_OF_MONTH));
					ncData.setV04004(cal.get(Calendar.HOUR_OF_DAY));
					ncData.setV04005(cal.get(Calendar.MINUTE));
					ncData.setV04006(cal.get(Calendar.SECOND));
					// ncData.setV04320(); //预报时效
					ncData.setV_AREACODE(area);
					// ncData.setV_FIELD_TYPE(); //场类型
					ncData.setV_FILE_FORMAT(fileFormat);
					ncData.setV_FILE_NAME(fileName);
					ncData.setV_FILE_NAME_SOURCE(fileName);
					// ncData.setV_GENPRO_TYPE(); //资料加工过程标识
					// ncData.setValidTime(); //预报时效
					ncData.setValueByteNum(4); // 每个格点场数据占用的字节

					LatLonRect latLonRect = this.getLatLonRect(variable);

					double lonMin = latLonRect.getLonMin();// 最小经度(起始经度)
					double latMin = latLonRect.getLatMin(); // 最小纬度(起始纬度)
					double lonMax = latLonRect.getLonMax(); // 最大经度(终止经度)
					double latMax = latLonRect.getLatMax();// 最大纬度(终止纬度)

					ncData.setStartX((float) lonMin);
					ncData.setEndX((float) lonMax);
					ncData.setStartY((float) latMin);
					ncData.setEndY((float) latMax);
					ncData.setXCount(xs.length);
					ncData.setYCount(ys.length);
					ncData.setXStep(xs[1] - xs[0]);
					ncData.setYStep(ys[1] - ys[0]);

					Array array = this.readData(lonMin, latMin, lonMax, latMax, variable);
					DataType dataType = variable.getDataType();
					String dataTypeName = dataType.name();
					if ("float".equalsIgnoreCase(dataTypeName)) {
						float[] datas = (float[]) array.copyTo1DJavaArray();
						ncData.setData(datas);
					} else if ("double".equalsIgnoreCase(dataTypeName)) {
						// 此处数据存储为float将double转为float应该问题不大，暂时先这么转，nc数据中存储的应该都是float，此处暂时也用不到，以防万一吧
						float[] datas = (float[]) array.copyToNDJavaArray();
						ncData.setData(datas);
					}
					ncDataList.add(ncData);
				}
			}
		} finally {
			this.close();
		}

		return ncDataList;

	}

	
	public List<Variable> getVariables() {
		return variables;
	}

	private void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

}
