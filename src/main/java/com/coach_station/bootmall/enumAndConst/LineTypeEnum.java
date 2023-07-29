package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/15:32
 * @Description:
 */
@Getter
public enum LineTypeEnum {
    HIGHWAY("高速",1),LOWWAY("低速",2);

    private Integer index;
    private String name;

    private LineTypeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getLineTypeName(Integer index){
        for (LineTypeEnum lineTypeEnum: LineTypeEnum.values()) {
            if (lineTypeEnum.getIndex().equals(index)){
                return lineTypeEnum.name;
            }
        }
        return "-";
    }

    public static Integer getLineTypeIndex(String name){
        for (LineTypeEnum lineTypeEnum: LineTypeEnum.values()) {
            if (lineTypeEnum.getName().equals(name)){
                return lineTypeEnum.index;
            }
        }
        return -1;
    }
}
