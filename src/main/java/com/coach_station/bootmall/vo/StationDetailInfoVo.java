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
 * @Date: 2022/01/14/23:17
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StationDetailInfoVo {
    @JsonProperty(value = "station_id")
    private Long stationId;

    @JsonProperty(value = "station_name")
    private String stationName;

    @JsonProperty(value = "station_english_name")
    private String stationEnglishName;

    @JsonProperty(value = "longitude")
    private BigDecimal longitude;

    @JsonProperty(value = "latitude")
    private BigDecimal latitude;

    @JsonProperty(value = "station_address")
    private String stationAddress;
}
