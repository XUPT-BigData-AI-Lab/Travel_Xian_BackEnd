package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/10/16:26
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PassengerVo {
    @JsonProperty(value = "passenger_id")
    private Long passengerId;

    @JsonProperty(value = "card_type")
    private String cardType;

    @JsonProperty(value = "card_number")
    private String cardNumber;

    private String name;
}
