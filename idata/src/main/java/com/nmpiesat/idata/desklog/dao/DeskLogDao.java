package com.nmpiesat.idata.desklog.dao;

import com.nmpiesat.idata.desklog.entity.DeskLog;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskLogDao {
    int insert(DeskLog record);

}