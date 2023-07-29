package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.dao.UserCenterDao;
import com.coach_station.bootmall.entity.ContactPerson;
import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.entity.User;
import com.coach_station.bootmall.enumAndConst.CardTypeEnum;
import com.coach_station.bootmall.enumAndConst.Const;
import com.coach_station.bootmall.enumAndConst.SexEnum;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.ModifyPasswordVo;
import com.coach_station.bootmall.vo.PassengerVo;
import com.coach_station.bootmall.vo.UserProfileVo;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/13:48
 * @Description:
 */
@Log4j2
@Service
public class UserCenterService implements UserCenterDao {
    @Autowired
    UserNewService userNewService;

    @Autowired
    PassengerService passengerService;

    @Autowired
    ContactPersonService contactPersonService;

    @Override
    public UserProfileVo getProfileInfo() {
        Long userId = (Long) getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            log.info("Controller: getProfile,Service: getProfileInfo Session中的userId为空");
            return null;
        }
        User user = userNewService.findByUserId(userId);
        if (user == null) {
            log.info("Controller: getProfile,Service: getProfileInfo findByUserId的user为空，userId为" + userId);
            return null;
        }
        return packUserProfile(user);
    }

    @Override
    public ResultCodeEnum modifyProfile(UserProfileVo userProfile) {
        Long userId = (Long) getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            log.info("Controller: modifyProfile,Service: modifyProfile Session中的userId为空");
            return ResultCodeEnum.USERCENTER_UPDATEPROFILE_ERROR;
        }
        Integer sex = SexEnum.getSexIndex(userProfile.getSex());
        Integer cardType = CardTypeEnum.getCardIndex(userProfile.getCardType());
        userNewService.updateByUserProfile(userProfile.getName(),
                sex,cardType,
                userProfile.getCardNumber(),userId);
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum addPassager(PassengerVo passengerVo) {
        ResultCodeEnum checkParamsCode = checkParamsOfAddPassager(passengerVo);
        if (!checkParamsCode.getSuccess()){
            return checkParamsCode;
        }
        Long userId = (Long)getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            log.info("Controller: addPassager,Service: addPassager Session中的userId为空");
            return null;
        }
        ResultCodeEnum checkRepeatCode = checkRepeatCardNumber(userId, passengerVo.getCardNumber());
        if (!checkRepeatCode.getSuccess()){
            log.info("Controller: addPassager,Service: addPassager 证件号码已存在乘车人中，passengerVo: " + passengerVo);
            return checkRepeatCode;
        }
        packAndAddPassager(passengerVo, userId);
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum modifyPassager(PassengerVo passengerVo) {
        if (passengerVo.getPassengerId() == null || passengerVo.getPassengerId() < 0){
            return ResultCodeEnum.USERCENTER_ADDPASSGERS_ERROR;
        }
        ResultCodeEnum checkParamsCode = checkParamsOfAddPassager(passengerVo);
        if (!checkParamsCode.getSuccess() ){
            return checkParamsCode;
        }

        Long userId = (Long)getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            log.info("Controller: modifyPassager,Service: modifyPassager Session中的userId为空");
            return ResultCodeEnum.USERCENTER_UPDATEPROFILE_ERROR;
        }
        ResultCodeEnum checkRepeatCode = checkRepeatCardNumberAndPassengerId(userId, passengerVo.getCardNumber(), passengerVo.getPassengerId());
        if (!checkRepeatCode.getSuccess()){
            return checkRepeatCode;
        }
        passengerService.updatePassenger(passengerVo.getName(),CardTypeEnum.getCardIndex(passengerVo.getCardType()),
                passengerVo.getCardNumber(),passengerVo.getPassengerId());
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum deletePassager(Long passengerId) {
        ResultCodeEnum code = checkPassengerId(passengerId);
        if (!code.getSuccess())
            return code;
//        passengerService.deletePassenger(passengerId);
        Long userId = (Long)getSessionAttribute("userId");
        Passenger passenger = passengerService.findPassengerByPassengerId(passengerId);
        if (passenger == null || !passenger.getUserId().equals(userId)){
            return ResultCodeEnum.USERCENTER_DELETEPASSGER_ERROR;
            //TODO 打日志
        }
        passenger.setIsDelete(Const.DELETE);
        Passenger addResult = passengerService.addPassenger(passenger);
        if (addResult == null || addResult.getIsDelete().equals(Const.NOT_DELETE)){
            return ResultCodeEnum.USERCENTER_DELETEPASSGER_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public Page<Passenger> getPassagers(Integer page, Integer size) {
        Long userId = (Long) getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            return null;
            //TODO 打日志
        }
        Sort sort = new Sort(Sort.Direction.ASC,"passengerId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        return passengerService.findPassengersByUserId(userId,pageable);
    }

    @Override
    public ResultCodeEnum modifyPassword(ModifyPasswordVo modifyPasswordVo) {

        Long userId = (Long) getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            return ResultCodeEnum.USERCENTER_MODIFYPASSWORD_ERROR;
            //TODO 打日志
        }

        User user = userNewService.findByUserId(userId);
        if (user == null || (!user.getPassword().equals(modifyPasswordVo.getOldPassword()))){
            return ResultCodeEnum.USERCENTER_MODIFYPASSWORD_ERROR;
            //TODO 打日志
        }

        user.setPassword(modifyPasswordVo.getPassword());
        user.setOldPassword(modifyPasswordVo.getOldPassword());
        userNewService.saveUser(user);
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public Page<ContactPerson> getContactPersons(Integer page, Integer size) {
        Long userId = (Long) getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            return null;
            //TODO 打日志
        }
        Sort sort = new Sort(Sort.Direction.ASC,"personId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        return contactPersonService.findContactPersonByUserId(userId,pageable);
    }

    @Override
    public ResultCodeEnum addContactPerson(ContactPerson contactPerson) {
        ResultCodeEnum checkParamsCode = checkParamsOfAddcontactPerson(contactPerson);
        if (!checkParamsCode.getSuccess()){
            return checkParamsCode;
        }
        Long userId = (Long)getSessionAttribute("userId");
        if (userId == null || userId <= 0) {
            return null;
            //TODO 打日志
        }
        packAndAddContactPerson(contactPerson, userId);
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum modifyContactPerson(ContactPerson contactPerson) {
        if (contactPerson.getPersonId() == null || contactPerson.getPersonId() < 0){
            return ResultCodeEnum.USERCENTER_MODIFYCONTACTPERSON_ERROR;
        }
        ResultCodeEnum checkParamsCode = checkParamsOfAddcontactPerson(contactPerson);
        if (!checkParamsCode.getSuccess()){
            return checkParamsCode;
        }
        Integer updateCode = contactPersonService.updateContactPerson(contactPerson.getName(),
                contactPerson.getPhoneNumber(),
                contactPerson.getEmail(),
                contactPerson.getPersonId());
        if (!updateCode.equals(1)){
            //打日志
            System.out.println(JSONObject.toJSONString(updateCode));
            return ResultCodeEnum.USERCENTER_MODIFYCONTACTPERSON_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum deleteContactPerson(Long personId) {
        ResultCodeEnum code = checkPersonId(personId);
        if (!code.getSuccess())
            return code;
        Integer deleteCode = contactPersonService.deleteContactPerson(personId);
        if (!deleteCode.equals(1)){
            //打日志
            System.out.println(JSONObject.toJSONString(deleteCode));
            return ResultCodeEnum.USERCENTER_DELETECONTACTPERSON_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkRepeatCardNumberAndPassengerId(Long userId, String cardNumber, Long passengerId) {
        List<Passenger> passengers = passengerService.findPassengersByUserId(userId);
        boolean realPassengerId = false;
        for (Passenger passenger: passengers) {
            if (passenger.getCardNumber().equals(cardNumber)){
                return ResultCodeEnum.USERCENTER_CARDNUMBERREPEAT_ERROR;
            }
            if (passenger.getPassengerId().equals(passengerId)){
                realPassengerId = true;
            }
        }
        if (!realPassengerId){
            return ResultCodeEnum.USERCENTER_MODIFYPASSGER_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private Object getSessionAttribute(String attributeKey){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session.getAttribute(attributeKey);
    }

    private UserProfileVo packUserProfile(User user){
        UserProfileVo userProfile = new UserProfileVo();
        userProfile.setPhoneNumber(user.getPhoneNumber());
        userProfile.setName(encryptionName(user.getName()));
        userProfile.setSex(SexEnum.getSexName(user.getSex()));
        userProfile.setCardType(CardTypeEnum.getCardName(user.getCardType()));
        userProfile.setCardNumber(encryptionCardNumber(user.getCardNumber()));
        return userProfile;
    }

    private String encryptionCardNumber(String cardNumber){
        if (cardNumber == null || cardNumber.length() < 5)
            return "-";
        return cardNumber.substring(0,2) + "****" + cardNumber.substring(cardNumber.length()-1);
    }

    private String encryptionName(String userName){
        if (userName == null || userName.length() <= 1)
            return "-";
        StringBuilder encryptionName = new StringBuilder(userName.substring(0, 1));
        for (int i = 1; i < userName.length(); i++) {
            encryptionName.append("*");
        }
        return encryptionName.toString();
    }


    private void packAndAddPassager(PassengerVo passengerVo, Long userId){
        Passenger passenger = new Passenger();
        passenger.setUserId(userId);
        passenger.setName(passengerVo.getName());
        passenger.setCardType(CardTypeEnum.getCardIndex(passengerVo.getCardType()));
        passenger.setCardNumber(passengerVo.getCardNumber());
        passenger.setIsDelete(Const.NOT_DELETE);
        passengerService.addPassenger(passenger);
    }

    private void packAndAddContactPerson(ContactPerson contactPerson, Long userId){
        ContactPerson contactPersonDto = new ContactPerson();
        contactPersonDto.setUserId(userId);
        contactPersonDto.setName(contactPerson.getName());
        contactPersonDto.setPhoneNumber(contactPerson.getPhoneNumber());
        String email = contactPerson.getEmail();
        if (email == null)
            email = "";
        contactPersonDto.setEmail(email);
        contactPersonDto.setIsDelete(Const.NOT_DELETE);
        contactPersonService.addContactPerson(contactPersonDto);
    }

    private ResultCodeEnum checkParamsOfAddcontactPerson(ContactPerson contactPerson) {
        String name = contactPerson.getName();
        String phoneNumber = contactPerson.getPhoneNumber();
        Long userId = (Long)getSessionAttribute("userId");
        if (name == null || name.length() == 0 || userId == null || userId <= 0 || phoneNumber == null || phoneNumber.length() < 10){
            return ResultCodeEnum.USERCENTER_ADDCONTACTPERSON_ERROR;
            //todo 打日志
        }
        ContactPerson inUsingContactPerson = contactPersonService.findByUserIdAndPhoneNumber(userId, phoneNumber);
        if (inUsingContactPerson != null){
            return ResultCodeEnum.USERCENTER_PHONENUMBERREPEAT_ERROR;
            //todo 打日志
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkParamsOfAddPassager(PassengerVo passengerVo) {
        String name = passengerVo.getName();
        Long userId = (Long)getSessionAttribute("userId");
        if (name == null || name.length() == 0 || userId == null || userId <= 0){
            return ResultCodeEnum.USERCENTER_ADDPASSGERS_ERROR;
            //todo 打日志
        }
        return checkCardTypeAndCardNumber(passengerVo.getCardType(),passengerVo.getCardNumber());
    }

    public static ResultCodeEnum checkCardTypeAndCardNumber(String cardType, String cardNumber){
        if (cardType == null || cardType.length() == 0 ){
            System.out.println("号码为空");
            return ResultCodeEnum.USERCENTER_ADDPASSGERS_ERROR;
            //todo 打日志
        }
        if (cardNumber == null || cardNumber.length() == 0){
            System.out.println("号码为空");
            return ResultCodeEnum.USERCENTER_ADDPASSGERS_ERROR;
            //todo 打日志
        }
        Integer cardTypeNumber = CardTypeEnum.getCardIndex(cardType);
        if ((cardTypeNumber == -1) ||
                ((cardTypeNumber == 1) && (cardNumber.length() != 18)) ||
                ((cardTypeNumber == 2) && ((cardNumber.length() <= 5) || (cardNumber.length() > 8))) ||
                ((cardTypeNumber == 3) && (cardNumber.length() != 9)) ||
                ((cardTypeNumber == 4) && (cardNumber.length() != 11)) ||
                ((cardTypeNumber == 5) && (cardNumber.length() != 8)) ||
                ((cardTypeNumber == 6) && (cardNumber.length() != 18))){
            System.out.println("号码和类型不对应");
            return ResultCodeEnum.USERCENTER_CARDTYPE_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkPersonId(Long personId) {
        Long userId = (Long)getSessionAttribute("userId");
        if (personId == null)
            return ResultCodeEnum.USERCENTER_DELETECONTACTPERSON_ERROR;
        ContactPerson contactPerson = contactPersonService.findContactPersonByPersonId(personId);
        if (contactPerson == null || !contactPerson.getUserId().equals(userId)){
            return ResultCodeEnum.USERCENTER_DELETECONTACTPERSON_ERROR;
            //TODO 打日志
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkPassengerId(Long passengerId) {
        if (passengerId == null)
            return ResultCodeEnum.USERCENTER_DELETEPASSGER_ERROR;
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkRepeatCardNumber(Long userId, String cardNumber) {
        Passenger passenger = passengerService.findByUserIdAndCardNumber(userId,cardNumber);
        if (passenger != null){
            return ResultCodeEnum.USERCENTER_PASSGERCARDNUMBER_REPEAT_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

}
