package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/0:27
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShuttleVo {
    @JsonProperty(value = "start_region_id")
    private Long startRegionId;

    @JsonProperty(value = "final_region_id")
    private Long finalRegionId;

    @JsonProperty(value = "start_region")
    private String startRegion;

    @JsonProperty(value = "final_region")
    private String finalRegion;
}
