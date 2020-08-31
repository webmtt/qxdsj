package com.nmpiesat.idata.data.web;

import com.nmpiesat.idata.compara.services.ComparasService;
import com.nmpiesat.idata.data.entity.*;
import com.nmpiesat.idata.data.service.*;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.user.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Api(description = "数据下载")
@Controller
@RequestMapping(value = "/data")
public class DataController {
    @Autowired
    private ComparasService comparasService;
    @Autowired
    private DataCategoryDefService dataCategoryDefService;
    @Autowired
    private DataDefService dataDefService;
    @Autowired
    private CategoryDataReltService categoryDataReltService;
    @Autowired
    private DataReferDefService dataReferDefService;
    @Autowired
    private DataLinksService dataLinksService;

    @ApiOperation(value="数据下载功能导航", notes="导航菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "userid", value = "用户id", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getTree"})
    @ResponseBody
    public RestResult getTree(HttpServletRequest request,HttpServletResponse response){
        RestResult restResult = new RestResult();
        List<DataCategoryDef> categoryList = null;
        String keyword = request.getParameter("keyword");
        String userid=request.getParameter("userid");
//        if ((!"".equals(userid))&&userid != null) {
//            categoryList = dataCategoryDefService.findDataCategoryDefByUid(userid, keyword);
//        } else {
            categoryList = dataCategoryDefService.findDataCategoryDef();
//        }
        List<Map<String ,Object> > resultList=new ArrayList<>();
        Map<String ,Object> resultMapParent=null;
        Map<String ,Object> resultMapChild=null;
        boolean flag=false;
        if(categoryList.size()>0) {
            for(DataCategoryDef dataCategoryDef:categoryList){
                resultMapParent=new HashMap<>();
                resultMapParent.put("id",dataCategoryDef.getCategoryid());
                resultMapParent.put("name",dataCategoryDef.getChnname());
                resultMapParent.put("pid",0);
                flag=false;
                if(!"".equals(keyword)&&keyword!=null){
                    if(dataCategoryDef.getChnname().contains(keyword)) {
                        flag=true;
                        resultList.add(resultMapParent);
                    }
                }else{
                    resultList.add(resultMapParent);
                }
                List<DataDef> dataDefList=dataDefService.findDataDefByCateId(dataCategoryDef.getCategoryid());

                if(dataDefList.size()>0){
                    for(DataDef def:dataDefList){
                        resultMapChild=new HashMap<>();
                        resultMapChild.put("id",def.getDatacode());
                        resultMapChild.put("name",def.getChnname());
                        resultMapChild.put("pid",dataCategoryDef.getCategoryid());
                        if(!"".equals(keyword)&&keyword!=null){
                            if(def.getChnname().contains(keyword)) {
                                if(!flag) {
                                    resultList.add(resultMapParent);
                                    flag=true;
                                }
                                resultList.add(resultMapChild);
                            }
                        }else{
                            resultList.add(resultMapChild);
                        }
                    }
                }

            }
        }

        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(resultList);
        return restResult;
    }
    @RequestMapping(value = {"/getBigData"})
    @ResponseBody
    public RestResult getBigData(HttpServletRequest request,HttpServletResponse response){
        RestResult restResult = new RestResult();
        List<DataCategoryDef> categoryList = null;
        categoryList = dataCategoryDefService.findDataCategoryDef();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(categoryList);
        return restResult;
    }
    @ResponseBody
    @RequestMapping(value = {"/getSmallData"})
    public RestResult getSmallData(String name){
        RestResult restResult = new RestResult();
        List<DataDef> dataDefList=dataDefService.findDataDefByCateName(name);

        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(dataDefList);
        return restResult;
    }
    @ApiOperation(value="导航一级菜单", notes="查询子产品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资料id", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "userid", value = "用户id", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/firInfo"})
    @ResponseBody
    public RestResult firInfo(
            HttpServletRequest request, HttpServletResponse response) {
        RestResult restResult = new RestResult();
        int id=Integer.parseInt(request.getParameter("id"));
        DataCategoryDef dataCategoryDef = null;
        String userid=request.getParameter("userid");
        if ((!"".equals(userid))&&userid != null) {
            dataCategoryDef = dataCategoryDefService.findDataCategoryDefByUserId(userid, id);
        } else {
            dataCategoryDef = dataCategoryDefService.findDataCategoryDefById(id);
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(dataCategoryDef);
        return restResult;
    }
    @ApiOperation(value="导航二级菜单", notes="查询资料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资料id", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "userid", value = "用户id", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/secInfo"})
    @ResponseBody
    public RestResult secInfo(
            HttpServletRequest request, HttpServletResponse response,String dataCode) {
//        int id=Integer.parseInt(request.getParameter("id"));
//        int pageNum=Integer.parseInt(request.getParameter("pageNum"));
//        RestResult restResult = new RestResult();
//        DataCategoryDef dataCategoryDef = dataCategoryDefService.findDataCategoryDefByIdUnique(id);
//        List<CategoryDataRelt> list = null;
//        String userid=request.getParameter("userid");
//        if ((!"".equals(userid))&&userid != null) {
//            list = categoryDataReltService.findCategoryDataReltByUserId(userid, id);
//        } else {
//            list = categoryDataReltService.findCategoryDataReltById(id);
//        }
//        Map map = dataDefService.findDataDefByCodes(list, pageNum);
//        // music接口
//        String mUSICLinkUrl = (String) comparasService.getComparasByKey("MUSICLinkUrl");
//        List<DataDef> dataDefList = (List<DataDef>) map.get("dataDefList");
//        int maxPage = (Integer) map.get("maxPage");
//        int countInt = (Integer) map.get("countInt");
        DataDef dataDef = dataDefService.findDataDefByDataCode(dataCode);
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(dataDef);
        return restResult;
    }
    @ApiOperation(value="产品详细信息", notes="产品详细信息")
    @RequestMapping(value = {"/detail"})
    @ResponseBody
    public RestResult detail(
            HttpServletRequest request, HttpServletResponse response, String dataCode) {
        RestResult restResult = new RestResult();
        // music接口
        String mUSICLinkUrl = (String) comparasService.getComparasByKey("MUSICLinkUrl");
        DataDef dataDef = dataDefService.findDataDefByDataCode(dataCode);
        List<DataReferDef> dataReferList = dataReferDefService.findDataReferDefByDataCode(dataCode);
        List<DataLinks> dataLinksList = dataLinksService.findDataLinksByDataCode(dataCode);
        List<DataLinks> dataLinksList0 = new ArrayList();
        List<DataLinks> dataLinksList1 = new ArrayList();
        List<DataLinks> dataLinksList2 = new ArrayList();
        List<DataLinks> dataLinksList3 = new ArrayList();
        List<DataLinks> dataLinksList4 = new ArrayList();
        List<DataLinks> dataLinksList5 = new ArrayList();
        List<DataLinks> dataLinksList6 = new ArrayList();
        for (DataLinks dataLinks : dataLinksList) {
            if (dataLinks.getLinktype() == 0) {
                dataLinksList0.add(dataLinks);
            } else if (dataLinks.getLinktype() == 1) {
                dataLinksList1.add(dataLinks);
            } else if (dataLinks.getLinktype() == 2) {
                dataLinksList2.add(dataLinks);
            } else if (dataLinks.getLinktype() == 3) {
                dataLinksList3.add(dataLinks);
            } else if (dataLinks.getLinktype() == 4) {
                dataLinksList4.add(dataLinks);
            } else if (dataLinks.getLinktype() == 5) {
                dataLinksList5.add(dataLinks);
            } else if (dataLinks.getLinktype() == 6) {
                dataLinksList6.add(dataLinks);
            }
        }
        Map<String,Object> data=new HashMap<String,Object>();
        data.put("dataDef", dataDef);
        data.put("dataReferList", dataReferList);
        data.put("dataLinksList0", dataLinksList0);
        data.put("dataLinksList1", dataLinksList1);
        data.put("dataLinksList2", dataLinksList2);
        data.put("dataLinksList3", dataLinksList3);
        data.put("dataLinksList4", dataLinksList4);
        data.put("dataLinksList5", dataLinksList5);
        data.put("dataLinksList6", dataLinksList6);
        data.put("mUSICLinkUrl", mUSICLinkUrl);
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }
    /**
     * 首页数据服务信息
     */
    @ApiOperation(value="获取数据产品统计信息", notes="数据服务")
    @RequestMapping(value = {"/getDataSortCount"})
    @ResponseBody
    public RestResult getDataSortCount(
            HttpServletRequest request, HttpServletResponse response, String dataCode) {
        RestResult restResult = new RestResult();
        List<DataCategoryDef> list=dataCategoryDefService.getNewDataSortCount();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(list);
        return restResult;
    }
}
