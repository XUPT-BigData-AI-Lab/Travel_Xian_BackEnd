package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.vo.LoginInfoVo;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.RegisterInfoVo;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/19:56
 * @Description:
 */
public interface PermissionsDao {
    ResultCodeEnum login(LoginInfoVo loginInfo);
    ResultCodeEnum register(RegisterInfoVo registerInfoVo,int checkPhoneCode);
    ResultCodeEnum sendPhoneCode(String phoneNumber,int checkPhoneCode);
    int getCacheCheckPhoneCode(String phoneNumber);
    int cacheAndCreateCheckPhoneCode(String phoneNumber);
    ResultCodeEnum modifyPassword(RegisterInfoVo modifyPasswordInfo,int checkPhoneCode);
}
