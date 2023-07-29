package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/15:04
 * @Description:
 */
@Getter
public enum InsuranceStatusEnum {
    UNPAID("未购保",1),PAID("已购保",2);

    private Integer index;
    private String name;

    private InsuranceStatusEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getInsuranceStatusName(Integer index){
        for (InsuranceStatusEnum insuranceStatus: InsuranceStatusEnum.values()) {
            if (insuranceStatus.getIndex().equals(index)){
                return insuranceStatus.name;
            }
        }
        return "-";
    }

    public static Integer getInsuranceStatusIndex(String name){
        for (InsuranceStatusEnum insuranceStatus: InsuranceStatusEnum.values()) {
            if (insuranceStatus.getName().equals(name)){
                return insuranceStatus.index;
            }
        }
        return -1;
    }

    public static Integer getInsuranceStatusIndex(boolean isBuyingInsurance){
        if (isBuyingInsurance)
            return 2;
        return 1;
    }
}
