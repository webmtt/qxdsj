package com.thinkgem.jeesite.modules.sys.web;


import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.Userinterface;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.service.UserInterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据授权
 * @author zhaoxiaojun 2019年12月17日14:10:23
 */
@Controller
@RequestMapping(value = "/sys/userinter")
public class UserInterController extends BaseController {

    @Autowired
    private UserInterService userInterService;

    /**
     * 保存授权结果
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(HttpServletRequest request,RedirectAttributes redirectAttributes) {
        String dataroleId =request.getParameter("dataroleId");
        String interfaceId = request.getParameter("menuIds");
        String[] strings = interfaceId.split(",");
        userInterService.deltetUserinterface(dataroleId);
        for (int i = 0; i <strings.length ; i++) {
            Userinterface userinterface = new Userinterface();
            userinterface.setDataroleId(dataroleId);
            userinterface.setInterfaceId(strings[i]);
            userInterService.saveUserinterface(userinterface);
        }
        addMessage(redirectAttributes, "保存数据成功");
        return "redirect:" + "/dataRole/list";
    }

    /**
     * 数据授权
     * @param datarole
     * @param model
     * @return
     */
    @RequestMapping(value = "/form")
    public String form(Userinterface userinterface,String id, Model model) {
        userinterface.setDataroleId(id);
        model.addAttribute("userinterface", userinterface);
        model.addAttribute("menuList", userInterService.findAllInterface());
        return "modules/sys/dataLicense";
    }

    /**
     * 登录接口
     *
     * @param path
     * @return
     *//*
    @RequestMapping(value = "login")
    @ResponseBody
    public ApiResponse tokenLogin(String path) {
        String username = "";
        String password = "";
        try {
            String data = TokenUtils.sendPost(path,"");
            username = "lisi";//data.split(",")[0];
            password = "88ec1ffa8c535311cf8eb18c0f79bdaa0ecc5547bd7e9fd0eaaeb94f";//data.split(",")[1];
        }catch (Exception e){
            e.printStackTrace();
        }
        if(username !=null && password !=null){
            //身份验证是否成功
            boolean isSuccess = systemService.checkUser(username, password);
            if (isSuccess) {
                User user = systemService.getUserByLoginName(username);
                if (user != null) {
                    //返回token
                    String token = JwtUtil.sign(user.getName(), user.getId());
                    if (token != null) {
                        return ApiResponseUtil.getApiResponse(token);
                    }
                }
            }
        }
        //返回登陆失败消息
        return ApiResponseUtil.getApiResponse(ApiResponseEnum.LOGIN_FAIL);
    }*/
}
