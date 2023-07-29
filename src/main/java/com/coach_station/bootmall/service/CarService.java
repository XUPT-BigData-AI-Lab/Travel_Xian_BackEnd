package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.CarDao;
import com.coach_station.bootmall.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/21:48
 * @Description:
 */
@Service
public class CarService {

    @Autowired
    CarDao carDao;

    Car findByCarId(Long carId){
        return carDao.findByCarId(carId);
    }
}
