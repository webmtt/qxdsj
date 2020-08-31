package com.thinkgem.jeesite.modules.access.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.access.dao.FuncItemsDefDao;
import com.thinkgem.jeesite.modules.access.entity.FuncItemsDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncItemsDefService extends BaseService {
	@Autowired
	private FuncItemsDefDao funcitemsdefdao;

	public FuncItemsDef getByID(String id) {
		return this.funcitemsdefdao.get(Integer.valueOf(id));
	}

	public List<FuncItemsDef> findFirstItems() {
		return this.funcitemsdefdao.findFirstItems();
	}
}
