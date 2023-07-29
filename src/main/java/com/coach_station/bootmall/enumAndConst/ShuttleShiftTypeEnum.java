package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/15:22
 * @Description:
 */
@Getter
public enum ShuttleShiftTypeEnum {
    FLOW("流水班",1),FIXED("固定班",2);

    private Integer index;
    private String name;

    private ShuttleShiftTypeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getShuttleShiftTypeName(Integer index){
        for (ShuttleShiftTypeEnum shuttleShiftTypeEnum: ShuttleShiftTypeEnum.values()) {
            if (shuttleShiftTypeEnum.getIndex().equals(index)){
                return shuttleShiftTypeEnum.name;
            }
        }
        return "-";
    }

    public static Integer getShuttleShiftTypeIndex(String name){
        for (ShuttleShiftTypeEnum shuttleShiftTypeEnum: ShuttleShiftTypeEnum.values()) {
            if (shuttleShiftTypeEnum.getName().equals(name)){
                return shuttleShiftTypeEnum.index;
            }
        }
        return -1;
    }
}
