package com.nmpiesat.idata.interfacedata.web;

import com.nmpiesat.idata.interfacedata.entity.InterfaceData;
import com.nmpiesat.idata.interfacedata.service.InterfaceDataService;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.user.entity.MusicInfo;
import com.nmpiesat.idata.util.DesUtils;
import com.nmpiesat.idata.util.HttpClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * API接口分类展示
 * @author zhaoxiaojun 2019年12月20日13:25:14
 */
@RestController
@RequestMapping(value = "/interfacedata")
@CrossOrigin
@Api(value = "InterfaceDataController", description = "API展示接口")
public class InterfaceDataController{

    @Autowired
    private InterfaceDataService interfaceDataService;


    @Value("${musicPath}")
    private String musicPath;

    @Value("${progPath}")
    private String progPath;

    /**
     * 查询所有资料
     * @param
     * @return
     */
    @ApiOperation(value="查询所有资料", notes="查询所有资料")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/allinter"})
    public RestResult getAllInter(String userid){
        RestResult restResult = new RestResult();
        List<InterfaceData>  interList = interfaceDataService.getAllInter(userid);
        if (interList.size() != 0){
           restResult.setCode("200");
           restResult.setMessage("查询成功");
           restResult.setData(interList);
           restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询失败");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }
        return restResult;
    }


    /**
     * 查询资料分类下所有已授权资料
     * @param id
     * @param userid
     * @return
     */
    @ApiOperation(value="查询已授权资料", notes="查询已授权资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "id", value = "资料ID", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getInterface"})
    public RestResult getInter(String id,String userid){
        RestResult restResult = new RestResult();
        List<Map<String,Object>>  interList = interfaceDataService.getInterface(id,userid);
        if (interList.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(interList);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setData(null);
            restResult.setMessage("查询为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }
        return restResult;
    }

    /**
     * 查询资料下所有接口
     * @param id
     * @return
     */
    @ApiOperation(value="查询接口信息", notes="查询接口详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资料ID", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/interfaces"})
    public RestResult getInterface(String id){
        RestResult restResult = new RestResult();
        Map<String,Object> map = interfaceDataService.getFactor(id);
        if (map.size()!=0){
            String apiList = map.get("APILIST").toString().replace("[","")
                    .replace("]","").replace("\"","");
            String[] apis = apiList.split(",");
            ArrayList<Map<String,Object>> list = new ArrayList<>();
            for (int i = 0; i <apis.length ; i++) {
                Map<String,Object> interMap = interfaceDataService.getInterForId(apis[i]);
                list.add(interMap);
            }
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询失败");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }
        return restResult;
    }

    /**
     * 查询接口下所有要素信息
     * @param id
     * @return
     */
    @ApiOperation(value="查询要素信息", notes="查询要素信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口ID", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getEleForInter"})
    public RestResult getEleForInter(String id){
        RestResult restResult = new RestResult();
        List<Map<String,Object>> list = interfaceDataService.getEleForInter(id);
        for (int i = 0; i <list.size() ; i++) {
            if("N".equals(list.get(i).get("IS_OPTIONAL"))){
                list.get(i).put("IS_OPTIONAL","必选参数");
            }else{
                list.get(i).put("IS_OPTIONAL","可选参数");
            }
        }
        if (list.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询失败");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }
        return restResult;
    }

    /**
     * 空间属性要素字段代码
     * @param
     */
    @RequestMapping(value = {"/getElements"})
    public RestResult getElements(String id) {
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        List<Map<String, Object>> list = new ArrayList<>();
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("elements")){
                        com.alibaba.fastjson.JSONArray element = com.alibaba.fastjson.JSONArray.parseArray(picArray.getJSONObject(i).get("val").toString());
                        for (int j = 0; j <element.size() ; j++) {
                            String eleCode = element.getJSONObject(j).get("user_ele_code").toString();
                            Map<String,Object> elemap =interfaceDataService.getEleMent(eleCode);
                            list.add(elemap);
                        }
                    }
                }
            }
        }
        if (list.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询数据为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }

        return restResult;
    }

    /**
     * 空间属性预报层次（单个）
     * @param
     */
    @RequestMapping(value = {"/getFcstLevel"})
    public RestResult getFcstLevel(String id){
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        List<String> list = new ArrayList<>();
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("fcstLevel")){
                        com.alibaba.fastjson.JSONArray element = com.alibaba.fastjson.JSONArray.parseArray(picArray.getJSONObject(i).get("val").toString());
                        for (int j = 0; j <element.size() ; j++) {
                            String eleCode = element.getJSONObject(j).get("names").toString();
                            list.add(eleCode);
                        }
                    }
                }
            }
        }
        if (list.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询数据为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }

        return restResult;
    }

    /**
     * 空间属性台站站网
     * @param
     */
    @RequestMapping(value = {"/getNetCodes"})
    public RestResult getNetCodes(String id){
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        List<Map<String,Object>> list = new ArrayList<>();
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("netCodes")){
                        com.alibaba.fastjson.JSONArray element = com.alibaba.fastjson.JSONArray.parseArray(picArray.getJSONObject(i).get("val").toString());
                        for (int j = 0; j <element.size() ; j++) {
                            Map<String, Object> mape = new HashMap<>();
                            String ids = element.getJSONObject(j).get("id").toString();
                            String names = element.getJSONObject(j).get("names").toString();
                            mape.put("id",ids);
                            mape.put("names",names);
                            list.add(mape);
                        }
                    }
                }
            }
        }
        if (list.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询数据为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }

        return restResult;
    }

    /**
     * 空间属性土壤深度
     * @param
     */
    @RequestMapping(value = {"/getSoilDepths"})
    public RestResult getSoilDepths(String id){
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        List<Map<String,Object>> list = new ArrayList<>();
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("soilDepths")){
                        com.alibaba.fastjson.JSONArray element = com.alibaba.fastjson.JSONArray.parseArray(picArray.getJSONObject(i).get("val").toString());
                        for (int j = 0; j <element.size() ; j++) {
                            Map<String, Object> mape = new HashMap<>();
                            String ids = element.getJSONObject(j).get("id").toString();
                            String names = element.getJSONObject(j).get("names").toString();
                            mape.put("id",ids);
                            mape.put("names",names);
                            list.add(mape);
                        }
                    }
                }
            }
        }
        if (list.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询数据为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }

        return restResult;
    }

    /**
     * 空间属性国内行政编码
     * @param
     */
    @RequestMapping(value = {"/getAdminCodes"})
    public RestResult getAdminCodes(String id){
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        List<Map<String,Object>> disList = new ArrayList<>();
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("adminCodes")){
                        com.alibaba.fastjson.JSONArray element = com.alibaba.fastjson.JSONArray.parseArray(picArray.getJSONObject(i).get("val").toString());
                        for (int j = 0; j <element.size() ; j++) {
                            String ids = com.alibaba.fastjson.JSONArray.toJSONString(element.getJSONArray(j)).replace("[","")
                                    .replace("]","").replace("\"","");
                            String[] apis = ids.split(",");
                            Map<String,Object> adminCodes = interfaceDataService.getAdminCode(apis[1]);
                            disList.add(adminCodes);
                        }
                    }
                }
            }
        }
        List<Map<String, Object>> list = interfaceDataService.getAdminCodes();
        ArrayList<Map<String, Object>> cityList = new ArrayList<>();
        for (Map<String,Object> maps:list) {
            if ("city".equals(maps.get("level"))){
                cityList.add(maps);
            }
        }
        for (Map<String,Object> cityMap:cityList) {
            List<Map<String, Object>> districtList = new ArrayList<>();
            for (Map<String, Object> districtMap : disList) {
                if (cityMap.get("id").equals(districtMap.get("parent_id"))&&"district".equals(districtMap.get("level"))) {
                    districtList.add(districtMap);
                }
            }
            cityMap.put("children",districtList);
        }
        List<Map> noteList = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            ArrayList str = (ArrayList) cityList.get(i).get("children");
            if(str.size()!=0){
                noteList.add(cityList.get(i));
            }
        }
        if (cityList.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(noteList);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询数据为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }

        return restResult;
    }

    /**
     * 空间属性预报要素（单个）
     * @param
     */
    @RequestMapping(value = {"/getFcstEle"})
    public RestResult getFcstEle(String id){
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        List<Map<String, Object>> list = new ArrayList<>();
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("fcstEle")){
                        com.alibaba.fastjson.JSONArray element = com.alibaba.fastjson.JSONArray.parseArray(picArray.getJSONObject(i).get("val").toString());
                        for (int j = 0; j <element.size() ; j++) {
                            Map<String, Object> mape = new HashMap<>();
                            String USER_FCST_ELE = element.getJSONObject(j).get("USER_FCST_ELE").toString();
                            String DB_ELE_UNIT = element.getJSONObject(j).get("DB_ELE_UNIT").toString();
                            mape.put("USER_FCST_ELE",USER_FCST_ELE);
                            mape.put("DB_ELE_UNIT",DB_ELE_UNIT);
                            list.add(mape);
                        }
                    }
                }
            }
        }
        if (list.size()!=0){
            restResult.setCode("200");
            restResult.setMessage("查询成功");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }else{
            restResult.setCode("201");
            restResult.setMessage("查询数据为空");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }

        return restResult;
    }

    /**
     * 时间要素time,times,timeRange,tenRangeOfYear判断
     * @param
     */
    @RequestMapping(value = {"/getTimeSted"})
    public RestResult getTimeSted(String id,String time){
        RestResult restResult = new RestResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String sTime ="";
        String eTime ="";
        Map<String, Object> map = interfaceDataService.getFactor(id);
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (picArray.getJSONObject(i).get("type").equals("time")||picArray.getJSONObject(i).get("type").equals("times")
                    ||picArray.getJSONObject(i).get("type").equals("timeRange")||picArray.getJSONObject(i).get("type").equals("tenRangeOfYear")){
                        String element = picArray.getJSONObject(i).get("val").toString().replace("[","")
                                .replace("]","").replace("\"","");
                        String[] times = element.split(",");
                        sTime = times[0].replace(" ","").replace("-","").replace(":","");
                        eTime = times[1].replace(" ","").replace("-","").replace(":","");
                        break;
                    }
                }
            }
            try{
                Date nowTime = simpleDateFormat.parse(time.substring(0,8));
                Date startTime = simpleDateFormat.parse(sTime);
                Date endTime = simpleDateFormat.parse(eTime);
                if (nowTime.getTime() == startTime.getTime()
                        || nowTime.getTime() == endTime.getTime()) {
                    restResult.setCode("200");
                    restResult.setMessage("时间符合要求");
                    restResult.setData(true);
                    restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                }

                Calendar date = Calendar.getInstance();
                date.setTime(nowTime);

                Calendar begin = Calendar.getInstance();
                begin.setTime(startTime);

                Calendar end = Calendar.getInstance();
                end.setTime(endTime);

                if (date.after(begin) && date.before(end)) {
                    restResult.setCode("200");
                    restResult.setMessage("时间符合要求");
                    restResult.setData(true);
                    restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                } else {
                    restResult.setCode("201");
                    restResult.setData(false);
                    restResult.setMessage("时间不符合要求");
                    restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return restResult;
    }

    /**
     * 剩余要素判断
     * @param
     */
    @RequestMapping(value = {"/checkOtherEle"})
    public RestResult checkOtherEle(String id,String name,String types){
        RestResult restResult = new RestResult();
        Map<String, Object> map = interfaceDataService.getFactor(id);
        if (map.size() != 0) {
            String data = map.get("DATA").toString();
            com.alibaba.fastjson.JSONArray picArray = com.alibaba.fastjson.JSONArray.parseArray(data);
            if (picArray != null) {
                for (int i = 0; i < picArray.size(); i++) {
                    if (types.equals(picArray.getJSONObject(i).get("type"))){
                        String minLon = picArray.getJSONObject(i).get("val").toString();
                        if (Double.parseDouble(name)>=Double.parseDouble(minLon)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (types.equals(picArray.getJSONObject(i).get("type"))){
                        String maxLon = picArray.getJSONObject(i).get("val").toString();
                        if (Double.parseDouble(name)<=Double.parseDouble(maxLon)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String minLat = picArray.getJSONObject(i).get("val").toString();
                        if (Double.parseDouble(name)>=Double.parseDouble(minLat)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String maxLat = picArray.getJSONObject(i).get("val").toString();
                        if (Double.parseDouble(name)<=Double.parseDouble(maxLat)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String minMD = picArray.getJSONObject(i).get("val").toString();
                        if (Integer.parseInt(name)>=Integer.parseInt(minMD)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String maxMD = picArray.getJSONObject(i).get("val").toString();
                        if (Integer.parseInt(name)<=Integer.parseInt(maxMD)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String minYear = picArray.getJSONObject(i).get("val").toString();
                        if (Integer.parseInt(name)>=Integer.parseInt(minYear)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String maxYear = picArray.getJSONObject(i).get("val").toString();
                        if (Integer.parseInt(name)<=Integer.parseInt(maxYear)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String month = picArray.getJSONObject(i).get("val").toString();
                        String[] months = month.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(months[0])&&Integer.parseInt(name)<=Integer.parseInt(months[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String day = picArray.getJSONObject(i).get("val").toString();
                        String[] days = day.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(days[0])&&Integer.parseInt(name)<=Integer.parseInt(days[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String daysOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] daysOfYears = daysOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(daysOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(daysOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String monsOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] monsOfYears = monsOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(monsOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(monsOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String pensOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] pensOfYears = pensOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(pensOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(pensOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String tensOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] tensOfYears = tensOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(tensOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(tensOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String dayRangeOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] dayRangeOfYears = dayRangeOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(dayRangeOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(dayRangeOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String monRangeOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] monRangeOfYears = monRangeOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(monRangeOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(monRangeOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String penRangeOfYear = picArray.getJSONObject(i).get("val").toString();
                        String[] penRangeOfYears = penRangeOfYear.split("-");
                        if (Integer.parseInt(name)>=Integer.parseInt(penRangeOfYears[0])&&Integer.parseInt(name)<=Integer.parseInt(penRangeOfYears[1])){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String minStaId = picArray.getJSONObject(i).get("val").toString();
                        if (Integer.parseInt(name)>=Integer.parseInt(minStaId)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }else if (picArray.getJSONObject(i).get("type").equals(types)){
                        String maxStaId = picArray.getJSONObject(i).get("val").toString();
                        if (Integer.parseInt(name)<=Integer.parseInt(maxStaId)){
                            restResult.setCode("200");
                            restResult.setMessage("符合要求");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }else{
                            restResult.setCode("300");
                            restResult.setMessage("超出范围");
                            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                        }
                    }
                }
            }
        }
        return restResult;
    }

    /**
     * 生成脚本信息
     * @return
     */
    @ApiOperation(value="生成脚本信息", notes="生成脚本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message", value = "参数信息", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/script"},method={RequestMethod.POST})
    public RestResult getScript(HttpServletRequest request){
        RestResult restResult = new RestResult();
        String info ="";
        String infos ="";
        try{
            InputStreamReader reader=new InputStreamReader(request.getInputStream(),"UTF-8");
            char [] buff=new char[1024];
            int length=0;
            while((length=reader.read(buff))!=-1){
                String x=new String(buff,0,length);
                info+=x;
            }
            //后台接收
            JSONObject json = JSONObject.fromObject(info);
            String inter=json.getString("interfaceId");
            String type=json.getString("dataFormat");
            String userid=json.getString("userid");
            //String id=json.getString("id");
            MusicInfo musicInfo = interfaceDataService.getMusic(userid);
            JSONArray factor=json.getJSONArray("factor");
            infos ="#接口名称，用中括号[]括起来"+"\n"
                    +"["+inter+"]"+"\n"
                    +"#用户信息：用户名/密码\n"
                    +"userinfo="+musicInfo.getName()+"/"+musicInfo.getPassword()+"\n"
                    +"#文件类型"+"\n"
                    + "dataFormat="+type+"\n"
                    +"#文件保存路径"+"\n"
                    +"savePath=./"+inter+"."+type+"\n"
                    +"#参数列表"+"\n";
            for(int i=0;i<factor.size();i++){
                JSONObject object=factor.getJSONObject(i);
                infos+="#"+object.getString("name")+"\n"
                        +object.getString("item")+"="+object.getString("value")+"\n";
            }
            restResult.setCode("200");
            restResult.setMessage("脚本执行成功");
            restResult.setData(infos);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            /*if (restResult.getMessage().contains("超出范围")){
                restResult.setCode("300");
                restResult.setMessage("脚本执行失败");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }else{

            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return restResult;
    }

    /**
     * 生成并执行url
     * @return
     */
    @ApiOperation(value="生成并执行url", notes="生成并执行url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request", value = "参数信息", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/createurl"},method={RequestMethod.POST})
    public RestResult createUrl(HttpServletRequest request){
        RestResult restResult = new RestResult();
        String info ="";
        String infos ="";
        String str="";
        String jsonObject="";
        try{
            DesUtils des = new DesUtils("leemenz");
            infos =progPath+"interfacedata/getApiData?str=";
            InputStreamReader reader=new InputStreamReader(request.getInputStream(),"UTF-8");
            char [] buff=new char[1024];
            int length=0;
            while((length=reader.read(buff))!=-1){
                String x=new String(buff,0,length);
                info+=x;
            }
            //后台接收
            JSONObject json = JSONObject.fromObject(info);
            String inter=json.getString("interfaceId");
            String type=json.getString("dataFormat");
            String userid=json.getString("userid");
            //String id=json.getString("id");
            MusicInfo musicInfo = interfaceDataService.getMusic(userid);
            JSONArray factor=json.getJSONArray("factor");
            str ="userId="+musicInfo.getName()+"&pwd="+musicInfo.getPassword()+"&interfaceId="+inter;
            for(int i=0;i<factor.size();i++){
                JSONObject object=factor.getJSONObject(i);
                str+="&"+object.getString("item")+"="+object.getString("value");
            }
            str=str+"&dataFormat="+type;
            jsonObject = HttpClientUtil.doGet(musicPath+str,"UTF-8");
            /*com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(jsonObject);
            com.alibaba.fastjson.JSONArray jsonArray = js.getJSONArray("DS");
            String jss = js.get("fieldNames").toString();
            ArrayList<String> idcList = new ArrayList<>();
            ArrayList<String> prsList = new ArrayList<>();
            for (int i = 0; i <jsonArray.size() ; i++) {
                com.alibaba.fastjson.JSONObject ds = jsonArray.getJSONObject(i);
                String idc = ds.get("Station_Id_C").toString();
                String prs = ds.get("PRS").toString();
                idcList.add(idc);
                prsList.add(prs);
            }*/
            str=des.encrypt(str);
            infos=infos+str;
            if (jsonObject.isEmpty()){
                restResult.setCode("300");
                restResult.setMessage("url生成失败,参数不符合");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }else{
                Map<String,Object> map = new HashMap<>();
                map.put("url",infos);
                map.put("urlData",jsonObject);
                restResult.setCode("200");
                restResult.setMessage("url生成成功");
                restResult.setData(map);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return restResult;
    }

    /**
     * 获取接口的返回数据
     * @return
     */
    @ApiOperation(value="获取接口的返回数据", notes="获取接口的返回数据")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/getApiData"},method={RequestMethod.GET})
    public Object getApiData(String str){
        String jsonObject ="";
        try{
            DesUtils des = new DesUtils("leemenz");
            /*String url = "http://10.62.89.55/cimiss-web/api?";*/
            String fromt = des.decrypt(str);
            jsonObject = HttpClientUtil.doGet(musicPath+fromt,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 下载API接口
     * @return
     */
    @ApiOperation(value="下载API接口", notes="下载API接口")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/uploadApiData"},method={RequestMethod.GET})
    public RestResult uploadApiData(String url){
        RestResult restResult = new RestResult();
        try{
            DesUtils des = new DesUtils("leemenz");
            /*String ip = "http://10.62.89.55/cimiss-web/api?";*/
            String[] urls = url.split("=");
            String fromt = des.decrypt(urls[1]);
            restResult.setData(musicPath+fromt);
            restResult.setMessage("url返回成功");
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return restResult;
    }

    /**
     * 查询接口详细信息
     * @param
     * @return
     */
    @ApiOperation(value="查询最新接口", notes="查询最新接口")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/newInterface"})
    public RestResult newInterface(){
        RestResult restResult = new RestResult();
        try{
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            List<Map<String,Object>> list = interfaceDataService.newInterface();
            if (list.size()!=0){
                for (Map<String,Object> map:list) {
                    map.put("PUBLISH_TIME",sf.format(sf.parse(map.get("PUBLISH_TIME").toString())));
                }
                restResult.setCode("200");
                restResult.setMessage("查询成功");
                restResult.setData(list);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }else{
                restResult.setCode("201");
                restResult.setMessage("查询失败");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return restResult;
    }

    @RequestMapping(value = {"/getAllElements"})
    public RestResult getAllElements(){
        RestResult restResult = new RestResult();
        try{
            List<Map<String,Object>> list = interfaceDataService.getAllElements();
            if (list.size()!=0){
                restResult.setCode("200");
                restResult.setMessage("查询成功");
                restResult.setData(list);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }else{
                restResult.setCode("201");
                restResult.setMessage("查询失败");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return restResult;
    }
}
