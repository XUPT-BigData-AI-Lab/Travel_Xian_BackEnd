package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.RegionDao;
import com.coach_station.bootmall.entity.Region;
import com.coach_station.bootmall.entity.ShuttleLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/15:38
 * @Description:
 */
@Service
public class RegionService {

    @Autowired
    RegionDao regionDao;

    List<Region> findAll(){
        return regionDao.findAll();
    }
}
