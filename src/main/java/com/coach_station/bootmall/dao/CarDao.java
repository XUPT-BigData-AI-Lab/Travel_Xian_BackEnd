package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/21:47
 * @Description:
 */
public interface CarDao extends JpaRepository<Car,Integer> {
    Car findByCarId(Long carId);
}
