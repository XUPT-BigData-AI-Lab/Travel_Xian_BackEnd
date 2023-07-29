package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/15:47
 * @Description:
 */
public interface StationDao extends JpaRepository<Station,Integer> {
    Page<Station> findByStar(Integer star, Pageable pageable);

    Page<Station> findByStationId(Integer star, Pageable pageable);
}
