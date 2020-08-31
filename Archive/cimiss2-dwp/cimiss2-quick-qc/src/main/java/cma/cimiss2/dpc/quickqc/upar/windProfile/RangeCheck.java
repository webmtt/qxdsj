package cma.cimiss2.dpc.quickqc.upar.windProfile;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 范围值检查
 */
public class RangeCheck implements BaseCheck{
	
	public QualityCode rangeCheck(MeteoElement element, Object value, Double height) {
		if(element == MeteoElement.WIN_D){
			return WindDirRangeCheck(Double.parseDouble(value.toString()));
		}else if(element == MeteoElement.WIN_S){
			return WindSpeedRangeCheck(Double.parseDouble(value.toString()), height);
		}else{
			System.out.println("Range Check element is neither windSpeed or windDir!");
			return QualityCode.Z_QC_NO_QC;
		}
		
	}
	/**
	 * 风速质控码：范围检查
	 */
	private QualityCode WindSpeedRangeCheck(Double windSpeed, Double height){
		if(windSpeed==null||height==null){
			return null;
		}
	/*	>和<=,      >=和<=
	 * -600～3000 m 0～100 m/s
		3000～5500 m 0～120 m/s
		5500～7000 m 0～150 m/s
		7000～14000 m 0～180 m/s
		14000～22000 m 0～170 m/s
		22000～30000 m 0～110 m/s
		30000～36000 m 0～140 m/s
		36000～39000 m 0～170 m/s
		39000～99999 m 0～220 m/s
		*/
		// 不同采样层 ，风速值上限
		int arraySpeed[] = {100, 120, 150, 180, 170, 110, 140, 170, 220};
		// 采样层高度
		List<Double> Heights = new ArrayList<>(Arrays.asList(-600.0, 3000.0, 5500.0, 7000.0, 14000.0, 22000.0, 30000.0, 36000.0,39000.0, 99999.0));
		// 高度边界
		if(height < -600 || height > 99999){
			//不进入质量质控
			return QualityCode.Z_QC_NO_QC;
		}
		else {
			if(height == -600){
				if(windSpeed >= 0 && windSpeed <= 100){
					return QualityCode.Z_QC_OK;
				}
				else{
					return QualityCode.Z_QC_ERROR;
				}
			}
			// 高度在范围内
			Heights.add(height);
			Object a[] = Heights.toArray();
			Arrays.sort(a);
			// 获取采样高度所在位置，取风速范围对应位置值比较
			int pos = Arrays.binarySearch(a, height);
			// 由于重复值时，二值搜索不确定返回是哪个，所以需要保证返回的是前面的；
			if(pos >= 1 && ((Double)a[pos]).doubleValue() == ((Double)a[pos - 1]).doubleValue()){
				// 如果前一个值和查找的值相同，应该取前一个索引
				pos --;
			}
			Heights.remove(height);
			if(windSpeed >= 0 && windSpeed <= arraySpeed[pos - 1]){
				// 数据正确
				return QualityCode.Z_QC_OK;
			}
			else {
				// 数据错误
				return QualityCode.Z_QC_ERROR;
			}
		}
	}
	
	/**
	 * 风向质控码：范围检查
	 */
	private QualityCode WindDirRangeCheck(double windDir){
		if(windDir >= 0 &&  windDir <= 360){

			return QualityCode.Z_QC_OK;
		}
		else{

			return QualityCode.Z_QC_ERROR;
		}
	}
}
