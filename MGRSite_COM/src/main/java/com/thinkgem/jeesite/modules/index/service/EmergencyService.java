package com.thinkgem.jeesite.modules.index.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thinkgem.jeesite.common.utils.ConPostUtils;
import com.thinkgem.jeesite.common.utils.XMLParseUtil;
import com.thinkgem.jeesite.modules.index.entity.Emergency;
import com.thinkgem.jeesite.modules.index.entity.EmergencyNotice;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
@Service
public class EmergencyService {

	/*@Autowired
	private AreaService areaService;*/

	@Autowired
	private ComparasService comparasService;

	public List<EmergencyNotice> getEmergencyNotice(String url) {
		List<EmergencyNotice> list=new ArrayList<EmergencyNotice>();
		try {
			String xml = ConPostUtils.readContentFromHttpGet(url, new ArrayList<NameValuePair>());
			XMLParseUtil xmlParse = null;
			xmlParse = new XMLParseUtil();
			if (null != xmlParse) {
				Document doc = null;
				doc = xmlParse.parseDocument(xml);
				if (null != doc) {
					NodeList nodeList = null;
					nodeList = (NodeList)xmlParse.selectNodes(doc, "//channel/item");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						String id=xmlParse.getNodeStringValue(node, "./id");
						String title=xmlParse.getNodeStringValue(node, "./title");
						String content = xmlParse.getNodeStringValue(node, "./content");
						content=content.replaceAll("<P>", "").replaceAll("</P>", "").replaceAll("\n", "")
								.replaceAll("\"", "").replaceAll("\'", "");
						String[] contents=content.split("<BR>");
						String publishDep= xmlParse.getNodeStringValue(node, "./publishDep");
						String publishDate = xmlParse.getNodeStringValue(node, "./publishDate");
						for(String str:contents){
							EmergencyNotice en=new EmergencyNotice();
							en.setId(id);
							en.setTitle(title);
							en.setContent(str);
							en.setPublishDep(publishDep);
							en.setPublishDate(publishDate);
							list.add(en);							
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 国家级
	 * @param url
	 * @return
	 */
	public List<EmergencyNotice> getEmergencyNotice2(String url) {
		List<EmergencyNotice> list=new ArrayList<EmergencyNotice>();
		try {
			String xml = ConPostUtils.readContentFromHttpGet(url, new ArrayList<NameValuePair>());
			XMLParseUtil xmlParse = null;
			xmlParse = new XMLParseUtil();
			if (null != xmlParse) {
				Document doc = null;
				doc = xmlParse.parseDocument(xml);
				if (null != doc) {
					NodeList nodeList = null;
					nodeList = (NodeList)xmlParse.selectNodes(doc, "//channel/item");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						String id=xmlParse.getNodeStringValue(node, "./id");
						String title=xmlParse.getNodeStringValue(node, "./title");
						String content = xmlParse.getNodeStringValue(node, "./content");
						content=content.replaceAll("<P>", "").replaceAll("</P>", "").replaceAll("\n", "")
								.replaceAll("\"", "").replaceAll("\'", "");
						content=Html2Text(content);
						String[] contents=content.split("<BR>");
						String publishDep= xmlParse.getNodeStringValue(node, "./publishDep");
						String publishDate = xmlParse.getNodeStringValue(node, "./publishDate");
						for(String str:contents){
							EmergencyNotice en=new EmergencyNotice();
							en.setId(id);
							en.setTitle(title);
							en.setContent(str);
							en.setPublishDep(publishDep);
							en.setPublishDate(publishDate);
							list.add(en);							
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	 public static String Html2Text(String inputString) {  
	        String htmlStr = inputString; // 含html标签的字符串  
	        String textStr = "";  
	        java.util.regex.Pattern p_script;  
	        java.util.regex.Matcher m_script;  
	        java.util.regex.Pattern p_style;  
	        java.util.regex.Matcher m_style;  
	        java.util.regex.Pattern p_html;  
	        java.util.regex.Matcher m_html;  
	        try {  
	            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
	            m_script = p_script.matcher(htmlStr);  
	            htmlStr = m_script.replaceAll(""); // 过滤script标签  
	            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
	            m_style = p_style.matcher(htmlStr);  
	            htmlStr = m_style.replaceAll(""); // 过滤style标签  
	            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	            m_html = p_html.matcher(htmlStr);  
	            htmlStr = m_html.replaceAll(""); // 过滤html标签  
	            textStr = htmlStr;  
	        } catch (Exception e) {System.err.println("Html2Text: " + e.getMessage()); }  
	        //剔除空格行  
	        textStr=textStr.replaceAll("[ ]+", " ");  
	        textStr=textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");  
	        return textStr;// 返回文本字符串  
	    } 
	 /**
	  * 省级
	  * @param URL
	  * @return
	  */
	public List<Emergency> getEmergencyInfo(String URL, Integer emergencyAnalysisMode) {
		List<Emergency> list=new ArrayList<Emergency>();
		String xml = ConPostUtils.readContentFromHttpGet(URL, new ArrayList<NameValuePair>());
		XMLParseUtil xmlParse = null;
		NodeList nodeList = null;
		Document doc = null;
		Emergency eg=null;
		String[] provinces=null;
		String[] types=null;
		String[] levels=null;
		String provinceName="";
		String provinceCode="";
		String type="";
		String typeCode="";
		String level="";
		String levelCode="";	
		String provinceRuler=(String)comparasService.getComparasByKey("yjProvinceRuler");
		String typeRuler=(String)comparasService.getComparasByKey("yjTypeRuler");
		String levelRuler=(String)comparasService.getComparasByKey("yjLevelRuler");
		Area area=null;
		int preIndex=0;
		int lastIndex=0;
		String timeStr="";
		if(provinceRuler!=null&&!"".equals(provinceRuler)){
			provinces=provinceRuler.split(",");
		}
		if(typeRuler!=null&&!"".equals(typeRuler)){
			types=typeRuler.split(",");
		}
		if(levelRuler!=null&&!"".equals(levelRuler)){
			levels=levelRuler.split(",");
		}
		try {
			xmlParse = new XMLParseUtil();
			if (null != xmlParse) {
				doc = xmlParse.parseDocument(xml);
				if(doc!=null){
					nodeList=(NodeList)xmlParse.selectNodes(doc, "//channel/item");
					for(int i=0;i<nodeList.getLength();i++){
						Node node = nodeList.item(i);
						String id=xmlParse.getNodeStringValue(node, "./id");
						String title=xmlParse.getNodeStringValue(node, "./title");
						String content = xmlParse.getNodeStringValue(node, "./content");
						if (emergencyAnalysisMode==0) {
							eg=new Emergency();
							eg.setDataTime(timeStr);
							//设置emergencyInfo
							eg.setEmergencyInfo(title);
							//获取province provinceCode
							for(String provinceStr:provinces){
								provinceName=provinceStr.split("##")[0];
								provinceCode=provinceStr.split("##")[1];
								if(title.indexOf(provinceName)>=0){
									eg.setProvince(provinceName);
									eg.setProvinceCode(provinceCode);
									break;
								}
							}
							//获取type typeCode
							for(String typeStr:types){
								type=typeStr.split("##")[0];
								typeCode=typeStr.split("##")[1];
								if(title.indexOf(type)>=0){
									eg.setType(type);
									eg.setTypeCode(typeCode);
									break;
								}		
							}
							//获取level levelCode
							for(String levelStr:levels){
								level=levelStr.split("##")[0];
								levelCode=levelStr.split("##")[1];
								if(title.indexOf(level)>=0){
									eg.setLevel(level);
									eg.setLevelCode(levelCode);
									break;
								}		
							}
							//设置dateTime
							list.add(eg);
						} else {
							content=content.replaceAll("<P>", "").replaceAll("</P>", "").replaceAll("\n", "")
									.replaceAll("\"", "").replaceAll("\'", "").replaceAll("</p>", "").replaceAll("<p>", "").replaceAll("&nbsp;", "");;
							content=content.replace("<br>|<BR>|<Br>|<bR>","").trim();
							String[] contents=content.split("&#13;");
							for(String str:contents){
								if(str!=null&&!"".equals(str)){
									str=Html2Text(str);	
									if(str==null||"".equals(str)){
										continue;
									}
									if(str.indexOf("测试")<0){
										eg=new Emergency();
										/*preIndex=str.indexOf("局");
										lastIndex=str.indexOf("分")+1;
										if(lastIndex==0){
											lastIndex=str.indexOf("时")+1;
										}
										timeStr=str.substring(preIndex+1, lastIndex);*/
										eg.setDataTime(timeStr);
										for(String provinceStr:provinces){
											provinceName=provinceStr.split("##")[0];
											provinceCode=provinceStr.split("##")[1];
											if(str.indexOf(provinceName)>=0){
												eg.setProvince(provinceName);
												eg.setProvinceCode(provinceCode);
												break;
											}
										}
										String t="";
										String tCode="";
										int k=1;
										if(str.contains("、")){
											String[] typeStrs=str.split("、");
											for(String s:typeStrs){
												for(String typeStr:types){
													type=typeStr.split("##")[0];
													typeCode=typeStr.split("##")[1];
													if(s.indexOf(type)>=0){
														if(k<typeStrs.length){
															t+=type+"、";	
															tCode+=typeCode+"、";
														}else{
															t+=type;
															tCode+=typeCode;
														}
														k++;
													}
												}
											}
											eg.setType(t);
											eg.setTypeCode(tCode);
										}else{
											for(String typeStr:types){
												type=typeStr.split("##")[0];
												typeCode=typeStr.split("##")[1];
												if(str.indexOf(type)>=0){
													eg.setType(type);
													eg.setTypeCode(typeCode);
													break;
												}		
											}
										}
//									}
										if(str.contains("提升") || str.contains("调整") ||str.contains("变更")){
											String[] strs=str.split("为");
											for(String levelStr:levels){
												level=levelStr.split("##")[0];
												levelCode=levelStr.split("##")[1];
												if(strs[1].indexOf(level)>=0){
													eg.setLevel(level);
													eg.setLevelCode(levelCode);
													break;
												}
											}
											eg.setEmergencyInfo(str);								
										}else{
											for(String levelStr:levels){
												level=levelStr.split("##")[0];
												levelCode=levelStr.split("##")[1];
												if(str.indexOf(level)>=0){
													eg.setLevel(level);
													eg.setLevelCode(levelCode);
													break;
												}
											}
											eg.setEmergencyInfo(str);			
										}
										if(eg.getLevel()==null||"".equals(levelCode)){
											eg.setLevel("Ⅳ级");
											eg.setLevelCode("4");
										}
										list.add(eg);	
									}
								}
							}
						}
					}
				}/*
				comparasService.updateCompas("yjProvinceRuler",provinceRuler.trim());
				comparasService.updateCompas("yjTypeRuler",typeRuler.trim());
				comparasService.updateCompas("yjLevelRuler",levelRuler.trim());*/
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 国家级
	 * @param URL
	 * @return
	 */
	public List<Emergency> getEmergencyInfo2(String URL) {
		List<Emergency> list=new ArrayList<Emergency>();
		String xml = ConPostUtils.readContentFromHttpGet(URL, new ArrayList<NameValuePair>());
		XMLParseUtil xmlParse = null;
		NodeList nodeList = null;
		Document doc = null;
		Emergency eg=null;
		String[] provinces=null;
		String[] types=null;
		String[] levels=null;
		String provinceName="";
		String provinceCode="";
		String type="";
		String typeCode="";
		String level="";
		String levelCode="";	
		String provinceRuler=(String)comparasService.getComparasByKey("yjProvinceRuler");
		String typeRuler=(String)comparasService.getComparasByKey("yjTypeRuler");
		String levelRuler=(String)comparasService.getComparasByKey("yjLevelRuler");
		Area area=null;
		int preIndex=0;
		int lastIndex=0;
		String timeStr="";
		if(provinceRuler!=null&&!"".equals(provinceRuler)){
			provinces=provinceRuler.split(",");
		}
		if(typeRuler!=null&&!"".equals(typeRuler)){
			types=typeRuler.split(",");
		}
		if(levelRuler!=null&&!"".equals(levelRuler)){
			levels=levelRuler.split(",");
		}
		try {
			xmlParse = new XMLParseUtil();
			if (null != xmlParse) {
				doc = xmlParse.parseDocument(xml);
				if(doc!=null){
					nodeList=(NodeList)xmlParse.selectNodes(doc, "//channel/item");
					for(int i=0;i<nodeList.getLength();i++){
						eg=new Emergency();
						Node node = nodeList.item(i);
						String id=xmlParse.getNodeStringValue(node, "./id");
						String title=xmlParse.getNodeStringValue(node, "./title");
						String content = xmlParse.getNodeStringValue(node, "./content");
						String affix = xmlParse.getNodeStringValue(node, "./affix");
						String name = xmlParse.getNodeStringValue(node, "./affix/@name");
						eg.setLinkUrl(affix);
						eg.setLinkName(name);
						String ncontent=content;
						content=content.replaceAll("<P>", "").replaceAll("</P>", "").replaceAll("\n", "")
								.replaceAll("\"", "").replaceAll("\'", "");
						content=Html2Text(content);
						//String[] contents=content.split("<br>|<BR>|<Br>|<bR>");
						String str=content;
						if(str.indexOf("测试")<0){
							preIndex=str.indexOf("局");
							lastIndex=str.indexOf("分")+1;
							if(lastIndex==0){
								lastIndex=str.indexOf("时")+1;
							}
							if(lastIndex==0){
								timeStr="";
							}else{
								timeStr=str.substring(preIndex+1, lastIndex);
							}
							eg.setDataTime(timeStr);
							for(String provinceStr:provinces){
								provinceName=provinceStr.split("##")[0];
								provinceCode=provinceStr.split("##")[1];
								if(str.indexOf(provinceName)>=0){
									eg.setProvince(provinceName);
									eg.setProvinceCode(provinceCode);
									break;
								}
							}
							String t="";
							String tCode="";
							int k=1;
							if(str.contains("、")){
								String[] typeStrs=str.split("、");
								for(String s:typeStrs){
									for(String typeStr:types){
										type=typeStr.split("##")[0];
										typeCode=typeStr.split("##")[1];
										if(s.indexOf(type)>=0){
											if(k<typeStrs.length){
												t+=type+"、";	
												tCode+=typeCode+"、";
											}else{
												t+=type;
												tCode+=typeCode;
											}
											k++;
										}
									}
								}
								eg.setType(t);
								eg.setTypeCode(tCode);
							}else{
								for(String typeStr:types){
									type=typeStr.split("##")[0];
									typeCode=typeStr.split("##")[1];
									if(str.indexOf(type)>=0){
										eg.setType(type);
										eg.setTypeCode(typeCode);
										break;
									}		
								}
							}
//							}
							if(str.contains("提升")|| str.contains("调整") ||str.contains("变更")){
								String[] strs=str.split("为");
								for(String levelStr:levels){
									level=levelStr.split("##")[0];
									levelCode=levelStr.split("##")[1];
									if(strs[1].indexOf(level)>=0){
										eg.setLevel(level);
										eg.setLevelCode(levelCode);
										break;
									}
								}
								eg.setEmergencyInfo(str);								
							}else{
								for(String levelStr:levels){
									level=levelStr.split("##")[0];
									levelCode=levelStr.split("##")[1];
									if(str.indexOf(level)>=0){
										eg.setLevel(level);
										eg.setLevelCode(levelCode);
										break;
									}
								}
								eg.setEmergencyInfo(str);			
							}
							eg.setNemergencyInfo(ncontent);
							list.add(eg);								
						}
					}
				}/*
				comparasService.updateCompas("yjProvinceRuler",provinceRuler.trim());
				comparasService.updateCompas("yjTypeRuler",typeRuler.trim());
				comparasService.updateCompas("yjLevelRuler",levelRuler.trim());*/
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
