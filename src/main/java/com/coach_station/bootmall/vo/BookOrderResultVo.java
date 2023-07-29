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
 * @Date: 2022/01/16/19:11
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookOrderResultVo {
    @JsonProperty(value = "order_info")
    private List<BookOrderDetailResultVo> orderInfos;

    @JsonProperty(value = "master_order_number")
    private String masterOrderNumber;

    @JsonProperty(value = "mster_total_amount")
    private BigDecimal masterTotalAmount;
}
