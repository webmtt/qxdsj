/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interf.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import com.thinkgem.jeesite.modules.interf.service.SysInterfaceService;
import com.thinkgem.jeesite.modules.sys.service.UserInterService;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 树结构生成Controller
 * @author zhaoxiaojun
 * @version 2019-12-05
 */
@Controller
@RequestMapping(value = "/interf/sysInterface")
public class SysInterfaceController extends BaseController {

	@Autowired
	private SysInterfaceService sysInterfaceService;

	@Autowired
	private UserInterService userInterService;
	
	@ModelAttribute
	public SysInterface get(@RequestParam(required=false) String id) {
		SysInterface entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysInterfaceService.get(id);
		}
		if (entity == null){
			entity = new SysInterface();
		}
		return entity;
	}

	@RequestMapping(value = {"/list", ""})
	public String list(SysInterface sysInterface, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SysInterface> list = sysInterfaceService.findList(sysInterface);
		model.addAttribute("list", list);
		return "modules/interf/sysInterfaceList";
	}

	@RequestMapping(value = "/form")
	public String form(SysInterface sysInterface, Model model) {
		if (sysInterface.getParent()!=null && StringUtils.isNotBlank(sysInterface.getParent().getId())){
			sysInterface.setParent(sysInterfaceService.get(sysInterface.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(sysInterface.getId())){
				SysInterface sysInterfaceChild = new SysInterface();
				sysInterfaceChild.setParent(new SysInterface(sysInterface.getParent().getId()));
				List<SysInterface> list = sysInterfaceService.findList(sysInterface);
				if (list.size() > 0){
					sysInterface.setSort(list.get(list.size()-1).getSort());
					if (sysInterface.getSort() != null){
						sysInterface.setSort(sysInterface.getSort() + 30);
					}
				}
			}
		}
		model.addAttribute("sysInterface", sysInterface);
		return "modules/interf/sysInterfaceForm";
	}

	@RequestMapping(value = "/save")
	public String save(SysInterface sysInterface, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysInterface)){
			return form(sysInterface, model);
		}
		/*List<SysInterface> list = sysInterfaceService.findAllList();
		int index = 0;
		for (SysInterface sys:list) {
			if(sys.getParentIds().equals(sysInterface.getParentIds())&&sys.getName().equals(sysInterface.getName())) {
				addMessage(redirectAttributes, "重复数据");
			}else{
				index+=1;
			}
		}
		if (index==list.size()){

		}*/
		/*for (SysInterface sys:list) {
			if(userInterService.findById(sys.getId())==false) {
				userInterService.saveUserinter(sys.getId());
			}
		}*/
		sysInterfaceService.save(sysInterface);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+ "/interf/sysInterface/?repage";
	}

	@RequestMapping(value = "/delete")
	public String delete(SysInterface sysInterface, RedirectAttributes redirectAttributes) {
		sysInterfaceService.deleteById(sysInterface.getId());
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+ "/interf/sysInterface/?repage";
	}

	@ResponseBody
	@RequestMapping(value = "/treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysInterface> list = sysInterfaceService.findList(new SysInterface());
		for (int i=0; i<list.size(); i++){
			SysInterface e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	@ResponseBody
	@RequestMapping(value = "/import")
	public JSONObject upload(MultipartFile file, RedirectAttributes redirectAttributes){
		//读取IO流文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		try {
			input=file.getInputStream();
			wb=new XSSFWorkbook(input);

			//读取页
			for(int sheetNum=0;sheetNum<wb.getNumberOfSheets();sheetNum++){
				XSSFSheet xssfSheet = wb.getSheetAt(sheetNum);
				if(xssfSheet==null){
					continue;
				}
				//读取行
				DecimalFormat df = new DecimalFormat("0");
				for(int rowNum=1;rowNum<xssfSheet.getLastRowNum()+1;rowNum++){
					SysInterface sysInterface = new SysInterface();
					XSSFRow row= xssfSheet.getRow(rowNum);
					if(row!=null){
						sysInterface.setId(df.format(row.getCell(0).getNumericCellValue()));
						sysInterface.setName(row.getCell(1).toString());
						sysInterface.setParent(sysInterfaceService.get(df.format(row.getCell(2).getNumericCellValue())));
						sysInterface.setDataEncoding(row.getCell(3).toString());
						sysInterface.setDataType(row.getCell(4).toString());
						sysInterface.setInerfaceId(row.getCell(5).toString());
						sysInterface.setParameterId(row.getCell(6).toString());
						sysInterface.setParameterType(row.getCell(7).toString());
						sysInterface.setSort(Integer.parseInt(df.format(row.getCell(8).getNumericCellValue())));
						sysInterface.setCreateDate(new Date());
						sysInterfaceService.insertInter(sysInterface);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		addMessage(redirectAttributes, "导入成功");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","ok");
		jsonObject.put("code",0);
		return jsonObject;
	}

	@RequestMapping(value = "/download")
	public void download(HttpServletRequest request,HttpServletResponse response,@RequestParam("fileName") String fileName) throws IOException{
		String realPath = request.getSession().getServletContext().getRealPath("WEB-INF/download/")+"\\"+fileName;
		File file = new File(realPath);
		if(!file.exists()){
			response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
			response.getWriter().print("<html><body><script type='text/javascript'>alert('您要下载的资源已被删除！');</script></body></html>");
			response.getWriter().close();
			System.out.println("您要下载的资源已被删除！！");
			return;
		}
		fileName = URLEncoder.encode(fileName,"UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		// 读取要下载的文件，保存到文件输入流
		FileInputStream in = new FileInputStream(realPath);
		// 创建输出流
		OutputStream out = response.getOutputStream();
		// 创建缓冲区
		byte buffer[] = new byte[1024];
		int len = 0;
		//循环将输入流中的内容读取到缓冲区当中
		while((len = in.read(buffer)) > 0){
			out.write(buffer, 0, len);
		}
		//关闭文件输入流
		in.close();
		// 关闭输出流
		out.close();
	}

}