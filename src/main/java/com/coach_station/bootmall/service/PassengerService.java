package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.dao.PassengerDao;
import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.enumAndConst.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/10/13:16
 * @Description:
 */
@Service
public class PassengerService {

    @Autowired
    PassengerDao passengerDao;

    public Page<Passenger> findPassengersByUserId(Long userId, Pageable pageable){
        return passengerDao.findAllByUserIdAndIsDelete(userId,pageable, Const.NOT_DELETE);
    }

    public Passenger addPassenger(Passenger passenger){
        return passengerDao.save(passenger);
    }

    public List<Passenger> addPassengers(List<Passenger> passengers){
        return passengerDao.saveAll(passengers);
    }

    public List<Passenger> findPassengersByUserId(Long userId){
        return passengerDao.findAllByUserIdAndIsDelete(userId,Const.NOT_DELETE);
    }

    public void updatePassenger(String name, Integer cardType, String cardNumber, Long passengerId){
        passengerDao.updatePassenger(name, cardType, cardNumber, passengerId);
    }

    public void deletePassenger(Long passengerId){
        passengerDao.deleteByPassengerId(passengerId);
    }

    public Passenger findPassengerByPassengerId(Long passengerId){
        return passengerDao.findByPassengerId(passengerId);
    }

    public Passenger findByUserIdAndCardNumber(Long passengerId, String cardNumber){
        return passengerDao.findByUserIdAndCardNumberAndIsDelete(passengerId, cardNumber,Const.NOT_DELETE);
    }

    List<Passenger> findAllByCardNumberAndCardTypeAndName(String cardNumber, Integer cardType, String name){
        return passengerDao.findAllByCardNumberAndCardTypeAndName(cardNumber,cardType,name);
    }

}
