package cma.cimiss2.dpc.quickqc.upar.windProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.cfg.UparWindProfileCfg;
/**
 * 阈值检查，根据风廓线的站，查找探空站，找不到探空，根据经纬度找200km内的探空站，再找到对应的阈值，进行比较判断
 */
public class ThresholdCheck implements BaseCheck{
	public QualityCode thresoldCheck(MeteoElement element, Object value, String sta, Double height, Double lat, Double lon){
		if(element == MeteoElement.WIN_S){
			return WindSpeedThresholdCheck(Double.parseDouble(value.toString()),  sta,  height, lat, lon);
		}else if(element == MeteoElement.WIN_D){
			return WindDirThresholdCheck(Double.parseDouble(value.toString())); 
		}else{
			System.out.println("Threshold Check element is neither windSpeed or windDir!");
			return QualityCode.Z_QC_NO_QC;
		}
	}
	/**
	 * 风向阈值检查
	 */
	private QualityCode WindDirThresholdCheck(Double windDir){
		if(windDir >= 0 &&  windDir <= 360)
			return QualityCode.Z_QC_OK;
		else
			return QualityCode.Z_QC_ERROR;
	}
	
	/**
	 * 风速阈值检查
	 * （1） 确定匹配站：根据待检站点以及中国探空站表（见附件 1）的经纬度信
		息，利用半正矢公式（见附件 2）计算出与待检站距离最近且不超过 200km 的探
		空站，作为该站的匹配站。
		（2） 确定匹配层次：找出匹配站对应的气候学界限值参数文件（见附件 3）
		中观测高度所对应的层次。
		（3） 确定质控码：若待检站某观测高度上的水平风速值大于匹配站参数文件
		对应层次的阈值，则质量控制码赋为 2，否则赋为 0。
	 */
	private QualityCode WindSpeedThresholdCheck(Object value, String sta, Double height, Double lat, Double lon){
		UparWindProfileCfg qc = UparWindProfileCfg.getInstance();
		boolean exist = qc.isExist(sta);
		if(!exist){
			 // 不存在,遍历探空站,统计符合的站点信息,key为站号,value为站点距离
            Map<String, Integer> staMap = qc.haversineFormula(lat, lon);
            if (staMap.size() > 0) {
                // 存符合的站点信息
                List<Map.Entry<String, Integer>> list = new ArrayList(staMap.entrySet());
                list.sort((o1, o2) -> (o1.getValue() - o2.getValue()));
                qc.setStation(list.get(0).getKey());
                exist = true;
            } else {
                // 不存在,记录区站号
                System.out.println("No corresponding sounding station was found : " + sta);
            }
		}
        if (exist) {
            int wind = qc.getWSpeed(height); // 计算阈值
            if(Double.parseDouble(value.toString()) < 0){
            	return QualityCode.Z_QC_ERROR;
            }
            else if(wind == 999999){
            	return QualityCode.Z_QC_NO_QC; // 返回范围值检查结果
            }
            else if (Double.parseDouble(value.toString()) >= 0 && Double.parseDouble(value.toString()) <= wind) {
                return QualityCode.Z_QC_OK; // 正确
            } else {
                return QualityCode.Z_QC_ERROR;// 错误
            }
        } else {
            // 不存在,记录区站号
            System.out.println("No corresponding sounding station was found: " + sta);
            return QualityCode.Z_QC_OK;
        }
	}
}
