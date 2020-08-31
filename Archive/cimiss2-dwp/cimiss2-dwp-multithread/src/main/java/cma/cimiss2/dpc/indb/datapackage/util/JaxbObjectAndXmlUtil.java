package cma.cimiss2.dpc.indb.datapackage.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbObjectAndXmlUtil {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	/**
	 * xml 文档内容反序列化为实体对象
	 * @param xml  文档内容
	 * @param clzz 实体类对象
	 * @return 实体类对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Object(String xml, Class<T> clzz) {
		try {
			JAXBContext context = JAXBContext.newInstance(clzz);
			Unmarshaller unmarshaller = context.createUnmarshaller(); 
			T t = (T) unmarshaller.unmarshal(new StringReader(xml));
			return t;
		} catch (JAXBException e) {
			infoLogger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
