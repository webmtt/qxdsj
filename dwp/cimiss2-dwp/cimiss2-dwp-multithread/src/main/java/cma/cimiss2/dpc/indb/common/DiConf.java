package cma.cimiss2.dpc.indb.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


/**
 * 
 * <br>
 * @Title:  DiConf.java   
 * @Package org.cimiss2.dwp.RADAR.config   
 * @Description:    TODO(读取DI配置信息)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午9:00:43   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class DiConf {
	public static final Logger logger = LoggerFactory.getLogger(DiConf.class);
	public static HashMap<String, DiConf> sm_DI_info;
	
	String m_strDpcType; //DPC四级编码
	String m_strSodType; //SOD四级编码
	String m_strHeader; //报头
	int [] m_iDateConfArray = new int[3]; //日期定义：${日期字段索引:开始位置:长度}
	int [] m_iTimeConfArray = new int[3]; //时次定义：${时次字段索引:开始位置:长度}
	int [] m_iStatConfArray = new int[3]; //站号定义：${站号字段索引:开始位置:长度}
	
	public DiConf() {
	}

	public DiConf(DiConf diConf)
	{
		m_strDpcType = diConf.getDpcType();
		m_strSodType = diConf.getSodType();
		m_strHeader = diConf.getHeader();
		setDateConfArray(diConf.getDateConfArray());
		setTimeConfArray(diConf.getTimeConfArray());
		setStatConfArray(diConf.getStatConfArray());
	}

	public String getDpcType() {
		return m_strDpcType;
	}

	public void setDpcType(String strDpcType) {
		m_strDpcType = strDpcType;
	}

	public String getSodType() {
		return m_strSodType;
	}

	public void setSodType(String strSodType) {
		m_strSodType = strSodType;
	}

	public String getHeader() {
		return m_strHeader;
	}

	public void setHeader(String strHeader) {
		m_strHeader = strHeader;
	}

	public int[] getDateConfArray() {
		return m_iDateConfArray;
	}

	public void setDateConfArray(int[] iDateConfArray) {
		System.arraycopy(iDateConfArray, 0, m_iDateConfArray, 0, m_iDateConfArray.length);
	}

	public int[] getTimeConfArray() {
		return m_iTimeConfArray;
	}

	public void setTimeConfArray(int[] iTimeConfArray) {
		System.arraycopy(iTimeConfArray, 0, m_iTimeConfArray, 0, m_iTimeConfArray.length);
	}

	public int[] getStatConfArray() {
		return m_iStatConfArray;
	}

	public void setStatConfArray(int[] iStatConfArray) {
		System.arraycopy(iStatConfArray, 0, m_iStatConfArray, 0, m_iStatConfArray.length);
	}

	public static boolean readDI(String diFile) {
		BufferedReader br = null;
		String strCtsType = ""; //CTS四级编码
		DiConf diConf = null; //某类资料DI配置信息
		try {
			br = new BufferedReader(new FileReader(diFile));
			sm_DI_info = new HashMap<String, DiConf>();
			String strLine;	//DI配置文件中一行信息
			while((strLine = br.readLine()) != null) {
				if (!strLine.isEmpty()) { //不为空
					if (strLine.startsWith("#")) { //注释行
						continue;
					} else if (strLine.startsWith("[") && strLine.endsWith("]")) { //CTS四级编码
						strCtsType = strLine.substring(1, strLine.length()-1);
						diConf = new DiConf();
					} else {
						//资料配置信息读取
						if (strLine.contains("c_dpc_type")) {
							diConf.setDpcType(strLine.split("=")[1]);
						} else if (strLine.contains("c_sod_type")) {
							diConf.setSodType(strLine.split("=")[1]);
						} else if (strLine.contains("c_data_header")) {
							diConf.setHeader(strLine.split("=")[1]);
						} else {
							String [] sp = strLine.split("="); //以=分隔字段和值
							int [] iIndexArray = null;
							if(sp[1].contains(":")){
								if(sp[1].startsWith("${") && (sp[1].endsWith("}"))){
									sp[1] = sp[1].substring(2, sp[1].length() - 1);
									iIndexArray = new int[3];
									String [] tmp = sp[1].split(":");
									iIndexArray[0] = Integer.parseInt(tmp[0]);
									iIndexArray[1] = Integer.parseInt(tmp[1]);
									iIndexArray[2] = Integer.parseInt(tmp[2]);
								}
								else{
									System.out.println("ERROR: Wrong Format Of The DI Config! Cts_Type=" + strCtsType + "Field=" + sp[0]);
									logger.error("ERROR: Wrong Format Of The DI Config! Cts_Type=" + strCtsType + "Field=" + sp[0]);
									return false;
								}
							}
							else{
								sp[1] = sp[1].substring(2, sp[1].length() - 1); //${index}
								iIndexArray = new int[3];
								iIndexArray[0] = Integer.parseInt(sp[1]);
								iIndexArray[1] = 0;
								iIndexArray[2] = 100;
							}
							if (sp[0].equalsIgnoreCase("c_date")) {
								diConf.setDateConfArray(iIndexArray);
							} else if (sp[0].equalsIgnoreCase("c_time")) {
								diConf.setTimeConfArray(iIndexArray);
							} else if (sp[0].equalsIgnoreCase("c_station")) {
								diConf.setStatConfArray(iIndexArray);
								sm_DI_info.put(strCtsType, diConf); //读取到c_station则认为某个资料类型的DI配置结束
							}
						}
					}
				} //strLine is not empty
			} //end while
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR: Open DI Config File Error " + e.getMessage());
			logger.error("ERROR: Open DI Config File Error " + e.getMessage());
			return false;
			
		} finally {
			if (br!=null) {
				try {
					br.close();
					br = null;
				} catch (IOException e2) {
					// TODO: handle exception
				}
			}
		}
		
		return true;
	}

}
