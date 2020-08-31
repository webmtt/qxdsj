package com.thinkgem.jeesite.modules.data.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.data.entity.DataSearchDef2;
import com.thinkgem.jeesite.modules.data.entity.EleSetEleGroupDef;
import com.thinkgem.jeesite.modules.data.entity.EleSetElementDef;
import com.thinkgem.jeesite.modules.data.entity.SearchCondDef;
import com.thinkgem.jeesite.modules.data.entity.SearchSetDef;
import com.thinkgem.jeesite.modules.data.service.CategoryDataReltService;
import com.thinkgem.jeesite.modules.data.service.DataCategoryDefService;
import com.thinkgem.jeesite.modules.data.service.DataDefService;
import com.thinkgem.jeesite.modules.data.service.DataSearchDefService2;
import com.thinkgem.jeesite.modules.data.service.EleSetEleGroupDefService;
import com.thinkgem.jeesite.modules.data.service.EleSetElementDefService;
import com.thinkgem.jeesite.modules.data.service.SearchCondCfgService;
import com.thinkgem.jeesite.modules.data.service.SearchCondDefService;
import com.thinkgem.jeesite.modules.data.service.SearchInterfaceDefService;
import com.thinkgem.jeesite.modules.data.service.SearchSetDefService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;


@Controller
@RequestMapping(value = "/dataSearchDef")
public class DataSearchDefController extends BaseController {
	@Autowired
	private DataDefService dataDefService;
	@Autowired
	private DataSearchDefService2 dataSearchDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private DataCategoryDefService dataCategoryDefService;
	@Autowired
	private CategoryDataReltService categoryDataReltService;
	@Autowired
	private SearchSetDefService searchSetDefService;
	@Autowired
	private DictService  dictService;
	@Autowired
	private SearchCondDefService searchCondDefService;
	@Autowired
	private SearchCondCfgService searchCondCfgService;
	@Autowired
	private SearchInterfaceDefService searchInterfaceDefService;
	@Autowired
	private EleSetEleGroupDefService eleSetEleGroupDefService;
	@Autowired
	private EleSetElementDefService eleSetElementDefService;
    /**
     * 选择框查询的list
     * @param paramMap
     * @param request
     * @param response
     * @param model
     * @param dataid 默认的资料类型id
     * @param sid
     * @param fid
     * @return
     */
	@RequestMapping(value = "/list")
	public String dataSearchDefList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model) {
		String categoryid=request.getParameter("categoryid");//获得类型id
		String pid=request.getParameter("pid");//类型中文名
		String datacode=request.getParameter("datacode");
		List<DataCategory> plist=dataCategoryDefService.getDataCategoryList(0);
		//默认点进去
		if(datacode==null||"".equals(datacode)){
			if(plist.size()>0){
				//大类的list，默认第一个选择
				model.addAttribute("plist",plist);
				pid=String.valueOf(plist.get(0).getCategoryid());
				//子类的list
				List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
				model.addAttribute("clist",clist);
				if("17".equals(pid)){
					categoryid="17";
				}else{
					//categoryid=String.valueOf(clist.get(0).getCategoryid());
					if(clist.size()==0){
						clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(17));
						categoryid="17";
						pid="17";
					}else{
						categoryid=String.valueOf(clist.get(0).getCategoryid());
					}
				}
				model.addAttribute("pid",pid);
				model.addAttribute("categoryid",categoryid);
				List<Object[]> dlist=categoryDataReltService.findCategoryListCById(String.valueOf(categoryid));
				model.addAttribute("dlist",dlist);
				if(dlist.size()>0){
					model.addAttribute("datacode",dlist.get(0)[0]);
					datacode=(String)dlist.get(0)[0];
				}
			}
		}else{//点击查询
			
			model.addAttribute("categoryid",categoryid);	
			model.addAttribute("datacode",datacode);
			model.addAttribute("plist",plist);
			//子类的list
			List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
			model.addAttribute("clist",clist);
			if("17".equals(pid)){
				categoryid="17";
			}
			if(categoryid==null||"".equals(categoryid)){
				//categoryid=String.valueOf(clist.get(0).getCategoryid());
				if(clist.size()==0){
					clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(17));
					categoryid="17";
					pid="17";
				}else{
					categoryid=String.valueOf(clist.get(0).getCategoryid());
				}
			}
			List<Object[]> dlist=categoryDataReltService.findCategoryListCById(String.valueOf(categoryid));
			model.addAttribute("pid",pid);	
			model.addAttribute("dlist",dlist);
			model.addAttribute("datacode",datacode);
		}
		if(datacode!=null&&!"".equals(datacode)){
			//根据datacode查询数据资料
			DataDef dataDef=dataDefService.getDataDefByCode(datacode);
			//是否有子类，无子类一对一的关系，有子类一对多的关系
			model.addAttribute("isIncludeSub",dataDef.getIsincludesub());
			//根据datacode去查询检索条件
			if(dataDef.getIsincludesub()==0){//无子类
				List<DataSearchDef2> list=dataSearchDefService.getListByDataCode(datacode);
				model.addAttribute("list",list);
				model.addAttribute("status",list.size());
			}else if(dataDef.getIsincludesub()==1){//有子类
				List<DataSearchDef2> list=dataSearchDefService.getListByDatapCode(datacode);
				model.addAttribute("list",list);
				model.addAttribute("status",list.size());
			}
			//文件型和要素型
			model.addAttribute("storageType",dataDef.getStoragetype());
		}
		return "modules/data/dataSearchDefList";
	}
	/**
	 * 验证searchcode
	 * @param DataClass
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/checkSearchCode")
	public void checkSearchCode(String datacode,String flag,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据大类型获取子类的列表
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
        String searchsetcode=dataSearchDef.getSearchsetcode();
        List<DataSearchDef2> list=dataSearchDefService.getListByCodeFlag(searchsetcode, "0");
        String status="";
        if(searchsetcode!=null&&!"".equals(searchsetcode)){
        	if(list.size()==1){
            	status="0";
            }else{
            	status="1";
            }
        }else{
        	status="0";
        }
        Map map=new HashMap();
        map.put("status", status);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	/**
	 * 验证searchcode
	 * @param DataClass
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/checkelesetCode")
	public void checkelesetCode(String datacode,String flag,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据大类型获取子类的列表
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
        String elesetcode=dataSearchDef.getElesetcode();
        List<DataSearchDef2> list=dataSearchDefService.getListByCodeFlag(elesetcode, "1");
        String status="";
        if(elesetcode!=null&&!"".equals(elesetcode)){
        	 if(list.size()==1){
             	status="0";
             }else{
             	status="1";
             }
        }else{
        	status="0";
        }
        Map map=new HashMap();
        map.put("status", status);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	
	//空白页
	@RequestMapping(value="/getSearchsetcodeList")
	public String  getSearchsetcodeList(String datacode,HttpServletRequest request,HttpServletResponse response,Model model){
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
        String searchsetcode=dataSearchDef.getSearchsetcode();
        List<Object[]> list=dataCategoryDefService.getDataSearchSetList(searchsetcode);
        model.addAttribute("list",list);
	  return "modules/data/dataSearchList";
	}
	//空白页
	@RequestMapping(value="/getElesetcodeList")
	public String  getElesetcodeList(String datacode,HttpServletRequest request,HttpServletResponse response,Model model){
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
        String elesetcode=dataSearchDef.getElesetcode();
        List<Object[]> list=dataCategoryDefService.getelesetcodeList(elesetcode);
        model.addAttribute("list",list);
	  return "modules/data/dataElementList";
	}
	/**
	 * 根据大类获取子类列表
	 * @param DataClass
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getDataSubClassList")
	public void getDataSubClassList(String DataClass,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据大类型获取子类的列表
		List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(DataClass));
		String json=JsonMapper.toJsonString(clist);
		renderText(json, response);
	}
	//空白页
	@RequestMapping(value="/getBlank")
	public String  getBlank(String DataClass,HttpServletRequest request,HttpServletResponse response,Model model){
		return "modules/data/blank";
	}
	/**
	 * 根据子类型获取元数据集合列表
	 * @param DataClass
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getDatacodeList")
	public void getDatacodeList(String DataSubclass,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据子类型获取元数据集合列表
		List<Object[]> dlist=categoryDataReltService.findCategoryListById(String.valueOf(DataSubclass));
		String json=JsonMapper.toJsonString(dlist);
		renderText(json, response);
	}
	/**
	 * 根据子类型获取元数据集合列表-选择检索定制的
	 * @param DataClass
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getCDatacodeList")
	public void getCDatacodeList(String DataSubclass,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据子类型获取元数据集合列表
		List<Object[]> dlist=categoryDataReltService.findCategoryListCById(String.valueOf(DataSubclass));
		String json=JsonMapper.toJsonString(dlist);
		renderText(json, response);
	}
	//跳转到添加页面-无子类
	@RequestMapping("/dataSearchAdd")
	public String dataSearchAdd(HttpServletRequest request,HttpServletResponse response,Model model,
			String categoryid,String datacode,String pid){
		//查询该资料下所有存在的检索条件集合code 和要素集合code
		List<DataCategoryDef> plist=dataCategoryDefService.findDataReferByPid(pid);
		model.addAttribute("plist",plist);
		if(plist.size()>0){
			categoryid=plist.get(0).getCategoryid().toString();
		}
		List<Object[]> slist=dataCategoryDefService.getDataSearchSetCode(Integer.valueOf(categoryid));
		model.addAttribute("slist",slist);
		List<Object[]> elist=dataCategoryDefService.getDataEleSetCode(Integer.valueOf(categoryid));
		model.addAttribute("elist",elist);
		//接口类型
		List<Object[]> namelist=searchInterfaceDefService.getTypename();
		model.addAttribute("namelist",namelist);
		model.addAttribute("datacode",datacode);
		model.addAttribute("pid",pid);
		model.addAttribute("categoryid",categoryid);
		return "modules/data/dataSearchDefAdd";		
	}
	//跳转到添加页面-有子类
	@RequestMapping("/dataSearchAdd2")
	public String dataSearchAdd2(HttpServletRequest request,HttpServletResponse response,Model model,
			String datacode,String categoryid,String pid){
		//查询该资料下所有存在的检索条件集合code 和要素集合code
		List<DataCategoryDef> plist=dataCategoryDefService.findDataReferByPid(pid);
		model.addAttribute("plist",plist);
		if(plist.size()>0){
			categoryid=plist.get(0).getCategoryid().toString();
		}
		List<Object[]> slist=dataCategoryDefService.getDataSearchSetCode(Integer.valueOf(categoryid));
		model.addAttribute("slist",slist);
		List<Object[]> elist=dataCategoryDefService.getDataEleSetCode(Integer.valueOf(categoryid));
		model.addAttribute("elist",elist);
		//接口类型
		List<Object[]> namelist=searchInterfaceDefService.getTypename();
		model.addAttribute("namelist",namelist);
		model.addAttribute("datacode",datacode);
		model.addAttribute("pid",pid);
		model.addAttribute("categoryid",categoryid);
		return "modules/data/dataSearchDefAdd2";		
	}
		//跳转到添加页面-无子类
		@RequestMapping("/editDataSearch")
		public String editDataSearch(HttpServletRequest request,HttpServletResponse response,Model model,
				String categoryid,String datacode,String pid){
			//查询该资料下所有存在的检索条件集合code 和要素集合code
			List<DataCategoryDef> plist=dataCategoryDefService.findDataReferByPid(pid);
			model.addAttribute("plist",plist);
			List<Object[]> slist=dataCategoryDefService.getDataSearchSetCode(Integer.valueOf(categoryid));
			model.addAttribute("slist",slist);
			List<Object[]> elist=dataCategoryDefService.getDataEleSetCode(Integer.valueOf(categoryid));
			model.addAttribute("elist",elist);
			//接口类型
			List<Object[]> namelist=searchInterfaceDefService.getTypename();
			model.addAttribute("namelist",namelist);
			DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
			model.addAttribute("categoryid",categoryid);
			model.addAttribute("pid",pid);
			model.addAttribute("datacode",datacode);
			model.addAttribute("dataSearchDef",dataSearchDef);
			return "modules/data/dataSearchDefEdit";		
		}
		//跳转到添加页面-有子类
		@RequestMapping("/editDataSearch2")
		public String editDataSearch2(HttpServletRequest request,HttpServletResponse response,Model model,
				String datacode,String categoryid,String pid){
			DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
			List<DataCategoryDef> plist=dataCategoryDefService.findDataReferByPid(pid);
			model.addAttribute("plist",plist);
			List<Object[]> slist=dataCategoryDefService.getDataSearchSetCode(Integer.valueOf(categoryid));
			model.addAttribute("slist",slist);
			List<Object[]> elist=dataCategoryDefService.getDataEleSetCode(Integer.valueOf(categoryid));
			model.addAttribute("elist",elist);
			//接口类型
			List<Object[]> namelist=searchInterfaceDefService.getTypename();
			model.addAttribute("namelist",namelist);
			model.addAttribute("categoryid",categoryid);
			model.addAttribute("pid",pid);
			model.addAttribute("datacode",datacode);
			model.addAttribute("pcode",dataSearchDef.getParentdatacode());
			model.addAttribute("dataSearchDef",dataSearchDef);
			return "modules/data/dataSearchDefEdit2";		
		}
		/**
		 * 根据categoryid查询所有的searchsetcode
		 * @param request
		 * @param response
		 * @param model
		 * @param categoryid
		 */
		@RequestMapping("/getSearchCode")
		public void getSearchCode(HttpServletRequest request,HttpServletResponse response,Model model,
				String categoryid){
			List<Object[]> elist=dataCategoryDefService.getDataSearchSetCode(Integer.valueOf(categoryid));
			String json=JsonMapper.toJsonString(elist);
			renderText(json, response);	
		}
		/**
		 * 根据categoryid查询所有的searchsetcode
		 * @param request
		 * @param response
		 * @param model
		 * @param categoryid
		 */
		@RequestMapping("/getElementCode")
		public void getElementCode(HttpServletRequest request,HttpServletResponse response,Model model,
				String categoryid){
			List<Object[]> elist=dataCategoryDefService.getDataEleSetCode(Integer.valueOf(categoryid));
			String json=JsonMapper.toJsonString(elist);
			renderText(json, response);	
		}
		
		
	// 添加-保存
	@RequestMapping("/save")
	public String dataSearchDefSave(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		DataSearchDef2 dataSearchDef=new DataSearchDef2 ();
		//字段保存
		extracMethod(request, model, dataSearchDef);
		try{
			dataSearchDefService.saveDataSearchDef(dataSearchDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/dataSearchDef/list";
	}
	// 数据资料检索定义表 BMD_DATASEARCHDEF 的保存事件   无子类
	@SuppressWarnings("null")
	@RequestMapping("/saveDataSearchDef")
	public String saveDataSearchDef(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String datacode=request.getParameter("datacode");
		String pid=request.getParameter("pid");
		String searchsetcode_option=request.getParameter("searchsetcode");
		String categoryid=request.getParameter("categoryid");
		String searchsetcode="";
		String elesetcode="";
		String interfaceCode="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//检索条件
		String newtime=sdf.format(new Date());
		if("0".equals(searchsetcode_option)){
			String datacode1=request.getParameter("searchalloption");
			searchsetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getSearchsetcode();
		}else if("1".equals(searchsetcode_option)){
			//自动生成
			searchsetcode=datacode+"_"+newtime;
		}else if("2".equals(searchsetcode_option)){
			//新的资料的searchsetcode
			String tempsearchsetcode=datacode+"_"+newtime;
			String datacode1=request.getParameter("searchalloption");
			String searchsetcode1= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getSearchsetcode();
			List<SearchSetDef> list=searchSetDefService.getsearchSetDefListById(searchsetcode1);
			//BMD_SEARCHSETDEF表的复制
			SearchSetDef temp=null;
			for(int i=0;i<list.size();i++){
				temp=new SearchSetDef();
				SearchSetDef searchSetDef=list.get(i);
				temp.setId(searchSetDefService.getMaxId()+1);
				temp.setSearchsetcode(tempsearchsetcode);
				String searchgroupcode=searchSetDef.getSearchgroupcode();
				temp.setSearchgroupcode(searchgroupcode.replace(searchsetcode1, tempsearchsetcode));
				temp.setChnname(searchSetDef.getChnname());
				temp.setEngname(searchSetDef.getEngname());
				temp.setInvalid(searchSetDef.getInvalid());
				temp.setIsoptional(searchSetDef.getIsoptional());
				temp.setOrderno(searchSetDef.getOrderno());
				searchSetDefService.saveSearchSetDef(temp);
			}
			//BMD_SEARCHCONDDEF表的复制
			List<SearchCondDef> list2=searchCondDefService.getSearchListGroupCode(searchsetcode1);
			SearchCondDef searchCondDef0=null;
			for(int i=0;i<list2.size();i++){
				searchCondDef0=new SearchCondDef();
				SearchCondDef searchCondDef=list2.get(i);
				String searchgroupcode=searchCondDef.getSearchgroupcode();
				searchCondDef0.setId(searchCondDefService.getMaxId()+1);
				searchCondDef0.setSearchgroupcode(searchgroupcode.replace(searchsetcode1, tempsearchsetcode));
				searchCondDef0.setAttachsearchcode(searchCondDef.getAttachsearchcode());
				searchCondDef0.setBegindate(searchCondDef.getBegindate());
				searchCondDef0.setBegindatetype(searchCondDef.getBegindatetype());
				searchCondDef0.setChnname(searchCondDef.getChnname());
				searchCondDef0.setDatasourcetype(searchCondDef.getDatasourcetype());
				searchCondDef0.setDefaultvalue(searchCondDef.getDefaultvalue());
				searchCondDef0.setEnddate(searchCondDef.getEnddate());
				searchCondDef0.setEnddatetype(searchCondDef.getEnddatetype());
				searchCondDef0.setEngname(searchCondDef.getEngname());
				searchCondDef0.setGroupdefaultsearch(searchCondDef.getGroupdefaultsearch());
				searchCondDef0.setInvalid(searchCondDef.getInvalid());
				searchCondDef0.setIshidden(searchCondDef.getIshidden());
				searchCondDef0.setIsoptional(searchCondDef.getIsoptional());
				searchCondDef0.setIsvaluelimit(searchCondDef.getIsvaluelimit());
				searchCondDef0.setMaxvalue(searchCondDef.getMaxvalue());
				searchCondDef0.setMinvalue(searchCondDef.getMinvalue());
				searchCondDef0.setOptiondefault(searchCondDef.getOptiondefault());
				searchCondDef0.setOrderno(searchCondDef.getOrderno());
				searchCondDef0.setSearchattach(searchCondDef.getSearchattach());
				searchCondDef0.setSearchattach2(searchCondDef.getSearchattach2());
				searchCondDef0.setSearchcode(searchCondDef.getSearchcode());
				searchCondDef0.setSearchsubcode(searchCondDef.getSearchsubcode());
				searchCondDef0.setSearchtype(searchCondDef.getSearchtype());
				searchCondDef0.setValuelimit(searchCondDef.getValuelimit());
				searchCondDef0.setValuelimitunit(searchCondDef.getValuelimitunit());
				searchCondDefService.saveSearchCondDef(searchCondDef0);
			}
			searchsetcode=tempsearchsetcode;
		}
		//检索结果
		String elesetcode_option=request.getParameter("elementsetcode");
		if("0".equals(elesetcode_option)){
			String datacode1=request.getParameter("elealloption");
			elesetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getElesetcode();
		}else if("1".equals(elesetcode_option)){
			elesetcode=datacode+"_"+newtime;
		}else if("2".equals(elesetcode_option)){
			String tempelesetcode=datacode+"_"+newtime;
			String datacode1=request.getParameter("elealloption");
			String elesetcode1= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getElesetcode();
			List<EleSetEleGroupDef> list=eleSetEleGroupDefService.getListByelesetcode(elesetcode1);
			EleSetEleGroupDef eleSetEleGroupDef0=null;
			for(int i=0;i<list.size();i++){
				eleSetEleGroupDef0=new EleSetEleGroupDef();
				EleSetEleGroupDef eleSetEleGroupDef=list.get(i);
				eleSetEleGroupDef0.setId(eleSetEleGroupDefService.getMaxId()+1);
				eleSetEleGroupDef0.setElesetcode(tempelesetcode);
				eleSetEleGroupDef0.setChnname(eleSetEleGroupDef.getChnname());
				eleSetEleGroupDef0.setElegroupcode(eleSetEleGroupDef.getElegroupcode());
				eleSetEleGroupDef0.setInvalid(eleSetEleGroupDef.getInvalid());
				eleSetEleGroupDef0.setOrderno(eleSetEleGroupDef.getOrderno());
				eleSetEleGroupDefService.saveEleSetEleGroupDef(eleSetEleGroupDef0);
			}
			List<EleSetElementDef> list2=eleSetElementDefService.getListByelesetcode2(elesetcode1);
			EleSetElementDef eleSetElementDef0=null;
			for(int i=0;i<list2.size();i++){
				eleSetElementDef0=new EleSetElementDef();
				EleSetElementDef eleSetElementDef=list2.get(i);
				eleSetElementDef0.setId(eleSetElementDefService.getMaxId()+1);
				eleSetElementDef0.setElesetcode(tempelesetcode);
				eleSetElementDef0.setCelecode(eleSetElementDef.getCelecode());
				eleSetElementDef0.setDataformat(eleSetElementDef.getDataformat());
				eleSetElementDef0.setDatatype(eleSetElementDef.getDatatype());
				eleSetElementDef0.setDataunit(eleSetElementDef.getDataunit());
				eleSetElementDef0.setDescriptionchn(eleSetElementDef.getDescriptionchn());
				eleSetElementDef0.setDescriptioneng(eleSetElementDef.getDescriptioneng());
				eleSetElementDef0.setElegroupcode(eleSetElementDef.getElegroupcode());
				eleSetElementDef0.setElementname(eleSetElementDef.getElementname());
				eleSetElementDef0.setElementrange(eleSetElementDef.getElementrange());
				eleSetElementDef0.setFiltervalue(eleSetElementDef.getFiltervalue());
				eleSetElementDef0.setInvalid(eleSetElementDef.getInvalid());
				eleSetElementDef0.setIsfilter(eleSetElementDef.getIsfilter());
				eleSetElementDef0.setIshidden(eleSetElementDef.getIshidden());
				eleSetElementDef0.setIsoptional(eleSetElementDef.getIsoptional());
				eleSetElementDef0.setIsqc(eleSetElementDef.getIsqc());
				eleSetElementDef0.setIsseleted(eleSetElementDef.getIsseleted());
				eleSetElementDef0.setOrderno(eleSetElementDef.getOrderno());
				eleSetElementDef0.setSpecvalue(eleSetElementDef.getSpecvalue());
				eleSetElementDef0.setSpecvaluedesc(eleSetElementDef.getSpecvaluedesc());
				eleSetElementDef0.setUelecode(eleSetElementDef.getUelecode());
				eleSetElementDefService.saveEleSetElementDef(eleSetElementDef0);
			}
			elesetcode=tempelesetcode;
		}
		//接口
		String interfacecode=request.getParameter("interfacecode");
		if("0".equals(interfacecode)){
			interfaceCode=request.getParameter("interfaceoption");
		}
		String searchpageorderby=request.getParameter("searchpageorderby");
		String defaultstaions=request.getParameter("defaultstaions");
		DataSearchDef2 dataSearchDef=new DataSearchDef2();
		dataSearchDef.setInvalid(0);
		dataSearchDef.setDatacode(datacode);
		dataSearchDef.setDsaccesscode("MUSIC-DataSearch");
		dataSearchDef.setSearchsetcode(searchsetcode);
		dataSearchDef.setElesetcode(elesetcode);
		dataSearchDef.setSearchpageorderby(searchpageorderby);
		dataSearchDef.setDefaultstaions(defaultstaions);
		dataSearchDef.setInterfacesetcode(interfaceCode);
		//字段保存
		try{
			dataSearchDefService.saveDataSearchDef(dataSearchDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/dataSearchDef/list?datacode="+datacode+"&pid="+pid+"&categoryid="+categoryid;
	}
	
	// 数据资料检索定义表 BMD_DATASEARCHDEF 的保存事件   有子类
		@RequestMapping("/saveDataSearchDef2")
		public String saveDataSearchDef2(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
			String datacode=request.getParameter("pdatacode");
			String searchsetcode_option=request.getParameter("searchsetcode");
			String categoryid=request.getParameter("categoryid");
			String searchsetcode="";
			String elesetcode="";
			String interfaceCode="";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			//检索条件
			String newtime=sdf.format(new Date());
			if("0".equals(searchsetcode_option)){
				String datacode1=request.getParameter("searchalloption");
				 searchsetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getSearchsetcode();
			}else if("1".equals(searchsetcode_option)){
				//自动生成
				searchsetcode=datacode+"_"+newtime;
			}
			//检索结果
			String elesetcode_option=request.getParameter("elementsetcode");
			if("0".equals(elesetcode_option)){
				String datacode1=request.getParameter("elealloption");
				 elesetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getElesetcode();
			}else if("1".equals(elesetcode_option)){
				elesetcode=datacode+"_"+newtime;
			}
			//接口
			String interfacecode=request.getParameter("interfacecode");
			if("0".equals(interfacecode)){
				interfaceCode=request.getParameter("interfaceoption");
			}
			String searchpageorderby=request.getParameter("searchpageorderby");
			String defaultstaions=request.getParameter("defaultstaions");
			DataSearchDef2 dataSearchDef=new DataSearchDef2();
			dataSearchDef.setDatacode(datacode+"_"+newtime);
			dataSearchDef.setElesetcode(elesetcode);
			dataSearchDef.setSearchpageorderby(searchpageorderby);
			dataSearchDef.setDefaultstaions(defaultstaions);
			dataSearchDef.setSearchsetcode(searchsetcode);
			dataSearchDef.setInterfacesetcode(interfaceCode);
			dataSearchDef.setParentdatacode(datacode);
			dataSearchDef.setDsaccesscode("MUSIC-DataSearch");
			//资料中文名称
			String datachnname=request.getParameter("datachnname");
			dataSearchDef.setDatachnname(datachnname);
			//检索名称
			String searchname=request.getParameter("searchname");
			dataSearchDef.setSearchname(searchname);
			//查询接口资料代码
			String udatacode=request.getParameter("udatacode");
			dataSearchDef.setUdatacode(udatacode);
			dataSearchDef.setInvalid(0);
			//字段保存
			try{
				dataSearchDefService.saveDataSearchDef(dataSearchDef);
//				cacheCleanController.cleanCache();
				addMessage(redirectAttributes, "保存成功");
			}catch (Exception e) {
				addMessage(redirectAttributes, "保存失败");
				e.printStackTrace();
			}
			String pid=request.getParameter("pid");
			return "redirect:"+"/dataSearchDef/list?datacode="+datacode+"&pid="+pid+"&categoryid="+categoryid;
			//return "redirect:"+"/dataSearchDef/editDataSearch2?datacode="+dataSearchDef.getParentdatacode()+"&categoryid="+categoryid+"&pid="+pid;
		}
	// 数据资料检索定义表 BMD_DATASEARCHDEF 的保存事件   无子类-编辑保存操作
		@RequestMapping("/saveupdateDataSearch")
		public String saveupdateDataSearch(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
			String datacode=request.getParameter("datacode");
			String pid=request.getParameter("pid");
			String searchsetcode_option=request.getParameter("searchsetcode");
			String searchsetcode="";
			String elesetcode="";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			//检索条件
			String newtime=sdf.format(new Date());
			if("0".equals(searchsetcode_option)){
				 String datacode1=request.getParameter("searchalloption");
				 searchsetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getSearchsetcode();
			}else if("1".equals(searchsetcode_option)){
				//自动生成
				searchsetcode=datacode+"_"+newtime;
			}
			//检索结果
			String elesetcode_option=request.getParameter("elementsetcode");
			if("0".equals(elesetcode_option)){
				String datacode1=request.getParameter("elealloption");
				 elesetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getElesetcode();
			}else if("1".equals(elesetcode_option)){
				elesetcode=datacode+"_"+newtime;
			}
			//接口
			String searchpageorderby=request.getParameter("searchpageorderby");
			String defaultstaions=request.getParameter("defaultstaions");
			DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
			dataSearchDef.setDatacode(datacode);
			dataSearchDef.setSearchsetcode(searchsetcode);
			dataSearchDef.setElesetcode(elesetcode);
			dataSearchDef.setSearchpageorderby(searchpageorderby);
			dataSearchDef.setDefaultstaions(defaultstaions);
			//字段保存
			try{
				dataSearchDefService.saveDataSearchDef(dataSearchDef);
//				cacheCleanController.cleanCache();
				model.addAttribute("status",0);
				addMessage(redirectAttributes, "保存成功");
			}catch (Exception e) {
				model.addAttribute("status",1);
				addMessage(redirectAttributes, "保存失败");
				e.printStackTrace();
			}		
			String categoryid=request.getParameter("categoryid");
			return "modules/data/blankClose";	
			//return "redirect:"+"/dataSearchDef/editDataSearch2?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid;
		}
		// 数据资料检索定义表 BMD_DATASEARCHDEF 的保存事件   无子类-编辑保存操作
	@RequestMapping("/saveupdateDataSearch2")
	public String saveupdateDataSearch2(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String datacode=request.getParameter("datacode");
		String searchsetcode_option=request.getParameter("searchsetcode");
		String searchsetcode="";
		String elesetcode="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//检索条件
		String newtime=sdf.format(new Date());
		if("0".equals(searchsetcode_option)){
			 String datacode1=request.getParameter("searchalloption");
			 searchsetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getSearchsetcode();
		}else if("1".equals(searchsetcode_option)){
			//自动生成
			searchsetcode=datacode+"_"+newtime;
		}
		//检索结果
		String elesetcode_option=request.getParameter("elementsetcode");
		if("0".equals(elesetcode_option)){
			String datacode1=request.getParameter("elealloption");
			 elesetcode= dataSearchDefService.getDataSearchDefByDataCode(datacode1).getElesetcode();
		}else if("1".equals(elesetcode_option)){
			elesetcode=datacode+"_"+newtime;
		}
		//接口
		String interfacecode=request.getParameter("interfacecode");
		String searchpageorderby=request.getParameter("searchpageorderby");
		String defaultstaions=request.getParameter("defaultstaions");
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
		dataSearchDef.setDatacode(datacode);
		dataSearchDef.setSearchsetcode(searchsetcode);
		dataSearchDef.setElesetcode(elesetcode);
		dataSearchDef.setSearchpageorderby(searchpageorderby);
		dataSearchDef.setDefaultstaions(defaultstaions);
		//字段保存
		try{
			dataSearchDefService.saveDataSearchDef(dataSearchDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
			model.addAttribute("status",0);
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			model.addAttribute("status",1);
			e.printStackTrace();
		}		
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		
		return "modules/data/blankClose";	
		//return "redirect:"+"/dataSearchDef/editDataSearch2?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid;
	}
			
	//字段保存
	private void extracMethod(HttpServletRequest request, Model model, DataSearchDef2 dataSearchDef) {
		String datacode=request.getParameter("datacode");
		dataSearchDef.setDatacode(datacode);
		String dsaccesscode=request.getParameter("dsaccesscode");
		String searchsetcode=request.getParameter("searchsetcode");
		String elesetcode=request.getParameter("elesetcode");
		String searchpageattachcond=request.getParameter("searchpageattachcond");
		String searchpageorderby=request.getParameter("searchpageorderby");
		String invalid=request.getParameter("invalid");
		String interfacesetcode=request.getParameter("interfacesetcode");
		String defaultstaions=request.getParameter("defaultstaions");
		String parentdatacode=request.getParameter("parentdatacode");
		String datachnname=request.getParameter("datachnname");
		String searchname=request.getParameter("searchname");
		String udatacode=request.getParameter("udatacode");
		
		String created=request.getParameter("created");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(null!=created||!"".equals(created)){
			model.addAttribute("created", created);
			try {
				dataSearchDef.setCreated(sdf.parse(created));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			Calendar c=Calendar.getInstance();
			try {
				c.setTime(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dataSearchDef.setCreated(c.getTime());
		}
		
		
		dataSearchDef.setDatachnname(datachnname);
		dataSearchDef.setDefaultstaions(defaultstaions);
		dataSearchDef.setDsaccesscode(dsaccesscode);
		dataSearchDef.setElesetcode(elesetcode);
		dataSearchDef.setInterfacesetcode(interfacesetcode);
		dataSearchDef.setInvalid(Integer.valueOf(invalid));
		dataSearchDef.setParentdatacode(parentdatacode);
		dataSearchDef.setSearchname(searchname);
		dataSearchDef.setSearchpageattachcond(searchpageattachcond);
		dataSearchDef.setSearchpageorderby(searchpageorderby);
		dataSearchDef.setSearchsetcode(searchsetcode);
		dataSearchDef.setUdatacode(udatacode);
	}
	//修改
	//跳转到添加页面
	@RequestMapping("/toEdit")
	public String dataSearchDefToEdit(HttpServletRequest request,HttpServletResponse response,Model model){
		String datacode=request.getParameter("datacode");
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
		model.addAttribute("dataSearchDef",dataSearchDef);
		return "modules/data/dataSearchDefEdit";		
	}	
	
	@RequestMapping("/edit")
	public String dataSearchDefEdit(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		DataSearchDef2 dataSearchDef=new DataSearchDef2 ();
		
		extracMethod(request, model, dataSearchDef);
		
		try{
			dataSearchDefService.saveDataSearchDef(dataSearchDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/dataSearchDef/list";		
	}	
	//删除
	@RequestMapping("/delete")
	public String dataSearchDefDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String datacode=request.getParameter("datacode");
		DataSearchDef2 dataSearchDef=dataSearchDefService.getDataSearchDefByDataCode(datacode);
		try{
			if(dataSearchDef.getInvalid()==0){
				dataSearchDef.setInvalid(1);
			}else if(dataSearchDef.getInvalid()==1){
				dataSearchDef.setInvalid(0);
			}
			dataSearchDefService.saveDataSearchDef(dataSearchDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "更新成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "更新失败");
			e.printStackTrace();
		}						
		return "redirect:"+"/dataSearchDef/list";	
	}
	/**
	 * 根据datacode去查询检索条件集合
	 * @param datacode
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/getSearchSetDef")
	public void getSearchSetDef(String searchsetcode,HttpServletRequest request,HttpServletResponse response,Model model){
		List<SearchSetDef> list1=searchSetDefService.getsearchSetDefListById(searchsetcode);
		List<SearchCondDef> list2=searchCondDefService.getSearchClistByCode(searchsetcode);
		Map map=new HashMap();
		map.put("list1", list1);
		map.put("list2", list2);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	/**
	 * 根据类型在字典表中查询
	 * @param 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/getDict")
	public void getDict(String type,HttpServletRequest request,HttpServletResponse response,Model model){
		List<Dict> list=dictService.findListByTpye(type);
		String json=JsonMapper.toJsonString(list);
		renderText(json, response);
	}
	//新增检索条件分组跳转
	@RequestMapping(value="/dataSearchGroup")
	public String dataSearchGroup(String type,String searchSetCode,String index,
			HttpServletRequest request,HttpServletResponse response,Model model){
		List<Dict> list=dictService.findListByTpye(type);
		model.addAttribute("list",list);
		model.addAttribute("type",type);
		model.addAttribute("index",index);
		model.addAttribute("searchSetCode",searchSetCode);
		return "modules/data/dataSearchGoup";
	}
	//新增检索条件分组跳转
		@RequestMapping(value="/updateGpCondition")
		public String updateGpCondition(String type,String searchSetCode,String id,
				HttpServletRequest request,HttpServletResponse response,Model model){
			List<Dict> list=dictService.findListByTpye(type);
			SearchSetDef searchSetDef=searchSetDefService.getsearchSetDefById(Integer.valueOf(id));
			String  searchgroupcode=searchSetDef.getSearchgroupcode();
			searchgroupcode=searchgroupcode.substring(searchgroupcode.lastIndexOf("-"), searchgroupcode.length());
			model.addAttribute("list",list);
			model.addAttribute("searchSetCode",searchSetCode);
			model.addAttribute("searchSetDef",searchSetDef);
			model.addAttribute("searchgroupcode",searchgroupcode.replace("-", ""));
			return "modules/data/dataSearchGoupEdit";
		}
	/**
	 * ajax保存成功，新增一种类型
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping("/SaveSearchGroup")
	public void SaveSearchGroup(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		//自定义的类型
		String chnname=request.getParameter("chnname");
		String seachCode=request.getParameter("seachCode");
		String searchGroup=request.getParameter("SearchGroup");//类型
		String IsOptional=request.getParameter("IsOptional");//可选 
		String searchSetCode=request.getParameter("searchSetCode");//编码
		String value=request.getParameter("value");//编码
		//自定义
		if("ComConfig".equals(searchGroup)){
			Map map=new HashMap();
				map.put("status", 0);
				SearchSetDef searchSetDef=new SearchSetDef();
				searchSetDef.setId(searchSetDefService.getMaxId()+1);
				searchSetDef.setSearchsetcode(searchSetCode);
				searchSetDef.setIsoptional(Integer.valueOf(IsOptional));
				searchSetDef.setSearchgroupcode(searchSetCode+"-"+seachCode);
				searchSetDef.setChnname(chnname);
				searchSetDef.setOrderno(0);
				searchSetDef.setInvalid(0);
				try{
					searchSetDefService.saveSearchSetDef(searchSetDef);
//					cacheCleanController.cleanCache();
					
				}catch (Exception e) {
					e.printStackTrace();
				}	
			List<SearchSetDef> list2=searchSetDefService.getsearchSetDefListById(searchSetCode);
			List<SearchCondDef> list3=searchCondDefService.getSearchClistByCode(searchSetCode);
			map.put("list1", list2);
			map.put("list2", list3);
			String json=JsonMapper.toJsonString(map);
			renderText(json, response);
		}else{
			List<SearchSetDef> islist=searchSetDefService.getsearchSetDefListBy(searchSetCode,searchSetCode+"-"+searchGroup);
			Map map=new HashMap();
			if(islist.size()>0){
				map.put("status", 1);
			}else{
				map.put("status", 0);
				SearchSetDef searchSetDef=new SearchSetDef();
				List<Dict> list=dictService.findListByTpye(value);
				for(int i=0;i<list.size();i++){
					if(list.get(i).getValue().equals(searchGroup)){
						searchSetDef.setChnname(list.get(i).getLabel());
						break;
					}
				}
				searchSetDef.setId(searchSetDefService.getMaxId()+1);
				searchSetDef.setSearchsetcode(searchSetCode);
				searchSetDef.setIsoptional(Integer.valueOf(IsOptional));
				searchSetDef.setSearchgroupcode(searchSetCode+"-"+searchGroup);
				searchSetDef.setOrderno(0);
				searchSetDef.setInvalid(0);
				try{
					searchSetDefService.saveSearchSetDef(searchSetDef);
//					cacheCleanController.cleanCache();
					
				}catch (Exception e) {
					e.printStackTrace();
				}	
			}
			List<SearchSetDef> list2=searchSetDefService.getsearchSetDefListById(searchSetCode);
			List<SearchCondDef> list3=searchCondDefService.getSearchClistByCode(searchSetCode);
			map.put("list1", list2);
			map.put("list2", list3);
			String json=JsonMapper.toJsonString(map);
			renderText(json, response);
		}
		
		
	}
	/**
	 * ajax保存成功，新增一种类型
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping("/editSearchGroup")
	public void editSearchGroup(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		//自定义的类型
		String chnname=request.getParameter("chnname");
		String seachCode=request.getParameter("seachCode");
		String searchGroup=request.getParameter("SearchGroup");//类型
		String IsOptional=request.getParameter("IsOptional");//可选 
		String searchSetCode=request.getParameter("searchSetCode");//编码
		String value=request.getParameter("value");//编码
		String id=request.getParameter("id");//编码
		//自定义
		SearchSetDef searchSetDef=searchSetDefService.getsearchSetDefById(Integer.valueOf(id));
		if("ComConfig".equals(searchGroup)){
			Map map=new HashMap();
				map.put("status", 0);
				searchSetDef.setSearchsetcode(searchSetCode);
				searchSetDef.setIsoptional(Integer.valueOf(IsOptional));
				searchSetDef.setSearchgroupcode(searchSetCode+"-"+seachCode);
				searchSetDef.setChnname(chnname);
				try{
					searchSetDefService.saveSearchSetDef(searchSetDef);
//					cacheCleanController.cleanCache();
					
				}catch (Exception e) {
					e.printStackTrace();
				}	
			List<SearchSetDef> list2=searchSetDefService.getsearchSetDefListById(searchSetCode);
			List<SearchCondDef> list3=searchCondDefService.getSearchClistByCode(searchSetCode);
			map.put("list1", list2);
			map.put("list2", list3);
			String json=JsonMapper.toJsonString(map);
			renderText(json, response);
		}else{
			Map map=new HashMap();
				map.put("status", 0);
				List<Dict> list=dictService.findListByTpye(value);
				for(int i=0;i<list.size();i++){
					if(list.get(i).getValue().equals(searchGroup)){
						searchSetDef.setChnname(list.get(i).getLabel());
						break;
					}
				}
				searchSetDef.setSearchsetcode(searchSetCode);
				searchSetDef.setIsoptional(Integer.valueOf(IsOptional));
				searchSetDef.setSearchgroupcode(searchSetCode+"-"+searchGroup);
				try{
					searchSetDefService.saveSearchSetDef(searchSetDef);
//					cacheCleanController.cleanCache();
					
				}catch (Exception e) {
					e.printStackTrace();
				}	
			List<SearchSetDef> list2=searchSetDefService.getsearchSetDefListById(searchSetCode);
			List<SearchCondDef> list3=searchCondDefService.getSearchClistByCode(searchSetCode);
			map.put("list1", list2);
			map.put("list2", list3);
			String json=JsonMapper.toJsonString(map);
			renderText(json, response);
		}
		
		
	}
	/**
	 * 删除分组
	 * @param 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/deleteGroup")
	public void deleteGroup(String id,HttpServletRequest request,HttpServletResponse response,Model model){
		Map map=new HashMap();
		SearchSetDef searchSetDef=searchSetDefService.getsearchSetDefById(Integer.valueOf(id));
		try{
			if(searchSetDef.getInvalid()==0){
				searchSetDef.setInvalid(1);
			}else if(searchSetDef.getInvalid()==1){
				searchSetDef.setInvalid(0);
			}
			//searchSetDefService.delSearchSetDefById(Integer.valueOf(id));
			searchSetDefService.saveSearchSetDef(searchSetDef);
			map.put("status", 0);
		}catch (Exception e) {
			map.put("status", 1);
		}
		//SearchSetDef searchSetDef=searchSetDefService.getsearchSetDefById(Integer.valueOf(id));
		List<SearchSetDef> list2=searchSetDefService.getsearchSetDefListById(searchSetDef.getSearchsetcode());
		List<SearchCondDef> list3=searchCondDefService.getSearchClistByCode(searchSetDef.getSearchsetcode());
		map.put("list1", list2);
		map.put("list2", list3);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	
	/**
	 * 分组选中状态
	 * @param 
	 * @param request
	 * @param response
	 * @param model
	
	@RequestMapping(value="/updategroupdefault")
	public void updategroupdefault(String ids,String id,HttpServletRequest request,HttpServletResponse response,Model model){
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(id));
		int groupdefaultsearch=searchCondDef.getGroupdefaultsearch();
		if(groupdefaultsearch==0){
			searchCondDef.setGroupdefaultsearch(1);
		}else if(groupdefaultsearch==1){
			searchCondDef.setGroupdefaultsearch(0);
		}
		Map map=new HashMap();
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
			map.put("status", 0);
		}catch (Exception e) {
			map.put("status", 1);
		}
		SearchSetDef searchSetDef=searchSetDefService.getsearchSetDefById(Integer.valueOf(ids));
		List<SearchSetDef> list2=searchSetDefService.getsearchSetDefListById(searchSetDef.getSearchsetcode());
		List<SearchCondDef> list3=searchCondDefService.getSearchClistByCode(searchSetDef.getSearchsetcode());
		map.put("list1", list2);
		map.put("list2", list3);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	 */
	/**
	 * 删除条件
	 * @param  id 主键
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/deleteCondition")
	public void deleteCondition(String id,HttpServletRequest request,HttpServletResponse response,Model model){
		Map map=new HashMap();
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(id));
		try{
			if(searchCondDef.getInvalid()==0){
				searchCondDef.setInvalid(1);
			}else if(searchCondDef.getInvalid()==1){
				searchCondDef.setInvalid(0);
			}
			searchCondDefService.saveSearchCondDef(searchCondDef);
			map.put("status", 0);
		}catch (Exception e) {
			map.put("status", 1);
		}
		
		List<SearchCondDef> list=searchCondDefService.getSearchListByGroupCode(searchCondDef.getSearchgroupcode());
		map.put("list", list);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	/**
	 * 刷新条件
	 * @param  id 主键
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/freshCondition")
	public void freshCondition(String groupCode,HttpServletRequest request,HttpServletResponse response,Model model){
		List<SearchCondDef> list=searchCondDefService.getSearchListByGroupCode(groupCode);
		String json=JsonMapper.toJsonString(list);
		renderText(json, response);
	}
	/**
	 * 验证searchsetcode或者elesetcode
	 * @param  flag code的标识
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/checkCode")
	public void checkCode(String code,String flag,HttpServletRequest request,HttpServletResponse response,Model model){
		List<DataSearchDef2> list=dataSearchDefService.getListByCodeFlag(code,flag);
		Map map=new HashMap();
		if(list.size()>0){
			map.put("status", 1);
		}
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	/**
	 * 通过类型去查询  返回数据
	 * @param type
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addCondition")
	public String openCondition(String groupcode,String searchgroupcode,HttpServletRequest request,HttpServletResponse response,Model model){
		if("TimeSel".equals(groupcode)){
			  
		}else if("StationSel".equals(groupcode)){
			
		}else if("ElementSel".equals(groupcode)){
			
		}else if("ElementFilter".equals(groupcode)){
			
		}else if("FormatSel".equals(groupcode)){
			
		}else if("QCSel".equals(groupcode)){
			
		}else if("ComConfig".equals(groupcode)){
			
		}
		
		model.addAttribute("searchgroupcode",searchgroupcode);
		model.addAttribute("groupcode",groupcode);
		return "modules/data/searchCondCfgAdd";
	}
	/**
	 * 修改
	 * @param type
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/updateCondition")
	public String updateCondition(String groupcode,String id,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据groupcode查询
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(id));
		 model.addAttribute("id",id);
		 model.addAttribute("GroupDefaultSearch",searchCondDef.getGroupdefaultsearch());
		if("TimeSel".equals(groupcode)){
			  String searchType=searchCondDef.getSearchtype();
			  String searchSubType=searchCondDef.getSearchsubtype();
			  String searchAttach=searchCondDef.getSearchattach();
			  Integer isValueLimit=searchCondDef.getIsvaluelimit();
			  String valuelimit=searchCondDef.getValuelimit();
			  String valuelimitunit=searchCondDef.getValuelimitunit();
			  Integer enddatetype=searchCondDef.getEnddatetype();
			  String enddate=searchCondDef.getEnddate();
			  Integer minvalue=searchCondDef.getMinvalue();
			  Integer maxvalue=searchCondDef.getMaxvalue();
			  String defaultvalue=searchCondDef.getDefaultvalue();
			  Integer ishidden=searchCondDef.getIshidden();
			  model.addAttribute("searchType",searchType);
			  model.addAttribute("searchSubType",searchSubType);
			  model.addAttribute("searchAttach",searchAttach);
			  model.addAttribute("isValueLimit",isValueLimit);
			  model.addAttribute("valuelimit",valuelimit);
			  model.addAttribute("valuelimitunit",valuelimitunit);
			  model.addAttribute("enddatetype",enddatetype);
			  model.addAttribute("enddate",enddate);
			  model.addAttribute("minvalue",minvalue);
			  model.addAttribute("maxvalue",maxvalue);
			  model.addAttribute("ishidden",ishidden);
			  if("TimeScope".equals(searchType)){
				  if("yyyyMMXU".equals(searchSubType)){
					  model.addAttribute("searchAttach",searchAttach);
					  if(defaultvalue!=null&&!"".equals(defaultvalue)){
						  String start=defaultvalue.split(";")[0];
						  String end=defaultvalue.split(";")[1];
						  String dValue=start.split(",")[0].replace("<", "").replace(">", "");
						  String xunStyle1=Integer.valueOf(start.split(",")[1])+"";
						  String xunStyle2=Integer.valueOf(end.split(",")[1])+"";
						  if(start.split(",")[0].replace("<CurrMonth>", "")!=null&&!"".equals(start.split(",")[0].replace("<CurrMonth>", ""))){
							  String startTime=String.valueOf((0-Integer.valueOf(
									  start.split(",")[0].replace("<CurrMonth>", ""))));
							  model.addAttribute("startTime2",startTime);
						  }
						  if(end.split(",")[0].replace("<CurrMonth>", "")!=null&&!"".equals(end.split(",")[0].replace("<CurrMonth>", ""))){
							  String endTime=String.valueOf((0-Integer.valueOf(
									  end.split(",")[0].replace("<CurrMonth>", ""))));
							  model.addAttribute("endTime2",endTime);
						  }
						 
						  model.addAttribute("dValue","CurrMonth");
						  model.addAttribute("xunStyle1",xunStyle1);
						  model.addAttribute("xunStyle2",xunStyle2);
						  
					  } 
				  }else if("yyyyMMHU".equals(searchSubType)){
					  model.addAttribute("searchAttach",searchAttach);
					  if(defaultvalue!=null&&!"".equals(defaultvalue)){
						  if(defaultvalue!=null&&!"".equals(defaultvalue)){
							  String start=defaultvalue.split(";")[0];
							  String end=defaultvalue.split(";")[1];
							  String dValue=start.split(",")[0].replace("<", "").replace(">", "");
							  String startTime=String.valueOf((0-Integer.valueOf(start.split(",")[1])));
							  String endTime=String.valueOf((0-Integer.valueOf(end.split(",")[1])));
							  model.addAttribute("defaultvalue",defaultvalue);
							  model.addAttribute("startTime",startTime);
							  model.addAttribute("endTime",endTime);
						  } 
					  } 
				  }else{
					  if(defaultvalue!=null&&!"".equals(defaultvalue)){
						  if(defaultvalue!=null&&!"".equals(defaultvalue)){
							  String start=defaultvalue.split(";")[0];
							  String end=defaultvalue.split(";")[1];
							  String startTime="";
							  String endTime="";
							  if(start.split(">").length==1){
								   startTime="0";
							  }else{
								   startTime=String.valueOf((0-Integer.valueOf(
										  ( start.split(">")[1]==""?"0":start.split(">")[1]))));
							  }
							  if(end.split(">").length==1){
								   endTime="0";
							  }else{
								   endTime=String.valueOf((0-Integer.valueOf(
											 ( end.split(">")[1]==""?"0":end.split(">")[1]))));
							  }
							  model.addAttribute("defaultvalue",defaultvalue);
							  model.addAttribute("startTime",startTime);
							  model.addAttribute("endTime",endTime);
						  } 
					  } 
				  }
			  
			  }else if("TimeCycle".equals(searchType)){
				  model.addAttribute("searchType",searchType);
				  model.addAttribute("searchSubType",searchSubType);
				  String startTime4=defaultvalue.split(";")[0];
				  String endTime4=defaultvalue.split(";")[1];
				  String startTime44=defaultvalue.split(";")[2];
				  String endTime44=defaultvalue.split(";")[3];
				  String temp1=startTime4.replace("<CurrYear>", "");
				  String temp2=endTime4.replace("<CurrYear>", "");
				  if(temp1!=null&&!"".equals(temp1)){
					  model.addAttribute("startTime4",temp1.replace("-", ""));
				  }
				  if(temp2!=null&&!"".equals(temp2)){
					  model.addAttribute("endTime4",temp2.replace("-", ""));
				  }
				  model.addAttribute("startTime44",startTime44);
				  model.addAttribute("endTime44",endTime44);
				  
			  }else if("DataScope".equals(searchType)){
				  model.addAttribute("searchType",searchType);
				  model.addAttribute("defaultvalue",defaultvalue);
			  }else if("ComConfig".equals(searchType)){
				  model.addAttribute("searchAttach",searchAttach);
				  model.addAttribute("defaultvalue",defaultvalue);
				  model.addAttribute("searchSubType",searchAttach);
			  }
			 
			
		}else if("StationSel".equals(groupcode)){
			 String searchType=searchCondDef.getSearchtype();
			 if("ALLSel".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
				 
			 }else if("StationLevel".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
				 model.addAttribute("StationSel_ALLSel",searchCondDef.getSearchcode());
				 model.addAttribute("StationLevel_SM",searchCondDef.getSearchsubtype());
				 model.addAttribute("StationSel_IsHidden",searchCondDef.getIshidden());
			 }else if("StationList".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
				 model.addAttribute("StationSel_StationList",searchCondDef.getSearchcode());
				 if(searchCondDef.getDatasourcetype()==null||"".equals(searchCondDef.getDatasourcetype())){
					 model.addAttribute("StationList_Type",1);
				 }else{
					 model.addAttribute("StationList_Type",0);
				 }
				 model.addAttribute("StationList_defaultvalue",searchCondDef.getDefaultvalue());
				 model.addAttribute("StationList_SM",searchCondDef.getSearchsubtype());
			 }else if("StationGIS".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
				 if(searchCondDef.getDatasourcetype()==null||"".equals(searchCondDef.getDatasourcetype())){
					 model.addAttribute("StationGIS_Type",1);
				 }else{
					 model.addAttribute("StationGIS_Type",0);
				 }
				 model.addAttribute("StationGIS_SM",searchCondDef.getSearchsubtype());
			 }else if("Region".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
				 model.addAttribute("StationSel_Region",searchCondDef.getSearchcode());
				 model.addAttribute("searchSubType_Region",searchCondDef.getSearchsubtype());
			 }else if("Spatial".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
				 model.addAttribute("StationSel_Spatial",searchCondDef.getSearchcode());
				 model.addAttribute("searchSubType_Spatial",searchCondDef.getSearchsubtype());
			 }else if("StationScope".endsWith(searchType)){
				 model.addAttribute("StationSel",searchType);
			 }
			
		}else if("ElementSel".equals(groupcode)){
			String searchType=searchCondDef.getSearchtype();
			 model.addAttribute("searchType",searchType);
		}else if("ElementFilter".equals(groupcode)){
			String searchType=searchCondDef.getSearchtype();
			 model.addAttribute("searchType",searchType);
		}else if("FormatSel".equals(groupcode)){
			String searchType=searchCondDef.getSearchtype();
			 model.addAttribute("searchType",searchType);
			 model.addAttribute("FormatSel_chname",searchCondDef.getChnname());
		}else if("QCSel".equals(groupcode)){
			String searchType=searchCondDef.getSearchtype();
			model.addAttribute("searchType",searchType);
			model.addAttribute("QCSel_chname",searchCondDef.getChnname());
		}else{
			model.addAttribute("searchType",searchCondDef.getSearchtype());
			model.addAttribute("ComConfig_chname",searchCondDef.getChnname());
			model.addAttribute("ComConfig_subCode",searchCondDef.getSearchsubcode());
			model.addAttribute("ComConfig_SM",searchCondDef.getSearchsubtype());
		}
		model.addAttribute("searchSetCode",searchCondDef.getSearchgroupcode());
		model.addAttribute("groupcode",groupcode);
		return "modules/data/searchCondCfgEdit";
	}
	/**
	 * ajax保存成功
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveSearchSetDef")
	public void saveBottom(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		//类型
		String groupcode=request.getParameter("groupcode");
		String searchgroupcode=request.getParameter("searchgroupcode");
		//时间范围
		String TimeSel_Type=request.getParameter("TimeSel_Type");
		//时间类型
		String TimeSel_SubType=request.getParameter("TimeSel_SubType");
		//是否组默认选中
		String GroupDefaultSearch=request.getParameter("GroupDefaultSearch");
		SearchCondDef searchCondDef=new SearchCondDef();
		if(GroupDefaultSearch==null||"".equals(GroupDefaultSearch)){
			searchCondDef.setGroupdefaultsearch(0);
		}else{
			searchCondDef.setGroupdefaultsearch(Integer.valueOf(GroupDefaultSearch));
		}
		searchCondDef.setSearchgroupcode(searchgroupcode);
		searchCondDef.setId(searchCondDefService.getMaxId()+1);
		searchCondDef.setOrderno(0);
		searchCondDef.setInvalid(0);
		searchCondDef.setIsoptional(0);
		searchCondDef.setBegindatetype(0);
		String endDateType=request.getParameter("endDateType");
		String endDate=request.getParameter("endDate");
		if("0".equals(endDateType)||endDateType==null||"".equals(endDateType)){
			searchCondDef.setEnddatetype(0);
		}else{
			searchCondDef.setEnddate("<"+endDate+">");
			searchCondDef.setEnddatetype(Integer.valueOf(endDateType));
		}
		searchCondDef.setGroupdefaultsearch(0);
		if("TimeSel".equals(groupcode)){
			searchCondDef.setSearchtype(TimeSel_Type);
			searchCondDef.setSearchsubtype(TimeSel_SubType);
			String isvalueLimit=request.getParameter("isvalueLimit");
			searchCondDef.setChnname("时间范围");
			searchCondDef.setSearchcode("timeRange");
			searchCondDef.setIshidden(0);
			//限制单位和数量  TimeScope、DataScope、TimeCycle
			if("1".equals(isvalueLimit)){
				searchCondDef.setIsvaluelimit(1);
				String valueLimit=request.getParameter("valueLimit");
				String TimeSel_valueLimit=request.getParameter("TimeSel_valueLimit");
				searchCondDef.setValuelimit(valueLimit);
				searchCondDef.setValuelimitunit(TimeSel_valueLimit);
			}else{
				searchCondDef.setIsvaluelimit(0);
			}
			//结束日期类型和结果
			
			
			if("TimeScope".equals(TimeSel_Type)){
				//旬
				if("yyyyMMXU".equals(TimeSel_SubType)){
					String xunChose=request.getParameter("xunChose");
					//旬值的默认
					searchCondDef.setSearchattach(xunChose);
					String startTime2=request.getParameter("startTime2");
					String xunStyle1=request.getParameter("xunStyle1");
					String endTime2=request.getParameter("endTime2");
					String xunStyle2=request.getParameter("xunStyle2");
					if("SEQ".equals(xunChose)){
						if("1".equals(xunStyle1)){
							xunStyle1="01";
						}else if("2".equals(xunStyle1)){
							xunStyle1="02";
						}else if("3".equals(xunStyle1)){
							xunStyle1="03";
						}
						if("1".equals(xunStyle2)){
							xunStyle2="01";
						}else if("2".equals(xunStyle2)){
							xunStyle2="02";
						}else if("3".equals(xunStyle2)){
							xunStyle2="03";
						}
						searchCondDef.setDefaultvalue("<CurrMonth>"+(startTime2==null||"".equals(startTime2) ? "":"-"+startTime2)+","+xunStyle1+";<CurrMonth>"+(endTime2==null||"".equals(endTime2) ? "":"-"+endTime2)+","+xunStyle2);
					}else if("DIV".equals(xunChose)){
						if("1".equals(xunStyle1)){
							xunStyle1="01";
						}else if("2".equals(xunStyle1)){
							xunStyle1="11";
						}else if("3".equals(xunStyle1)){
							xunStyle1="21";
						}
						if("1".equals(xunStyle2)){
							xunStyle2="01";
						}else if("2".equals(xunStyle2)){
							xunStyle2="11";
						}else if("3".equals(xunStyle2)){
							xunStyle2="21";
						}
						searchCondDef.setDefaultvalue("<CurrMonth>"+(startTime2==null||"".equals(startTime2) ? "":"-"+startTime2)+","+xunStyle1+";<CurrMonth>"+(endTime2==null||"".equals(endTime2) ? "":"-"+endTime2)+","+xunStyle2);
					}
					
				}else if("yyyyMMHU".equals(TimeSel_SubType)){//候
					
				}else{
					String defaultValue=request.getParameter("defaultValue");
					String startTime=request.getParameter("startTime");
					String endTime=request.getParameter("endTime");
					if("CurrDate".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrDate>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrDate>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}else if("CurrHour".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrHour>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrHour>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}else if("CurrMonth".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrMonth>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrMonth>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}else if("CurrYear".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrYear>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrYear>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}
				}
			}else if("TimeCycle".equals(TimeSel_Type)){
				searchCondDef.setChnname("历年同期");
				searchCondDef.setSearchtype("TimeCycle");
				searchCondDef.setSearchsubtype("yyyy-MMdd");
				searchCondDef.setSearchcode("minYear;maxYear;minMD;maxMD");
				String startTime=request.getParameter("startTime4");
				String endTime=request.getParameter("endTime4");
				//默认月日
				String startTime44=request.getParameter("startTime44");
				String endTime44=request.getParameter("endTime44");
				String defaultvalue=("<CurrYear>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrYear>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", "");
				defaultvalue=defaultvalue+";"+startTime44+";"+endTime44;
				searchCondDef.setDefaultvalue(defaultvalue);
			}else if("DataScope".equals(TimeSel_Type)){
				searchCondDef.setChnname("日序选择");
				searchCondDef.setSearchtype("DataScope");
				searchCondDef.setSearchcode("dayRangeOfYear");
				String minValue=request.getParameter("minValue");
				String maxValue=request.getParameter("maxValue");
				searchCondDef.setMinvalue(Integer.valueOf(minValue));
				searchCondDef.setMaxvalue(Integer.valueOf(maxValue));
				String defaultValue2=request.getParameter("defaultValue2");
				searchCondDef.setDefaultvalue(defaultValue2);
			}if("ComConfig".equals(TimeSel_Type)){
				if("MonthSpan".equals(TimeSel_SubType)){
					searchCondDef.setChnname("月序选择");
					searchCondDef.setSearchattach("MonthSpan");
					searchCondDef.setSearchcode("monsOfYear");
				}else if("TenDaySpan".equals(TimeSel_SubType)){
					searchCondDef.setChnname("旬序选择");
					searchCondDef.setSearchattach("TenDaySpan");
					searchCondDef.setSearchcode("tensOfYear");
				}else if("FiveDaySpan".equals(TimeSel_SubType)){
					searchCondDef.setChnname("候序选择");
					searchCondDef.setSearchattach("FiveDaySpan");
					searchCondDef.setSearchcode("pensOfYear");
				}
				searchCondDef.setSearchsubtype("");
				searchCondDef.setSearchtype("ComConfig");
				String minValue=request.getParameter("minValue");
				String maxValue=request.getParameter("maxValue");
				searchCondDef.setMinvalue(Integer.valueOf(minValue));
				searchCondDef.setMaxvalue(Integer.valueOf(maxValue));
				String defaultValue2=request.getParameter("defaultValue2");
				searchCondDef.setDefaultvalue(defaultValue2);
			}else{
				
			}
			
			
		}else if("StationSel".equals(groupcode)){
			String StationSel=request.getParameter("StationSel");
			if("ALLSel".equals(StationSel)){
				searchCondDef.setChnname("所有台站");
				searchCondDef.setSearchtype("ALLSel");
				searchCondDef.setSearchsubtype("Station");
				searchCondDef.setIshidden(0);
				searchCondDef.setIsvaluelimit(0);
			}else if("StationLevel".equals(StationSel)){
				searchCondDef.setSearchtype("StationLevel");
				//通用或者隐藏
				String StationSel_ALLSel=request.getParameter("StationSel_ALLSel");
				searchCondDef.setSearchcode(StationSel_ALLSel);
				if("eleValueRanges".equals(StationSel_ALLSel)){
					searchCondDef.setSearchsubcode("Station_levl");
				}else if("staLevels".equals(StationSel_ALLSel)){
					
				}
				String StationSel_IsHidden=request.getParameter("StationSel_IsHidden");
				searchCondDef.setIshidden(Integer.valueOf(StationSel_IsHidden));
				searchCondDef.setChnname("台站级别");
				searchCondDef.setSearchtype("StationLevel");
				searchCondDef.setDatasourcetype("ComConfig");
				searchCondDef.setSearchattach("SurfStationLevel");
				//选择方式
				String StationLevel_SM=request.getParameter("StationLevel_SM");
				searchCondDef.setSearchsubtype(StationLevel_SM);
				searchCondDef.setIsvaluelimit(0);
			}else if("StationList".equals(StationSel)){
				searchCondDef.setSearchtype("StationList");
				//通用或者隐藏
				String StationSel_StationList=request.getParameter("StationSel_StationList");
				//默认台站
				String StationList_defaultvalue=request.getParameter("StationList_defaultvalue");
				searchCondDef.setDefaultvalue(StationList_defaultvalue);
				if("eleValueRanges".equals(StationSel_StationList)){
					searchCondDef.setSearchsubcode("Station_Id_C");
				}else if("staLevels".equals(StationSel_StationList)){
					searchCondDef.setSearchsubcode("");
				}
				searchCondDef.setSearchcode(StationSel_StationList);
				searchCondDef.setChnname("台站列表");
				//g台站名称过滤
				searchCondDef.setSearchtype("StationList");
				String dataSourceType=request.getParameter("dataSourceType");
				if("0".equals(dataSourceType)){
					searchCondDef.setDatasourcetype("DMD_STATION_AUTO_EXAM");
				}
				//列表类型选择
				String StationList_SM=request.getParameter("StationList_SM");
				searchCondDef.setSearchsubtype(StationList_SM);
				searchCondDef.setIsvaluelimit(0);
				searchCondDef.setIshidden(0);
			}else if("StationGIS".equals(StationSel)){
				searchCondDef.setSearchtype("StationGIS");
				//通用或者隐藏
				searchCondDef.setSearchcode("staIds");
				searchCondDef.setChnname("地图选站");
				searchCondDef.setSearchtype("StationGIS");
				searchCondDef.setIshidden(0);
				searchCondDef.setIsvaluelimit(0);
				String dataSourceType=request.getParameter("dataSourceType");
				if("0".equals(dataSourceType)){
					searchCondDef.setDatasourcetype("DMD_STATION_AUTO_EXAM");
				}
				//StationGIS类型选择
				String StationGIS_SM=request.getParameter("StationGIS_SM");
				searchCondDef.setSearchsubtype(StationGIS_SM);
				String isvalueLimit=request.getParameter("isvalueLimit");
				searchCondDef.setIsvaluelimit(Integer.valueOf(isvalueLimit));
				if("1".equals(isvalueLimit)){
					String valueLimit=request.getParameter("valueLimit");
					String TimeSel_valueLimit=request.getParameter("TimeSel_valueLimit");
					searchCondDef.setValuelimit(valueLimit);
					searchCondDef.setValuelimitunit(TimeSel_valueLimit);
				}
			}else if("Region".equals(StationSel)){
				searchCondDef.setSearchtype("Region");
				//通用或者隐藏
				String StationSel_Region=request.getParameter("StationSel_Region");
				searchCondDef.setSearchcode(StationSel_Region);
				if("eleValueRanges".equals(StationSel_Region)){
					searchCondDef.setSearchsubcode("Admin_Code_CHN");
				}else if("adminCodes".equals(StationSel_Region)){
					
				}
				//台站级别过滤
				String searchSubType_Region=request.getParameter("searchSubType_Region");
				searchCondDef.setSearchsubtype(searchSubType_Region);
				searchCondDef.setChnname("行政区划");
				searchCondDef.setIshidden(0);
				searchCondDef.setIsvaluelimit(0);
			}else if("Spatial".equals(StationSel)){
				searchCondDef.setSearchtype("Spatial");
				//通用或者隐藏
				String StationSel_Spatial=request.getParameter("StationSel_Spatial");
				if("eleValueRanges".equals(StationSel_Spatial)){
					searchCondDef.setSearchsubcode("Lat;Lon");
				}
				searchCondDef.setSearchcode(StationSel_Spatial);
				searchCondDef.setChnname("地图范围");
				String searchSubType_Spatial=request.getParameter("searchSubType_Spatial");
				if("FM".equals(searchSubType_Spatial)){
					searchCondDef.setAttachsearchcode("staLevels");
				}else if("FS".equals(searchSubType_Spatial)){
					searchCondDef.setAttachsearchcode("staLevels");
				}else if("XX".equals(searchSubType_Spatial)){
					searchCondDef.setAttachsearchcode("");
				}
				searchCondDef.setSearchsubtype(searchSubType_Spatial);
				searchCondDef.setIshidden(0);
				searchCondDef.setIsvaluelimit(0);
			}else if("StationScope".equals(StationSel)){
				searchCondDef.setSearchtype("StationScope");
				searchCondDef.setChnname("台站范围");
				searchCondDef.setSearchcode("minStaId;maxStaId");
				searchCondDef.setSearchsubtype("XX");
				searchCondDef.setIshidden(0);
				searchCondDef.setIsvaluelimit(0);
			}
			
		}else if("ElementSel".equals(groupcode)){
			String ElementSel=request.getParameter("ElementSel");
			searchCondDef.setSearchcode("elements");
			searchCondDef.setIshidden(0);
			if("ALLSel".equals(ElementSel)){
				searchCondDef.setSearchtype("ALLSel");
				searchCondDef.setChnname("所有要素");
				searchCondDef.setSearchsubtype("Element");
			}else if("Element".equals(ElementSel)){
				searchCondDef.setSearchtype("Element");
				searchCondDef.setChnname("要素选择");
				searchCondDef.setSearchsubtype("M");
			}
			searchCondDef.setIsvaluelimit(0);
		}else if("ElementFilter".equals(groupcode)){
			String ElementFilter=request.getParameter("ElementFilter");
			searchCondDef.setIshidden(0);
			if("ALLSel".equals(ElementFilter)){
				searchCondDef.setSearchtype("ALLSel");
				searchCondDef.setChnname("不过滤");
				searchCondDef.setSearchsubtype("EleFilter");
			}else if("EleFilter".equals(ElementFilter)){
				searchCondDef.setSearchcode("eleValueRanges");
				searchCondDef.setSearchtype("EleFilter");
				searchCondDef.setChnname("要素过滤");
			}
			searchCondDef.setIsvaluelimit(0);
		}else if("FormatSel".equals(groupcode)){
			String FormatSel_chname=request.getParameter("FormatSel_chname");
			if(FormatSel_chname!=null&&!"".equals(FormatSel_chname)){
				searchCondDef.setChnname(FormatSel_chname);
			}else{
				searchCondDef.setChnname("格式选择");
			}
			searchCondDef.setIshidden(0);
			searchCondDef.setSearchtype("DataFormat");
			searchCondDef.setDatasourcetype("ComConfig");
			searchCondDef.setSearchattach("SurfDataFormat");
			searchCondDef.setDefaultvalue("Text/,");
			searchCondDef.setOptiondefault("Text/,");
			searchCondDef.setIsvaluelimit(0);
		}else if("QCSel".equals(groupcode)){
			String QCSel_chname=request.getParameter("QCSel_chname");
			if(QCSel_chname!=null&&!"".equals(QCSel_chname)){
				searchCondDef.setChnname(QCSel_chname);
			}else{
				searchCondDef.setChnname("质量级别");
			}
			searchCondDef.setIshidden(0);
			searchCondDef.setSearchtype("QCSel");
			searchCondDef.setDatasourcetype("ComConfig");
			searchCondDef.setSearchattach("SurfQCSel");
			searchCondDef.setIsvaluelimit(0);
			searchCondDef.setDefaultvalue("0");
		}else{
			String ComConfig_chname=request.getParameter("ComConfig_chname");
			String ComConfig_subCode=request.getParameter("ComConfig_subCode");
			String ComConfig_SM=request.getParameter("ComConfig_SM");
			String ComConfig=request.getParameter("ComConfig");
			if("ALLSel".equals(ComConfig)){
				searchCondDef.setSearchtype("ALLSel");
			}else if("ComConfig".equals(ComConfig)){
				searchCondDef.setSearchtype("ComConfig");
				searchCondDef.setSearchsubcode(ComConfig_subCode);
				searchCondDef.setSearchsubtype(ComConfig_SM);
				searchCondDef.setSearchcode("eleValueRanges");
			}
			searchCondDef.setIshidden(0);
			searchCondDef.setChnname(ComConfig_chname);
			searchCondDef.setIsvaluelimit(0);
		}
		Map map=new HashMap();
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			map.put("status", 0);
			
		}catch (Exception e) {
			map.put("status", 1);
		}
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
		
	}
	/**
	 * ajax保存成功
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveUpdateSearchSetDef")
	public void saveUpdateSearchSetDef(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		//类型
		String id=request.getParameter("id");
		SearchCondDef searchCondDef =searchCondDefService.getSearchCondDefById(Integer.valueOf(id));
		String groupcode=request.getParameter("groupcode");
		//时间范围
		String TimeSel_Type=request.getParameter("TimeSel_Type");
		//时间类型
		String TimeSel_SubType=request.getParameter("TimeSel_SubType");
		String endDateType=request.getParameter("endDateType");
		String endDate=request.getParameter("endDate");
		String GroupDefaultSearch=request.getParameter("GroupDefaultSearch");
		if(GroupDefaultSearch==null||"".equals(GroupDefaultSearch)){
			searchCondDef.setGroupdefaultsearch(0);
		}else{
			searchCondDef.setGroupdefaultsearch(Integer.valueOf(GroupDefaultSearch));
		}
		if("0".equals(endDateType)||endDateType==null||"".equals(endDateType)){
			searchCondDef.setEnddatetype(0);
		}else{
			searchCondDef.setEnddate("<"+endDate+">");
			searchCondDef.setEnddatetype(Integer.valueOf(endDateType));
		}
		searchCondDef.setGroupdefaultsearch(0);
		if("TimeSel".equals(groupcode)){
			searchCondDef.setSearchtype(TimeSel_Type);
			searchCondDef.setSearchsubtype(TimeSel_SubType);
			String isvalueLimit=request.getParameter("isvalueLimit");
			searchCondDef.setChnname("时间范围");
			searchCondDef.setSearchcode("timeRange");
			searchCondDef.setIshidden(0);
			//限制单位和数量  TimeScope、DataScope、TimeCycle
			if("1".equals(isvalueLimit)){
				searchCondDef.setIsvaluelimit(1);
				String valueLimit=request.getParameter("valueLimit");
				String TimeSel_valueLimit=request.getParameter("TimeSel_valueLimit");
				searchCondDef.setValuelimit(valueLimit);
				searchCondDef.setValuelimitunit(TimeSel_valueLimit);
			}else{
				searchCondDef.setIsvaluelimit(0);
			}
			//结束日期类型和结果
			
			
			if("TimeScope".equals(TimeSel_Type)){
				//旬
				if("yyyyMMXU".equals(TimeSel_SubType)){
					String xunChose=request.getParameter("xunChose");
					//旬值的默认
					searchCondDef.setSearchattach(xunChose);
					String startTime2=request.getParameter("startTime2");
					String xunStyle1=request.getParameter("xunStyle1");
					String endTime2=request.getParameter("endTime2");
					String xunStyle2=request.getParameter("xunStyle2");
					if("SEQ".equals(xunChose)){
						if("1".equals(xunStyle1)){
							xunStyle1="01";
						}else if("2".equals(xunStyle1)){
							xunStyle1="02";
						}else if("3".equals(xunStyle1)){
							xunStyle1="03";
						}
						if("1".equals(xunStyle2)){
							xunStyle2="01";
						}else if("2".equals(xunStyle2)){
							xunStyle2="02";
						}else if("3".equals(xunStyle2)){
							xunStyle2="03";
						}
						searchCondDef.setDefaultvalue("<CurrMonth>"+(startTime2==null||"".equals(startTime2) ? "":"-"+startTime2)+","+xunStyle1+";<CurrMonth>"+(endTime2==null||"".equals(endTime2) ? "":"-"+endTime2)+","+xunStyle2);
					}else if("DIV".equals(xunChose)){
						if("1".equals(xunStyle1)){
							xunStyle1="01";
						}else if("2".equals(xunStyle1)){
							xunStyle1="11";
						}else if("3".equals(xunStyle1)){
							xunStyle1="21";
						}
						if("1".equals(xunStyle2)){
							xunStyle2="01";
						}else if("2".equals(xunStyle2)){
							xunStyle2="11";
						}else if("3".equals(xunStyle2)){
							xunStyle2="21";
						}
						searchCondDef.setDefaultvalue("<CurrMonth>"+(startTime2==null||"".equals(startTime2) ? "":"-"+startTime2)+","+xunStyle1+";<CurrMonth>"+(endTime2==null||"".equals(endTime2) ? "":"-"+endTime2)+","+xunStyle2);
					}
					
				}else if("yyyyMMHU".equals(TimeSel_SubType)){//候
					
				}else{
					String defaultValue=request.getParameter("defaultValue");
					String startTime=request.getParameter("startTime");
					String endTime=request.getParameter("endTime");
					if("CurrDate".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrDate>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrDate>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}else if("CurrHour".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrHour>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrHour>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}else if("CurrMonth".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrMonth>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrMonth>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}else if("CurrYear".equals(defaultValue)){
						searchCondDef.setDefaultvalue(("<CurrYear>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrYear>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", ""));
					}
					
				}
				
			}else if("TimeCycle".equals(TimeSel_Type)){
				searchCondDef.setChnname("历年同期");
				searchCondDef.setSearchtype("TimeCycle");
				searchCondDef.setSearchsubtype("yyyy-MMdd");
				searchCondDef.setSearchcode("minYear;maxYear;minMD;maxMD");
				String startTime=request.getParameter("startTime4");
				String endTime=request.getParameter("endTime4");
				//默认月日
				String startTime44=request.getParameter("startTime44");
				String endTime44=request.getParameter("endTime44");
				String defaultvalue=("<CurrYear>"+(startTime==null||"".equals(startTime) ? "":"-"+startTime)+";<CurrYear>"+(endTime==null||"".equals(endTime) ? "":"-"+endTime)).replace("-0", "");
				defaultvalue=defaultvalue+";"+startTime44+";"+endTime44;
				searchCondDef.setDefaultvalue(defaultvalue);
			}else if("DataScope".equals(TimeSel_Type)){
				searchCondDef.setChnname("日序选择");
				searchCondDef.setSearchtype("DataScope");
				searchCondDef.setSearchcode("dayRangeOfYear");
				String minValue=request.getParameter("minValue");
				String maxValue=request.getParameter("maxValue");
				searchCondDef.setMinvalue(Integer.valueOf(minValue));
				searchCondDef.setMaxvalue(Integer.valueOf(maxValue));
				String defaultValue2=request.getParameter("defaultValue2");
				searchCondDef.setDefaultvalue(defaultValue2);
			}if("ComConfig".equals(TimeSel_Type)){
				if("MonthSpan".equals(TimeSel_SubType)){
					searchCondDef.setChnname("月序选择");
					searchCondDef.setSearchattach("MonthSpan");
					searchCondDef.setSearchcode("monsOfYear");
				}else if("TenDaySpan".equals(TimeSel_SubType)){
					searchCondDef.setChnname("旬序选择");
					searchCondDef.setSearchattach("TenDaySpan");
					searchCondDef.setSearchcode("tensOfYear");
				}else if("FiveDaySpan".equals(TimeSel_SubType)){
					searchCondDef.setChnname("候序选择");
					searchCondDef.setSearchattach("FiveDaySpan");
					searchCondDef.setSearchcode("pensOfYear");
				}
				searchCondDef.setSearchsubtype("");
				String minValue=request.getParameter("minValue");
				String maxValue=request.getParameter("maxValue");
				searchCondDef.setMinvalue(Integer.valueOf(minValue));
				searchCondDef.setMaxvalue(Integer.valueOf(maxValue));
				String defaultValue2=request.getParameter("defaultValue2");
				searchCondDef.setDefaultvalue(defaultValue2);
				searchCondDef.setSearchtype("ComConfig");
			}else{
				
			}
			
			
		}else if("StationSel".equals(groupcode)){
			String StationSel=request.getParameter("StationSel");
			if("ALLSel".equals(StationSel)){
				searchCondDef.setChnname("所有台站");
				searchCondDef.setSearchtype("ALLSel");
				searchCondDef.setSearchsubtype("Station");
				searchCondDef.setIshidden(0);
			}else if("StationLevel".equals(StationSel)){
				searchCondDef.setSearchtype("StationLevel");
				//通用或者隐藏
				String StationSel_ALLSel=request.getParameter("StationSel_ALLSel");
				searchCondDef.setSearchcode(StationSel_ALLSel);
				if("eleValueRanges".equals(StationSel_ALLSel)){
					searchCondDef.setSearchsubcode("Station_levl");
				}else if("staLevels".equals(StationSel_ALLSel)){
					searchCondDef.setSearchsubcode("");
				}
				String StationSel_IsHidden=request.getParameter("StationSel_IsHidden");
				searchCondDef.setIshidden(Integer.valueOf(StationSel_IsHidden));
				searchCondDef.setChnname("台站级别");
				searchCondDef.setSearchtype("StationLevel");
				searchCondDef.setDatasourcetype("ComConfig");
				searchCondDef.setSearchattach("SurfStationLevel");
				//选择方式
				String StationLevel_SM=request.getParameter("StationLevel_SM");
				searchCondDef.setSearchsubtype(StationLevel_SM);
			}else if("StationList".equals(StationSel)){
				searchCondDef.setSearchtype("StationList");
				//通用或者隐藏
				String StationSel_StationList=request.getParameter("StationSel_StationList");
				if("eleValueRanges".equals(StationSel_StationList)){
					searchCondDef.setSearchsubcode("Station_Id_C");
				}else if("staLevels".equals(StationSel_StationList)){
					searchCondDef.setSearchsubcode("");
				}
				String StationList_defaultvalue=request.getParameter("StationList_defaultvalue");
				searchCondDef.setDefaultvalue(StationList_defaultvalue);
				searchCondDef.setSearchcode(StationSel_StationList);
				searchCondDef.setChnname("台站列表");
				//g台站名称过滤
				searchCondDef.setSearchtype("StationList");
				String dataSourceType=request.getParameter("dataSourceType");
				if("0".equals(dataSourceType)){
					searchCondDef.setDatasourcetype("DMD_STATION_AUTO_EXAM");
				}
				//列表类型选择
				String StationList_SM=request.getParameter("StationList_SM");
				searchCondDef.setSearchsubtype(StationList_SM);
			}else if("StationGIS".equals(StationSel)){
				searchCondDef.setSearchtype("StationGIS");
				//通用或者隐藏
				searchCondDef.setSearchcode("staIds");
				searchCondDef.setChnname("地图选站");
				searchCondDef.setSearchtype("StationGIS");
				searchCondDef.setIshidden(0);
				searchCondDef.setIsvaluelimit(0);
				String dataSourceType=request.getParameter("dataSourceType");
				if("0".equals(dataSourceType)){
					searchCondDef.setDatasourcetype("DMD_STATION_AUTO_EXAM");
				}
				//StationGIS类型选择
				String StationGIS_SM=request.getParameter("StationGIS_SM");
				String isvalueLimit=request.getParameter("isvalueLimit");
				searchCondDef.setIsvaluelimit(Integer.valueOf(isvalueLimit));
				if("1".equals(isvalueLimit)){
					String valueLimit=request.getParameter("valueLimit");
					String TimeSel_valueLimit=request.getParameter("TimeSel_valueLimit");
					searchCondDef.setValuelimit(valueLimit);
					searchCondDef.setValuelimitunit(TimeSel_valueLimit);
				}
				searchCondDef.setSearchsubtype(StationGIS_SM);
			}else if("Region".equals(StationSel)){
				searchCondDef.setSearchtype("Region");
				//通用或者隐藏
				String StationSel_Region=request.getParameter("StationSel_Region");
				searchCondDef.setSearchcode(StationSel_Region);
				if("eleValueRanges".equals(StationSel_Region)){
					searchCondDef.setSearchsubcode("Admin_Code_CHN");
				}else if("adminCodes".equals(StationSel_Region)){
					
				}
				//台站级别过滤
				String searchSubType_Region=request.getParameter("searchSubType_Region");
				searchCondDef.setSearchsubtype(searchSubType_Region);
				searchCondDef.setChnname("行政区划");
				searchCondDef.setIshidden(0);
			}else if("Spatial".equals(StationSel)){
				searchCondDef.setSearchtype("Spatial");
				//通用或者隐藏
				String StationSel_Spatial=request.getParameter("StationSel_Spatial");
				if("eleValueRanges".equals(StationSel_Spatial)){
					searchCondDef.setSearchsubcode("Lat;Lon");
				}
				searchCondDef.setSearchcode(StationSel_Spatial);
				searchCondDef.setChnname("地图范围");
				String searchSubType_Spatial=request.getParameter("searchSubType_Spatial");
				if("FM".equals(searchSubType_Spatial)){
					searchCondDef.setAttachsearchcode("staLevels");
				}else if("FS".equals(searchSubType_Spatial)){
					searchCondDef.setAttachsearchcode("staLevels");
				}else if("XX".equals(searchSubType_Spatial)){
					searchCondDef.setAttachsearchcode("");
				}
				searchCondDef.setSearchsubtype(searchSubType_Spatial);
				searchCondDef.setIshidden(0);
			}else if("StationScope".equals(StationSel)){
				searchCondDef.setSearchtype("StationScope");
				searchCondDef.setChnname("台站范围");
				searchCondDef.setSearchcode("minStaId;maxStaId");
				searchCondDef.setSearchsubtype("XX");
				searchCondDef.setIshidden(0);
			}
			
		}else if("ElementSel".equals(groupcode)){
			String ElementSel=request.getParameter("ElementSel");
			searchCondDef.setSearchcode("elements");
			searchCondDef.setIshidden(0);
			if("ALLSel".equals(ElementSel)){
				searchCondDef.setSearchtype("ALLSel");
				searchCondDef.setChnname("所有要素");
				searchCondDef.setSearchsubtype("Element");
			}else if("Element".equals(ElementSel)){
				searchCondDef.setSearchtype("Element");
				searchCondDef.setChnname("要素选择");
				searchCondDef.setSearchsubtype("M");
			}
			searchCondDef.setIsvaluelimit(0);
		}else if("ElementFilter".equals(groupcode)){
			String ElementFilter=request.getParameter("ElementFilter");
			searchCondDef.setIshidden(0);
			if("ALLSel".equals(ElementFilter)){
				searchCondDef.setSearchtype("ALLSel");
				searchCondDef.setChnname("不过滤");
				searchCondDef.setSearchsubtype("EleFilter");
			}else if("EleFilter".equals(ElementFilter)){
				searchCondDef.setSearchcode("eleValueRanges");
				searchCondDef.setSearchtype("EleFilter");
				searchCondDef.setChnname("要素过滤");
			}
			searchCondDef.setIsvaluelimit(0);
		}else if("FormatSel".equals(groupcode)){
			String FormatSel_chname=request.getParameter("FormatSel_chname");
			if(FormatSel_chname!=null&&!"".equals(FormatSel_chname)){
				searchCondDef.setChnname(FormatSel_chname);
			}else{
				searchCondDef.setChnname("格式选择");
			}
			searchCondDef.setIshidden(0);
			searchCondDef.setSearchtype("DataFormat");
			searchCondDef.setDatasourcetype("ComConfig");
			searchCondDef.setSearchattach("SurfDataFormat");
			searchCondDef.setDefaultvalue("Text/,");
			searchCondDef.setOptiondefault("Text/,");
			searchCondDef.setIsvaluelimit(0);
		}else if("QCSel".equals(groupcode)){
			String QCSel_chname=request.getParameter("QCSel_chname");
			if(QCSel_chname!=null&&!"".equals(QCSel_chname)){
				searchCondDef.setChnname(QCSel_chname);
			}else{
				searchCondDef.setChnname("质量级别");
			}
			searchCondDef.setIshidden(0);
			searchCondDef.setSearchtype("DataFormat");
			searchCondDef.setDatasourcetype("ComConfig");
			searchCondDef.setSearchattach("SurfQCSe");
			searchCondDef.setIsvaluelimit(0);
		}else{
			String ComConfig_chname=request.getParameter("ComConfig_chname");
			String ComConfig_subCode=request.getParameter("ComConfig_subCode");
			String ComConfig_SM=request.getParameter("ComConfig_SM");
			searchCondDef.setIshidden(0);
			searchCondDef.setSearchtype("ComConfig");
			searchCondDef.setChnname(ComConfig_chname);
			searchCondDef.setSearchsubcode(ComConfig_subCode);
			searchCondDef.setSearchsubtype(ComConfig_SM);
			searchCondDef.setIsvaluelimit(0);
			searchCondDef.setSearchcode("eleValueRanges");
		}
		Map map=new HashMap();
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			map.put("status", 0);
		}catch (Exception e) {
			map.put("status", 1);
		}
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	/**
	 *排序功能-大小类排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortSearchGroup")
	public String sortSearchGroup(HttpServletRequest request,HttpServletResponse response,Model model,
			String searchsetcode){
		List<SearchSetDef> list=searchSetDefService.getsearchSetDefListById(searchsetcode);
		model.addAttribute("list",list);
		model.addAttribute("searchsetcode",searchsetcode);
		return "modules/data/searchSortList";
		
	}
	/**
	 *排序功能-条件的排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortSearchCondDef")
	public String sortSearchCondDef(HttpServletRequest request,HttpServletResponse response,Model model,
			String GroupCode){
		List<SearchCondDef> list=searchCondDefService.getSearchListGroupCode(GroupCode);
		model.addAttribute("list",list);
		model.addAttribute("GroupCode",GroupCode);
		return "modules/data/searchConSortList";
		
	}
	/**
	 * 批量修改菜单排序  dataDef表
	 */
	@RequestMapping(value = "searchSort")
	public String elementSort(@RequestParam
			Map<String, Object> paramMap,String[] ids, RedirectAttributes redirectAttributes) {
		String searchsetcode=(String)paramMap.get("searchsetcode");
		try {
		int len = ids.length;
		SearchSetDef[] searchSetDef = new SearchSetDef[len];
    	
    		for (int i = 0; i < len; i++) {
    			searchSetDef[i] = searchSetDefService.getsearchSetDefById(Integer.valueOf(ids[i]));
    			searchSetDef[i].setOrderno(i);
    			searchSetDefService.saveSearchSetDef(searchSetDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/dataSearchDef/sortSearchGroup?searchsetcode="+searchsetcode;	
	}
	/**
	 * 批量修改菜单排序  dataDef表
	 */
	@RequestMapping(value = "searchCondSort")
	public String searchCondSort(@RequestParam
			Map<String, Object> paramMap,String[] ids, RedirectAttributes redirectAttributes) {
		String GroupCode=(String)paramMap.get("GroupCode");
		try {
		int len = ids.length;
		SearchCondDef[] searchCondDef = new SearchCondDef[len];
    	
    		for (int i = 0; i < len; i++) {
    			searchCondDef[i] = searchCondDefService.getSearchCondDefById(Integer.valueOf(ids[i]));
    			searchCondDef[i].setOrderno(i);
    			searchCondDefService.saveSearchCondDef(searchCondDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/dataSearchDef/sortSearchCondDef?GroupCode="+GroupCode;	
	}
	
	@RequestMapping(value="/updategroupdefault")
	public String updategroupdefault(HttpServletRequest request,HttpServletResponse response,Model model,
			String searchgroupcode){
		List<SearchCondDef> list=searchCondDefService.getSearchListByGroupCode(searchgroupcode);
		model.addAttribute("list",list);
		model.addAttribute("searchgroupcode",searchgroupcode);
		return "modules/data/searchConChoseList";
		
	}
	@RequestMapping(value="/searchCondChose")
	public String searchCondChose(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model,
			String[] ids){
		String searchgroupcode=(String)paramMap.get("searchgroupcode");
		String cids=(String)paramMap.get("cids");
		if(cids==null||"".equals(cids)){
			try {
				searchCondDefService.updateSearchCondDefById(searchgroupcode);
				model.addAttribute("status",2);
			} catch (Exception e) {
				model.addAttribute("status",3);
			}
		}else{
			try {
				searchCondDefService.updateSearchCondDefById(searchgroupcode);
				String cid[]=cids.split(",");
		    		for (int i = 0; i < cid.length; i++) {
		    			SearchCondDef searchCondDef  = searchCondDefService.getSearchCondDefById(Integer.valueOf(cid[i]));
		    			searchCondDef.setGroupdefaultsearch(1);
		    			searchCondDefService.saveSearchCondDef(searchCondDef);
		        	}
//		        	cacheCleanController.cleanCache();
		        	model.addAttribute("status",2);
		    	} catch (Exception e1) {
					e1.printStackTrace();
					model.addAttribute("status",3);
				}
		}
		return "modules/data/blankClose";
		
	}
	
}






