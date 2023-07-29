package com.coach_station.bootmall.enumAndConst;

import lombok.Getter;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/16:22
 * @Description:
 */
@Getter
public enum OperateOrderStatusEnum {
    BOOK("预订",1),ARREARAGE("下单时间：",2),
    STANDBY("购票时间：",3), REFUND_PART("退票时间：",5),
    REFUND_ALL("退票时间：",6), FINISH("完成时间：",7),
    CANCEL("取消时间：",8);

    private Integer index;
    private String name;

    private OperateOrderStatusEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static String getOperateOrderStatusName(Integer index){
        for (OperateOrderStatusEnum operateOrderStatus: OperateOrderStatusEnum.values()) {
            if (operateOrderStatus.getIndex().equals(index)){
                return operateOrderStatus.name;
            }
        }
        return "-";
    }

    public static Integer getOperateOrderStatusIndex(String name){
        for (OperateOrderStatusEnum operateOrderStatus: OperateOrderStatusEnum.values()) {
            if (operateOrderStatus.getName().equals(name)){
                return operateOrderStatus.index;
            }
        }
        return -1;
    }
}
