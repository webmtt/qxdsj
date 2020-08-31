/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.ServletContext;

/**
 * FreeMarkers工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
public class FreeMarkers {

	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Configuration buildConfiguration(String directory) throws IOException {
		Configuration cfg = new Configuration();
		Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(path.getFile());
		return cfg;
	}

	public static void createHtml(String realPath,String targetFileName,Map<String, Object> map) throws Exception{
		System.getProperty("user.dir");
		//创建fm的配置
		Configuration config = new Configuration();
		System.out.println();
		//指定默认编码格式
		config.setDefaultEncoding("UTF-8");

		//设置模版文件的路径
		config.setDirectoryForTemplateLoading(new File(realPath+"\\WEB-INF\\tlds"));

		Template template = config.getTemplate("intercalate.ftl");
		//从参数文件中获取指定输出路径 ,路径示例：E:\冬奥项目\QXDSJ\code\MGRSite_COM
		String path = realPath+"\\visua";
		//定义输出流，注意必须指定编码
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path+"/"+targetFileName)),"UTF-8"));
		//生成模版
		template.process(map, writer);

	}
	
}
