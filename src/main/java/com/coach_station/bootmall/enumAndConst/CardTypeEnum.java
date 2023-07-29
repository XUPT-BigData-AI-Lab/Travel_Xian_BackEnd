package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/14:39
 * @Description:
 */
@Getter
public enum CardTypeEnum {

    COMMON_CARD("身份证",1),ARMYMAN_CARD("军人证",2),PASSPORT("护照",3),
    HONG_KONG_AND_MACAO_MAINLAND_PASS_CARD("港澳居民来往内地通行证",4),
    TAIWAN_MAINLAND_PASS_CARD("台湾居民来往内地通行证",5),
    HONG_KONG_AND_MACAO_AND_TAIWAN_CARD("港澳台居民居住证",6);

    private Integer index;
    private String name;

    private CardTypeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getCardName(Integer index){
        for (CardTypeEnum cardInfo: CardTypeEnum.values()) {
            if (cardInfo.getIndex().equals(index)){
                return cardInfo.name;
            }
        }
        return "-";
    }

    public static Integer getCardIndex(String name){
        for (CardTypeEnum cardInfo: CardTypeEnum.values()) {
            if (cardInfo.getName().equals(name)){
                return cardInfo.index;
            }
        }
        return -1;
    }
}
