package com.thinkgem.jeesite.modules.Users;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class test {
  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < 10; i++) {
      list.add(i + "");
    }
    String t = JSON.toJSONString(list);
    System.out.println(t);
  }
}
