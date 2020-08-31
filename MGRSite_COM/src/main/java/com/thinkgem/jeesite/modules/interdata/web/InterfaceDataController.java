/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interdata.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.modules.interdata.entity.InterDeploy;
import com.thinkgem.jeesite.modules.interdata.entity.InterfaceDefine;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.interdata.entity.InterfaceData;
import com.thinkgem.jeesite.modules.interdata.service.InterfaceDataService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.spec.OAEPParameterSpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * API接口展示Controller
 * @author zhaoxiaojun
 * @version 2019-12-06
 */
@Controller
@RequestMapping(value = "/interdata/interfaceData")
public class InterfaceDataController extends BaseController {

	@Autowired
	private InterfaceDataService interfaceDataService;

	@RequestMapping(value = {"/interfa"})
	public String getAllInter(Model model){
		List<InterfaceData>  interList = interfaceDataService.getAllInter();
		if (interList.size() != 0){
			model.addAttribute("interList", interList);
		}
		return "modules/interdata/interfaceDataList";
	}

    @RequestMapping(value = {"/interfam"})
    public String getInter(String id,Model model){
        model.addAttribute("id", id);
        return "modules/sys/dataLicense";
    }

    @RequestMapping(value = {"/interfams"})
    public void interFams(HttpServletResponse response){
        List<InterfaceData>  interList = interfaceDataService.getAllInter();
        if (interList.size() != 0){
            renderText(JsonMapper.toJsonString(interList), response);
        }
    }

    @RequestMapping(value = {"/interface"})
    public void getInterface(String id, HttpServletResponse response){
        List<InterfaceDefine>  interfaceList = interfaceDataService.getInterface(id);
        if (interfaceList.size() != 0){
            renderText(JsonMapper.toJsonString(interfaceList), response);
        }
    }

    @RequestMapping(value = {"/projectcdinter"})
    public void projectcdinter(String id, HttpServletResponse response){
	    /*String[] ids = id.split(",");*/
        List<Map<String, Object>>  interList = interfaceDataService.projectcdinter(id);
        for (int i = 0; i < interList.size(); i++) {
            String name = interfaceDataService.projectcdinterName(interList.get(i).get("CUSTOM_API_ID").toString());
            interList.get(i).put("name",name);
        }
        /*ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int j = 0; j <ids.length ; j++) {
            List<Map<String, Object>>  interList = interfaceDataService.projectcdinter(ids[j]);
            for (int i = 0; i < interList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                String name = interfaceDataService.projectcdinterName(interList.get(i).get("CUSTOM_API_ID").toString());
                map.put("name",name);
                map.put("CUSTOM_API_ID",interList.get(i).get("CUSTOM_API_ID").toString());
                map.put("DATA_CODE",interList.get(i).get("DATA_CODE").toString());
                list.add(map);
            }
        }*/
        if (interList.size() != 0){
            renderText(JsonMapper.toJsonString(interList), response);
        }
    }


    @RequestMapping(value = {"/interElement"})
    public void interElement(String id, HttpServletResponse response){
        String[] ids = id.split(",");
        ArrayList<Map<String, Object>> lists = new ArrayList<>();
        for (int j = 0; j <ids.length ; j++) {
            List<Map<String, Object>> interList = interfaceDataService.interElement(ids[j]);
            String name = interfaceDataService.projectcdinterName(ids[j]);
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < interList.size(); i++) {
                Map<String, Object> elemap = interfaceDataService.interElementName(interList.get(i).get("PARAM_ID").toString());
                list.add(elemap);
            }
            map.put("inter",ids[j]+"("+name+")");
            map.put("interList",list);
            lists.add(map);
        }
        if (lists.size() != 0){
            renderText(JsonMapper.toJsonString(lists), response);
        }
    }

    /**
     * 获取资料下的所有特定要素
     * @param id
     * @param response
     */
    @RequestMapping(value = {"/getInterElement"})
    public void getInterElement(String id, HttpServletResponse response){
        String[] inters = null;
        if(id!=""&&id!=null){
            inters = id.split(",");
        }
        List<Map<String, Object>> emList = interfaceDataService.getInterElement(inters);
        ArrayList<Map<String, Object>> allList = new ArrayList<>();
        for (Map<String, Object> map:emList) {
            if("soilDepths".equals(map.get("PARAM_ID"))||"adminCodes".equals(map.get("PARAM_ID"))||"netCodes".equals(map.get("PARAM_ID"))
            ||"maxLat".equals(map.get("PARAM_ID"))||"minLat".equals(map.get("PARAM_ID"))||"maxLon".equals(map.get("PARAM_ID"))
            ||"minLon".equals(map.get("PARAM_ID"))||"maxStaId".equals(map.get("PARAM_ID"))||"minStaId".equals(map.get("PARAM_ID"))
            ||"penRangeOfYear".equals(map.get("PARAM_ID"))||"monRangeOfYear".equals(map.get("PARAM_ID"))
            ||"dayRangeOfYear".equals(map.get("PARAM_ID"))||"tensOfYear".equals(map.get("PARAM_ID"))||"pensOfYear".equals(map.get("PARAM_ID"))
            ||"monsOfYear".equals(map.get("PARAM_ID"))||"daysOfYear".equals(map.get("PARAM_ID"))||"day".equals(map.get("PARAM_ID"))
            ||"month".equals(map.get("PARAM_ID"))||"maxYear".equals(map.get("PARAM_ID"))||"minYear".equals(map.get("PARAM_ID"))
            ||"maxMD".equals(map.get("PARAM_ID"))||"minMD".equals(map.get("PARAM_ID"))||"tenRangeOfYear".equals(map.get("PARAM_ID"))
            ||"timeRange".equals(map.get("PARAM_ID"))||"time".equals(map.get("PARAM_ID"))||"fcstLevel".equals(map.get("PARAM_ID"))
            ||"fcstEle".equals(map.get("PARAM_ID"))||"elements".equals(map.get("PARAM_ID"))||"dataCode".equals(map.get("PARAM_ID"))
                    ||"times".equals(map.get("PARAM_ID"))||"staIds".equals(map.get("PARAM_ID"))){
                allList.add(map);
            }
        }
        if (allList.size() != 0){
            renderText(JsonMapper.toJsonString(allList), response);
        }
    }

    /**
     * 获取资料下的所有特定要素
     * @param id
     * @param response
     *//*
    @RequestMapping(value = {"/getAllInterElement"})
    public void getInterElement(HttpServletResponse response){
        List<Map<String, Object>> interList = interfaceDataService.projectcdinterall();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < interList.size(); i++) {
            String cUSTOMAPIIDName = interList.get(i).get("CUSTOM_API_ID").toString();
            list.add(cUSTOMAPIIDName);
        }
        List<Map<String, Object>> emList = interfaceDataService.getInterElement(list);
        ArrayList<Map<String, Object>> allList = new ArrayList<>();
        for (Map<String, Object> map:emList) {
            if("soilDepths".equals(map.get("PARAM_ID"))||"adminCodes".equals(map.get("PARAM_ID"))||"netCodes".equals(map.get("PARAM_ID"))
                    ||"maxLat".equals(map.get("PARAM_ID"))||"minLat".equals(map.get("PARAM_ID"))||"maxLon".equals(map.get("PARAM_ID"))
                    ||"minLon".equals(map.get("PARAM_ID"))||"maxStaId".equals(map.get("PARAM_ID"))||"minStaId".equals(map.get("PARAM_ID"))
                    ||"staIds".equals(map.get("PARAM_ID"))||"penRangeOfYear".equals(map.get("PARAM_ID"))||"monRangeOfYear".equals(map.get("PARAM_ID"))
                    ||"dayRangeOfYear".equals(map.get("PARAM_ID"))||"tensOfYear".equals(map.get("PARAM_ID"))||"pensOfYear".equals(map.get("PARAM_ID"))
                    ||"monsOfYear".equals(map.get("PARAM_ID"))||"daysOfYear".equals(map.get("PARAM_ID"))||"day".equals(map.get("PARAM_ID"))
                    ||"month".equals(map.get("PARAM_ID"))||"maxYear".equals(map.get("PARAM_ID"))||"minYear".equals(map.get("PARAM_ID"))
                    ||"maxMD".equals(map.get("PARAM_ID"))||"minMD".equals(map.get("PARAM_ID"))||"tenRangeOfYear".equals(map.get("PARAM_ID"))
                    ||"timeRange".equals(map.get("PARAM_ID"))||"time".equals(map.get("PARAM_ID"))||"fcstLevel".equals(map.get("PARAM_ID"))
                    ||"fcstEle".equals(map.get("PARAM_ID"))||"elements".equals(map.get("PARAM_ID"))||"dataCode".equals(map.get("PARAM_ID"))||"times".equals(map.get("PARAM_ID"))){
                allList.add(map);
            }
        }
        if (allList.size() != 0){
            renderText(JsonMapper.toJsonString(allList), response);
        }
    }*/


    /**
     * 空间属性预报层次（单个）
     * @param response
     */
    @RequestMapping(value = {"/getFcstLevel"})
    public void getFcstLevel(HttpServletResponse response){
	    List<Map<String, Object>> list = interfaceDataService.getFcstLevel();
        if (list.size() != 0){
            renderText(JsonMapper.toJsonString(list), response);
        }
    }

    /**
     * 空间属性台站站网
     * @param response
     */
    @RequestMapping(value = {"/getNetCodes"})
    public void getNetCodes(HttpServletResponse response){
        List<Map<String, Object>> list = interfaceDataService.getNetCodes();
        if (list.size() != 0){
            renderText(JsonMapper.toJsonString(list), response);
        }
    }

    /**
     * 空间属性土壤深度
     * @param response
     */
    @RequestMapping(value = {"/getSoilDepths"})
    public void getSoilDepths(HttpServletResponse response){
        List<Map<String, Object>> list = interfaceDataService.getSoilDepths();
        if (list.size() != 0){
            renderText(JsonMapper.toJsonString(list), response);
        }
    }

    /**
     * 空间属性国内行政编码
     * @param response
     */
    @RequestMapping(value = {"/getAdminCodes"})
    public void getAdminCodes(HttpServletResponse response){
        List<Map<String, Object>> list = interfaceDataService.getAdminCodes();
        Map<String, Object> codeMap = new HashMap<>();
        ArrayList<Map<String, Object>> cityList = new ArrayList<>();
        codeMap.put("mit","内蒙古自治区");
        for (Map<String,Object> map:list) {
            if ("city".equals(map.get("level"))){
                cityList.add(map);
            }
        }
        codeMap.put("mitList",cityList);
        for (Map<String,Object> cityMap:cityList) {
            List<Map<String, Object>> districtList = new ArrayList<>();
            for (Map<String, Object> districtMap : list) {
                if (cityMap.get("id").equals(districtMap.get("parent_id"))&&"district".equals(districtMap.get("level"))) {
                    districtList.add(districtMap);
                }
            }
            cityMap.put("districtList",districtList);
        }
        if (codeMap.size() != 0){
            renderText(JsonMapper.toJsonString(codeMap), response);
        }
    }

    /**
     * 空间属性预报要素（单个）
     * @param response
     */
    @RequestMapping(value = {"/getFcstEle"})
    public void getFcstEle(String id,HttpServletResponse response){
        List<Map<String, Object>> list = interfaceDataService.getFcstEle(id);
        if (list.size() != 0){
            renderText(JsonMapper.toJsonString(list), response);
        }
    }

    /**
     * 空间属性要素字段代码
     * @param response
     */
    @RequestMapping(value = {"/getElements"})
    public void getElements(String id,HttpServletResponse response){
        List<Map<String, Object>> list = interfaceDataService.getElements(id);
        if (list.size() != 0){
            Map<String,Object> map = new HashMap<>();
            map.put("user_ele_code","Station_Name");
            map.put("ele_name","站名");
            map.put("ele_unit","-");
            list.add(map);
            Collections.reverse(list);
            renderText(JsonMapper.toJsonString(list), response);
        }else{
            renderText(JsonMapper.toJsonString(null), response);
        }
    }

    /**
     * 授权信息保存
     * @param response
     */
    @RequestMapping(value = {"/addInterValue"})
    public void addInterValue(@RequestBody InterDeploy interDeploy,HttpServletResponse response){
        List<InterDeploy> list = interfaceDataService.getAllInterValue();
        if(list.size()!=0){
            int index = 0;
            for (int i = 0; i <list.size() ; i++) {
                if(list.get(i).getDataClassId().equals(interDeploy.getDataClassId())&&list.get(i).getOtherID().equals(interDeploy.getOtherID())){
                    interDeploy.setId(list.get(i).getId());
                    interDeploy.setTimes(new Date());
                    int type = interfaceDataService.updateInterValue(interDeploy);
                    if(type==1){
                        renderText(JsonMapper.toJsonString("1"), response);
                    }else{
                        renderText(JsonMapper.toJsonString("0"), response);
                    }
                }else{
                    index+=1;
                }
            }
            if(index==list.size()){
                String uuid = UUID.randomUUID().toString().replace("-","");
                interDeploy.setId(uuid);
                interDeploy.setTimes(new Date());
                int type = interfaceDataService.addInterValue(interDeploy);
                if(type==1){
                    renderText(JsonMapper.toJsonString("1"), response);
                }else{
                    renderText(JsonMapper.toJsonString("0"), response);
                }
            }
        }else{
            String uuid = UUID.randomUUID().toString().replace("-","");
            interDeploy.setId(uuid);
            interDeploy.setTimes(new Date());
            int type = interfaceDataService.addInterValue(interDeploy);
            if(type==1){
                renderText(JsonMapper.toJsonString("1"), response);
            }else{
                renderText(JsonMapper.toJsonString("0"), response);
            }
        }
    }


}