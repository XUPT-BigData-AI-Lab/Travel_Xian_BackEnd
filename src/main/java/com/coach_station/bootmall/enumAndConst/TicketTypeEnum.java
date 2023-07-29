package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/14:51
 * @Description:
 */
@Getter
public enum TicketTypeEnum {

    ADULT("成人票",1,"成人"),HAVECHILD("携童票",2, "成人携童"),HALF("半票",3 ,"儿童");

    private Integer index;
    private String name;
    private String personType;

    private TicketTypeEnum(String name, Integer index, String personType) {
        this.name = name;
        this.index = index;
        this.personType = personType;
    }

    public static String geticketTypeName(Integer index){
        for (TicketTypeEnum ticketTypeEnum: TicketTypeEnum.values()) {
            if (ticketTypeEnum.getIndex().equals(index)){
                return ticketTypeEnum.name;
            }
        }
        return "-";
    }

    public static Integer geticketTypeIndex(String name){
        for (TicketTypeEnum ticketTypeEnum: TicketTypeEnum.values()) {
            if (ticketTypeEnum.getName().equals(name)){
                return ticketTypeEnum.index;
            }
        }
        return -1;
    }

    public static String getPersonType(Integer index){
        for (TicketTypeEnum ticketTypeEnum: TicketTypeEnum.values()) {
            if (ticketTypeEnum.getIndex().equals(index)){
                return ticketTypeEnum.personType;
            }
        }
        return "-";
    }

}
