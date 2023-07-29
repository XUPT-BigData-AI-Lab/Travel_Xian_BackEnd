package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/21:53
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInfoManageVo {
    @JsonProperty(value = "order_id")
    private Long orderId;

    @JsonProperty(value = "create_time")
    private String createTime;

    @JsonProperty(value = "shuttle_line")
    private String shuttleLine;

    @JsonProperty(value = "passenger_name")
    private String passengerName;

    @JsonProperty(value = "passenger_card_type")
    private String passengerCardType;

    @JsonProperty(value = "passenger_card_number")
    private String passengerCardNumber;

    @JsonProperty(value = "ride_code")
    private String rideCode;
}