//package cma.cimiss2.dpc.quickqc;
//
//import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
//import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
//import cma.cimiss2.dpc.quickqc.bean.enums.Quality;
//import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
//import cma.cimiss2.dpc.quickqc.surf.awshour.LimitCheck;
//import cma.cimiss2.dpc.quickqc.surf.awshour.MissingCheck;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
//* *******************************************************************************************<br>
//* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
//* *******************************************************************************************<br>
//* <b>Description:</b><br>
//* @author wuzuoqiang
//* @version 1.0
//* @Note
//* <b>ProjectName: 快速质量控制</b> cimiss2-quick-qc
//* <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc
//* <br><b>ClassName: 快速质控基类  </b> BaseQuickQC0
//* <br><b>Date:</b> 2019年6月11日 下午3:41:59
// */
//public interface BaseQuickQC0<T> {
//
//	/**
//	 * 返回逐步质控和最终质控结果,缺测、界限检查
//	 */
//	default List<Quality> checkTEMSteps2(BaseStationInfo stationInfo, LocalDateTime obsTime, int value){
//		MissingCheck missingCheck = new MissingCheck();
//		LimitCheck limitCheck = new LimitCheck();
//		int obs_occur_ext_time = value;
//		// 缺测检查
//		Quality qc_missCheck = missingCheck.missingCheck(value,stationInfo.getStationCode(),obsTime,MeteoElement.TEM,null,stationInfo);
//		if(qc_missCheck != Quality.Z_QC_NO_TASK){
//			if(obs_occur_ext_time == 999999){
//				qc_missCheck = Quality.Z_QC_LACK;
//			}
//			else if(obs_occur_ext_time == 999998){
//				qc_missCheck = Quality.Z_QC_NO_TASK;
//			}
//			else {
//				qc_missCheck = Quality.Z_QC_OK;
//			}
//		}
//		Quality qc_limitCheck = null;
//		// 质控结果
//		Quality qcResult = qc_missCheck;
//		qcResult = getQcResult(qc_missCheck, qc_limitCheck);
//
//		List<Quality> Codes = new ArrayList<>();
//		Codes.add(qc_missCheck);
//		Codes.add(qc_limitCheck);
//		Codes.add(qcResult);
//		return Codes;
//	}
//
//	/**
//	 * 只做缺测、界限检查两步质控时，返回最后的质控
//	 */
//	default  Quality getQcResult(Quality qc_missing, Quality qc_limit){
//		Quality qcResult = qc_missing; // 0,7,8
//		if(qc_missing == Quality.Z_QC_NO_TASK || qc_missing == Quality.Z_QC_LACK){
//			qcResult.setDescription("_" + "缺测检查");
//			return qc_missing; // 无观测任务或者缺测，返回
//		}
//		else if(qc_missing == Quality.Z_QC_OK){ // 有效值
//			qcResult = qc_limit; // 0, 2
//			qcResult.setDescription("_" + "界限检查");
//			return qcResult; // 界限检查错误，返回
//		}
//
//		return qcResult;
//	}
//	/**
//	 * 完整四步的质控,返回最后的质控
//	 */
//	default  Quality getQcResult(Quality qc_missing, Quality qc_limit, Quality qc_range, Quality qc_internal){
//		Quality qcResult = qc_missing; // 0,7,8
//		if(qc_missing == Quality.Z_QC_NO_TASK || qc_missing == Quality.Z_QC_LACK){
//			qcResult.setDescription(qcResult.getDescription() + "_" + "缺测检查");
//			return qc_missing; // 无观测任务或者缺测，返回
//		}
//		else if(qc_missing == Quality.Z_QC_OK){ // 有效值
//			qcResult = qc_limit; // 0, 2
//			if(qc_limit == Quality.Z_QC_ERROR){
//				qcResult.setDescription(qcResult.getDescription() + "_" + "界限检查");
//				return qcResult; // 界限检查错误，返回
//			}
//			else{ // 界限检查正确，下一步范围检查
//				qcResult = qc_range; // -2  -1  0 1 2 9
//				if(qc_range.getCode() == QualityCode.Z_QC_ERROR.getCode()
//						|| qc_range.getCode() == QualityCode.Z_QC_ERROR_NEG.getCode()
//						|| qc_range.getCode() == QualityCode.Z_QC_NO_QC.getCode()){ // -2 2 9
//					qcResult.setDescription(qcResult.getDescription() + "_" + "范围检查");
//					return qcResult;
//				}
//				else { // -1 1 0
//					// 一致性检查
//					if(qc_internal == Quality.Z_QC_ERROR){ //2, 可更新
//						qcResult = qc_internal;
//						qcResult.setDescription(qcResult.getDescription() + "_" + "内部一致性检查");
//						return qcResult;
//					}
//					else { // 0， range检查是否为1或-1 ， 不可更新
//						if(qcResult == Quality.Z_QC_DOUBT || qcResult == Quality.Z_QC_DOUBT_NEG){
//							qcResult.setDescription(qcResult.getDescription() + "_" + "范围检查");
//							return qcResult;
//						}
//						else {  // 范围检查返回0， 返回内部一致性结果
//							qcResult = qc_internal;
//							qcResult.setDescription(qcResult.getDescription() + "_" + "内部一致性检查");
//							return qcResult;
//						}
//					}
//				}
//			}
//		}
//		return qcResult;
//	}
//
//}
