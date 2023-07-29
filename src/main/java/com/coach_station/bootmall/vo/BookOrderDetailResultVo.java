package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/18/15:57
 * @Description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookOrderDetailResultVo {
    @JsonProperty(value = "order_number")
    private String orderNumber;

    @JsonProperty(value = "shuttle_shift_time")
    private String shuttleShiftTime;

    @JsonProperty(value = "start_region")
    private String startRegion;

    @JsonProperty(value = "final_region")
    private String finalRegion;

    @JsonProperty(value = "start_station")
    private String startStation;

    @JsonProperty(value = "final_station")
    private String finalStation;

    @JsonProperty(value = "ticket_price")
    private BigDecimal ticketPrice;

    @JsonProperty(value = "station_fee")
    private BigDecimal stationFee;

    @JsonProperty(value = "insurance_price")
    private BigDecimal insurancePrice;

    @JsonProperty(value = "ticket_type")
    private String ticketType;

    @JsonProperty(value = "shuttle_shift_id")
    private Long shuttleShiftId;

    @JsonProperty(value = "passenger_name")
    private String passengerName;

    @JsonProperty(value = "passenger_card_number")
    private String passengerCardNumber;

    @JsonProperty(value = "passenger_type")
    private String passengerType;

    @JsonProperty(value = "car_model")
    private String carModel;

    @JsonProperty(value = "total_amount")
    private BigDecimal totalAmount;

}
