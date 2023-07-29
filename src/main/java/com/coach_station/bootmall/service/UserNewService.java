package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.UserNewDao;
import com.coach_station.bootmall.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/19:35
 * @Description:
 */
@Service
public class UserNewService {
    @Autowired
    UserNewDao userNewDao;

    public User findByName(String name){
        return userNewDao.findByName(name);
    }

    public User findByPhoneNumber(String phoneNumber){
        return userNewDao.findByPhoneNumber(phoneNumber);
    }

    public User findByUserId(Long userId){
        return userNewDao.findByUserId(userId);
    }

    public void updateByUserProfile(String name, Integer sex, Integer cardType, String cardNumber, Long userId){
        userNewDao.updateByUserProfile(name, sex, cardType, cardNumber,userId);
    }
    public void saveUser(User user){
        userNewDao.save(user);
    }

    public List<User> findAllUsers(){
        return userNewDao.findAll();
    }

}
