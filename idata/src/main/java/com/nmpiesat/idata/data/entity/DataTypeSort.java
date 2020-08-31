package com.nmpiesat.idata.data.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页数据服务统计
 * @author yangkq
 * @version 1.0
 * @date 2020/3/6
 */

public class DataTypeSort {
    private int cateGoryId;//产品id
    private int count;//数量
    private String name;//名称
    private List<Integer>  list=new ArrayList<>();//存储categoryid
    private List<DataTypeSort> parent=new ArrayList<>();//存储parent
    public String getName() {
        return name;
    }

    public void setCateGoryId(int cateGoryId) {
        this.cateGoryId = cateGoryId;
    }

    public void setParent(List<DataTypeSort> parent) {
        this.parent = parent;
    }

    public int getCateGoryId() {
        return cateGoryId;
    }

    public List<DataTypeSort> getParent() {
        return parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
