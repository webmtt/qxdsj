package com.thinkgem.jeesite.modules.products.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.products.entity.ProductRepert;
import com.thinkgem.jeesite.modules.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 产品清单配置Controller
 * @author zhaoxiaojun
 * @version 2020-06-17
 */
@Controller
@RequestMapping(value = "/productRepert")
public class ProductRepertController extends BaseController {

    @Autowired
    private ProductsService productsService;

    @RequestMapping(value = {"/list",""})
    public String list(Model model) {
        ProductRepert productRepert =productsService.getProductRepert();
        model.addAttribute("productRepert", productRepert);
        return "modules/products/productRepert";
    }

    /**
     * 保存产品配置信息
     */
    @RequestMapping(value = {"/saveConfig"})
    public void saveConfig(@RequestBody Map<String, String> map, HttpServletResponse response) {
        ProductRepert productRepert = new ProductRepert();
        productRepert.setMSP1(map.get("MSP1"));
        productRepert.setMSP2(map.get("MSP2"));
        productRepert.setMSP3(map.get("MSP3"));
        int forecastCode = productsService.saveProductRepert(productRepert);
        if (forecastCode==1){
            renderText(JsonMapper.toJsonString(1), response);
        }else{
            renderText(JsonMapper.toJsonString(0), response);
        }
    }
}
