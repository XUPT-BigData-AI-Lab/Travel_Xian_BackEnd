package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.ManageService;
import com.coach_station.bootmall.service.QueryService;
import com.coach_station.bootmall.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/13:37
 * @Description:
 */
@RequestMapping("/manage")
@Controller
@CrossOrigin
public class ManageController {
    @Autowired
    ManageService manageService;

    // 权限校验接口
    @GetMapping("/checkPermission")
    @ResponseBody
    public Result checkPermission() {
        return Result.ok();
    }

    // 查询班次接口
    @PostMapping("/getShuttleInfoList")
    @ResponseBody
    public ResultOfDataPage getShuttleInfoList(@RequestBody GetShuttleInfoListRequestVo requestVo) {
        return manageService.getShuttleInfoList(requestVo);
    }

    // 新建班次接口
    @PostMapping("/createShuttleInfo")
    @ResponseBody
    public Result createShuttleInfo(@RequestBody ShuttleInfoManageVo requestVo) {
        return Result.setResult(manageService.createShuttleInfo(requestVo));
    }

    // 编辑班次接口
    @PostMapping("/modifyShuttleInfo")
    @ResponseBody
    public Result modifyShuttleInfo(@RequestBody ShuttleInfoManageVo requestVo) {
        return Result.setResult(manageService.createShuttleInfo(requestVo));
    }

    // 删除班次接口
    @GetMapping("/deleteShuttleInfo")
    @ResponseBody
    public Result deleteShuttleInfo(@RequestParam(name = "Shuttle_id") Long shuttleId) {
        return Result.setResult(manageService.deleteShuttleInfo(shuttleId));
    }

    // 更改班次状态接口
    @GetMapping("/changeShuttleStatus")
    @ResponseBody
    public Result changeShuttleStatus(@RequestParam(name = "Shuttle_id") Long shuttleId) {
        return Result.setResult(manageService.changeShuttleStatus(shuttleId));
    }

    // 查询车票接口
    @PostMapping("/getOrderList")
    @ResponseBody
    public Result getOrderList(@RequestBody getOrderRequestVo requestVo) {
        return manageService.getOrderList(requestVo);
    }

    // 查询车票接口
    @PostMapping("/modifyOrderInfo")
    @ResponseBody
    public Result modifyOrderInfo(@RequestBody modifyOrderRequestVo requestVo) {
        return Result.setResult(manageService.modifyOrderInfo(requestVo));
    }
}
