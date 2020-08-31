/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.arithmetic.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 算法信息管理Controller
 *
 * @author yang.kq
 * @version 2019-11-22
 */
@Controller
@RequestMapping(value = "/arithmetic/supArithmeticsPackage")
public class SupArithmeticsPackageController extends BaseController {

    @Autowired
    private SupArithmeticsPackageService supArithmeticsPackageService;
    @Autowired
    private SupArithmeticsStreamService supArithmeticsStreamService;

    @ModelAttribute
    public SupArithmeticsPackage get(@RequestParam(required = false) String id) {
        SupArithmeticsPackage entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = supArithmeticsPackageService.get(id);
        }
        if (entity == null) {
            entity = new SupArithmeticsPackage();
        }
        return entity;
    }

    @ModelAttribute
    @RequestMapping(value = "getById")
    public void getById(@RequestParam(required = false) String id, HttpServletResponse response) {
        SupArithmeticsPackage entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = supArithmeticsPackageService.get(id);
        }
        if (entity == null) {
            entity = new SupArithmeticsPackage();
        }
        renderText(entity.getPurpose(), response);
    }

    /**
     * 查询算法
     * @param supArithmeticsPackage
     * @param request
     * @param response
     * @param model
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:view")
    @RequestMapping(value = {"list", ""})
    public String list(SupArithmeticsPackage supArithmeticsPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<SupArithmeticsPackage> page = supArithmeticsPackageService.findPage(new Page<SupArithmeticsPackage>(request, response), supArithmeticsPackage);
        model.addAttribute("page", page);
        return "modules/arithmetic/supArithmeticsPackageList";
    }

    /**
     * 修改算法
     * @param supArithmeticsPackage
     * @param model
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:view")
    @RequestMapping(value = "form")
    public String form(SupArithmeticsPackage supArithmeticsPackage, Model model) {
        model.addAttribute("supArithmeticsPackage", supArithmeticsPackage);
        return "modules/arithmetic/supArithmeticsPackageForm";
    }

    /**
     * 算法测试跳转页面
     * @param supArithmeticsPackage
     * @param model
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:view")
    @RequestMapping(value = "preTest")
    public String preTest(SupArithmeticsPackage supArithmeticsPackage, Model model) {
        model.addAttribute("supArithmeticsPackage", supArithmeticsPackage);
        return "modules/arithmetic/supArithmeticsPackagePreTest";
    }
    /**
     * 数据源测试
     */
    @RequestMapping(value = "preTests")
    public String preTests(SupArithmeticsPackage supArithmeticsPackage, Model model) {
        model.addAttribute("supArithmeticsPackage", supArithmeticsPackage);
        return "modules/arithmetic/supArithmeticsPackagePreTests";
    }
    /**
     * 新增算法
     * @param supArithmeticsPackage
     * @param model
     * @param redirectAttributes
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:edit")
    @RequestMapping(value = "save")
    public String save(
            SupArithmeticsPackage supArithmeticsPackage,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, supArithmeticsPackage)) {
            return form(supArithmeticsPackage, model);
        }
        supArithmeticsPackage.setStatus("0");
        supArithmeticsPackageService.save(supArithmeticsPackage);
        if(!supArithmeticsPackage.getId().isEmpty()){
            //修改算法池状态
            SupArithmeticsStream supArithmeticsStream =  new SupArithmeticsStream();
            supArithmeticsStream.setSapId(supArithmeticsPackage.getId());
            supArithmeticsStreamService.updateStreamStatus(supArithmeticsStream);
        }
        addMessage(redirectAttributes, "保存算法成功");
        return "redirect:" + "/arithmetic/supArithmeticsPackage/?repage";
    }

    /**
     * 测试算法
     * @param supArithmeticsPackage
     * @param redirectAttributes
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:edit")
//    @RequestMapping(value = "test")
//    public String preTestUtil(SupArithmeticsPackage supArithmeticsPackage, RedirectAttributes redirectAttributes) {
//        if(supArithmeticsPackage.getParams().contains("，")){
//            addMessage(redirectAttributes, "请输入英文','!");
//        }else{
//            try{
//                Object result = ReFleixUtil.functionHandle(
//                        supArithmeticsPackage.getAriPackageName(),
//                        supArithmeticsPackage.getClassUrl(),
//                        supArithmeticsPackage.getAriMethod(),
//                        supArithmeticsPackage.getParams());
//                Object values = null;
//                if (result instanceof Integer) {
//                    values = Integer.parseInt(supArithmeticsPackage.getPridictValue());
//                } else if (result instanceof String) {
//                    values = supArithmeticsPackage.getPridictValue();
//                } else if (result instanceof Double) {
//                    values = Double.parseDouble(supArithmeticsPackage.getPridictValue());
//                } else if (result instanceof Float) {
//                    values = Float.parseFloat(supArithmeticsPackage.getPridictValue());
//                } else if (result instanceof Long) {
//                    values = Long.parseLong(supArithmeticsPackage.getPridictValue());
//                } else if (result instanceof Boolean) {
//                    values = Boolean.valueOf(supArithmeticsPackage.getPridictValue());
//                } else if (result instanceof Date) {
//                    values = Date.parse(supArithmeticsPackage.getPridictValue());
//                }
//                if (values.equals(result)) {
//                    addMessage(redirectAttributes, "算法测试成功");
//                    // 修改算法包状态
//                    if(supArithmeticsPackage.getStatus().equals("0")){
//                        supArithmeticsPackage.setStatus("1");
//                    }
//                    supArithmeticsPackageService.updateAriState(supArithmeticsPackage);
//                } else {
//                    addMessage(redirectAttributes, "算法测试失败");
//                }
//            }catch (Exception e){
//                addMessage(redirectAttributes, "算法信息配置错误，算法测试失败");
//            }
//        }
//        return "redirect:" + "/arithmetic/supArithmeticsPackage/?repage";
//    }

    /**
     * 修改数据源状态
     * @param supArithmeticsPackage
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "testss")
    public String preTestUtilss(SupArithmeticsPackage supArithmeticsPackage, RedirectAttributes redirectAttributes) {
        addMessage(redirectAttributes, "算法测试成功");
        // 修改算法包状态
        if(supArithmeticsPackage.getStatus().equals("0")) {
            supArithmeticsPackage.setStatus("1");
        }
        supArithmeticsPackageService.updateAriState(supArithmeticsPackage);
        return "redirect:" + "/arithmetic/supArithmeticsPackage/?repage";
    }
    /**
     * 数据源测试
     */
    @ResponseBody
    @RequestMapping(value = "tests")
    public JSONObject preTestUtils(String url,String id,String purposeValue) {
        url = url.replace("amp;","");
        SupArithmeticsPackage supArithmeticsPackage = supArithmeticsPackageService.get(id);
        String AriPackageName = supArithmeticsPackage.getAriPackageName();
        String ClassUrl = supArithmeticsPackage.getClassUrl();
        String AriMethod = supArithmeticsPackage.getAriMethod();
        HashMap<String, Object> params = new HashMap<String, Object>();
        JSONObject ja = null;
        try{
            String result = HttpClientUtil.post(url, params);
            ja = ReFleixUtil.functionHandles(AriPackageName,ClassUrl,AriMethod,result,purposeValue);
        }catch (Exception e){
            String json = "{\"code\":400,\"DS\":[{\"Message\":'API接口错误',\"data\":''}],\"message\":\"查询数据成功\"}";
            ja = JSONObject.fromObject(json);
        }
        return ja;
    }
    /**
     * 发布算法
     * @param supArithmeticsPackage
     * @param redirectAttributes
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:edit")
    @RequestMapping(value = "release")
    public String release(SupArithmeticsPackage supArithmeticsPackage, RedirectAttributes redirectAttributes) {
        String status = supArithmeticsPackage.getStatus();
        if("0".equals(status)){
            addMessage(redirectAttributes, "请先进行算法测试!");
        }else if("1".equals(status)){
            addMessage(redirectAttributes, "算法发布成功");
            // 修改算法包状态
            supArithmeticsPackage.setStatus("2");
            supArithmeticsPackageService.updateAriState(supArithmeticsPackage);
            /*Dict dict = new Dict();
            dict.setType("arithmetics_name");
            dict.setValue(supArithmeticsPackage.getId());
            dict.setLabel(supArithmeticsPackage.getAriName());
            dict.setDescription(supArithmeticsPackage.getPurpose());
            dict.setSort(10);
            dictService.save(dict);*/
        }else if("2".equals(status)){
            addMessage(redirectAttributes, "算法已发布,请勿多次发布!");
        }
        return "redirect:" + "/arithmetic/supArithmeticsPackage/?repage";
    }

    /**
     * 删除算法
     * @param supArithmeticsPackage
     * @param redirectAttributes
     * @return
     */
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:edit")
    @RequestMapping(value = "delete")
    public String delete(
            SupArithmeticsPackage supArithmeticsPackage, RedirectAttributes redirectAttributes) {
        int counts = supArithmeticsPackageService.selectCounts(supArithmeticsPackage);
        if(counts > 0){
            addMessage(redirectAttributes, "算法被占用,删除失败!");
        }else {
            supArithmeticsPackageService.delete(supArithmeticsPackage);
            addMessage(redirectAttributes, "删除算法成功");
        }
        return "redirect:" + "/arithmetic/supArithmeticsPackage/?repage";
    }
//    @RequiresPermissions("arithmetic:supArithmeticsPackage:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {

        addMessage(redirectAttributes, "上传成功!");
        return "redirect:" + "/arithmetic/supArithmeticsPackage/?repage";
    }
    @ResponseBody
    @RequestMapping(value = "findObjectByAirName", method=RequestMethod.POST)
    public SupArithmeticsPackage findObjectByAirName(@RequestParam(required=false) String airName) {
        return supArithmeticsPackageService.findObjectByAirName(airName);
    }

}
