package com.coach_station.bootmall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/14:38
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetailInfoVo {
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

    @JsonProperty(value = "ticket_type")
    private String ticketType;

    @JsonProperty(value = "card_type")
    private String cardType;

    @JsonProperty(value = "card_number")
    private String cardNumber;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "refund_amount")
    private BigDecimal refundAmount;

    @JsonProperty(value = "ride_code")
    private String rideCode;

    @JsonProperty(value = "start_station")
    private String startStation;

    @JsonProperty(value = "final_station")
    private String finalStation;

    @JsonProperty(value = "ticket_price")
    private BigDecimal ticketPrice;

    @JsonProperty(value = "station_fee")
    private BigDecimal stationFee;

    @JsonProperty(value = "refund_fee")
    private BigDecimal refundFee;

    @JsonProperty(value = "insurance_price")
    private BigDecimal insurancePrice;

    @JsonProperty(value = "insurance_status")
    private String insuranceStatus;

    @JsonProperty(value = "shuttle_shift_type")
    private String shuttleShiftType;

    @JsonProperty(value = "line_type")
    private String lineType;

    @JsonProperty(value = "car_model")
    private String carModel;

    @JsonProperty(value = "shift_id")
    private Integer shiftId;

    @JsonProperty(value = "order_operation_log")
    private String orderOperationLog;
}
