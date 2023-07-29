package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/14:18
 * @Description:
 */
@Getter
public enum SexEnum {

    MAN("男",1),WOMAN("女",2);


    private Integer index;
    private String name;

    private SexEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getSexName(Integer index){
        for (SexEnum sexInfo: SexEnum.values()) {
            if (sexInfo.getIndex() == index){
                return sexInfo.name;
            }
        }
        return "-";
    }

    public static Integer getSexIndex(String sexName){
        for (SexEnum sexInfo: SexEnum.values()) {
            if (sexInfo.getName().equals(sexName)){
                return sexInfo.index;
            }
        }
        return -1;
    }
}
