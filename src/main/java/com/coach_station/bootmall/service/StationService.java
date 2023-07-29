package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.StationDao;
import com.coach_station.bootmall.entity.OrderInfo;
import com.coach_station.bootmall.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/15:48
 * @Description:
 */
@Service
public class StationService {

    @Autowired
    StationDao stationDao;

    public Page<Station> findByStar(Integer star, Pageable pageable){
        return stationDao.findByStar(star, pageable);
    }

    public List<Station> findAll(){
        return stationDao.findAll();
    }

}
