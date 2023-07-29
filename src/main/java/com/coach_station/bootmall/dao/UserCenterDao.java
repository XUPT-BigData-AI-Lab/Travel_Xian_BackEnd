package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.ContactPerson;
import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.ContactPersonVo;
import com.coach_station.bootmall.vo.ModifyPasswordVo;
import com.coach_station.bootmall.vo.PassengerVo;
import com.coach_station.bootmall.vo.UserProfileVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/13:45
 * @Description:
 */
public interface UserCenterDao {
    UserProfileVo getProfileInfo();

    ResultCodeEnum modifyProfile(UserProfileVo userProfile);

    ResultCodeEnum modifyPassword(ModifyPasswordVo modifyPasswordVo);

    Page<Passenger> getPassagers(Integer page, Integer size);

    ResultCodeEnum addPassager(PassengerVo passengerVo);

    ResultCodeEnum modifyPassager(PassengerVo passengerVo);

    ResultCodeEnum deletePassager(Long passengerId);

    Page<ContactPerson> getContactPersons(Integer page, Integer size);

    ResultCodeEnum addContactPerson(ContactPerson contactPerson);

    ResultCodeEnum modifyContactPerson(ContactPerson contactPerson);

    ResultCodeEnum deleteContactPerson(Long passengerId);

}
