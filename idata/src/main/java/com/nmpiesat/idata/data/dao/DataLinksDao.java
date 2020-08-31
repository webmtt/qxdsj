package com.nmpiesat.idata.data.dao;

import com.nmpiesat.idata.data.entity.DataLinks;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/16
 */
@Repository
public interface DataLinksDao {
    List<DataLinks> findDataLinksByDataCode(String dataCode);
}
