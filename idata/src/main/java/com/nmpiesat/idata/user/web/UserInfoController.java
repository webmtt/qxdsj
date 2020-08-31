package com.nmpiesat.idata.user.web;

import com.nmpiesat.idata.aboutus.entity.AboutUs;
import com.nmpiesat.idata.aboutus.service.AboutUsService;
import com.nmpiesat.idata.products.entity.ProductesConfig;
import com.nmpiesat.idata.products.service.ProductsService;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.user.entity.MusicInfo;
import com.nmpiesat.idata.user.entity.OrgInfo;
import com.nmpiesat.idata.user.entity.UserExamInfo;
import com.nmpiesat.idata.user.entity.UserInfo;
import com.nmpiesat.idata.user.service.OrgInfoService;
import com.nmpiesat.idata.user.service.UserAndDataRoleService;
import com.nmpiesat.idata.user.service.UserInfoService;
import com.nmpiesat.idata.util.Mail;
import com.nmpiesat.idata.util.StringUtil;
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
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * 用户管理相关信息
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
@Api(description = "用户管理相关信息")
@Controller
@RequestMapping(value = "/userInfo")
public class UserInfoController {
    @Autowired
    private AboutUsService aboutUsService;
    @Autowired
    private UserInfoService uiService;
    @Autowired
    private UserAndDataRoleService udrService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private OrgInfoService oiService;
    @Autowired
    private Environment environment;
    @ApiOperation(value="用户名验证", notes="用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true ,dataType = "string")
    })
    @RequestMapping("/checkUserName")
    @ResponseBody
    public RestResult checkUserName(HttpServletRequest request,HttpServletResponse response) {
        String userName= request.getParameter("username");
        boolean flag=uiService.checkUserName(userName);
        RestResult restResult = new RestResult();
        if(flag){
            restResult.setMessage("用户名可用！");
            restResult.setData(true);
        }else{
            restResult.setMessage("用户名已存在！");
            restResult.setData(false);
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }
    /**
     * 注册用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value="注册用户", notes="用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "chName", value = "姓名", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "orgName", value = "工作单位", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "办公电话", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "wechatNumber", value = "微信号", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "emailName", value = "邮箱", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "dataInfo", value = "所需数据", required = true ,dataType = "string")
    })
    @RequestMapping("/addUser")
    @ResponseBody
    public RestResult addUserInfo(HttpServletRequest request,HttpServletResponse response) {
        RestResult restResult = new RestResult();
        Map<String,Object> mapresult=new HashMap<String, Object>();
        try {
            UserInfo user=new UserInfo();
            String userName= request.getParameter("username");
            String password= request.getParameter("password");
            String chName= request.getParameter("chName");
            String orgName= request.getParameter("orgName");
            String phone= request.getParameter("phone");
            String mobile= request.getParameter("mobile");
            String wechatNumber= request.getParameter("wechatNumber");
            String emailName= request.getParameter("emailName");
            String dataInfo=request.getParameter("dataInfo");
            if(dataInfo!=null&&(!"".equals(dataInfo))){
                user.setDataInfo(dataInfo);
            }

            UserInfo userInfo = uiService.getUserInfo(userName);
            if(userInfo==null){
                Date date=new Date();
                user.setUpdated(date);
                user.setCreated(date);
                String ip=StringUtil.getRemoteAddr(request);
                user.setCreatedBy(ip);
                user.setUpdatedBy(ip);
                user.setUserName(userName);
                user.setPassword(password);
                user.setChName(chName);
                if(phone==null||"".equals(phone)) {
                    user.setPhone("9999999999");
                }else{
                    user.setPhone(phone);
                }

                user.setMobile(mobile);
                user.setWechatNumber(wechatNumber);
                if(emailName==null){
                    user.setEmailName("");
                }else {
                    user.setEmailName(emailName);
                }
                user.setiD(UUID.randomUUID().toString());
                user.setCreated(new Timestamp(new Date().getTime()));
                user.setUpdated(new Timestamp(new Date().getTime()));
                user.setDelFlag("0");
                user.setIsActive(0);
                user.setIscreate(0);
                String orgid = oiService.getOrgListBypId(orgName);
                user.setOrgID(orgid);
                if (user.getAgeRange() == null) {
                    user.setAgeRange(0);
                }
                if (user.getPhoneModel() == null) {
                    user.setPhoneModel(0);
                }
                // 默认国家级用户权限
                user.setUserRankID(2);
//            String checkRole = request.getParameter("selectRole");
//            if("2".equals(dataType)){
//                //新增数据角色
//                udrService.insertDataRole(orgName,checkRole);
//            }

//            udrService.saveBatch(checkRole, user.getiD());
                uiService.saveUserInfo(user);
                mapresult.put("flag",true);
                mapresult.put("message","注册成功，等待审核！");
            }else{
                mapresult.put("flag",false);
                mapresult.put("message","用户名已存在！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mapresult.put("flag",false);
            mapresult.put("message","注册失败！");
        }
        restResult.setMessage("请求成功！");
        restResult.setData(mapresult);
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }
    /**
     * 主页登录
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value="登录模块", notes="用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/login")
    @ResponseBody
    public RestResult login(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("username");
         String password=request.getParameter("password");
        UserInfo userInfo = uiService.getUserInfo(userName);
        RestResult restResult = new RestResult();
        Map<String,Object> map=new HashMap<>();
        if(null!=userInfo) {
            MusicInfo musicInfo = uiService.getMusicInfo(userInfo);
            ProductesConfig pui=productsService.getProductByUrl(userInfo.getUrl());
            int isActive = userInfo.getIsActive();
            if (password.equals(userInfo.getPassword())) {
                if (isActive == 0) {
                    map.put("flag",false);
                    map.put("message","该用户正在审核，请稍后！");
                } else if (isActive == 1) {
                    // 审核通过
                    map.put("flag",true);
                    map.put("message","登录成功！");
                    map.put("userinfo",userInfo);
                    map.put("musicinfo",musicInfo);
                    map.put("userproduct",pui);
                    HttpSession session=request.getSession();
                    System.out.println("sessionId="+session.getId());
                    request.getSession().setAttribute("loginUser", session);
                }else{
                    List<UserExamInfo> list = uiService.getExamInfo(userName);
                    // 审核不通过
                    map.put("flag",false);
                    map.put("message",list.get(0).getRemarks());
                }
            } else {
                map.put("flag",false);
                map.put("message","密码错误！");
            }
        }else{
            map.put("flag",false);
            map.put("message","用户不存在，请先注册！");
        }
        restResult.setData(map);
        restResult.setMessage("请求成功!");
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @ApiOperation(value="退出登录", notes="用户管理")
    @RequestMapping(value = "/existLogin")
    @ResponseBody
    public RestResult existLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String,Object> message=new HashMap<String,Object>();
        session.removeAttribute("chName");
        session.removeAttribute("user");
        message.put("flag",true);
        RestResult restResult = new RestResult();
        restResult.setMessage("退出成功！");
        restResult.setData(message);
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }
    /**
     * 忘记密码发送验证码邮件
     *
     * @param username
     */
    @ApiOperation(value="忘记密码发送验证码", notes="用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/findPasswordMail")
    @ResponseBody
    public RestResult findPasswordMail(String username) {
        UserInfo userInfo = uiService.getUserInfo(username);
//        String activeCode = createActiveCode();
//        userInfo.setActiveCode(activeCode);
//        uiService.setActiveCode(userInfo);
//        String emailHost=environment.getProperty("email.host");
//        String emailIp=environment.getProperty("email.ip");
//        String password=environment.getProperty("email.password");
//        String port=environment.getProperty("email.port");
//        String url=environment.getProperty("email.url");
//        StringBuffer sb = new StringBuffer(username + "用户：<br>&ensp;&ensp;&ensp;&ensp;您好，");
//        sb.append("感谢注册内蒙古业务内网。给您带来的不便，敬请谅解！");
//        sb.append("<br>&ensp;&ensp;&ensp;&ensp;请点击链接:<url>" +url+""+activeCode+"</url>");
//       boolean flag=Mail.sendMail(userInfo.getEmailName(), "找回密码", sb.toString(),emailHost,emailIp,password,port);
        RestResult restResult = new RestResult();
        restResult.setMessage("请求成功！");
//        if(flag) {
//            restResult.setData("发送邮件成功！");
//        }else{
//            restResult.setData("发送邮件失败！");
//        }
        List<AboutUs> aboutUs = aboutUsService.findList();
        if(userInfo!=null){
            restResult.setData("请通知系统管理员重置密码，电话:"+aboutUs.get(0).getTelephone()+"！");
        }else{
            restResult.setData("用户不存在，请注册用户！");
        }

        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }
    /**
     * 修改密码
     *
     * @param username
     */
    @ApiOperation(value="修改密码", notes="用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "oldpassword", value = "旧密码", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/updatePassword")
    @ResponseBody
    public RestResult findPasswordMail(String username,String password,String oldpassword) {
        UserInfo userInfo = uiService.getUserInfo(username);
        RestResult restResult = new RestResult();
        Map<String,Object> message=new HashMap<String,Object>();
        if(null!=userInfo) {
            if (oldpassword.equals(userInfo.getPassword())) {
                uiService.updatePassword(username, password);
                message.put("flag", true);
                restResult.setMessage("密码修改成功！");
            } else {
                restResult.setData(false);
                restResult.setMessage("原密码错误！");
            }
        }else{
            restResult.setData(false);
            restResult.setMessage("用户不存在！");
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }
    /**
     * 随机产生激活码
     *
     * @return
     */
    private String createActiveCode() {
        Random r = new Random();
        String activeCode = r.nextInt(899999) + 100000 + "";
        return activeCode;
    }

    /**
     * 获取工作单位
     * @param keyword
     * @return
     */
    @ApiOperation(value="获取工作单位", notes="用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "单位关键字", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/getOrgInfo")
    @ResponseBody
    public RestResult getOrgInfo(String keyword) {
        List<OrgInfo> oi = oiService.getOrgInfo(keyword);
        RestResult restResult = new RestResult();
        Map<String,Object> message=new HashMap<String,Object>();
        restResult.setData(oi);
        restResult.setMessage("查询成功！");
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        return restResult;
    }
}
