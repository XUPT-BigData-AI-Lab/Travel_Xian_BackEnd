package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/12/14:48
 * @Description:
 */
@Getter
public enum OrderStatusEnum {

    BOOK("预订",1,"book"),ARREARAGE("待支付",2,"arrearage"),
    STANDBY("已支付",3,"standby"), REFUND_PART("部分退款",4,"refund_part"),
    REFUND_ALL("已退款",5,"refund"), FINISH("已完成",6,"finish"),
    CANCEL("已取消",7,"cancel"),ALL("已取消",10,"all");

    private Integer index;
    private String name;
    private String EngName;

    private OrderStatusEnum(String name, Integer index, String engName) {
        this.name = name;
        this.index = index;
        this.EngName = engName;
    }

    public static String getOrderStatusName(Integer index){
        for (OrderStatusEnum OrderStatus: OrderStatusEnum.values()) {
            if (OrderStatus.getIndex().equals(index)){
                return OrderStatus.name;
            }
        }
        return "-";
    }

    public static Integer getOrderStatusIndexByEngName(String Engname){
        for (OrderStatusEnum OrderStatus: OrderStatusEnum.values()) {
            if (OrderStatus.getEngName().equals(Engname)){
                return OrderStatus.index;
            }
        }
        return -1;
    }
}
