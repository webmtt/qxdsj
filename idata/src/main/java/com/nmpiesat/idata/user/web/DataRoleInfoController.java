package com.nmpiesat.idata.user.web;

import com.nmpiesat.idata.data.entity.DataCateDef;
import com.nmpiesat.idata.data.entity.DataCategoryDef;
import com.nmpiesat.idata.data.service.DataCategoryDefService;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.user.entity.DataRole;
import com.nmpiesat.idata.user.entity.DataRolelimits;
import com.nmpiesat.idata.user.entity.ZTreeInfo;
import com.nmpiesat.idata.user.service.DataRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据授权角色相关信息
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
@Api(description = "数据角色授权")
@Controller
@RequestMapping(value = "/dataRole")
public class DataRoleInfoController {
    @Autowired
    private DataCategoryDefService dataCategoryDefService;
    @Autowired
    private DataRoleService dataRoleService;

    @ApiOperation(value="数据授权角色信息获取", notes="数据授权角色信息获取")
    @RequestMapping(value = "/getDataRoleInfo")
    @ResponseBody
    public RestResult getDataRoleInfo(HttpServletRequest request, HttpServletResponse response) {
        // 编码或名称
        List<DataRole> list = dataRoleService.findAll();
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(list);
        return restResult;
    }
    @ApiOperation(value="查看数据角色信息", notes="数据授权角色信息查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataroleid", value = "数据角色id", required = true ,dataType = "string")
    })
    @RequestMapping(value = "/getSelectDataView")
    @ResponseBody
    public RestResult getSelectDataView(HttpServletRequest request, HttpServletResponse response) {
        String dataroleid = request.getParameter("dataroleid");
        List<DataRolelimits> listSelect=null;
        List<String> listSelectDataId = new ArrayList<>();
        if(dataroleid!=null&&(!"".equals(dataroleid))) {
            listSelect = dataRoleService.getDataRoleLimitsById(dataroleid);
            for (DataRolelimits drl : listSelect) {
                listSelectDataId.add(drl.getDataId());
            }
        }
        List<DataCategoryDef> list = dataCategoryDefService.findAll();
        // 获取数据集
        List<DataCateDef> listdata = dataCategoryDefService.findDataCategory();
        List<ZTreeInfo> rs = new ArrayList<ZTreeInfo>();
        ZTreeInfo zt = null;
        for (DataCategoryDef d : list) {
            zt = new ZTreeInfo();
            if(!listSelectDataId.isEmpty()) {
                if (!listSelectDataId.contains(d.getCategoryid() + "")) {
                    continue;
                }
            }
            zt.setCategoryid(d.getCategoryid() + "");
            zt.setChnname(d.getChnname());
            zt.setPid(d.getParentid());
            rs.add(zt);
        }
        for (DataCateDef t : listdata) {
            zt = new ZTreeInfo();
            if(!listSelectDataId.isEmpty()) {
                if (!listSelectDataId.contains(t.getDataCode())) {
                    continue;
                }
                if (!listSelectDataId.contains(t.getCategoryid() + "")) {
                    continue;
                }
            }
            zt.setCategoryid(t.getDataCode() + "");
            zt.setChnname(t.getChnname());
            zt.setPid(Integer.parseInt(t.getCategoryid() + ""));
            rs.add(zt);
        }
        RestResult restResult = new RestResult();
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(new Date().getTime()));
        restResult.setMessage("查询数据成功");
        restResult.setData(rs);
        return restResult;
    }
}
