package com.thinkgem.jeesite.modules.products.web;


import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.products.entity.Newproducts;
import com.thinkgem.jeesite.modules.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 最新产品库配置Controller
 * @author zhaoxiaojun
 * @version 2020-03-04
 */
@Controller
@RequestMapping(value = "/newProducts")
public class NewProductsController extends BaseController {


    @Autowired
    private ProductsService productsService;

    @RequestMapping(value = {"/list",""})
    public String list(Model model) {
        List<Map<String,Object>> list = productsService.getNewproducts();
        model.addAttribute("newproducts1", list.get(0));
        model.addAttribute("newproducts2", list.get(1));
        return "modules/products/newProducts";
    }

    /**
     * 保存产品配置信息
     */
    @RequestMapping(value = {"/saveConfig"})
    public void saveConfig(@RequestBody Newproducts newproducts, HttpServletResponse response) {
        int alertCode = 0;
        int forecastCode = 0;
        newproducts.setCreated(new Date());
        if (newproducts.getAlerteproduct()!=""||newproducts.getAlertetype()!=""||newproducts.getAlerteunit()!=""){
            alertCode = productsService.saveAlert(newproducts);
        }
        if (newproducts.getForecastproduct()!=""||newproducts.getForecasttype()!=""||newproducts.getForecastunit()!=""){
            forecastCode = productsService.saveForecast(newproducts);
        }
        if (alertCode==1||forecastCode==1){
            renderText(JsonMapper.toJsonString(1), response);
        }else if(alertCode==0&&forecastCode==0){
            renderText(JsonMapper.toJsonString(0), response);
        }

    }
}
