package com.thinkgem.jeesite.modules.sys.entity;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Userinterface {

    private String dataroleId;
    private String interfaceId;

    private List<SysInterface> menuList = Lists.newArrayList(); // 拥有菜单列表

    public void setMenuList(List<SysInterface> menuList) {
        this.menuList = menuList;
    }
    public List<SysInterface> getMenuList() {
        return menuList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        menuList = Lists.newArrayList();
        for (String menuId : menuIdList) {
            SysInterface menu = new SysInterface();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }

    public List<String> getMenuIdList() {
        List<String> menuIdList = Lists.newArrayList();
        for (SysInterface menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return menuIdList;
    }

    public String getMenuIds() {
        return StringUtils.join(getMenuIdList(), ",");
    }

    public void setMenuIds(String menuIds) {
        menuList = Lists.newArrayList();
        if (menuIds != null){
            String[] ids = StringUtils.split(menuIds, ",");
            setMenuIdList(Lists.newArrayList(ids));
        }
    }

    public String getDataroleId() {
        return dataroleId;
    }

    public void setDataroleId(String dataroleId) {
        this.dataroleId = dataroleId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Userinterface(String dataroleId, String interfaceId) {
        this.dataroleId = dataroleId;
        this.interfaceId = interfaceId;
    }

    public Userinterface(){}
}
