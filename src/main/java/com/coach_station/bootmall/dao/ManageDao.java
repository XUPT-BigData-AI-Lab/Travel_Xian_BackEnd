package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.*;
import org.springframework.data.domain.Page;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/13:40
 * @Description:
 */
public interface ManageDao {
    ResultOfDataPage getShuttleInfoList(GetShuttleInfoListRequestVo requestVo);

    ResultCodeEnum createShuttleInfo(ShuttleInfoManageVo requestVo);

    ResultCodeEnum deleteShuttleInfo(Long shuttleId);

    ResultCodeEnum changeShuttleStatus(Long shuttleId);

    Result getOrderList(getOrderRequestVo requestVo);

    ResultCodeEnum modifyOrderInfo(modifyOrderRequestVo requestVo);

}
