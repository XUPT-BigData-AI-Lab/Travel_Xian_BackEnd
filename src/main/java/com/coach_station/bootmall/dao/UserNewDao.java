package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/19:32
 * @Description:
 */
public interface UserNewDao extends JpaRepository<User,Integer> {
    User findByName(String name);

    User findByPhoneNumber(String phoneNumber);

    User findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "update user u" +
            " set u.name = ?1,u.sex = ?2,u.card_type = ?3,u.card_number = ?4 " +
            "where u.user_id = ?5",nativeQuery = true)
    void updateByUserProfile(String name, Integer sex, Integer cardType, String cardNumber, Long userId);


}
