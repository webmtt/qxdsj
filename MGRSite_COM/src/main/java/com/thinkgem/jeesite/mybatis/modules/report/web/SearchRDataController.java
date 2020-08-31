package com.thinkgem.jeesite.mybatis.modules.report.web;


import com.thinkgem.jeesite.mybatis.modules.report.service.SearchRDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 周学东软件R文件查询接口Controller
 * @author zhongrongjie
 * @date 2020-3-10
 */
@Controller
@RequestMapping(value = "/report/searchrdata")
public class SearchRDataController {

    @Autowired
    private SearchRDataService searchRDataService;


    /**
     * 按月份month查询表radi_chn_mut_mmon_tab中的数据
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByMon")
    @ResponseBody
    public List<Map<String,Object>> queryDataByMon(HttpServletRequest request, HttpServletResponse response) {
      Integer month= Integer.parseInt(request.getParameter("month")) ;
        List<Map<String,Object>> list = searchRDataService.selectByMon(month);
        return list;
    }

    /**
     * 按月份month与候state查询表radi_chn_mut_mpen_tab的数据
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByMPen")
    @ResponseBody
    public List<Map<String,Object>> queryDataByMPen(HttpServletRequest request, HttpServletResponse response) {
        Integer month= Integer.parseInt(request.getParameter("month")) ;
        Integer state= Integer.parseInt(request.getParameter("state")) ;
        List<Map<String,Object>> list = searchRDataService.selectByMPen(month,state);
        return list;
    }

    /**
     * 按月份month与旬tenDays查询表radi_chn_mut_mten_tab的数据
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByMTen")
    @ResponseBody
    public List<Map<String,Object>> queryDataByMTen(HttpServletRequest request, HttpServletResponse response) {
        Integer month= Integer.parseInt(request.getParameter("month")) ;
        Integer tenDays= Integer.parseInt(request.getParameter("tenDays")) ;
        List<Map<String,Object>> list = searchRDataService.selectByMTen(month,tenDays);
        return list;
    }

    /**
     * 按年year与月份month及候state查询表radi_chn_mut_pen_tab
     * 该表只有1957年，月份只有1月
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByPen")
    @ResponseBody
    public List<Map<String,Object>> queryDataByPen(HttpServletRequest request, HttpServletResponse response) {
        Integer year= Integer.parseInt(request.getParameter("year")) ;
        Integer month= Integer.parseInt(request.getParameter("month")) ;
        Integer state= Integer.parseInt(request.getParameter("state")) ;
        List<Map<String,Object>> list = searchRDataService.selectByPen(year,month,state);
        return list;
    }

    /**
     * 按年月旬查询表radi_chn_mut_pen_tab
     * 其中该表只有2013年12月
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByTen")
    @ResponseBody
    public List<Map<String,Object>> queryDataByTen(HttpServletRequest request, HttpServletResponse response) {
        Integer year= Integer.parseInt(request.getParameter("year")) ;
        Integer month= Integer.parseInt(request.getParameter("month")) ;
        Integer tenDays= Integer.parseInt(request.getParameter("tenDays")) ;
        List<Map<String,Object>> list = searchRDataService.selectByTen(year,month,tenDays);
        return list;
    }

    /**
     * 按年份查询表radi_chn_mut_yer_tab,该表只有2005年
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByYer")
    @ResponseBody
    public List<Map<String,Object>> queryDataByYer(HttpServletRequest request, HttpServletResponse response) {
        Integer year= Integer.parseInt(request.getParameter("year")) ;
        List<Map<String,Object>> list = searchRDataService.selectByYer(year);
        return list;
    }

    /**
     * 按年月日时分查询表radi_dig_chn_mul_tab//
     * 该表只有2018年3月份且日、时、分、参数全为0
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryDataByDate")
    @ResponseBody
    public List<Map<String,Object>> queryDataByDate(HttpServletRequest request, HttpServletResponse response) {
        Integer year= Integer.parseInt(request.getParameter("year")) ;
        Integer month= Integer.parseInt(request.getParameter("month")) ;
        Integer day= Integer.parseInt(request.getParameter("day")) ;
        Integer hour= Integer.parseInt(request.getParameter("hour")) ;
        Integer minute= Integer.parseInt(request.getParameter("minute")) ;
        List<Map<String,Object>> list = searchRDataService.selectByDate(year,month,day,hour,minute);
        return list;
    }
}
