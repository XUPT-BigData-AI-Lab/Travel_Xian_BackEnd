package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/01/19/22:04
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PayOrderVo {
    @JsonProperty(value = "total_amount")
    private BigDecimal totalAmount;

    @JsonProperty(value = "master_order_number")
    private String masterOrderNumber;

    @JsonProperty(value = "completion_time")
    private String completionTime;
}
