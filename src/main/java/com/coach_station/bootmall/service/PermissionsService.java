package com.coach_station.bootmall.service;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.coach_station.bootmall.dao.PermissionsDao;
import com.coach_station.bootmall.dao.UserNewDao;
import com.coach_station.bootmall.entity.User;
import com.coach_station.bootmall.enumAndConst.Const;
import com.coach_station.bootmall.util.AliSMSUtil;
import com.coach_station.bootmall.vo.LoginInfoVo;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.RegisterInfoVo;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/20:01
 * @Description:
 */
@Log4j2
@Service
public class PermissionsService implements PermissionsDao {
    @Autowired
    UserNewService userNewService;

    @Override
    public ResultCodeEnum login(LoginInfoVo loginInfo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String phoneNumber = loginInfo.getPhoneNumber();
        String password = loginInfo.getPassword();
        String imgcode = (String) session.getAttribute("imgcode");
        if (loginInfo.getCheckCode().toUpperCase().equals(imgcode)){
            UsernamePasswordToken token = new UsernamePasswordToken(phoneNumber, password);
            try {
                subject.login(token);
                User user = userNewService.findByPhoneNumber(phoneNumber);
                session.setAttribute("userId",user.getUserId());
                return ResultCodeEnum.SUCCESS;
            }catch (UnknownAccountException e){//用户名不存在异常
                return ResultCodeEnum.LOGIN_INFO_ERROR;
            }catch (IncorrectCredentialsException e){//密码不存在
                return ResultCodeEnum.LOGIN_INFO_ERROR;
            }
        }else {
            return ResultCodeEnum.LOGIN_CAPATA_ERROR;
        }
    }

    @Override
    public ResultCodeEnum register(RegisterInfoVo registerInfoVo,int checkPhoneCode) {
        ResultCodeEnum checkCode = checkInfoAndPhoneCode(registerInfoVo,checkPhoneCode);
        if (checkCode != ResultCodeEnum.SUCCESS)
            return checkCode;
        User exitUser = userNewService.findByPhoneNumber(registerInfoVo.getPhoneNumber());
        if (exitUser != null){
            return ResultCodeEnum.PERMISSION_REGISTER_USEREXIT_ERROR;
        }
        User user = new User();
        user.setPhoneNumber(registerInfoVo.getPhoneNumber());
        user.setPassword(registerInfoVo.getPassword());
        user.setOldPassword(registerInfoVo.getPassword());
        user.setPerms(Const.ROLE_USER);
        userNewService.saveUser(user);
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum sendPhoneCode(String phoneNumber,int checkPhoneCode) {
        if (phoneNumber == null || phoneNumber.length() < 11){
            return ResultCodeEnum.LOGIN_PHONE_PATTARN_ERRROR;
        }
        SendSmsResponse sendResp;
        try {
            sendResp = AliSMSUtil.sendSMS(phoneNumber, checkPhoneCode, Const.REGISTER_TEMPLATE_CODE);
        }catch (Exception e) {
            return ResultCodeEnum.SEND_SMS_ERROR;
        }
        if (sendResp == null
                || sendResp.body == null
                || (!com.aliyun.teautil.Common.equalString(sendResp.body.code, "OK"))) {
            log.error("错误信息: " + sendResp.body.message + "");
            return ResultCodeEnum.SEND_SMS_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    @Cacheable(value = "checkPhoneCode", key = "#phoneNumber")
    public int getCacheCheckPhoneCode(String phoneNumber) {
        System.out.println( "缓存为空，手机号：" + phoneNumber);
        return -1;
    }

    @Override
    @CachePut(value = "checkPhoneCode", key = "#phoneNumber")
    public int cacheAndCreateCheckPhoneCode(String phoneNumber) {
        return new Random().nextInt(9999);
    }

    @Override
    public ResultCodeEnum modifyPassword(RegisterInfoVo modifyPasswordInfo,int checkPhoneCode) {
        ResultCodeEnum checkCode = checkInfoAndPhoneCode(modifyPasswordInfo,checkPhoneCode);
        if (checkCode != ResultCodeEnum.SUCCESS)
            return checkCode;
        User userInfo = userNewService.findByPhoneNumber(modifyPasswordInfo.getPhoneNumber());
        if (userInfo == null){
            return ResultCodeEnum.PARAM_ERROR;
        }
        userInfo.setOldPassword(userInfo.getPassword());
        userInfo.setPassword(modifyPasswordInfo.getPassword());
        userNewService.saveUser(userInfo);
        return ResultCodeEnum.SUCCESS;
    }

    private Object getSessionAttribute(String attributeKey){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session.getAttribute(attributeKey);
    }

    public ResultCodeEnum checkInfoAndPhoneCode(RegisterInfoVo info,int checkPhoneCode){
        if (info.getCheckCode() == null
                || info.getCheckCode().length() != 4
                || info.getPassword() == null){
//                || info.getPassword().length() != 16){
            return ResultCodeEnum.PARAM_ERROR;
        }
        if (checkPhoneCode < 0){
            return ResultCodeEnum.LOGIN_CODE_FAIL_ERROR;
        }
        if (Integer.parseInt(info.getCheckCode()) != checkPhoneCode){
            return ResultCodeEnum.LOGIN_CODE_INPUT_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }
}
