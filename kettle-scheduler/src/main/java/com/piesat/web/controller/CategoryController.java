package com.piesat.web.controller;

import com.piesat.common.toolkit.Constant;
import com.piesat.core.dto.BootTablePage;
import com.piesat.core.dto.ResultDto;
import com.piesat.core.model.KCategory;
import com.piesat.core.model.KUser;
import com.piesat.web.service.CategoryService;
import com.piesat.web.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/category/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("getSimpleList.shtml")
    public String simpleList(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JsonUtils.objectToJson(categoryService.getList(kUser.getuId()));
    }

    @RequestMapping("getList.shtml")
    public String getList(Integer offset, Integer limit, String targetTable, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage list = categoryService.getList(offset, limit, kUser.getuId());
        return JsonUtils.objectToJson(list);
    }

    @RequestMapping("getCategory.shtml")
    public String getQuartz(Integer categoryId) {
        return ResultDto.success(categoryService.getQuartz(categoryId));
    }

    @RequestMapping("insert.shtml")
    public String insert(KCategory kCategory, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        categoryService.insert(kCategory, kUser.getuId());
        return ResultDto.success();
    }

    @RequestMapping("delete.shtml")
    public String delete(Integer categoryId) {
        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        // 6个任务分类已确定，id不能变动，不能删除，名称可以修改
        if (categoryId <= 6) {
            return ResultDto.fail("该任务分类[编号：" + categoryId + "]不可删除!");
        } else {
            categoryService.delete(categoryId);
            return ResultDto.success();
        }
    }

    @RequestMapping("update.shtml")
    public String update(KCategory kCategory, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        try {
            categoryService.update(kCategory, kUser.getuId());
            return ResultDto.success();
        } catch (Exception e) {
            return ResultDto.success(e.toString());
        }
    }

    @RequestMapping("IsCategoryExist.shtml")
    public void IsCategoryExist(Integer categoryId, String categoryName, HttpServletResponse response) {
        try {
            if (categoryService.IsCategoryExist(categoryId, categoryName)) {
                response.getWriter().write("false");
            } else {
                response.getWriter().write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
