package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/15:33
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegionVo {
    @JsonProperty(value = "region_id")
    private Long orderId;

    @JsonProperty(value = "region_name")
    private String regionName;

    @JsonProperty(value = "region_english_name")
    private String regionEnglishName;

    @JsonProperty(value = "city_name")
    private String cityName;

    @JsonProperty(value = "province_name")
    private String provinceName;
}
