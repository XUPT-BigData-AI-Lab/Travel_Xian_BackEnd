package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

/**
 * @Auther: yjw
 * @Date: 2022/01/07/0:00
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ShuttleShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;

    private String shuttleShiftDate;

    private String shuttleShiftTime;

    private Long shuttleLineId;

    private String stationNumber;

    private String startStation;

    private String finalStation;

    private Integer childTicketQuantity;

    private Integer unuseChildTicketQuantity;

    private Integer ticketQuantity;

    private Integer unuseTicketQuantity;

    private BigDecimal ticketPrice;

    private Integer shuttleShiftType;

    private Integer lineType;

    private String duration;

    private Long carId;

    private String fullLenght;

    private BigDecimal insurancePrice;

    private BigDecimal refundFee;

    private BigDecimal stationFee;

    private Integer star;

    private Integer isDelete;
}
