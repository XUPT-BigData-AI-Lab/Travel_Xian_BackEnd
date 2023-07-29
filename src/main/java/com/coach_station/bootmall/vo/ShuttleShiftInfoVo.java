package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/16:25
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShuttleShiftInfoVo {

    @JsonProperty(value = "shift_id")
    private Long shiftId;

    @JsonProperty(value = "shuttle_shift_time")
    private String shuttleShiftTime;

    @JsonProperty(value = "start_region")
    private String startRegion;

    @JsonProperty(value = "final_region")
    private String finalRegion;

    @JsonProperty(value = "start_station")
    private String startStation;

    @JsonProperty(value = "final_station")
    private String finalStation;

    @JsonProperty(value = "unuse_ticket_quantity")
    private Integer unuseTicketQuantity;

    @JsonProperty(value = "unuse_child_ticket_quantity")
    private Integer unuseChildTicketQuantity;

    @JsonProperty(value = "ticket_price")
    private BigDecimal ticketPrice;

    @JsonProperty(value = "shuttle_shift_type")
    private String shuttleShiftType;

    @JsonProperty(value = "line_type")
    private String lineType;

    @JsonProperty(value = "duration")
    private String duration;

    @JsonProperty(value = "full_lenght")
    private String fullLenght;

    @JsonProperty(value = "car_model")
    private String carModel;

    @JsonProperty(value = "via_stations_name")
    private String viaStationsName;
}
