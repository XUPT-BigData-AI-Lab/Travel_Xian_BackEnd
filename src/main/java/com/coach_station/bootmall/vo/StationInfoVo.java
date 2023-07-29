package com.coach_station.bootmall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/18:30
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StationInfoVo {
    @JsonProperty(value = "station_id")
    private Long stationId;

    @JsonProperty(value = "station_name")
    private String stationName;

    @JsonProperty(value = "station_english_name")
    private String stationEnglishName;
}
