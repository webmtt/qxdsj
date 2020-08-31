/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.stream.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.arithmetic.entity.SupArithmeticsPackage;
import com.thinkgem.jeesite.mybatis.modules.arithmetic.service.SupArithmeticsPackageService;
import com.thinkgem.jeesite.mybatis.modules.arithmetic.util.HttpClientUtil;
import com.thinkgem.jeesite.mybatis.modules.arithmetic.util.ReFleixUtil;
import com.thinkgem.jeesite.mybatis.modules.stream.entity.SupArithmeticsStream;
import com.thinkgem.jeesite.mybatis.modules.stream.service.SupArithmeticsStreamService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 算法池基本信息Controller
 * @author yck
 * @version 2019-12-03
 */
@Controller
@RequestMapping(value = "/stream/supArithmeticsStream")
public class SupArithmeticsStreamController extends BaseController {

	@Autowired
	private SupArithmeticsStreamService supArithmeticsStreamService;
	@Autowired
	private SupArithmeticsPackageService supArithmeticsPackageService;

	@ModelAttribute
	public SupArithmeticsStream get(@RequestParam(required=false) String id) {
		SupArithmeticsStream entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supArithmeticsStreamService.get(id);
		}
		if (entity == null){
			entity = new SupArithmeticsStream();
		}
		return entity;
	}

    /**
     * 算法池查看
     * @param supArithmeticsStream
     * @param request
     * @param response
     * @param model
     * @return
     */
//	@RequiresPermissions("stream:supArithmeticsStream:view")
	@RequestMapping(value = {"list", ""})
	public String list(SupArithmeticsStream supArithmeticsStream, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupArithmeticsStream> page = supArithmeticsStreamService.findPage(new Page<SupArithmeticsStream>(request, response), supArithmeticsStream);
		for(int ii = 0; ii < page.getList().size(); ii++){
			String sapIds = page.getList().get(ii).getSapId();
			String sapName = "";
			if(sapIds.contains(",")) {
				String[] sapId = sapIds.split(",");
				for(int i = 0; i < sapId.length; i++){
					sapName += supArithmeticsPackageService.get(sapId[i]).getAriName()+",";
				}
				sapName = sapName.substring(0,sapName.length()-1);
			}else {
				sapName += supArithmeticsPackageService.get(sapIds).getAriName();
			}
			page.getList().get(ii).setSapId(sapName);
		}
		model.addAttribute("page", page);
		return "modules/stream/supArithmeticsStreamList";
	}

    /**
     * 跳转算法池修改页面
     * @param supArithmeticsStream
     * @param model
     * @return
     */
//	@RequiresPermissions("stream:supArithmeticsStream:view")
	@RequestMapping(value = "form")
	public String form(SupArithmeticsStream supArithmeticsStream, Model model, HttpServletRequest request, HttpServletResponse response, SupArithmeticsPackage supArithmeticsPackage) {
		Page<SupArithmeticsPackage> page =
				supArithmeticsPackageService.findPages(
						new Page<SupArithmeticsPackage>(request, response), supArithmeticsPackage);
		model.addAttribute("page", page);
		String sapIds = supArithmeticsStream.getSapId();
        String sapName = "";
        if(sapIds != null && sapIds !=""){
            if(sapIds.contains(",")) {
                String[] sapId = sapIds.split(",");
                for(int i = 0; i < sapId.length; i++){
                    sapName += supArithmeticsPackageService.get(sapId[i]).getAriName()+",";
                }
                sapName = sapName.substring(0,sapName.length()-1);
            }else {
                sapName += supArithmeticsPackageService.get(sapIds).getAriName();
            }
        }
        supArithmeticsStream.setSapId(sapName);
		model.addAttribute("supArithmeticsStream", supArithmeticsStream);
		return "modules/stream/supArithmeticsStreamForm";
	}

    /**
     * 跳转测试页面
     * @param supArithmeticsStream
     * @param model
     * @return
     */
//	@RequiresPermissions("stream:supArithmeticsStream:view")
	@RequestMapping(value = "preTest")
	public String preTest(SupArithmeticsStream supArithmeticsStream, Model model) {
		model.addAttribute("supArithmeticsStream", supArithmeticsStream);
		return "modules/stream/supArithmeticsStreamPreTest";
	}

	/**
	 * 跳转数据源测试页面
	 * @param supArithmeticsStream
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("stream:supArithmeticsStream:view")
	@RequestMapping(value = "preTests")
	public String preTests(SupArithmeticsStream supArithmeticsStream, Model model) {
		model.addAttribute("supArithmeticsStream", supArithmeticsStream);
		return "modules/stream/supArithmeticsStreamPreTests";
	}
    /**
     * 保存算法池
     * @param supArithmeticsStream
     * @param model
     * @param redirectAttributes
     * @return
     */
//	@RequiresPermissions("stream:supArithmeticsStream:edit")
	@RequestMapping(value = "save")
	public String save(SupArithmeticsStream supArithmeticsStream, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, supArithmeticsStream)){
//			return form(supArithmeticsStream, model);
//		}
		try{
			String sapNames = supArithmeticsStream.getSapId();
			String sapId = "";
			if(sapNames.contains(",")){
				String[] sapIds = sapNames.split(",");
				for(int i = 0; i < sapIds.length; i++){
					SupArithmeticsPackage supArithmeticsPackage = new SupArithmeticsPackage();
					supArithmeticsPackage.setAriName(sapIds[i]);
					sapId += supArithmeticsPackageService.findListByName(supArithmeticsPackage).getId()+",";
				}
			}else {
				SupArithmeticsPackage supArithmeticsPackage = new SupArithmeticsPackage();
				supArithmeticsPackage.setAriName(sapNames);
				sapId = supArithmeticsPackageService.findListByName(supArithmeticsPackage).getId()+",";
			}
			supArithmeticsStream.setSapId(sapId.substring(0,sapId.length()-1));
			supArithmeticsStream.setStatus("0");
			supArithmeticsStreamService.save(supArithmeticsStream);
			addMessage(redirectAttributes, "保存算法池成功");
			return "redirect:" + "/stream/supArithmeticsStream/?repage";
		}catch (Exception e){
			addMessage(redirectAttributes, "保存算法池失败，部分算法未发布！");
			return "redirect:" + "/stream/supArithmeticsStream/?repage";
		}
	}

//    /**
//     * 算法池测试
//     * @param supArithmeticsStream
//     * @param model
//     * @param redirectAttributes
//     * @return
//     */
//	@RequiresPermissions("stream:supArithmeticsStream:edit")
//	@RequestMapping(value = "test")
//	public String preTestUtil(SupArithmeticsStream supArithmeticsStream, Model model, RedirectAttributes redirectAttributes) {
//        String sapIds = supArithmeticsStreamService.get(supArithmeticsStream.getId()).getSapId();
//	    String sequences = supArithmeticsStreamService.get(supArithmeticsStream.getId()).getSequence();
//		Object result = null;
//		if(supArithmeticsStream.getParams().contains("，")){
//			addMessage(redirectAttributes, "请输入英文','!");
//		}else{
//			if(sapIds.contains(",")){
//			    String sapIdss = "";
//                Map<String, String> map = new TreeMap<String, String>();
//                for(int i = 0; i < sapIds.split(",").length; i++){
//                    map.put(sequences.split(",")[i],sapIds.split(",")[i]);
//                }
//                //这里将map.entrySet()转换成list
//                List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
//                //然后通过比较器来实现排序
//                Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
//                    //升序排序
//                    @Override
//                    public int compare(Map.Entry<String, String> o1,
//                                       Map.Entry<String, String> o2) {
//                        return o1.getKey().compareTo(o2.getKey());
//                    }
//                });
//                for(Map.Entry<String,String> mapping:list){
//                    sapIdss+=mapping.getValue()+",";
//                }
//				String[] sapId = sapIdss.substring(0,sapIdss.length()-1).split(",");
//                String[] param = supArithmeticsStream.getParams().split(",");
//				for(int i = 0; i < sapId.length; i++){
//					SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapId[i]);
//					String AriPackageName = supArithmeticsPackage.getAriPackageName();
//					String ClassUrl = supArithmeticsPackage.getClassUrl();
//					String AriMethod = supArithmeticsPackage.getAriMethod();
//					String Params = param[i] + ","+ param[i+1];
//					result = ReFleixUtil.functionHandle(AriPackageName,ClassUrl,AriMethod,Params);
//                    param[i+1] = result.toString();
//					//supArithmeticsStream.setParams(result.toString());
//				}
//
//			}else {
//				SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapIds);
//				String AriPackageName = supArithmeticsPackage.getAriPackageName();
//				String ClassUrl = supArithmeticsPackage.getClassUrl();
//				String AriMethod = supArithmeticsPackage.getAriMethod();
//				String Params = supArithmeticsStream.getParams();
//				result = ReFleixUtil.functionHandle(AriPackageName,ClassUrl,AriMethod,Params);
//			}
//			Object values = null;
//			if (result instanceof Integer) {
//				values = Integer.parseInt(supArithmeticsStream.getPridictValue());
//			} else if (result instanceof String) {
//				values = supArithmeticsStream.getPridictValue();
//			} else if (result instanceof Double) {
//				values = Double.parseDouble(supArithmeticsStream.getPridictValue());
//			} else if (result instanceof Float) {
//				values = Float.parseFloat(supArithmeticsStream.getPridictValue());
//			} else if (result instanceof Long) {
//				values = Long.parseLong(supArithmeticsStream.getPridictValue());
//			} else if (result instanceof Boolean) {
//				values = Boolean.valueOf(supArithmeticsStream.getPridictValue());
//			} else if (result instanceof Date) {
//				values = Date.parse(supArithmeticsStream.getPridictValue());
//			}
//			if (values.equals(result)) {
//				addMessage(redirectAttributes, "算法池测试成功");
//				// 修改算法池状态
//				if(supArithmeticsStream.getStatus().equals("0")){
//					supArithmeticsStream.setStatus("1");
//				}
//				supArithmeticsStreamService.updateAriState(supArithmeticsStream);
//
//			} else {
//				addMessage(redirectAttributes, "算法池测试失败");
//			}
//		}
//		return "redirect:" + "/stream/supArithmeticsStream/?repage";
//	}

	/**
	 * 修改算法池状态
	 * @param supArithmeticsStream
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "testss")
	public String preTestUtilss(SupArithmeticsStream supArithmeticsStream, Model model, RedirectAttributes redirectAttributes) {
		addMessage(redirectAttributes, "算法池测试成功");
		// 修改算法池状态
		if(supArithmeticsStream.getStatus().equals("0")){
			supArithmeticsStream.setStatus("1");
		}
		supArithmeticsStreamService.updateAriState(supArithmeticsStream);
		return "redirect:" + "/stream/supArithmeticsStream/?repage";
	}
	/**
	 * 数据源算法池测试
	 * @param
	 * @param
	 * @param
	 * @return
	 */
//	@RequiresPermissions("stream:supArithmeticsStream:edit")
	@ResponseBody
	@RequestMapping(value = "tests")
	public JSONObject preTestUtils(String url, String id,String purposeValue) {
		url = url.replace("amp;","");
		String sapIds = supArithmeticsStreamService.get(id).getSapId();
		String sequences = supArithmeticsStreamService.get(id).getSequence();
		HashMap<String, Object> params = new HashMap<String, Object>();
		String result = null;
		try{
			result = HttpClientUtil.post(url, params);
			if(sapIds.contains(",")){
				String sapIdss = "";
				Map<String, String> map = new TreeMap<String, String>();
				for(int i = 0; i < sapIds.split(",").length; i++){
					map.put(sequences.split(",")[i],sapIds.split(",")[i]);
				}
				//这里将map.entrySet()转换成list
				List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
				//然后通过比较器来实现排序
				Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
					//升序排序
					@Override
					public int compare(Map.Entry<String, String> o1,
									   Map.Entry<String, String> o2) {
						return o1.getKey().compareTo(o2.getKey());
					}
				});
				for(Map.Entry<String,String> mapping:list){
					sapIdss+=mapping.getValue()+",";
				}
				String[] sapId = sapIdss.substring(0,sapIdss.length()-1).split(",");
				for(int i = 0; i < sapId.length; i++){
					SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapId[i]);
					String AriPackageName = supArithmeticsPackage.getAriPackageName();
					String ClassUrl = supArithmeticsPackage.getClassUrl();
					String AriMethod = supArithmeticsPackage.getAriMethod();
					result = ReFleixUtil.functionHandles(AriPackageName,ClassUrl,AriMethod,result,purposeValue).toString();
				}

			}else {
				SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapIds);
				String AriPackageName = supArithmeticsPackage.getAriPackageName();
				String ClassUrl = supArithmeticsPackage.getClassUrl();
				String AriMethod = supArithmeticsPackage.getAriMethod();
				result = ReFleixUtil.functionHandles(AriPackageName,ClassUrl,AriMethod,result,purposeValue).toString();
			}
		}catch (Exception e){
			result =   "{\"code\":200,\"data\":[{\"Message\":'API接口错误',\"data\":''}],\"message\":\"查询数据成功\"}";
		}
		return JSONObject.fromObject(result);
	}
    /**
     * 发布算法池
     * @param supArithmeticsStream
     * @param redirectAttributes
     * @return
     */
//    @RequiresPermissions("stream:supArithmeticsStream:edit")
    @RequestMapping(value = "release")
    public String release(SupArithmeticsStream supArithmeticsStream, Model model, RedirectAttributes redirectAttributes) {
        String status = supArithmeticsStream.getStatus();
        if("0".equals(status)){
            addMessage(redirectAttributes, "请先进行算法池测试!");
        }else if("1".equals(status)){
            addMessage(redirectAttributes, "算法池发布成功");
            // 修改算法包状态
            supArithmeticsStream.setStatus("2");
            supArithmeticsStreamService.updateAriState(supArithmeticsStream);
        }else if("2".equals(status)){
            addMessage(redirectAttributes, "算法池已发布,请勿多次发布!");
        }
        return "redirect:" + "/stream/supArithmeticsStream/?repage";
    }
    /**
     * 删除算法池
     * @param supArithmeticsStream
     * @param redirectAttributes
     * @return
     */
//	@RequiresPermissions("stream:supArithmeticsStream:edit")
	@RequestMapping(value = "delete")
	public String delete(SupArithmeticsStream supArithmeticsStream, RedirectAttributes redirectAttributes) {
		supArithmeticsStreamService.delete(supArithmeticsStream);
		addMessage(redirectAttributes, "删除算法池成功");
		return "redirect:" + "/stream/supArithmeticsStream/?repage";
	}

    /**
     * 公用算法池处理数据接口
     * @param object
     * @param streamId
     * @return
     */
//	@RequestMapping(value = "common",method=RequestMethod.GET)
//	@ResponseBody
//	public Object common(Object object,String streamId) {
//	    SupArithmeticsStream supArithmeticsStream = supArithmeticsStreamService.get(streamId);
//	    String sapIds = supArithmeticsStream.getSapId();
//		String sequences = supArithmeticsStream.getSequence();
//        Object result = null;
//        try{
//			if("2".equals(supArithmeticsStream.getStatus())){
//				if(sapIds.contains(",")){
//					String sapIdss = "";
//					Map<String, String> map = new TreeMap<String, String>();
//					for(int i = 0; i < sapIds.split(",").length; i++){
//						map.put(sequences.split(",")[i],sapIds.split(",")[i]);
//					}
//					//这里将map.entrySet()转换成list
//					List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
//					//然后通过比较器来实现排序
//					Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
//						//升序排序
//						@Override
//						public int compare(Map.Entry<String, String> o1,
//										   Map.Entry<String, String> o2) {
//							return o1.getKey().compareTo(o2.getKey());
//						}
//					});
//					for(Map.Entry<String,String> mapping:list){
//						sapIdss+=mapping.getValue()+",";
//					}
//					String[] sapId = sapIdss.substring(0,sapIdss.length()-1).split(",");
//					String[] param =object.toString().split(",");
//					for(int i = 0; i < sapId.length; i++){
//						SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapId[i]);
//						String AriPackageName = supArithmeticsPackage.getAriPackageName();
//						String ClassUrl = supArithmeticsPackage.getClassUrl();
//						String AriMethod = supArithmeticsPackage.getAriMethod();
//						String Params = param[i] + ","+ param[i+1];
//						result = ReFleixUtil.functionHandle(AriPackageName,ClassUrl,AriMethod,Params);
//						param[i+1] = result.toString();
//					}
//
//				}else {
//					SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapIds);
//					String AriPackageName = supArithmeticsPackage.getAriPackageName();
//					String ClassUrl = supArithmeticsPackage.getClassUrl();
//					String AriMethod = supArithmeticsPackage.getAriMethod();
//					result = ReFleixUtil.functionHandle(AriPackageName,ClassUrl,AriMethod,object.toString());
//				}
//			}else {
//				result = "算法池未发布，不能进行数据处理!";
//			}
//		}catch (Exception e){
//        	return e.getMessage();
//		}
//		return result;
//	}
	/**
	 * 公用数据源算法池处理数据接口
	 * @param
	 * @param streamId
	 * @return
	 */
	@RequestMapping(value = "common",method=RequestMethod.GET)
	@ResponseBody
	public Object common(String object,String streamId,String meter) {
		SupArithmeticsStream supArithmeticsStream = supArithmeticsStreamService.get(streamId);
		String sapIds = supArithmeticsStream.getSapId();
		String sequences = supArithmeticsStream.getSequence();
		String purpose = supArithmeticsStream.getPurpose();
		purpose = purpose.replace("amp;","");
		Object result = null;
		if(object.isEmpty()){
			object = purpose;
		}else {
//				object = purpose + "&" + object;
			object = repaceDate(purpose, object);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		try{
			object = object.replace("amp;","");
			result = HttpClientUtil.post(object, params);
			if("2".equals(supArithmeticsStream.getStatus())){
				if(sapIds.contains(",")){
					String sapIdss = "";
					Map<String, String> map = new TreeMap<String, String>();
					for(int i = 0; i < sapIds.split(",").length; i++){
						map.put(sequences.split(",")[i],sapIds.split(",")[i]);
					}
					//这里将map.entrySet()转换成list
					List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
					//然后通过比较器来实现排序
					Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
						//升序排序
						@Override
						public int compare(Map.Entry<String, String> o1,
										   Map.Entry<String, String> o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});
					for(Map.Entry<String,String> mapping:list){
						sapIdss+=mapping.getValue()+",";
					}
					String[] sapId = sapIdss.substring(0,sapIdss.length()-1).split(",");
					for(int i = 0; i < sapId.length; i++){
						SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapId[i]);
						String AriPackageName = supArithmeticsPackage.getAriPackageName();
						String ClassUrl = supArithmeticsPackage.getClassUrl();
						String AriMethod = supArithmeticsPackage.getAriMethod();
						result = ReFleixUtil.functionHandles(AriPackageName,ClassUrl,AriMethod,result.toString(),meter).toString();
					}

				}else {
					SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(sapIds);
					String AriPackageName = supArithmeticsPackage.getAriPackageName();
					String ClassUrl = supArithmeticsPackage.getClassUrl();
					String AriMethod = supArithmeticsPackage.getAriMethod();
					result = ReFleixUtil.functionHandles(AriPackageName,ClassUrl,AriMethod,result.toString(),meter).toString();
				}
			}else {
				result = "{\"code\":400,\"DS\":[{\"Message\":'算法池未发布，不能进行数据处理',\"data\":''}],\"message\":\"查询数据成功\"}";
			}
		}catch (Exception e){
			result = "{\"code\":400,\"DS\":[{\"Message\":'API接口错误',\"data\":''}],\"message\":\"查询数据成功\"}";
		}

		return result;
	}
    @ResponseBody
    @RequestMapping(value = "findObjectByAirName", method=RequestMethod.POST)
    public SupArithmeticsStream findObjectByAirName(@RequestParam(required=false) String streamName) {
        return supArithmeticsStreamService.findObjectByAirName(streamName);
    }

	public static String repaceDate(String url, String object) {
		String parameters = url.substring(url.indexOf("?") + 1,url.length());
		String[] paramter = parameters.split("&");
		String[] objs = object.split("&");
		String pms = "";
		String pamm = "";
		for(int i = 0; i < objs.length; i++){
			String nm = objs[i].substring(0,objs[i].indexOf("="));
			for(int j = 0; j < paramter.length; j++){
				if(nm.equals(paramter[j].substring(0,paramter[j].indexOf("=")))){
					paramter[i] = objs[j];
					objs[j] = "0";
					continue;
				}
			}
		}
		for (String pams: objs) {
			if(!pams.equals("0")){
				pamm = pams + "&";
			}
		}
		pms = pamm;
		for (String pam: paramter) {
			pms = pms + pam + "&";
		}
		return url.substring(0,url.indexOf("?") - 1) + "?" + pms.substring(0,pms.length() - 1);
	}

}