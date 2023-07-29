package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/10/13:15
 * @Description:
 */
public interface PassengerDao extends JpaRepository<Passenger,Integer> {

    Page<Passenger> findAllByUserIdAndIsDelete(@Param("user_id")Long userId, Pageable pageable, Integer isDelete);

    List<Passenger> findAllByUserIdAndIsDelete(Long userId, Integer isDelete);

    @Modifying
    @Transactional
    @Query(value = "update passenger p" +
            " set p.name = ?1,p.card_type = ?2,p.card_number = ?3 " +
            "where p.passenger_id = ?4",nativeQuery = true)
    void updatePassenger(String name, Integer cardType, String cardNumber, Long passengerId);

    @Transactional
    void deleteByPassengerId(Long passengerId);

    Passenger findByPassengerId(Long passengerId);

    Passenger findByUserIdAndCardNumberAndIsDelete(Long passengerId, String cardNumber, Integer isDelete);

    List<Passenger> findAllByCardNumberAndCardTypeAndName(String cardNumber, Integer cardType, String name);
}
