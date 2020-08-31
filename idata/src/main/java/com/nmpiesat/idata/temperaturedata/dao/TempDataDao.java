package com.nmpiesat.idata.temperaturedata.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@Repository
public interface TempDataDao {

    List<HashMap> selectStaTemp();

    List<HashMap> selectStaMulTemp();
}
