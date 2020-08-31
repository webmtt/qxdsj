package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.access.entity.FuncItemsDef;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FuncItemsDefDao extends BaseDao<FuncItemsDef> {
	public List<FuncItemsDef> findFirstItems() {
		return this.find("from FuncItemsDef where funcItemId < 100 order by funcItemId");
	}
}
