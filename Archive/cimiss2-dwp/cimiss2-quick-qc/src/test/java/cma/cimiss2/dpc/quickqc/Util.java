package cma.cimiss2.dpc.quickqc;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.surf.DecodeBABJ;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.util.CommonUtil;
import out.ret2excel.ResultBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 测试公共方法
 *
 * @Author: When6passBye
 * @Date: 2019-08-22 15:06
 **/
public class Util {

    private static String CONFIG_PATH = System.getProperty("user.dir") + "/config/";
    public static final String SOURCE_DIR = "sourceDir";
    public static final String EXCEPT_SHEET = "exceptSheet";
    public static final String RESULT_SHEET = "resultSheet";
    public static final String EXCEL_DIR = "excelDir";
    public static final String TEST_OK = "✔";
    public static final String TEST_FAIL = "✖️";


    public static final String THEAD = "区站号,观测时次（UTC）,经度,纬度,海拔高度,气压表海拔高度,要素编码,观测任务[0（无）|1（自动）|2（人工）],要素值,";
    public static final String TLACK = "缺测检查质控码,缺测检查描述[通过|未通过],";
    public static final String TLIMIT = "界限值检查质控码,[左界限|右界限],";
    public static final String TRANGE = "范围值检查质控码,[最小值|最大值],";
    public static final String INTERNAL = "范围值检查质控码,[最小值|最大值],";
    public static final String FILEQC = "文件级质控码";
    public static final String SPILITE = ",";

    public static final String PROPER_NAME = "dwp_quick_qc.properties";
    public static final String TTITLE = THEAD + TLACK + TLIMIT + TRANGE + FILEQC + "\n";
    private static List<File> FILE_LIST = new ArrayList<>();
    private static Properties TEST_CONF;

    /**
     * 获取配置文件路径
     *
     * @return : java.lang.String
     * @author : When6passBye
     * @date : 2019/9/3 2:17 PM
     */
    public static String getConfigPath() {
        return CONFIG_PATH;
    }

    /**
     * 获取配置文件properties
     *
     * @return : java.util.Properties
     * @author : When6passBye
     * @date : 2019/9/3 2:15 PM
     */
    public static Properties getConfPro() {
        if (TEST_CONF == null) {
            Properties properties = new Properties();
            InputStream inp = Util.class.getClassLoader().getResourceAsStream(PROPER_NAME);
            try {
                properties.load(inp);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return properties;
        } else {
            return TEST_CONF;
        }

    }


    /**
     * 读取文件返回一个SurfaceObservationDataNation
     *
     * @param resourceDir : 目标目录，只会读取这个目录的第一个文件
     * @return : cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation
     * @author : When6passBye
     * @date : 2019/8/23 2:14 PM
     */
    public static List<SurfaceObservationDataNation> dataRead(String resourceDir, ResultBean bean) {
        //读取文件
        String path = getConfigPath() + resourceDir;
        getFile(new File(path), FILE_LIST);
        ParseResult<SurfaceObservationDataNation> pr;
        DecodeBABJ decodeBABJ = new DecodeBABJ();
        //就只有一个
        File file = FILE_LIST.get(0);
        bean.setFileName(file.getName());
        pr = decodeBABJ.decode(file, new HashSet<>());
        List<SurfaceObservationDataNation> data = pr.getData();
        if (data == null || data.size() < 1) {
            CommonUtil.MESSAGE_LOGGER.error(resourceDir + file.getName() + " :null data");
            throw new RuntimeException("null data");
        }
        CommonUtil.MESSAGE_LOGGER.info(resourceDir + file.getName());
        return data;
    }

    /**
     * 设置观测站基本信息
     *
     * @param obj :地面数据
     * @return : BaseStationInfo
     * @author : When6passBye
     * @date : 2019/8/16 4:24 PM
     */
    public static BaseStationInfo getStationInfo(SurfaceObservationDataNation obj) {
        BaseStationInfo stationInfo = new BaseStationInfo();
        String obs_station = obj.getStationNumberChina();
        double lat = obj.getLatitude();
        double lon = obj.getLongitude();
        double staHeight = obj.getHeightOfSationGroundAboveMeanSeaLevel();
        double barometricHeight = obj.getHeightOfBarometerAboveMeanSeaLevel();

        stationInfo.setStationCode(obs_station);
        stationInfo.setLatitude(lat);
        stationInfo.setLongitude(lon);
        stationInfo.setAltitude(staHeight);
        stationInfo.setAltitudeP(barometricHeight);
        return stationInfo;
    }


    /**
     * Gets file.
     *
     * @param file :
     * @return : void
     * @author : When6passBye
     * @date : 2019/8/16 4:36 PM
     */
    private static void getFile(File file, List<File> list) {
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        getFile(file1, list);
                    } else {
                        list.add(file1);
                    }
                }
            }
        }

    }
}
