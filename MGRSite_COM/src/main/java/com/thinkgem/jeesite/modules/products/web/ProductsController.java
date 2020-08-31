package com.thinkgem.jeesite.modules.products.web;


import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;
import com.thinkgem.jeesite.modules.Users.service.UserInfoService;
import com.thinkgem.jeesite.modules.products.entity.Products;
import com.thinkgem.jeesite.modules.products.entity.UploadProduct;
import com.thinkgem.jeesite.modules.products.service.ProductsService;
import com.thinkgem.jeesite.modules.sys.utils.ImageUploadUtil;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 产品库展示Controller
 * @author zhaoxiaojun
 * @version 2020-02-14
 */
@Controller
@RequestMapping(value = "/products/productsConfig")
public class ProductsController extends BaseController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private UserInfoService userInfoService;

    @ModelAttribute
    public Products get(@RequestParam(required=false) String id) {
        Products entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = productsService.get(id);
        }
        if (entity == null){
            entity = new Products();
        }
        return entity;
    }

    @RequestMapping(value = {"/list", ""})
    public String list(Products products, HttpServletRequest request, HttpServletResponse response, Model model) {
        String url= Global.getConfig("email.url");
        Page<Products> page = productsService.findPage(new Page<Products>(request, response), products);
        model.addAttribute("page", page);
        model.addAttribute("url",url );
        return "modules/products/productsList";
    }

    @RequestMapping(value = {"/productUploadList"})
    public String productUploadList(HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<UploadProduct> page = productsService.productUploadList(new Page<UploadProduct>(request, response));
        model.addAttribute("page", page);
        return "modules/products/productUpload";
    }

    @RequestMapping(value = {"/getProductUpload"})
    public void getProductUpload(String id,HttpServletResponse response) {
        String url = productsService.getProductUpload(id);
        url = "<img src='"+Global.getConfig("product.path")+url+"'/>";
        renderText(JsonMapper.toJsonString(url), response);
    }

    @RequestMapping(value = "/delete")
    public String delete(Products products, RedirectAttributes redirectAttributes) {
        productsService.delete(products);
        addMessage(redirectAttributes, "删除产品库成功");
        return "redirect:"+"/products/productsConfig/list";
    }

    @RequestMapping(value = "/deleteProductUp")
    public String deleteProductUp(String id, RedirectAttributes redirectAttributes) {
        productsService.deleteProductUp(id);
        addMessage(redirectAttributes, "删除静态资源成功");
        return "redirect:"+"/products/productsConfig/productUploadList";
    }

    @RequestMapping(value = "form")
    public String form(Products products, Model model) {
        model.addAttribute("products", products);
        return "modules/products/productsForm";
    }

    @RequestMapping(value = "formProductUp")
    public String formProductUp(UploadProduct uploadProduct, Model model) {
        model.addAttribute("productUpload", uploadProduct);
        return "modules/products/productUploadForm";
    }

    @RequestMapping(value = "save")
    public String save(Products products, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, products)){
            return form(products, model);
        }
        productsService.update(products);
        addMessage(redirectAttributes, "修改产品库配置成功");
        return "redirect:/products/productsConfig/list";
    }

    @RequestMapping(value = "saveProductUpload")
    public String saveProductUpload(UploadProduct uploadProduct, RedirectAttributes redirectAttributes) {
        productsService.saveProductUpload(uploadProduct);
        addMessage(redirectAttributes, "修改静态资源LINK成功");
        return "redirect:"+"/products/productsConfig/productUploadList";
    }

    @RequestMapping(value = "/getUser")
    public String getUser(Model model,HttpServletRequest request){
        String id = request.getParameter("id");
        Products products = productsService.get(id);
        String username = products.getUsername();
        List<UserInfo> list = userInfoService.getUsers();
        List<String> nameList = new ArrayList<>();
        if (username != null&&username != ""){
            String[] usernames = username.split(",");
            for (int i = 0; i <list.size() ; i++) {
                int index = 0;
                for (int j = 0; j <usernames.length ; j++) {
                    String str = list.get(i).getChName()+"("+list.get(i).getUserName()+")";
                    if(!str.equals(usernames[j])){
                        index+=1;
                    }
                }
               if (index==usernames.length){
                   nameList.add(list.get(i).getChName()+"("+list.get(i).getUserName()+")");
               }

            }
            model.addAttribute("userList", nameList);
            model.addAttribute("usernames", usernames);
        }else{
            model.addAttribute("userList", list);
        }
        return "modules/products/userPro";
    }

    @RequestMapping(value = "/saveUser")
    public String saveUser(HttpServletRequest request){
        String users = request.getParameter("usernames");
        String id = request.getParameter("id");
        String url = request.getParameter("url");
        String[] usernames = users.split(",");
        String name = "";
        for (int i = 0; i <usernames.length ; i++) {
            String[] use = usernames[i].split("\\(");
            String str = use[1].replace(")","");
            userInfoService.updateUrl(str,url);
        }
        productsService.updateUserName(users,id);
        return "redirect:/products/productsConfig/list";
    }

    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file,RedirectAttributes redirectAttributes){
        try{
            String fileName = file.getOriginalFilename();// 获取到上传文件的名字
            InputStream inputStream = file.getInputStream();//转换成输入流的方式
            String host = Global.getConfig("product.ip");
            int port = Integer.parseInt(Global.getConfig("product.port"));
            String userName = Global.getConfig("product.name");
            String password = Global.getConfig("product.password");
            String remoteFile = Global.getConfig("product.file");
            String url = Global.getConfig("product.url")+fileName;
            ImageUploadUtil.uploadFile(host,port,userName,password,remoteFile,inputStream,fileName);
            String id = UUID.randomUUID().toString().replace("-","");
            List<UploadProduct> list = productsService.getAllUploadProduct();
            if(list.size()!=0){
                int index = 0;
                for (int i = 0; i <list.size() ; i++) {
                    if(url.equals(list.get(i).getUrl())) {
                        index+=1;
                    }
                }
                if (index==0){
                    productsService.uploadProduct(id, url);
                    addMessage(redirectAttributes, "上传文件成功");
                }else{
                    addMessage(redirectAttributes, "文件已存在");
                }
            }else{
                productsService.uploadProduct(id, url);
                addMessage(redirectAttributes, "上传文件成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "modules/products/productUpload";
    }
}
