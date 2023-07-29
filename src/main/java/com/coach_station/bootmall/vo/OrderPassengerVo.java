package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/16/18:02
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderPassengerVo {

    @JsonProperty(value = "passenger_id")
    private Long passengerId;

    @JsonProperty(value = "passenger_name")
    private String passengerName;

    @JsonProperty(value = "passenger_card_number")
    private String passengerCardNumber;

    @JsonProperty(value = "passenger_card_type")
    private String passengerCardType;

    @JsonProperty(value = "ticket_type")
    private String ticketType;

    @JsonProperty(value = "buying_insurance")
    private boolean buyingInsurance;
}
