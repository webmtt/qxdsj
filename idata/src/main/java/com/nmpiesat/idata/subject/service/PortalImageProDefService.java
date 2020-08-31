package com.nmpiesat.idata.subject.service;

import com.nmpiesat.idata.subject.dao.SubjectDao;
import com.nmpiesat.idata.subject.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
@Service
public class PortalImageProDefService {
    @Autowired
    private SubjectDao dao;
    public List<PortalImageProDefModel> getPortalImageProDefList(String musicurl) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // model的list
        List<PortalImageProDefModel> alllist = new ArrayList<PortalImageProDefModel>();
        // 查询出所有的数据
        List<PortalImageProDef> templist = dao.getPortalImageProDefList();
        // 获得有效的大类过程
        List<PortalImageRull> listq = dao.getPortalImageRullListByType();
            for (int x = 0; x < listq.size(); x++) {
                for (int i = 0; i < templist.size(); i++) {
                    if (listq.get(x).getTid() == Integer.valueOf(templist.get(i).getId())) {
                        // 临时的对象里面包括一个list集合，其他都相同
                        PortalImageProDefModel pipf = new PortalImageProDefModel();
                        PortalImageProDef hsj = templist.get(i);
                        // 等于0的时候是静态
                        pipf.setTempid(listq.get(x).getId() + listq.get(x).getTid() + "");
                        if (hsj.getIsStatic() == 0) {
                            if (hsj.getImageurl() == null) {
                                pipf.setImageurl("");
                            } else {
                                pipf.setImageurl(hsj.getImageurl().trim());
                            }
                            if (hsj.getLinkurl() == null) {
                                pipf.setLinkurl("");
                            } else {
                                pipf.setLinkurl(hsj.getLinkurl().trim());
                            }
                            if ((!hsj.getImageurl().startsWith("http")) && (!hsj.getImageurl().equals(""))) {
                                pipf.setImageurl(hsj.getImageurl());
                            }
                            if (hsj.getLinkurl().indexOf("<ctx>") != -1) {
                                pipf.setLinkurl(hsj.getLinkurl());
                            }
                        } else if (hsj.getIsStatic() == 1) {
                            // 1的情况下自己库里面获取
                            if (hsj.getLinkurl() == null) {
                                pipf.setLinkurl("");
                            } else {
                                pipf.setLinkurl(hsj.getLinkurl().trim());
                            }
                            if (hsj.getLinkurl().indexOf("<ctx>") != -1) {
                                pipf.setLinkurl(hsj.getLinkurl());
                            }
                            pipf.setId(hsj.getId());
                            pipf.setTitle(hsj.getTitle());
                            pipf.setProductCode(hsj.getProductCode());
                            pipf.setDataCode(hsj.getDataCode());
                            pipf.setDataSourse(hsj.getDataSourse());
                            pipf.setImageurl(hsj.getImageurl());
                            pipf.setIsStatic(hsj.getIsStatic());
                            pipf.setShowType(hsj.getShowType());
                            pipf.setInterFaceId(hsj.getInterFaceId());
                            pipf.setConditions(hsj.getConditions());
                            pipf.setIsPlay(hsj.getIsPlay());
                            pipf.setElements(hsj.getElements());
                            pipf.setStartTime(listq.get(x).getStartTime());
                            pipf.setDefaultTime(hsj.getDefaultTime());
                            pipf.setDefaultCount(hsj.getDefaultCount());
                            pipf.setTableName(hsj.getTableName());
                            String tablename = hsj.getTableName();
                            String productcode = hsj.getProductCode();
                            List<String> oblist = new ArrayList<String>();
                            // 等于1就获取最新的一张图片，否则获取图片list列表
                            if (hsj.getDefaultCount() == 1) {
                                String maxDate = getProImgMaxDate(tablename,productcode);
                                List strlist = new ArrayList();
                                String[] strs = productcode.split(";");
                                for (int k = 0; k < strs.length; k++) {
                                    strlist.add(strs[k]);
                                }
                                String imgeurl="";
                                List<String> imgeurlList = dao.getNowPng1(tablename,maxDate, strlist);
                               if(imgeurlList.size()>0){
                                   imgeurl= imgeurlList.get(0);
                               }
                                pipf.setImageurl(imgeurl);
                            } else {
                                // 根据开始时间或者
                                Date starttime = listq.get(x).getStartTime();
                                // 返回的数量
                                int defaultcount = hsj.getDefaultCount();
                                    if (starttime != null) {
                                        oblist =dao.getNowPngList(
                                                        sdf.format(starttime), sdf.format(new Date()), tablename, productcode);
                                    } else {
                                        oblist = dao.getNowPngList1(tablename, productcode, defaultcount);
                                    }
                            }
                            List<String> clist = new ArrayList<String>();
                            for (int j = 0; j < oblist.size(); j++) {
                                clist.add(oblist.get(j));
                            }
                            pipf.setList(clist);
                            alllist.add(pipf);
                            // 首先有table
                        } else if (hsj.getIsStatic() == 2) {
                            if (hsj.getLinkurl() == null) {
                                pipf.setLinkurl("");
                            } else {
                                pipf.setLinkurl(hsj.getLinkurl().trim());
                            }
                            if (hsj.getLinkurl().indexOf("<ctx>") != -1) {
                                pipf.setLinkurl(hsj.getLinkurl());
                            }
                            pipf.setId(hsj.getId());
                            pipf.setTitle(hsj.getTitle());
                            pipf.setProductCode(hsj.getProductCode());
                            pipf.setDataCode(hsj.getDataCode());
                            pipf.setDataSourse(hsj.getDataSourse());
                            pipf.setImageurl(hsj.getImageurl());
                            pipf.setIsStatic(hsj.getIsStatic());
                            pipf.setShowType(hsj.getShowType());
                            pipf.setInterFaceId(hsj.getInterFaceId());
                            pipf.setConditions(hsj.getConditions());
                            pipf.setIsPlay(hsj.getIsPlay());
                            pipf.setElements(hsj.getElements());
                            pipf.setStartTime(listq.get(x).getStartTime());
                            pipf.setDefaultTime(hsj.getDefaultTime());
                            pipf.setDefaultCount(hsj.getDefaultCount());
                            // music接口获取数据
                            List<String> list = new ArrayList<String>();
//                                list =getLDUrlMusic(
//                                                listq.get(x).getStartTime(),
//                                                hsj.getInterFaceId(),
//                                                hsj.getDataCode(),
//                                                hsj.getConditions(),
//                                                hsj.getDefaultCount(),
//                                                musicurl,
//                                                listq.get(x).getType(),
//                                                listq.get(x).getArea(),
//                                                ServerUrl152);

                            if (hsj.getDefaultCount() == 1) {
                                if (list.size() > 0) {
                                    String imgeurl = list.get(0);
                                    pipf.setImageurl(imgeurl);
                                }
                            } else {
                                pipf.setList(list);
                            }
                            alllist.add(pipf);
                        } else if (hsj.getIsStatic() == 3) {
                            if (hsj.getLinkurl() == null) {
                                pipf.setLinkurl("");
                            } else {
                                pipf.setLinkurl(hsj.getLinkurl().trim());
                            }
                            if (hsj.getLinkurl().indexOf("<ctx>") != -1) {
                                pipf.setLinkurl(hsj.getLinkurl());
                            }
                            pipf.setId(hsj.getId());
                            pipf.setTitle(hsj.getTitle());
                            pipf.setProductCode(hsj.getProductCode());
                            pipf.setDataCode(hsj.getDataCode());
                            pipf.setDataSourse(hsj.getDataSourse());
                            pipf.setImageurl(hsj.getImageurl());
                            pipf.setIsStatic(hsj.getIsStatic());
                            pipf.setShowType(hsj.getShowType());
                            pipf.setInterFaceId(hsj.getInterFaceId());
                            pipf.setConditions(hsj.getConditions());
                            pipf.setIsPlay(hsj.getIsPlay());
                            pipf.setElements(hsj.getElements());
                            pipf.setStartTime(listq.get(x).getStartTime());
                            pipf.setDefaultTime(hsj.getDefaultTime());
                            pipf.setDefaultCount(hsj.getDefaultCount());
                            // 台风预警
                            Date starttime = listq.get(x).getStartTime();
                            List<NationWarnFileInfo> list = new ArrayList<NationWarnFileInfo>();
                                if (starttime == null) {
                                    // 默认24小时
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(new Date());
                                    calendar.add(Calendar.HOUR, -hsj.getDefaultTime());
                                    list =dao.findNationWarnFile(
                                                    sdf.format(calendar.getTime()), sdf.format(new Date()), "台风预警");
                                } else {
                                    list =dao.findNationWarnFile(
                                                    sdf.format(starttime), sdf.format(new Date()), "台风预警");
                                }

                            if (list.size() == 0) {
                                pipf.setImageurl(hsj.getImageurl());
                            } else {
                                // 取到内容中的第一章图片
                                List<String> clist = new ArrayList<String>();
                                for (int j = 0; j < list.size(); j++) {
                                    List<String> piclist = getImgStr(list.get(j).getContent());
                                    if (piclist.size() != 0) {
                                        // pipf.setImageurl(stringValue +"/"+ hsj.getImageurl());
                                        clist.add(piclist.get(0));
                                        break;
                                    } else {
                                        // pipf.setImageurl(piclist.get(0));
                                    }
                                }
                                if (clist.size() == 0) {
                                    pipf.setImageurl(hsj.getImageurl());
                                } else {
                                    pipf.setImageurl(clist.get(0));
                                }
                            }
                            alllist.add(pipf);

                        } else if (hsj.getIsStatic() == 4) {
                            if (hsj.getLinkurl() == null) {
                                pipf.setLinkurl("");
                            } else {
                                pipf.setLinkurl(hsj.getLinkurl().trim());
                            }
                            if (hsj.getLinkurl().indexOf("<ctx>") != -1) {
                                pipf.setLinkurl(hsj.getLinkurl());
                            }
                            pipf.setId(hsj.getId());
                            pipf.setTitle(hsj.getTitle());
                            pipf.setProductCode(hsj.getProductCode());
                            pipf.setDataCode(hsj.getDataCode());
                            pipf.setDataSourse(hsj.getDataSourse());
                            pipf.setImageurl(hsj.getImageurl());
                            pipf.setIsStatic(hsj.getIsStatic());
                            pipf.setShowType(hsj.getShowType());
                            pipf.setInterFaceId(hsj.getInterFaceId());
                            pipf.setConditions(hsj.getConditions());
                            pipf.setIsPlay(hsj.getIsPlay());
                            pipf.setElements(hsj.getElements());
                            pipf.setStartTime(listq.get(x).getStartTime());
                            pipf.setDefaultTime(hsj.getDefaultTime());
                            pipf.setDefaultCount(hsj.getDefaultCount());
                            pipf.setTableName(hsj.getTableName());
                            String tablename = hsj.getTableName();
                            String productcode = hsj.getProductCode();
                            List<String> oblist = new ArrayList<String>();
                            // 等于1就获取最新的一张图片，否则获取图片list列表
                            if (hsj.getDefaultCount() == 1) {
                                String imgeurl = dao.getNowPng(productcode);
                                pipf.setImageurl(imgeurl);
                            } else {
                                // 根据开始时间或者
                                Date starttime = listq.get(x).getStartTime();
                                // 返回的数量
                                int defaultcount = hsj.getDefaultCount();

                                    if (starttime != null) {
                                        oblist =dao.getNowPngList(
                                                        sdf.format(starttime), sdf.format(new Date()), tablename, productcode);
                                    } else {
                                        oblist = dao.getNowPngList1(tablename, productcode, defaultcount);
                                    }
                            }
                            List<String> clist = new ArrayList<String>();
                            for (int j = 0; j < oblist.size(); j++) {
                                clist.add(oblist.get(j));
                            }
                            pipf.setList(clist);
                            alllist.add(pipf);
                        }
                    }
                }
            }

        return alllist;
    }

    public static List<String> getImgStr(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }
    /**
     * 获取最新的时间
     *@param tablename
     * @param productCode
     * @return
     */
    public String getProImgMaxDate(String tablename,String productCode) {
        List<String> strlist = new ArrayList<String>();
        String[] strs = productCode.split(";");
        for (int i = 0; i < strs.length; i++) {
            strlist.add(strs[i]);
        }
        List<Object> list =
                dao.getProImgMaxDate(tablename,strlist);
        if (list.get(0) != null) {
            return list.get(0).toString();
        }
        return "";
    }
}
