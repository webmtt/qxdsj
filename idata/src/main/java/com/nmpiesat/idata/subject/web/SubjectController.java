package com.nmpiesat.idata.subject.web;

import com.google.common.collect.Lists;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.subject.dao.SubjectDao;
import com.nmpiesat.idata.subject.entity.*;
import com.nmpiesat.idata.subject.service.DSAccessDefService;
import com.nmpiesat.idata.subject.service.PortalImageProDefService;
import com.nmpiesat.idata.subject.service.SupSubjectdefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * 专题产品展示
 * @author yangkq
 * @date 2020.02.27
 */
@Api(description = "专题产品展示")
@Controller
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    private DSAccessDefService dSAccessDefService;
    @Autowired
    private PortalImageProDefService portalImageProDefService;
    @Autowired
    private SupSubjectdefService supSubjectdefService;
    @Autowired
    private Environment environment;

    @Autowired
    private SubjectDao dao;
    private String[] type =
            new String[]{"routine", "Typhoon", "Rainstorm", "HighTemperature", "Smog", "frozen"};
    private String[] name = new String[]{"常规", "台风", "暴雨", "高温", "雾霾", "冰冻"};
    /**
     * 获取图片服务器地址
     *
     */
    @ApiOperation(value="获取图片服务器地址", notes="获取图片服务器功能")
    @RequestMapping(value = "/getPNGSourceUrl")
    @ResponseBody
    public RestResult getPNGSourceUrl() {
        String staticUrl=environment.getProperty("basepath");
        RestResult restResult=new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(staticUrl);
        return restResult;
    }

    /**
     * 获取专题产品-old
     *
     * @param request
     * @param response
     */
//    @RequestMapping(value = "/getImportProduct")
    @ResponseBody
    public RestResult getImportProduct(
            HttpServletRequest request, HttpServletResponse response) {
        // 获取music接口参数
        DSAccessDef dSAccessDef = dSAccessDefService.getDSAccessDef("CIMISS-WS-REST-JSON");
        List<PortalImageProDefModel> list =
                portalImageProDefService.getPortalImageProDefList(dSAccessDef.getAccessURL());
        // 有效的规则list
        List<PortalImageRull> listq = dao.getPortalImageRullListByType();
        Map<String, Object> map = new HashMap<String, Object>();
        // 类型的list集合
        List typelist = new ArrayList();
        // 名称的list集合
        List namelist = new ArrayList();
        for (int i = 0; i < type.length; i++) {
            List<PortalImageProDefModel> templist = new ArrayList<PortalImageProDefModel>();
            for (int j = 0; j < listq.size(); j++) {
                if (type[i].equals(listq.get(j).getType())) {
                    int tid = listq.get(j).getTid();
                    String id = listq.get(j).getId();
                    for (int x = 0; x < list.size(); x++) {
                        if (list.get(x).getTempid().equals(id + String.valueOf(tid))) {
                            templist.add(list.get(x));
                            break;
                        }
                    }
                }
            }
            if (templist.size() == 3) {
                typelist.add(type[i]);
                namelist.add(name[i]);
                map.put(type[i], templist);
            }
        }
        RestResult restResult = new RestResult();
        // 一个集合，具体资料的map集合
        Map<String, Object> mapall = new HashMap<String, Object>();
        mapall.put("namelist", namelist);
        mapall.put("typelist", typelist);
        mapall.put("type", map);
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(mapall);
        return restResult;
    }

    /**
     * 获取专题产品
     *
     * @param request
     * @param response
     */
    @ApiOperation(value="专题产品主页信息", notes="专题产品功能")
    @RequestMapping(value = "/getIndexProduct")
    @ResponseBody
    public RestResult getProduct(
            HttpServletRequest request, HttpServletResponse response) {
        //当前页码
        String pageNum=request.getParameter("pageNum");
        String userid=request.getParameter("userid");
        Map<String,Object> data = supSubjectdefService.findAllConfig(pageNum,userid);
        RestResult restResult=new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }
    /**
     * 首页展示专题产品信息
     *
     * @param request
     * @param response
     */
    @ApiOperation(value="首页展示专题产品信息", notes="专题产品功能")
    @RequestMapping(value = "/getHomeProduct")
    @ResponseBody
    public RestResult getHomeProduct(
            HttpServletRequest request, HttpServletResponse response) {
        String userid=request.getParameter("userid");
        String pagenum=request.getParameter("pagenum");
        Map<String,Object> data = supSubjectdefService.getHomeProduct(pagenum,userid);
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }
    /**
     * 首页特色产品信息
     *
     * @param request
     * @param response
     */
    @ApiOperation(value="特色产品信息", notes="专题产品功能")
    @RequestMapping(value = "/getFeaturesProduct")
    @ResponseBody
    public RestResult getFeaturesProduct(
            HttpServletRequest request, HttpServletResponse response) {
        List<SupSubjectdef> sourcelist = supSubjectdefService.getFeaturesProduct();
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(sourcelist);
        return restResult;
    }
    /**
     * 首页热门产品信息
     *
     * @param request
     * @param response
     */
    @ApiOperation(value="首页热门产品信息", notes="专题产品功能")
    @RequestMapping(value = "/getHotProduct")
    @ResponseBody
    public RestResult getHotProduct(
            HttpServletRequest request, HttpServletResponse response) {
        List<SupSubjectdef> sourcelist = supSubjectdefService.getHotProduct();
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(sourcelist);
        return restResult;
    }
    /**
     * 专题子产品信息
     *
     * @param request
     * @param response
     */
    @ApiOperation(value="专题子产品导航栏信息", notes="专题产品功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "产品id", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/getTreeProduct")
    @ResponseBody
    public RestResult getTreeProduct(
            HttpServletRequest request, HttpServletResponse response) {
        String ids=request.getParameter("id");
        String userid=request.getParameter("userid");
        String type=request.getParameter("type");
        List<SupSubjectdef> list=new ArrayList<>();
        List<SupSubjectdef> sourcelist = supSubjectdefService.getTreeProductFree(list,ids,userid,type);
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(sourcelist);
        return restResult;
    }
    @ApiOperation(value="自定义专题子产品导航栏信息", notes="专题产品功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "产品id", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/getTreeProductFree")
    @ResponseBody
    public RestResult getTreeProductFree(
            HttpServletRequest request, HttpServletResponse response) {
        String ids=request.getParameter("id");
        List<SupSubjectdef> list=new ArrayList<>();
        List<SupSubjectdef> sourcelist = supSubjectdefService.getTreeProductFree(list,ids,null,null);
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(sourcelist);
        return restResult;
    }
    @RequestMapping(value = "/ispermissions")
    @ResponseBody
    public RestResult ispermissions(
            HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String userid=request.getParameter("userid");
        String type=request.getParameter("type");
        boolean flag = false;
        if(type.contains("1")) {
            flag=true;
        }else{
            flag = supSubjectdefService.ispermissions(id, userid);
        }
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        if(!flag) {
            restResult.setMessage("无产品权限！");
        }else{
            restResult.setMessage("请求成功！");
        }
        restResult.setData(flag);
        return restResult;
    }

    /**
     * 获取产品图片信息
     *
     * @param request
     * @param response
     */
    @ApiOperation(value="获取子产品图片信息", notes="获取产品图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start_time", value = "图片数据开始时间", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "end_time", value = "图片数据结束时间", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "table_name", value = "图片数据存储表", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/getProductImg")
    @ResponseBody
    public RestResult getProductImg(
            HttpServletRequest request, HttpServletResponse response) {
        String startTime=request.getParameter("start_time");
        String endTime=request.getParameter("end_time");
        String procode=request.getParameter("procode");
        String kind=request.getParameter("kind");
        List<ProductImgDef> sourcelist=null;
        if(procode!=null&&!("".equals(procode))&&kind!=null&&(!("".equals(kind)))) {
             sourcelist = supSubjectdefService.findProductImg(startTime, endTime, procode,kind);
        }
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(sourcelist);
        return restResult;
    }
}