package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/14:20
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetShuttleInfoListRequestVo {
    @JsonProperty(value = "start_region_id")
    private Long startRegionId;

    @JsonProperty(value = "final_region_id")
    private Long finalRegionId;

    @JsonProperty(value = "shuttle_shift_date")
    private String shuttleShiftDate;

    private Integer page;

    private Integer size;
}
