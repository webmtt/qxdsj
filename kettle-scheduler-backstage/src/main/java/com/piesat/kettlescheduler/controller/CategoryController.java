package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.BootTablePage;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.mapper.KCategoryDao;
import com.piesat.kettlescheduler.model.KCategory;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.CategoryService;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private KCategoryDao kCategoryDao;

    @RequestMapping("getSimpleList.shtml")
    public String simpleList(HttpServletRequest request) {
        List<KCategory> resultList = new ArrayList<KCategory>();
        KCategory kCategory = new KCategory();
        kCategory.setDelFlag(1);
//        kCategory.setAddUser(uId);
        resultList.addAll(kCategoryDao.template(kCategory));



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
        return Result.success(categoryService.getQuartz(categoryId));
    }

    @RequestMapping("insert.shtml")
    public String insert(KCategory kCategory, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        categoryService.insert(kCategory, kUser.getuId());
        return Result.success();
    }

    @RequestMapping("delete.shtml")
    public String delete(Integer categoryId) {
        // 1:文件推送，2:库表推送，3:接口推送，4:文件获取，5:库表获取，6:接口获取
        // 6个任务分类已确定，id不能变动，不能删除，名称可以修改
        if (categoryId <= 6) {
            return Result.fail("该任务分类[编号：" + categoryId + "]不可删除!");
        } else {
            categoryService.delete(categoryId);
            return Result.success();
        }
    }

    @RequestMapping("update.shtml")
    public String update(KCategory kCategory, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        try {
            categoryService.update(kCategory, kUser.getuId());
            return Result.success();
        } catch (Exception e) {
            return Result.success(e.toString());
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
