package com.nmpiesat.idata.aboutus.dao;

import com.nmpiesat.idata.aboutus.entity.AboutUs;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface AboutUsDao {

    List<AboutUs> findList();
}
