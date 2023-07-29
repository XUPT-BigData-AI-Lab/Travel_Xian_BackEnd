package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/22:24
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class modifyOrderRequestVo {
    @JsonProperty(value = "order_id")
    private Long orderId;

    @JsonProperty(value = "card_type")
    private String cardType;

    @JsonProperty(value = "card_number")
    private String cardNumber;

    private String name;
}