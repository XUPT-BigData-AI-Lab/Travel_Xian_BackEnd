package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.ContactPerson;
import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.entity.User;
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
 * @Date: 2022/01/10/23:12
 * @Description:
 */
public interface ContactPersonDao extends JpaRepository<ContactPerson,Integer> {
    Page<ContactPerson> findAllByUserIdAndIsDelete(Long userId, Pageable pageable, Integer isDelete);

    List<ContactPerson> findAllByUserIdAndIsDelete(Long userId, Integer isDelete);

    @Modifying
    @Transactional
    @Query(value = "update contact_person c" +
            " set c.name = ?1,c.phone_number = ?2,c.email = ?3 " +
            "where c.person_id = ?4",nativeQuery = true)
    Integer updateContactPerson(String name, String phoneNumber, String email, Long contact_person_id);

    @Transactional
    Integer deleteByPersonId(Long contactPersonId);

    ContactPerson findByPersonId(Long contactPersonId);

    ContactPerson findByUserIdAndPhoneNumberAndIsDelete(Long userId, String phoneNumber, Integer isDelete);
}
