package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FileDownLoadUtil;
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Deploy;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.DeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 用户配置展示Controller
 * @author zhaoxiaojun
 * @version 2019-12-11
 */
@Controller
@RequestMapping(value = "/sys/deploy")
public class DeployController extends BaseController {

    @Autowired
    private DeployService deployService;

    /**
     * 用户配置信息添加或修改
     * @param id
     * @param photoType
     * @param fileType
     * @param urlType
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveDeploy(HttpServletRequest request, HttpServletResponse response,/*String[] productType,String[] serviceType,*/String id,String photoType,String fileType,String urlType, RedirectAttributes redirectAttributes){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbs = new StringBuilder();
        /*if (productType.length==0&&serviceType.length==0){
            addMessage(redirectAttributes, "请添加产品子类型和业务门类");
            return "modules/sys/deploy";
        }else if(productType.length==0||serviceType.length==0){
            addMessage(redirectAttributes, "请添加产品子类型或业务门类");
            return "modules/sys/deploy";
        }else{*/
            /*for (int i = 0; i < productType.length; i++) {
                if (i==productType.length-1){
                    sb.append(productType[i]);
                }else{
                    sb.append(productType[i]+",");
                }
            }
            for (int j = 0; j < serviceType.length; j++) {
                if (j==serviceType.length-1){
                    sbs.append(serviceType[j]);
                }else{
                    sbs.append(serviceType[j]+",");
                }
            }*/
        List<User> list = new ArrayList<>();
        for(int i =0;i<20;i++){
            User user = new User();
            user.setLoginName("专题数据可视化");
            user.setName("2.6");
            user.setMobile("2019-12-12 13:56:30");
            user.setLoginIp("D:/ykq-java.doc");
            list.add(user);
        }
        List<User> list1 = new ArrayList<>();
        for(int j =0;j<20;j++){
            User user1 = new User();
            user1.setLoginName("干旱专题");
            user1.setName("1.5");
            user1.setMobile("2019-12-14 09:23:56");
            user1.setLoginIp("D:/ykq-java.doc");
            list1.add(user1);
        }
            try{
                Deploy deploy = new Deploy();
                deploy.setDataroleId(id);
                deploy.setProductType(sb.toString());
                deploy.setUrlType(urlType);
                deploy.setFileType(fileType);
                deploy.setPhotoType(photoType);
                deploy.setServiceType(sbs.toString());
                deploy.setUrlAdress("/visua/visualized-"+id+photoType+fileType+".jsp");
                deploy.setId(UUID.randomUUID().toString().replace("-",""));
                //按用户id查找配置信息
                List<Deploy> deploys = deployService.getById(id);
                Map<String,Object> map = new HashMap<>();
                int index = 0;
                for (Deploy depl:deploys) {
                    if(depl.getUrlType().equals(urlType)&&depl.getFileType().equals(fileType)&&depl.getPhotoType().equals(photoType)){
                        addMessage(redirectAttributes, "已有相关配置,无需再添加");
                    }else{
                        index+=1;
                    }
                }
                if (index==deploys.size()){
                    map.put("photoType",photoType);
                    map.put("fileType",fileType);
                    map.put("page",list);
                    map.put("page1",list1);
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    realPath = realPath.replaceAll("\\\\", "/");
                    FreeMarkers.createHtml(realPath,"visualized-"+id+photoType+fileType+".jsp" , map);
                    deployService.insertDeploys(deploy);
                    addMessage(redirectAttributes, "保存用户配置成功");

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "redirect:" + "/dataRole/list";
    }

    /**
     * 跳转用户配置页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/getdeploy")
    public String getDeploy(String id, Model model) {
        model.addAttribute("uuid", id);
        return "modules/sys/deploy";
    }


    /**
     * 数据可视化
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/visualized")
    public String getALLUrl(String id,Model model){
        id="zsxssd";
        List<String> list = deployService.getALLUrl(id);
        if (list.size() !=0){
            model.addAttribute("list", list);
        }
        return "modules/sys/visualized";
    }

    @RequestMapping(value = "/download")
    public String download(String path,HttpServletResponse response,HttpServletRequest request,RedirectAttributes redirectAttributes){
        try{
            FileDownLoadUtil.download(path,response,request);
            addMessage(redirectAttributes, "下载文件成功");
        }catch (Exception e){
            e.printStackTrace();
            addMessage(redirectAttributes, "下载文件失败");
        }
        return "modules/sys/visualized";
    }
}
