package cma.cimiss2.dpc.indb.agme.dc_agme_manl_grass.service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_08;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_09;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_10;

public class FindVBBBGrass {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String packagePath = "cma.cimiss2.dpc.indb.agme.grass.service.FindVBBBGrass";

	/**
	 * @param loggerBuffer 
	 * @param table 
	 * @Title: findVBBBGrass01 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_01 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass01(Agme_Grass_01 agme_Grass_01, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql01 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71501=? and V71002=?";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql01);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_01.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_01.getStationNumberChina());
				Pstmt.setInt(ii++, agme_Grass_01.getHerbageName());
				Pstmt.setInt(ii++, agme_Grass_01.getDevelopmentalPeriod());
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}

		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass01_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass02 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_02 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass02(Agme_Grass_02 agme_Grass_02, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql02 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71501=? ";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql02);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_02.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_02.getStationNumberChina());
				Pstmt.setInt(ii++, agme_Grass_02.getHerbageName());
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass02_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass03 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_03 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass03(Agme_Grass_03 agme_Grass_03, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql03 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71501=? ";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql03);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_03.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_03.getStationNumberChina());
				Pstmt.setInt(ii++, agme_Grass_03.getHerbageName());
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass03_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass04 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_04 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass04(Agme_Grass_04 agme_Grass_04, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71009=? ";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql04);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_04.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_04.getStationNumberChina());
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_04.getCoverDegree()));
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query  agme_grass04_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param string 
	 * @Title: findVBBBGrass05 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_05 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass05(Agme_Grass_05 agme_Grass_05, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71501=? ";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql04);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_05.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_05.getStationNumberChina());
				Pstmt.setInt(ii++, agme_Grass_05.getHerbageName());
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass05_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer); 
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass06 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_06 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass06(Agme_Grass_06 agme_Grass_06, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql06 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71920=? ";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql06);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_06.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_06.getStationNumberChina());
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_06.getRankCondition()));// 膘情等级
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass06_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass07 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_07 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass07(Agme_Grass_07 agme_Grass_07, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and  WEIGHT_01=? and WEIGHT_02=? and WEIGHT_03=? and WEIGHT_04=? and WEIGHT_05=?";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql04);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_07.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_07.getStationNumberChina());
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_07.getRamWeight_1()));// 羯羊_1体重
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_07.getRamWeight_2()));// 羯羊_2体重
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_07.getRamWeight_3()));// 羯羊_3体重
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_07.getRamWeight_4()));// 羯羊_4体重
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_07.getRamWeight_5()));// 羯羊_5体重
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass07_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null; 
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass08 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_08 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass08(Agme_Grass_08 agme_Grass_08, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71501=? and V71601=?";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql04);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_08.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_08.getStationNumberChina());
				Pstmt.setString(ii++, agme_Grass_08.getLivestockName());// 畜群家畜名称
				Pstmt.setString(ii++, agme_Grass_08.getLivestockBreeds());// 家畜品种
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass08_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param table 
	 * @Title: findVBBBGrass09 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_09 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass09(Agme_Grass_09 agme_Grass_09, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql09 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and  V71616_02=?";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql09);
				int ii = 1;
				Pstmt.setString(ii++, agme_Grass_09.getObservationStarTime());
				Pstmt.setString(ii++, agme_Grass_09.getStationNumberChina());
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_09.getAnimalHusbandryName()));// 牧事活动名称
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass09_chn_tab VBB  error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	/**
	 * @param loggerBuffer 
	 * @param tablename 
	 * @Title: findVBBBGrass10 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * agme_Grass_10 @param connection @return 返回值说明 @throws
	 */
	public static String findVBBBGrass10(Agme_Grass_10 agme_Grass_10, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {

		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql10 = "select  V_BBB from  "+tablename+"   where D_DATETIME=? and  V01301=? and V71912=? and V71116=?";
		try {

			
			if (connection != null) {
				Pstmt = connection.prepareStatement(sql10);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_10.getObservationTime().getTime()));
				Pstmt.setString(ii++, agme_Grass_10.getStationNumberChina());
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_10.getGrassLayerType()));// 草层类型
				Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_10.getMeasurementSite()));// 测量场地
				resultSet = Pstmt.executeQuery();
				if (resultSet.next())
					return resultSet.getString(1);
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + "database query agme_grass10_chn_tab VBB error" + e.getMessage());
		} finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}

	public static void free(ResultSet resultSet, PreparedStatement Pstmt, StringBuffer loggerBuffer) {
		if (resultSet != null) {
			try {
				resultSet.close();
				resultSet = null;
			} catch (SQLException e) {
				loggerBuffer.append("\n " + packagePath + "database query agme_grass close ResultSet error" + e.getMessage());
			}

		}
		if (Pstmt != null) {
			try {
				Pstmt.close();
				Pstmt = null;
			} catch (SQLException e) {
				loggerBuffer.append("\n " + packagePath + "database query agme_grass close PreparedStatement error" + e.getMessage());
			}
		}

	}

}
