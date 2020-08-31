package cma.cimiss2.dpc.decoder.tools.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XmlOper {
	public XmlOper(){}
	
	public static Document getDoc(String xmlname)
	{
		Document doc = null ;
		SAXReader sr = new SAXReader();

		try {
//            InputStream resource = XmlOper.class.getClassLoader().getResourceAsStream("config/"+xmlname);
			FileInputStream resource = new FileInputStream(xmlname);

			doc = sr.read(resource);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return doc;
	}
	
	public static Element getElement(Document doc ,String path)
    {
    	Element returnE =null;
		Element root = doc.getRootElement();  
		returnE = (Element)root.selectSingleNode(path);
        return returnE;
    }
	
	public static String getElementV(Document doc ,String path)
    {
		return getElement(doc,path).getText();
    }
	
	
	public static void main(String[] args) {
		
		
		String e = XmlOper.getElementV(XmlOper.getDoc("mongodb.xml"),"//mongodb//connectionsPerHost");
		System.out.println(e);
	}
}
