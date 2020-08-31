package cma.cimiss2.dpc.decoder.radi;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbHcTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbTlTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/5 0005 17:57
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class FXiliTurbTab {
    /**
     * 结果集
     */
    private ParseResult<SurfXiliTurbTab> parseResult = new ParseResult<SurfXiliTurbTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public Map<String, Object> decode(File file, String pathname) {
        Map<String, Object> resultMap = new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false);
        if (file != null && file.exists() && file.isFile()) {
            if (file.length() <= 0) {
                decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                resultMap.put("decodingInfo", decodingInfo);
                return resultMap;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;

                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                List<SurfXiliTurbHcTab> surfXiliTurbHcTabList = new ArrayList<>();
                List<SurfXiliTurbTlTab> surfXiliTurbTlTabList = new ArrayList<>();
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    if (pathname.contains("PBL_FLUX_O")) {
                        String messages = txtFileContent.get(0).split("\\s+")[0];
                        String V01301 = messages.substring(0, 5);
                        String V04001 = messages.substring(5, 9);
                        String V04002 = messages.substring(9, 11);
                        String V04003 = messages.substring(11, 13);
                        String V04004 = messages.substring(13, 15);
                        String V06001 = messages.substring(15, 22);
                        String V05001 = messages.substring(22, 28);
                        String V07001 = messages.substring(28, 35);
                        String HEITH_THREE_WIND = messages.substring(35, 40);
                        String ANGLE_THREE_WIND = messages.substring(40, 43);
                        String HEITH_RED_H2OCO2 = messages.substring(43, 48);
                        String V07031 = messages.substring(48, 55);
                        for (int i = 1; i < txtFileContent.size(); i++) {
                            SurfXiliTurbHcTab surfXiliTurbHcTab = new SurfXiliTurbHcTab();
                            String uuid = UUID.randomUUID().toString();
                            String dDataId = "A.0006.0007.S0001";
                            String dDatetime = V04001 + "-" + V04002 + "-" + V04003 + " " + txtFileContent.get(i).substring(0, 5);
                            String HOURMIN = txtFileContent.get(i).substring(0, 5);
                            String X_V11202 = txtFileContent.get(i).substring(10, 19);
                            String Y_V11202 = txtFileContent.get(i).substring(19, 28);
                            String Z_V11202 = txtFileContent.get(i).substring(28, 37);
                            String CO2_DENSITY = txtFileContent.get(i).substring(37, 45);
                            String VAPOR_DENSITY = txtFileContent.get(i).substring(45, 53);
                            String VTOU = txtFileContent.get(i).substring(53, 61);
                            if (txtFileContent.get(i).substring(53, 61).endsWith("////////")) {
                                VTOU = "0.0";
                            }
                            String FLUTRM = txtFileContent.get(i).substring(61, 69);
                            if (txtFileContent.get(i).substring(61, 69).endsWith("////////")) {
                                FLUTRM = "0.0";
                            }
                            String V10004 = txtFileContent.get(i).substring(69, 76);
                            String VTOU_WINDTEM = txtFileContent.get(i).substring(76, 77);
                            String RED_H2OCO2_VUE = txtFileContent.get(i).substring(77, 78);
                            String RED_H2OCO2_AGC = txtFileContent.get(i).substring(78, 80);

                            surfXiliTurbHcTab.setdRecordId(changeType(uuid));

                            surfXiliTurbHcTab.setdDataId(dDataId);

                            surfXiliTurbHcTab.setdDatetime(dDatetime);

                            surfXiliTurbHcTab.setV01301(changeType(V01301));

                            surfXiliTurbHcTab.setV04001(changeType(V04001));

                            surfXiliTurbHcTab.setV04002(changeType(V04002));

                            surfXiliTurbHcTab.setV04003(changeType(V04003));

                            surfXiliTurbHcTab.setV04004(changeType(V04004));

                            surfXiliTurbHcTab.setV06001(new BigDecimal(changeType(V06001)));

                            surfXiliTurbHcTab.setV05001(new BigDecimal(changeType(V05001)));

                            surfXiliTurbHcTab.setV07001(new BigDecimal(changeType(V07001)));

                            surfXiliTurbHcTab.setHeithThreeWind(new BigDecimal(changeType(HEITH_THREE_WIND)));

                            surfXiliTurbHcTab.setAngleThreeWind(new BigDecimal(changeType(ANGLE_THREE_WIND)));

                            surfXiliTurbHcTab.setHeithRedH2oco2(new BigDecimal(changeType(HEITH_RED_H2OCO2)));

                            surfXiliTurbHcTab.setV07031(new BigDecimal(changeType(V07031)));

                            surfXiliTurbHcTab.setHourmin(changeType(HOURMIN));

                            surfXiliTurbHcTab.setxV11202(new BigDecimal(changeType(X_V11202)));

                            surfXiliTurbHcTab.setyV11202(new BigDecimal(changeType(Y_V11202)));

                            surfXiliTurbHcTab.setzV11202(new BigDecimal(changeType(Z_V11202)));

                            surfXiliTurbHcTab.setCo2Density(new BigDecimal(changeType(CO2_DENSITY)));

                            surfXiliTurbHcTab.setVaporDensity(new BigDecimal(changeType(VAPOR_DENSITY)));

                            surfXiliTurbHcTab.setVtou(new BigDecimal(changeType(VTOU)));

                            surfXiliTurbHcTab.setFlutrm(new BigDecimal(changeType(FLUTRM)));

                            surfXiliTurbHcTab.setV10004(new BigDecimal(changeType(V10004)));

                            surfXiliTurbHcTab.setVtouWindtem(new BigDecimal(changeType(VTOU_WINDTEM)));

                            surfXiliTurbHcTab.setRedH2oco2Vue(new BigDecimal(changeType(RED_H2OCO2_VUE)));

                            surfXiliTurbHcTab.setRedH2oco2Agc(new BigDecimal(changeType(RED_H2OCO2_AGC)));

                            surfXiliTurbHcTabList.add(surfXiliTurbHcTab);
                        }
                    } else if (pathname.contains("PBL_FLUX_S")) {
                        String messages = txtFileContent.get(0);
                        String V01301 = messages.substring(0, 5);
                        String V04001 = messages.substring(5, 9);
                        String V04002 = messages.substring(9, 11);
                        String V06001 = messages.substring(11, 18);
                        String V05001 = messages.substring(18, 24);
                        String V07001 = messages.substring(24, 31);
                        String HEITH_THREE_WIND = messages.substring(31, 36);
                        String ANGLE_THREE_WIND = messages.substring(36, 39);
                        String HEITH_RED_H2OCO2 = messages.substring(39, 44);
                        String V07031 = messages.substring(44, 51);
                        String V02320 = messages.substring(51, 61);
                        String VERSION = "V1.00";
                        String THREE_WIND_MODE = messages.substring(61, 69);
                        String ANALYZER_MODEL_H20CO2 = messages.substring(69, 77);
                        String Q48008 = messages.substring(77, 78);
                        String HEIGH_VEGETATION = messages.substring(78, 82);
                        for (int i = 1; i < txtFileContent.size(); i++) {
                            SurfXiliTurbTlTab surfXiliTurbTlTab = new SurfXiliTurbTlTab();
                            String uuid = UUID.randomUUID().toString();
                            String post_list = txtFileContent.get(i);
                            String dDataId = "A.0006.0008.S0001";
                            String dDatetime = post_list.substring(0, 16);
                            String[] ytime=dDatetime.split("-");
                            V04001=ytime[0];
                            V04002= ytime[1];
                            String[] dd=ytime[2].split(" ");
                            String V04003=dd[0];
                            String[] mm=dd[1].split(":");
                            String V04004=mm[0];
                            String V04005=mm[1];
                            String CO2_WPL = post_list.substring(16, 24);
                            String LHF_WPL = post_list.substring(24, 32);
                            String VTOU_SHF = post_list.substring(32, 40);
                            String MOMFLUX = post_list.substring(40, 48);
                            String FRI_V11202 = post_list.substring(48, 56);
                            String NONCO2_WPL = post_list.substring(56, 64);
                            String NONLHF_WPL = post_list.substring(64, 72);
                            String MOI_CO2LHF_WPL = post_list.substring(72, 80);
                            String MOI_CO2SHF_WPL = post_list.substring(80, 88);
                            String MOI_LHF = post_list.substring(88, 96);
                            String MOI_SHF = post_list.substring(96, 104);
                            String UZ_V11202_VARI = post_list.substring(104, 112);
                            String UZ_UX_VARI = post_list.substring(112, 120);
                            String UZ_UY_VARI = post_list.substring(120, 128);
                            String UZ_V11202_CO2DENSITY_VARI = post_list.substring(128, 136);
                            String UZ_V11202_VAPOR_VARI = post_list.substring(136, 144);
                            String UZ_V11202_VTOU_VARI = post_list.substring(144, 152);
                            String UX_V11202_VARI = post_list.substring(152, 160);
                            String UX_UY_VARI = post_list.substring(160, 168);
                            String UX_V11202_CO2DENSITY_VARI = post_list.substring(168, 176);
                            String UX_V11202_VAPOR_VARI = post_list.substring(176, 184);
                            String UX_V11202_VTOU_VAR = post_list.substring(184, 192);
                            String UY_V11202_VARI = post_list.substring(192, 200);
                            String UY_V11202_CO2DENSITY_VARI = post_list.substring(200, 208);
                            String UY_V11202_VAPOR_VARI = post_list.substring(208, 216);
                            String UY_V11202_VTOU_VARI = post_list.substring(216, 224);
                            String CO2DENSITY_VARI = post_list.substring(224, 232);
                            String VAPOR_DENSITY_VARI = post_list.substring(232, 240);
                            String VTOU_VARI = post_list.substring(240, 248);
                            String UX_V11202 = post_list.substring(248, 255);
                            String UY_V11202 = post_list.substring(255, 262);
                            String UZ_V11202 = post_list.substring(262, 269);
                            String CO2DENSITY_AVG = post_list.substring(269, 276);
                            String VAPOR_DENSITY_AVG = post_list.substring(276, 283);
                            String VTOU_AVG = post_list.substring(283, 290);
                            String POHS_AVG = post_list.substring(290, 297);
                            String AIR_DENSITY_AVG = post_list.substring(297, 304);
                            String WATER_VAPOR_AVG = post_list.substring(304, 311);
                            String AIR_TEM_MEAN_ATM_AVG = post_list.substring(311, 318);
                            String MEAN_AIR_TRM = post_list.substring(318, 325);
                            String AVE_VAP_PRESSURE = post_list.substring(325, 332);
                            String V11201_XAVG = post_list.substring(332, 339);
                            String VCH_V11201 = post_list.substring(339, 346);
                            String COMPASS_V11201 = post_list.substring(346, 353);
                            String SDS_WIND = post_list.substring(353, 360);
                            String UWS_V11201 = post_list.substring(360, 367);
                            String NUM_SAMPLES = post_list.substring(367, 374);
                            String UWS_WARNINGNUM = post_list.substring(374, 381);
                            String ANALYZER_H2OCO2_WARNINGNUM = post_list.substring(381, 386);
                            String UWS_TEM_WARNINGNUM = post_list.substring(386, 391);
                            String UWS_LOCK_WARNINGNUM = post_list.substring(391, 396);
                            String UWS_L_WARNINGNUM = post_list.substring(396, 401);
                            String UWS_H_WARNINGNUM = post_list.substring(401, 406);
                            String ANALYZER_H2OCO2_BRNUM = post_list.substring(406, 411);
                            String ANALYZER_H2OCO2_TESTWARNINGNUM = post_list.substring(411, 416);
                            String ANALYZER_PLC_H2OCO2 = post_list.substring(416, 421);
                            String ANALYZER_H2OCO2_SYNCNUM = post_list.substring(421, 426);
                            String ANALYZER_H2OCO2_AVG = post_list.substring(426, 431);
                            String V02262_AVG = post_list.substring(431, 435);
                            String V02264_AVG = post_list.substring(435, 440);

                            surfXiliTurbTlTab.setdRecordId(new BigDecimal(getOrderIdByUUId()));

                            surfXiliTurbTlTab.setdDataId(dDataId);

                            surfXiliTurbTlTab.setdDatetime(dDatetime);


                            surfXiliTurbTlTab.setV01301(changeType(V01301));

                            surfXiliTurbTlTab.setV04001(new BigDecimal(changeType(V04001)));

                            surfXiliTurbTlTab.setV04002(new BigDecimal(changeType(V04002)));

                            surfXiliTurbTlTab.setV04003(new BigDecimal(changeType(V04003)));

                            surfXiliTurbTlTab.setV04004(new BigDecimal(changeType(V04004)));

                            surfXiliTurbTlTab.setV04005(new BigDecimal(changeType(V04005)));

                            surfXiliTurbTlTab.setV06001(new BigDecimal(changeType(V06001)));

                            surfXiliTurbTlTab.setV05001(new BigDecimal(changeType(V05001)));

                            surfXiliTurbTlTab.setV07001(new BigDecimal(changeType(V07001)));

                            surfXiliTurbTlTab.setHeithThreeWind(new BigDecimal(changeType(HEITH_THREE_WIND)));

                            surfXiliTurbTlTab.setAngleThreeWind(new BigDecimal(changeType(ANGLE_THREE_WIND)));

                            surfXiliTurbTlTab.setHeithRedH2oco2(new BigDecimal(changeType(HEITH_RED_H2OCO2)));

                            surfXiliTurbTlTab.setV07031(new BigDecimal(changeType(V07031)));

                            surfXiliTurbTlTab.setV02320(changeType(V02320));

                            surfXiliTurbTlTab.setVersion(changeType(VERSION));

                            surfXiliTurbTlTab.setThreeWindMode(changeType(THREE_WIND_MODE));

                            surfXiliTurbTlTab.setAnalyzerModelH20co2(changeType(ANALYZER_MODEL_H20CO2));

                            surfXiliTurbTlTab.setQ48008(changeType(Q48008));

                            surfXiliTurbTlTab.setHeighVegetation(changeType(HEIGH_VEGETATION));

                            try {
                                surfXiliTurbTlTab.setCo2Wpl(new BigDecimal(changeType(CO2_WPL)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setCo2Wpl(new BigDecimal(999999));
                            }

                            try {
                                surfXiliTurbTlTab.setLhfWpl(new BigDecimal(changeType(LHF_WPL)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setLhfWpl(new BigDecimal(999999));
                            }
                            try {
                                surfXiliTurbTlTab.setVtouShf(new BigDecimal(changeType(VTOU_SHF)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setVtouShf(new BigDecimal(999999));
                            }
                            try {
                                surfXiliTurbTlTab.setMomflux(new BigDecimal(changeType(MOMFLUX)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setMomflux(new BigDecimal(999999));
                            }
                            try {
                                if (changeType(FRI_V11202).equals("999999")) {
                                    surfXiliTurbTlTab.setFriV11202(new BigDecimal(999999));
                                } else {
                                    surfXiliTurbTlTab.setFriV11202(new BigDecimal(String.valueOf(Double.parseDouble(changeType(FRI_V11202)) / 10)));
                                }
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setFriV11202(new BigDecimal(999999));
                            }

                            try {
                                surfXiliTurbTlTab.setNonco2Wpl(new BigDecimal(changeType(NONCO2_WPL)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setNonco2Wpl(new BigDecimal(999999));
                            }
                            try {
                                surfXiliTurbTlTab.setNonlhfWpl(new BigDecimal(changeType(NONLHF_WPL)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setNonlhfWpl(new BigDecimal(999999));
                            }

                            try {
                                surfXiliTurbTlTab.setMoiCo2lhfWpl(new BigDecimal(changeType(MOI_CO2LHF_WPL)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setMoiCo2lhfWpl(new BigDecimal(999999));
                            }
                            try {
                                surfXiliTurbTlTab.setMoiCo2shfWpl(new BigDecimal(changeType(MOI_CO2SHF_WPL)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setMoiCo2shfWpl(new BigDecimal(999999));
                            }
                            try {
                                surfXiliTurbTlTab.setMoiLhf(new BigDecimal(changeType(MOI_LHF)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setMoiLhf(new BigDecimal(999999));
                            }
                            try {
                                surfXiliTurbTlTab.setMoiShf(new BigDecimal(changeType(MOI_SHF)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setMoiShf(new BigDecimal(999999));
                            }

                            try {
                                surfXiliTurbTlTab.setUzV11202Vari(new BigDecimal(changeType(UZ_V11202_VARI)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setUzV11202Vari(new BigDecimal(999999));
                            }

                            try {
                                surfXiliTurbTlTab.setUzUxVari(new BigDecimal(changeType(UZ_UX_VARI)));
                            } catch (Exception e) {
                                surfXiliTurbTlTab.setUzUxVari(new BigDecimal(999999));
                            }
                            try {

                                surfXiliTurbTlTab.setUzUyVari(new BigDecimal(changeType(UZ_UY_VARI)));

                                surfXiliTurbTlTab.setUzV11202Co2densityVari(new BigDecimal(changeType(UZ_V11202_CO2DENSITY_VARI)));

                                surfXiliTurbTlTab.setUzV11202VaporVari(new BigDecimal(changeType(UZ_V11202_VAPOR_VARI)));

                                surfXiliTurbTlTab.setUzV11202VtouVari(new BigDecimal(changeType(UZ_V11202_VTOU_VARI)));

                                surfXiliTurbTlTab.setUxV11202Vari(new BigDecimal(changeType(UX_V11202_VARI)));

                                surfXiliTurbTlTab.setUxUyVari(new BigDecimal(changeType(UX_UY_VARI)));

                                surfXiliTurbTlTab.setUxV11202Co2densityVari(new BigDecimal(changeType(UX_V11202_CO2DENSITY_VARI)));

                                surfXiliTurbTlTab.setUxV11202VaporVari(new BigDecimal(changeType(UX_V11202_VAPOR_VARI)));

                                surfXiliTurbTlTab.setUxV11202VtouVar(new BigDecimal(changeType(UX_V11202_VTOU_VAR)));

                                surfXiliTurbTlTab.setUyV11202Vari(new BigDecimal(changeType(UY_V11202_VARI)));

                                surfXiliTurbTlTab.setUyV11202Co2densityVari(new BigDecimal(changeType(UY_V11202_CO2DENSITY_VARI)));

                                surfXiliTurbTlTab.setUyV11202VaporVari(new BigDecimal(changeType(UY_V11202_VAPOR_VARI)));

                                surfXiliTurbTlTab.setUyV11202VtouVari(new BigDecimal(changeType(UY_V11202_VTOU_VARI)));

                                surfXiliTurbTlTab.setCo2densityVari(new BigDecimal(changeType(CO2DENSITY_VARI)));

                                surfXiliTurbTlTab.setVaporDensityVari(new BigDecimal(changeType(VAPOR_DENSITY_VARI)));

                                surfXiliTurbTlTab.setVtouVari(new BigDecimal(changeType(VTOU_VARI)));

                                surfXiliTurbTlTab.setUxV11202(new BigDecimal(changeType(UX_V11202)));

                                surfXiliTurbTlTab.setUyV11202(new BigDecimal(changeType(UY_V11202)));

                                surfXiliTurbTlTab.setUzV11202(new BigDecimal(changeType(UZ_V11202)));

                                surfXiliTurbTlTab.setCo2densityAvg(new BigDecimal(changeType(CO2DENSITY_AVG)));

                                surfXiliTurbTlTab.setVaporDensityAvg(new BigDecimal(changeType(VAPOR_DENSITY_AVG)));

                                surfXiliTurbTlTab.setVtouAvg(new BigDecimal(changeType(VTOU_AVG)));

                                surfXiliTurbTlTab.setPohsAvg(new BigDecimal(changeType(POHS_AVG)));

                                surfXiliTurbTlTab.setAirDensityAvg(new BigDecimal(changeType(AIR_DENSITY_AVG)));

                                surfXiliTurbTlTab.setWaterVaporAvg(new BigDecimal(changeType(WATER_VAPOR_AVG)));

                                surfXiliTurbTlTab.setAirTemMeanAtmAvg(new BigDecimal(changeType(AIR_TEM_MEAN_ATM_AVG)));

                                surfXiliTurbTlTab.setMeanAirTrm(new BigDecimal(changeType(MEAN_AIR_TRM)));

                                surfXiliTurbTlTab.setAveVapPressure(new BigDecimal(changeType(AVE_VAP_PRESSURE)));

                                if (changeType(V11201_XAVG).equals("999999")) {
                                    surfXiliTurbTlTab.setV11201Xavg(new BigDecimal(999999));
                                } else {
                                    surfXiliTurbTlTab.setV11201Xavg(new BigDecimal(String.valueOf(Double.parseDouble(changeType(V11201_XAVG)) / 10)));
                                }

//                                surfXiliTurbTlTab.setV11201Xavg(new BigDecimal(changeType(V11201_XAVG)));

                                if (changeType(VCH_V11201).equals("999999")) {
                                    surfXiliTurbTlTab.setVchV11201(new BigDecimal(999999));
                                } else {
                                    surfXiliTurbTlTab.setVchV11201(new BigDecimal(String.valueOf(Double.parseDouble(changeType(VCH_V11201)) / 10)));
                                }

//                                surfXiliTurbTlTab.setVchV11201(new BigDecimal(changeType(VCH_V11201)));

                                surfXiliTurbTlTab.setCompassV11201(new BigDecimal(changeType(COMPASS_V11201)));

                                surfXiliTurbTlTab.setSdsWind(new BigDecimal(changeType(SDS_WIND)));

                                surfXiliTurbTlTab.setUwsV11201(new BigDecimal(changeType(UWS_V11201)));

                                surfXiliTurbTlTab.setNumSamples(new BigDecimal(changeType(NUM_SAMPLES)));

                                surfXiliTurbTlTab.setUwsWarningnum(new BigDecimal(changeType(UWS_WARNINGNUM)));


                                surfXiliTurbTlTab.setAnalyzerH2oco2Warningnum(new BigDecimal(changeType(ANALYZER_H2OCO2_WARNINGNUM)));

                                surfXiliTurbTlTab.setUwsTemWarningnum(new BigDecimal(changeType(UWS_TEM_WARNINGNUM)));

                                surfXiliTurbTlTab.setUwsLockWarningnum(new BigDecimal(changeType(UWS_LOCK_WARNINGNUM)));

                                surfXiliTurbTlTab.setUwsLWarningnum(new BigDecimal(changeType(UWS_L_WARNINGNUM)));

                                surfXiliTurbTlTab.setUwsHWarningnum(new BigDecimal(changeType(UWS_H_WARNINGNUM)));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Brnum(new BigDecimal(changeType(ANALYZER_H2OCO2_BRNUM)));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Testwarningnum(new BigDecimal(changeType(ANALYZER_H2OCO2_TESTWARNINGNUM)));

                                surfXiliTurbTlTab.setAnalyzerPlcH2oco2(new BigDecimal(changeType(ANALYZER_PLC_H2OCO2)));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Syncnum(new BigDecimal(changeType(ANALYZER_H2OCO2_SYNCNUM)));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Avg(new BigDecimal(changeType(ANALYZER_H2OCO2_AVG)));

                                surfXiliTurbTlTab.setV02262Avg(new BigDecimal(changeType(V02262_AVG)));

                                surfXiliTurbTlTab.setV02264Avg(new BigDecimal(changeType(V02264_AVG)));
                            } catch (Exception e) {

                                surfXiliTurbTlTab.setUzUyVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUzV11202Co2densityVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUzV11202VaporVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUzV11202VtouVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUxV11202Vari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUxUyVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUxV11202Co2densityVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUxV11202VaporVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUxV11202VtouVar(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUyV11202Vari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUyV11202Co2densityVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUyV11202VaporVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUyV11202VtouVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setCo2densityVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setVaporDensityVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setVtouVari(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUxV11202(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUyV11202(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUzV11202(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setCo2densityAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setVaporDensityAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setVtouAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setPohsAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAirDensityAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setWaterVaporAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAirTemMeanAtmAvg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setMeanAirTrm(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAveVapPressure(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setV11201Xavg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setVchV11201(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setCompassV11201(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setSdsWind(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUwsV11201(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setNumSamples(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUwsWarningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Warningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUwsTemWarningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUwsLockWarningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUwsLWarningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setUwsHWarningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Brnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Testwarningnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAnalyzerPlcH2oco2(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Syncnum(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setAnalyzerH2oco2Avg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setV02262Avg(new BigDecimal("999999"));

                                surfXiliTurbTlTab.setV02264Avg(new BigDecimal("999999"));
                            }
                            surfXiliTurbTlTabList.add(surfXiliTurbTlTab);
                        }
                    }
                    if (!surfXiliTurbHcTabList.isEmpty()) {
                        resultMap.put("surfXiliTurbHcTabList", surfXiliTurbHcTabList);
                    }
                    if (!surfXiliTurbTlTabList.isEmpty()) {
                        resultMap.put("surfXiliTurbTlTabList", surfXiliTurbTlTabList);
                    }

                } else {
                    if (txtFileContent == null || txtFileContent.size() == 0) {
                        // 空文件
                        parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                    } else {
                        // 数据只有一行，格式不正确
                        parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
            } catch (FileNotFoundException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
            }
        } else {
            // file not exsit
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        decodingInfo.setSuccess(true);
        resultMap.put("decodingInfo", decodingInfo);
        return resultMap;
    }

    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/")>-1 || dates.equals("///") || dates.equals('"') || dates.equals("////") || dates.equals("/////") || dates.equals("//////") || dates.equals("////////") || dates.equals("---") ||  dates.equals("------") || dates.equals("-----")) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }

    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
}
