/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典DAO接口
 *
 * @author ThinkGem
 * @version 2013-8-23
 */
@Repository
public class DictDao extends BaseDao<Dict> {

  public List<Dict> findAllList() {
    return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
  }

  public List<Dict> findListByType(String type) {
    return find(
        "from Dict where delFlag=:p1 and type=:p2 order by sort",
        new Parameter(Dict.DEL_FLAG_NORMAL, type));
  }

  public List<String> findTypeList() {
    return find(
        "select type from Dict where delFlag=:p1 group by type",
        new Parameter(Dict.DEL_FLAG_NORMAL));
  }

  public Dict findDictByTypeAndValue(String value, String type) {
    List<Dict> list =
        find(
            "from Dict where delFlag=:p1 and value=:p2 and type=:p3 order by sort",
            new Parameter(Dict.DEL_FLAG_NORMAL, value, type));
    if (list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
