/*
package org.cimiss2.dwp.core.etl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.cimiss2.decode.tools.vo.bean.ParseResult;
import org.cimiss2.decode.tools.vo.bean.PrecipitationObservationDataReg;
import org.cimiss2.dwp.core.IocBuilder;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.nutz.ioc.Ioc;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;

*/
/**
 * 区域自动雨量站数据文件格式的测试
 * 
 * @author shevawen
 *
 *//*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrecipitationObservationDataRegEtlTest {

	static Log log = Logs.get();

	static File file = new File(PrecipitationObservationDataRegEtlTest.class.getResource("/").getPath()
			+ "Z_SURF_C_BCCD-REG_20171115000100_O_AWS-PRF_FTM_PQC.txt");

	static PrecipitationObservationDataRegEtl etl;

	static ParseResult<PrecipitationObservationDataReg> parseResult;

	static List<Map<String, Object>> datas;

	@BeforeClass
	public static void before() {
		Ioc ioc = IocBuilder.ioc();
		etl = ioc.get(PrecipitationObservationDataRegEtl.class);
		etl.setFile(file);
	}

	@Test
	public void test1Extract() {

		log.debug(file.getAbsolutePath());

		parseResult = etl.extract();
		log.debug(Json.toJson(parseResult));

	}

	@Test
	public void test2Transform() {
		// 整点
		datas = etl.transform(parseResult.getData().get(0));
		assertEquals(datas.size(), 61);
		// 分钟 map
		Map<String, Object> data = datas.get(27);
		// 这项有降水
		assertEquals(data.get("V13011"), 0.1);
		data = datas.get(28);
		assertEquals(data.get("V13011"), 0.0);
		// 非整点
		datas = etl.transform(parseResult.getData().get(1));
		assertEquals(datas.size(), 60);

	}

	@Test
	public void test3Load() {

		etl.load(datas);
	}
}
*/
