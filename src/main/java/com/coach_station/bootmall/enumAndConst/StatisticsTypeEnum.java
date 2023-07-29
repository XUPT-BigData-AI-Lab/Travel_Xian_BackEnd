package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/23/20:39
 * @Description:
 */
@Getter
public enum StatisticsTypeEnum {
    TODAY_QUANTITY("当日订单量",1),DAY_QUANTITY("各日订单量",2),MONTH_QUANTITY("各月订单量",3),YEAR_QUANTITY("各年订单量",4);

    private Integer index;
    private String name;

    private StatisticsTypeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getStatisticsTypeName(Integer index){
        for (StatisticsTypeEnum statisticsTypeEnum: StatisticsTypeEnum.values()) {
            if (statisticsTypeEnum.getIndex().equals(index)){
                return statisticsTypeEnum.name;
            }
        }
        return "-";
    }

    public static Integer getStatisticsTypeIndex(String name){
        for (StatisticsTypeEnum statisticsTypeEnum: StatisticsTypeEnum.values()) {
            if (statisticsTypeEnum.getName().equals(name)){
                return statisticsTypeEnum.index;
            }
        }
        return -1;
    }
}
