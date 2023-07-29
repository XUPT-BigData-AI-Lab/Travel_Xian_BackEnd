package com.coach_station.bootmall.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/9:09
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class OrderDetailInfoDto {
    @Id
    private Long orderId;

    private String orderNumber;

    private BigDecimal totalAmount;

    private String shuttleShiftTime;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date shuttleShiftDate;

    private Integer orderStatus;

    private String startRegion;

    private String finalRegion;

    private Integer ticketType;

    private Integer cardType;

    private String cardNumber;

    private String name;

    private BigDecimal refundAmount;

    private String rideCode;

    private String startStation;

    private String finalStation;

    private BigDecimal ticketPrice;

    private BigDecimal stationFee;

    private BigDecimal refundFee;

    private BigDecimal insurancePrice;

    private Integer insuranceStatus;

    private Integer shuttleShiftType;

    private Integer lineType;

    private String carModel;

    private Integer shiftId;
}
