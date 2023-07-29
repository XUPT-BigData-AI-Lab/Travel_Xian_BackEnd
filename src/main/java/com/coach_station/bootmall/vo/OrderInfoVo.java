package com.coach_station.bootmall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;


/**
 * @Auther: yjw
 * @Date: 2022/01/11/21:34
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInfoVo {
    @JsonProperty(value = "order_id")
    private Long orderId;

    @JsonProperty(value = "master_order_number")
    private String orderNumber;

    @JsonProperty(value = "total_amount")
    private BigDecimal totalAmount;

    @JsonProperty(value = "shuttle_shift_time")
    private String shuttleShiftTime;

    @JsonProperty(value = "order_status")
    private String orderStatus;

    @JsonProperty(value = "start_region")
    private String startRegion;

    @JsonProperty(value = "final_region")
    private String finalRegion;
}
