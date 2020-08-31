package com.nmpiesat.idata.compara.dao;

import com.nmpiesat.idata.data.entity.Comparas;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparasDao {
    List<Comparas> getComparasByKey(String key);
}
