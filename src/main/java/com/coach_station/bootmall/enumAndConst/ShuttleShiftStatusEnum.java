package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/15:50
 * @Description:
 */
@Getter
public enum ShuttleShiftStatusEnum {
    BAN("禁止中",1),USE("使用中",0);

    private Integer index;
    private String name;

    private ShuttleShiftStatusEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getShuttleShiftStatusName(Integer index){
        for (ShuttleShiftStatusEnum shuttleShiftStatusEnum: ShuttleShiftStatusEnum.values()) {
            if (shuttleShiftStatusEnum.getIndex().equals(index)){
                return shuttleShiftStatusEnum.name;
            }
        }
        return "-";
    }

    public static Integer changeShuttleShiftStatus(Integer index){
        for (ShuttleShiftStatusEnum shuttleShiftStatusEnum: ShuttleShiftStatusEnum.values()) {
            if (!shuttleShiftStatusEnum.getIndex().equals(index)){
                return shuttleShiftStatusEnum.getIndex();
            }
        }
        return index;
    }

    public static Integer getShuttleShiftStatusIndex(String name){
        for (ShuttleShiftStatusEnum shuttleShiftStatusEnum: ShuttleShiftStatusEnum.values()) {
            if (shuttleShiftStatusEnum.getName().equals(name)){
                return shuttleShiftStatusEnum.index;
            }
        }
        return -1;
    }
}
