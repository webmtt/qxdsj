package com.nmpiesat.idata.products.web;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.nmpiesat.idata.products.entity.*;
import com.nmpiesat.idata.products.service.ProductsService;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.util.HttpClientUtil;
import com.nmpiesat.idata.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 产品库展示
 * @author zhaoxiaojun 2020年02月13日13:25:14
 */

@RestController
@RequestMapping(value = "/products")
@Api(value = "ProductsController", description = "产品库接口")
@CrossOrigin
public class ProductsController{

    @Autowired
    private ProductsService productsService;

    @Value("${unitpath}")
    private String unitpath;

    @Value("${productpath}")
    private String productpath;

    @Value("${basepath}")
    private String basepath;

    @Value("${filePath}")
    private String filePath;

    @ApiOperation(value="根据url查询产品库配置", notes="根据url查询产品库配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "产品库配置url", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getProducts"})
    public RestResult getProducts(String id){
        RestResult restResult = new RestResult();
        ProductesConfig products = productsService.selectByPrimaryKey(id);
        if (products != null){
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("产品查询成功");
            restResult.setData(products);
        }else{
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("产品查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="保存产品库配置", notes="保存产品库配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "product", value = "产品库配置信息", required = true),
    })
    @RequestMapping(value = {"/insertProducts"})
    public RestResult insertProducts(@RequestBody ProductesConfig product){
        RestResult restResult = new RestResult();
        try{
            productsService.insertProducts(product);
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("产品创建成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("产品创建失败");

        }
        return restResult;
    }

    @ApiOperation(value="更新产品库配置", notes="更新产品库配置")
            @ApiImplicitParams({
                    @ApiImplicitParam(name = "product", value = "产品库配置信息", required = true ,dataType = "string"),
                    @ApiImplicitParam(name = "id", value = "产品库配置ID", required = true ,dataType = "string")
            })
    @RequestMapping(value = {"/updateProducts"})
    public RestResult updateProducts(@RequestBody ProductesConfig products){
        RestResult restResult = new RestResult();
        try{
            products.setCreate(new Date());
            productsService.updateProducts(products);
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据更新成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据更新失败");
        }
        return restResult;
    }

    @ApiOperation(value="条件列表", notes="条件列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/getFactor"})
    public RestResult getFactor(){
        RestResult restResult = new RestResult();
        List<Map<String,Object>> list = productsService.getFactor();
        List<Map<String,Object>> productList = productsService.getBmpProductInfo();
        List<Map<String,Object>> unitList = new ArrayList<>();
        List<Map<String,Object>> typeList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        if (list.size() != 0 && productList.size() != 0){
            for (int i = 0; i <list.size() ; i++) {
                if ("UnitCode".equals(list.get(i).get("ENUMTYPE"))){
                    unitList.add(list.get(i));
                }else if ("TypeCode".equals(list.get(i).get("ENUMTYPE"))){
                    typeList.add(list.get(i));
                }
            }
            map.put("unitList",unitList);
            map.put("typeList",typeList);
            map.put("productList",productList);
            restResult.setCode("200");
            restResult.setData(map);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询成功");
        }else{
            restResult.setCode("201");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询为空");
        }
        return restResult;
    }

    @ApiOperation(value="查询单图文件", notes="查询单图文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageRequest", value = "分页条件", required = true ,dataType = "PageRequest")
    })
    @RequestMapping(value = {"/getProductsPhoto"})
    public RestResult getProductsPhoto(@RequestBody PageRequest pageRequest){
        RestResult restResult = new RestResult();
        try{
            PageInfo<UploadProduct> list = productsService.getProductsFile(pageRequest);
            if(list.getSize()!=0){
                for (UploadProduct str:list.getList()) {
                    str.setUrl(basepath+str.getUrl());
                }
                restResult.setCode("200");
                restResult.setData(list);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="查询多图文件", notes="查询多图文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "图片个数", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "photoType", value = "图片类型", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "unit", value = "制作单位", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "typs", value = "业务门类", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "product", value = "产品子类", required = true ,dataType = "String")
    })
    @RequestMapping(value = {"/getMorePhoto"})
    public RestResult getMorePhoto(String num,String photoType,String unit,String typs,String product){
        RestResult restResult = new RestResult();
        try{
            String[] unitList =null;
            String[] typeList =null;
            String[] productList =null;
            String[] photoTypeList = photoType.split(",");
            if(unit!=null){
                unitList = unit.split(",");
            }
            if(typs!=null){
                typeList = typs.split(",");
            }
            if(product!=null){
                productList = product.split(",");
            }
            List<ProDataindex> list = productsService.getMorePhotoUT(Integer.parseInt(num),photoTypeList,unitList,typeList,productList);
            if(list.size()!=0){
                for (ProDataindex str:list) {
                    str.setUrl(unitpath+str.getUrl());
                }
                restResult.setCode("200");
                restResult.setData(list);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="查询标题图列表", notes="查询标题图列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoType", value = "图片类型", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "unit", value = "制作单位", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "typs", value = "业务门类", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "product", value = "产品子类", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true ,dataType = "String")
    })
    @RequestMapping(value = {"/getMenuPhoto"})
    public RestResult getMenuPhoto(String pageNum,String pageSize,String photoType,String unit,String typs,String product){
        RestResult restResult = new RestResult();
        try{
            String[] unitList =null;
            String[] typeList =null;
            String[] productList =null;
            String[] photoTypeList = photoType.split(",");
            if(unit!=null){
                unitList = unit.split(",");
            }
            if(typs!=null){
                typeList = typs.split(",");
            }
            if(product!=null){
                productList = product.split(",");
            }
            PageInfo<ProDataindex> list = productsService.getMenuPhotoUT(Integer.parseInt(pageNum),Integer.parseInt(pageSize),unitList,typeList,productList,photoTypeList);
            if(list.getList().size()!=0){
                for (ProDataindex str:list.getList()) {
                    str.setUrl(unitpath+str.getUrl());
                }
                restResult.setCode("200");
                restResult.setData(list);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="查询文本文件", notes="查询文本文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unit", value = "制作单位", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "typs", value = "业务门类", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "product", value = "产品子类", required = true ,dataType = "String")
    })
    @RequestMapping(value = {"/getMoreVersion"})
    public RestResult getMoreVersion(String pageNum,String pageSize,String unit,String typs,String product){
        RestResult restResult = new RestResult();
        try{
            String[] unitList =null;
            String[] typeList =null;
            String[] productList =null;
            if(unit!=null){
                unitList = unit.split(",");
            }
            if(typs!=null){
                typeList = typs.split(",");
            }
            if(product!=null){
                productList = product.split(",");
            }
            PageInfo<ProDataindex> list = productsService.getMoreVersionUT(Integer.parseInt(pageNum),Integer.parseInt(pageSize),unitList,typeList,productList);
            if(list.getSize()!=0){
                for (ProDataindex str:list.getList()) {
                    str.setUrl(unitpath+str.getUrl());
                }
                restResult.setCode("200");
                restResult.setData(list);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="生成文本树结构", notes="生成文本树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "product", value = "制作单位", required = true ,dataType = "String")
    })
    @RequestMapping(value = {"/getTreeData"})
    public RestResult getTreeData(String product){
        RestResult restResult = new RestResult();
        String[] products = null;
        try{
            if (product!=null&&product!=""){
                products=product.split(",");
            }
            List<Map> list1 = new ArrayList<>();
            List<Map> list2 = new ArrayList<>();
            List<Map> list3 = new ArrayList<>();
            List<String> typeList = productsService.getType(products);
            if(typeList.size()!=0){
                for (int i = 0; i < typeList.size(); i++) {
                    Map<String,Object> map = productsService.getTypeName(typeList.get(i));
                        if ("MSP1".equals(map.get("TOKENSCODE"))){
                            list1.add(map);
                        }else if("MSP2".equals(map.get("TOKENSCODE"))){
                            list2.add(map);
                        }else if("MSP3".equals(map.get("TOKENSCODE"))){
                            list3.add(map);
                        }
                }
            }
            Map<String,Object> maps = new HashMap<>();
            maps.put("天气实况产品",list1);
            maps.put("基础预报产品",list2);
            maps.put("气象服务产品",list3);
            maps.put("product",product);
            restResult.setCode("200");
            restResult.setData(maps);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="点击树生成文本列表", notes="点击树生成文本列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "product", value = "制作单位", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "type", value = "业务门类", required = true ,dataType = "String")
    })
    @RequestMapping(value = {"/getNoteData"})
    public RestResult getNoteData(String product,String type){
        RestResult restResult = new RestResult();
        String[] products = null;
        try{
            if (product!=null&&product!=""){
                products=product.split(",");
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String,Object>> list = productsService.getAllUnitTypes(type,products);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("URL",unitpath+list.get(i).get("URL"));
                list.get(i).put("UPDATED",sf.format(list.get(i).get("UPDATED")));
            }
            restResult.setCode("200");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="获取所有制作单位", notes="获取所有制作单位")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/getAllUnit"})
    public RestResult getAllUnit(){
        RestResult restResult = new RestResult();
        try{
            Map<String,Object> mapList = new LinkedHashMap();
            List<String> list = productsService.getUnit();
            List<Map<String,Object>> subList = productsService.getSubType();
            if(subList.size()!=0){
                mapList.put("区局单位",subList);
            }
            if(list.size()!=0){
                for (int i = 0; i <list.size() ; i++) {
                    List<Map<String,Object>> subTypeList = productsService.getAllSub(list.get(i));
                    mapList.put(list.get(i),subTypeList);
                }
                restResult.setCode("200");
                restResult.setData(mapList);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="获取树形结构下所有文件", notes="获取树形结构下所有文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "typscode", value = "业务门类编码", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "productcode", value = "产品子类编码", required = true ,dataType = "String")
    })
    @RequestMapping(value = {"/getAlltypsFile"})
    public RestResult getAlltypsFile(String pageNum,String pageSize,String typscode,String productcode){
        RestResult restResult = new RestResult();
        try{
            PageInfo<ProDataindex> list = new PageInfo<>();
            if(typscode!=null){
                list = productsService.getAlltypsFile(Integer.parseInt(pageNum),Integer.parseInt(pageSize),typscode);
                if(list.getSize()!=0) {
                    for (ProDataindex str : list.getList()) {
                        str.setUrl(unitpath + str.getUrl());
                    }
                }
            }else if(productcode!=null) {
                list = productsService.getAllProductcodeFile(Integer.parseInt(pageNum), Integer.parseInt(pageSize), productcode);
                if (list.getSize() != 0) {
                    for (ProDataindex str : list.getList()) {
                        str.setUrl(unitpath + str.getUrl());
                    }
                }
            }
            restResult.setCode("200");
            restResult.setData(list);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="获取热门产品种类", notes="获取热门产品种类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unit", value = "地区名称", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "dataType", value = "时间类型(1.今天 2.昨天 3.上周 4.上月 5.上年)", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getUnitSum"})
    public RestResult getUnitSum(String unit,String dataType){
        RestResult restResult = new RestResult();
        try{
            List<Map<String,Object>> map = productsService.getUnitSum(unit,dataType);
            if(map.size()!=0){
                for (Map<String,Object> unitMap:map) {
                    if(unitMap.get("num")==null){
                        unitMap.put("num","0");
                    }
                    if(dataType.equals("1")){
                        unitMap.put("mean","0");
                    }else if(dataType.equals("2")){
                        unitMap.put("mean","0");
                    }else if(dataType.equals("3")){
                        unitMap.put("mean",String.valueOf(Integer.parseInt(unitMap.get("num").toString())/7));
                    }else if(dataType.equals("4")){
                        unitMap.put("mean",String.valueOf(Integer.parseInt(unitMap.get("num").toString())/30));
                    }else if(dataType.equals("5")){
                        unitMap.put("mean",String.valueOf(Integer.parseInt(unitMap.get("num").toString())/365));
                    }
                }
                restResult.setCode("200");
                restResult.setData(map);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="按时间区间获取热门产品种类", notes="按时间区间获取热门产品种类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unit", value = "地区名称", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getUnitSumForTimes"})
    public RestResult getUnitSumForTimes(String unit,String startTime,String endTime){
        RestResult restResult = new RestResult();
        try{
            List<Map<String,Object>> map = productsService.getUnitSumForTimes(unit,startTime,endTime);
            if(map.size()!=0){
                for (Map<String,Object> unitMap:map) {
                    if (unitMap.get("num") == null) {
                        unitMap.put("num", "0");
                        unitMap.put("mean","0");
                    }else{
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d1 = df.parse(endTime);
                        Date d2 = df.parse(startTime);
                        long diff = d1.getTime() - d2.getTime();//这样得到的差值是毫秒级别
                        long days = diff / (1000 * 60 * 60 * 24);
                        unitMap.put("mean", String.valueOf(Integer.parseInt(unitMap.get("num").toString())/(int)days));
                    }
                }
                restResult.setCode("200");
                restResult.setData(map);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="按产品名称查询此产品下文件数量", notes="按产品名称查询此产品下文件数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unit", value = "地区名称", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "productname", value = "产品名称", required = true ,dataType = "string")
    })
    @RequestMapping(value = {"/getUnitCount"})
    public RestResult getUnitCount(String unit,String productname){
        RestResult restResult = new RestResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            String counts = productsService.getCounts(unit,productname);
            List<Map<String,Object>> map = productsService.getUnitCount(unit,productname);
            if(map.size()!=0){
                Map<String,Object> unitMap = map.get(0);
                unitMap.put("created",simpleDateFormat.format(simpleDateFormat.parse(unitMap.get("PRODATE")==null?unitMap.get("created").toString():unitMap.get("PRODATE").toString())));
                unitMap.put("counts",counts);
                unitMap.put("unit",unitMap.get("UNIT")==null?unitMap.get("unit").toString():unitMap.get("UNIT").toString());
                restResult.setCode("200");
                restResult.setData(unitMap);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="获取最新产品", notes="获取最新产品")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/getNewUnits"})
    public RestResult getNewUnits(String pageNum,String pageSize ){
        RestResult restResult = new RestResult();
        try{
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String alerteunit="";
            String alertetype ="";
            String alerteproduct ="";
            String forecastunit ="";
            String forecasttype ="";
            String forecastproduct ="";
            String[] alerteunitList =null;
            String[] alertetypeList =null;
            String[] alerteproductList =null;
            String[] forecastunitList =null;
            String[] forecasttypeList =null;
            String[] forecastproductList =null;
            List<Map> newproducts = productsService.getConfig();
            if (newproducts.get(0).get("unit")!=null&&!"".equals(newproducts.get(0).get("unit"))){
                alerteunit = newproducts.get(0).get("unit").toString();
                alerteunitList = alerteunit.split(",");
            }
            if (newproducts.get(0).get("types")!=null&&!"".equals(newproducts.get(0).get("types"))){
                alertetype = newproducts.get(0).get("types").toString();
                alertetypeList = alertetype.split(",");
            }
            if (newproducts.get(0).get("product")!=null&&!"".equals(newproducts.get(0).get("product"))){
                alerteproduct = newproducts.get(0).get("product").toString();
                alerteproductList = alerteproduct.split(",");
            }
            if (newproducts.get(1).get("unit")!=null&&!"".equals(newproducts.get(1).get("unit"))){
                forecastunit = newproducts.get(1).get("unit").toString();
                forecastunitList = forecastunit.split(",");
            }
            if (newproducts.get(1).get("types")!=null&&!"".equals(newproducts.get(1).get("types"))){
                forecasttype = newproducts.get(1).get("types").toString();
                forecasttypeList = forecasttype.split(",");
            }
            if (newproducts.get(1).get("product")!=null&&!"".equals(newproducts.get(1).get("product"))){
                forecastproduct = newproducts.get(1).get("product").toString();
                forecastproductList = forecastproduct.split(",");
            }

            PageInfo<Map<String,Object>> unitTypes = productsService.getUnitTypes(Integer.parseInt(pageNum), Integer.parseInt(pageSize),alerteunitList,alertetypeList,alerteproductList);
            PageInfo<Map<String,Object>> productMap = productsService.getProduct(Integer.parseInt(pageNum), Integer.parseInt(pageSize),forecastunitList,forecasttypeList,forecastproductList);
            for (Map<String,Object> unitMap:unitTypes.getList()) {
                unitMap.put("created",sf.format(sf.parse(unitMap.get("CREATED")==null?unitMap.get("created").toString():unitMap.get("CREATED").toString())));
                if(unitMap.get("URL")==null){
                    unitMap.put("url",unitpath+unitMap.get("url").toString());
                }else{
                    unitMap.put("url",unitpath+unitMap.get("URL").toString());
                }
                if(unitMap.get("FILESHOWNAME")==null){
                    unitMap.put("fileshowname",unitMap.get("fileshowname").toString());
                }else{
                    unitMap.put("fileshowname",unitMap.get("FILESHOWNAME").toString());
                }
            }
            for (Map<String,Object> prodMap:productMap.getList()) {
                prodMap.put("created",sf.format(sf.parse(prodMap.get("CREATED")==null?prodMap.get("created").toString():prodMap.get("CREATED").toString())));
                if(prodMap.get("URL")==null){
                    prodMap.put("url",unitpath+prodMap.get("url").toString());
                }else{
                    prodMap.put("url",unitpath+prodMap.get("URL").toString());
                }
                if(prodMap.get("FILESHOWNAME")==null){
                    prodMap.put("fileshowname",prodMap.get("fileshowname").toString());
                }else{
                    prodMap.put("fileshowname",prodMap.get("FILESHOWNAME").toString());
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("unitType","最新预报产品");
            map.put("product","最新预警产品");
            map.put("unitTypeList",unitTypes);
            map.put("productList",productMap);
            restResult.setCode("200");
            restResult.setData(map);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="产品文件阅读量统计", notes="产品文件阅读量统计")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/statisticsUnits"})
    public RestResult statisticsUnits(String id,String createdby,HttpServletRequest request){
        RestResult restResult = new RestResult();
        Map<String,String> map = new HashMap<>();
        try{
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int stat = productsService.statisticsUnits(id);
            String ip = HttpClientUtil.getIpAddress(request);
            ProductLog productLog = new ProductLog();
            long rightTime =(new Date().getTime() + 8 * 60 * 60 * 1000);
            String time = sd.format(rightTime);
            productLog.setDate(sd.parse(time));
            productLog.setId(UUID.randomUUID().toString().replace("-",""));
            productLog.setIp(ip);
            productLog.setWordId(id);
            productsService.insertLog(productLog);
            productsService.updateOracle(id);
            String json="{\"method\":\"getUser\",\"params\":["+"\""+createdby+"\""+"],\"id\":1}";
            JSONObject jsonObject = HttpClientUtil.doPostForJson(filePath,json);
            String result = jsonObject.get("result").toString();
            String realName = JSONObject.parseObject(result).get("realName").toString();
            String defaultScence = JSONObject.parseObject(result).get("defaultScence").toString();
            String term = JSONObject.parseObject(defaultScence).get("term").toString();
            String fullName = JSONObject.parseObject(term).get("fullName").toString();
            if(stat == 1){
                restResult.setCode("200");
                map.put("realName",realName);
                map.put("fullName",fullName);
                restResult.setData(map);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据插入成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据插入失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }


    @ApiOperation(value="服务产品清单展示", notes="服务产品清单展示")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/getNewProduct"})
    public RestResult getNewProduct(){
        RestResult restResult = new RestResult();
        try{
            Map<String,Object> unitMap = new HashMap<>();
            Map<String,Object> map = productsService.getProductRepert();
            if(map.size()!=0){
                String MSP1 = map.get("MSP1").toString();
                String MSP2 = map.get("MSP2").toString();
                String MSP3 = map.get("MSP3").toString();
                String[] MSP1List = MSP1.split(",");
                String[] MSP2List = MSP2.split(",");
                String[] MSP3List = MSP3.split(",");
                unitMap.put("天气实况产品",MSP1List);
                unitMap.put("基础预报产品",MSP2List);
                unitMap.put("气象服务产品",MSP3List);
                restResult.setCode("200");
                restResult.setData(unitMap);
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("数据查询成功");
            }else{
                restResult.setCode("201");
                restResult.setCurrentTime(new Timestamp(new Date().getTime()));
                restResult.setMessage("查询数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("数据查询失败");
        }
        return restResult;
    }

    @ApiOperation(value="请求更多产品文件统一入口", notes="请求更多产品文件统一入口")
    @ApiImplicitParams({
    })
    @RequestMapping(value = {"/getMoreProduct"})
    public RestResult getMoreProduct(){
        RestResult restResult = new RestResult();
        try{
            String path = unitpath;
            restResult.setCode("200");
            restResult.setData(path);
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("调取地址成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("调取地址失败");
        }
        return restResult;
    }

}
