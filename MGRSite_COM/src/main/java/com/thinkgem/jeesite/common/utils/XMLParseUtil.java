package com.thinkgem.jeesite.common.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;

public class XMLParseUtil {

	private DocumentBuilder builder;

	private XPath xpath;

	/**
	 * 默认构造函数
	 * 
	 * @throws ParserConfigurationException
	 *             创建XML解析器出错！
	 */
	public XMLParseUtil() throws ParserConfigurationException {
		DocumentBuilderFactory domfactory = DocumentBuilderFactory
				.newInstance();
		builder = domfactory.newDocumentBuilder();

		XPathFactory xpfactory = XPathFactory.newInstance();
		xpath = xpfactory.newXPath();
	}

	
	/**
	 * 根据XML文档
	 * 
	 * <pre>
	 * 可能产生而没有显式抛出的异常：
	 * 1) IllegalArgumentException ：传入参数错误！如对象不能为空！
	 * 2) SAXParseException ： 文件格式错误！无法解析！
	 * </pre>
	 * 
	 * @param is
	 *            输入流
	 * @return Document对象
	 * @throws IOException
	 *             IO异常
	 * @throws SAXException
	 *             SAX异常
	 * @see [类、类#方法、类#成员]
	 */
	public Document parseDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringReader sr = new StringReader(xml);
		InputSource is = new InputSource(sr);
		Document doc = builder.parse(is);
		return doc;
	}

	/**
	 * 通过xpath取得节点列表
	 * 
	 * @param node
	 *            节点
	 * @param expression
	 *            XPath表达式
	 * @return NodeList
	 * @throws XPathExpressionException
	 *             XPath表达式异常
	 * @see [类、类#方法、类#成员]
	 */
	public NodeList selectNodes(Node node, String expression)
			throws XPathExpressionException {
		// XPath对象编译XPath表达式
		XPathExpression xpexpreesion = this.xpath.compile(expression);
		Object object = xpexpreesion.evaluate(node, XPathConstants.NODESET);
		return (NodeList) object;
	}

	/**
	 * 通过xpath取得单个节点
	 * 
	 * @param node
	 *            节点
	 * @param expression
	 *            XPath表达式
	 * @return Node
	 * @throws XPathExpressionException
	 *             XPath表达式异常
	 * @see [类、类#方法、类#成员]
	 */
	public Node selectSingleNode(Node node, String expression)
			throws XPathExpressionException {
		XPathExpression xpexpreesion = this.xpath.compile(expression);
		Object object = xpexpreesion.evaluate(node, XPathConstants.NODE);
		return (Node) object;
	}

	/**
	 * 根据xpath取得节点的文本值
	 * 
	 * @param node
	 *            节点
	 * @param expression
	 *            XPath表达式
	 * @return String
	 * @throws XPathExpressionException
	 *             XPath表达式异常
	 * @see [类、类#方法、类#成员]
	 */
	public String getNodeStringValue(Node node, String expression)
			throws XPathExpressionException {
		XPathExpression xpexpreesion = this.xpath.compile(expression);
		Object object = xpexpreesion.evaluate(node, XPathConstants.STRING);
		return (String) object;
	}
}
