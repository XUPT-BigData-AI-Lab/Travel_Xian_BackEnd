package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.entity.ContactPerson;
import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.enumAndConst.CardTypeEnum;
import com.coach_station.bootmall.service.UserCenterService;
import com.coach_station.bootmall.vo.*;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/13:35
 * @Description:
 */
@RequestMapping("/userCenter")
@Controller
@CrossOrigin
@Log4j2
public class UserCenterController {

    @Autowired
    UserCenterService userCenterService;

    // 查询基本资料接口
    // 1.手机号和证件号使用*加密显示
    @GetMapping("/getProfile")
    @ResponseBody
    protected Result getProfile(){
        UserProfileVo profileInfo = userCenterService.getProfileInfo();
        if (profileInfo == null){
            return Result.setResult(ResultCodeEnum.USERCENTER_GETPROFILE_ERROR);
        }
        return Result.ok().data("profileInfo",profileInfo);
    }

    // 修改基本资料接口
    // 1.手机号和证件号使用*加密显示
    @PostMapping("/modifyProfile")
    @ResponseBody
    protected Result modifyProfile(@RequestBody UserProfileVo userProfile){
        ResultCodeEnum resultCode = userCenterService.modifyProfile(userProfile);
        return Result.setResult(resultCode);
    }

    // 已知旧密码修改密码接口
    @PostMapping("/modifyPassword")
    @ResponseBody
    protected Result modifyPassword(@RequestBody ModifyPasswordVo modifyPasswordVo){
        ResultCodeEnum resultCode = userCenterService.modifyPassword(modifyPasswordVo);
        return Result.setResult(resultCode);
    }

    // 查询常用乘车人接口
    @GetMapping("/getPassagers")
    @ResponseBody
    public ResultOfDataPage getPassagers  (
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size",defaultValue = "6") Integer size){
        Page<Passenger> passagers = userCenterService.getPassagers(page, size);
        if (passagers == null){
            return ResultOfDataPage.setResult(ResultCodeEnum.USERCENTER_GETPASSGERS_ERROR).setDataInfo(0,0, 0L);
        }
        ArrayList<PassengerVo> passengerVos = new ArrayList<>();
        for (Passenger passenger: passagers.getContent()) {
            PassengerVo passengerVo = new PassengerVo();
            BeanUtils.copyProperties(passenger,passengerVo);
            passengerVo.setCardType(CardTypeEnum.getCardName(passenger.getCardType()));
            passengerVos.add(passengerVo);
        }
        ResultOfDataPage result = ResultOfDataPage.ok().data("passager_list", passengerVos)
                .setDataInfo(page,size,passagers.getTotalElements());
        return result;
    }

    // 添加乘车人信息接口
    @PostMapping("/addPassager")
    @ResponseBody
    public Result addPassager (@RequestBody PassengerVo passengerVo){
        ResultCodeEnum code = userCenterService.addPassager(passengerVo);
        return Result.setResult(code);
    }

    // 修改乘车人信息接口
    @PostMapping("/modifyPassager")
    @ResponseBody
    public Result modifyPassager (@RequestBody PassengerVo passengerVo){
        ResultCodeEnum code = userCenterService.modifyPassager(passengerVo);
        return Result.setResult(code);
    }

    // 删除乘车人信息接口
    @GetMapping("/deletePassager")
    @ResponseBody
    public Result deletePassager (@RequestParam(value = "passenger_id") Long passengerId){
        ResultCodeEnum code = userCenterService.deletePassager(passengerId);
        return Result.setResult(code);
    }

    // 查询常用联系人接口
    @GetMapping("/getContactPersons")
    @ResponseBody
    public ResultOfDataPage getContactPersons  (
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size",defaultValue = "6") Integer size){
        Page<ContactPerson> contactPersons = userCenterService.getContactPersons(page, size);
        if (contactPersons == null){
            return ResultOfDataPage.setResult(ResultCodeEnum.USERCENTER_GETCONTACTPERSON_ERROR).setDataInfo(0,0, 0L);
        }
        ArrayList<ContactPersonVo> contactPersonVos = new ArrayList<>();
        for (ContactPerson contactPerson: contactPersons.getContent()) {
            ContactPersonVo contactPersonVo = new ContactPersonVo();
            BeanUtils.copyProperties(contactPerson,contactPersonVo);
            contactPersonVos.add(contactPersonVo);
        }
        ResultOfDataPage result = ResultOfDataPage.ok().data("contact_person_list", contactPersonVos)
                .setDataInfo(page,size,contactPersons.getTotalElements());
        return result;
    }

    // 添加联系人信息接口
    @PostMapping("/addContactPerson")
    @ResponseBody
    public Result addContactPerson (@RequestBody ContactPerson contactPerson){
        ResultCodeEnum code = userCenterService.addContactPerson(contactPerson);
        return Result.setResult(code);
    }

    // 修改联系人信息接口
    @PostMapping("/modifyContactPerson")
    @ResponseBody
    public Result modifyContactPerson (@RequestBody ContactPerson contactPerson){
        ResultCodeEnum code = userCenterService.modifyContactPerson(contactPerson);
        return Result.setResult(code);
    }

    // 删除联系人信息接口
    @GetMapping("/deleteContactPerson")
    @ResponseBody
    public Result deleteContactPerson (@RequestParam(value = "contact_person_id") Long personId){
        ResultCodeEnum code = userCenterService.deleteContactPerson(personId);
        return Result.setResult(code);
    }
}
