package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.dao.ContactPersonDao;
import com.coach_station.bootmall.entity.ContactPerson;
import com.coach_station.bootmall.enumAndConst.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/10/23:15
 * @Description:
 */
@Service
public class ContactPersonService {
    @Autowired
    ContactPersonDao contactPersonDao;

    public Page<ContactPerson> findContactPersonByUserId(Long userId, Pageable pageable){
        return contactPersonDao.findAllByUserIdAndIsDelete(userId,pageable, Const.NOT_DELETE);
    }

    public ContactPerson addContactPerson(ContactPerson contactPerson){
        return contactPersonDao.save(contactPerson);
    }

    public List<ContactPerson> findContactPersonsByUserId(Long userId){
        return contactPersonDao.findAllByUserIdAndIsDelete(userId,Const.NOT_DELETE);
    }

    public Integer updateContactPerson(String name, String phoneNumber, String email, Long contact_person_id){
        return contactPersonDao.updateContactPerson(name, phoneNumber, email, contact_person_id);
    }

    public Integer deleteContactPerson(Long passengerId){
        return contactPersonDao.deleteByPersonId(passengerId);
    }

    public ContactPerson findContactPersonByPersonId(Long personId){
        return contactPersonDao.findByPersonId(personId);
    }

    public ContactPerson findByUserIdAndPhoneNumber(Long userId, String phoneNumber){
        return contactPersonDao.findByUserIdAndPhoneNumberAndIsDelete(userId,phoneNumber,Const.NOT_DELETE);
    }
}
