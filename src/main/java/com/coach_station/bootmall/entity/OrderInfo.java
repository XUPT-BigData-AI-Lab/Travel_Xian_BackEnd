package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/01/06/23:01
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_info")
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderNumber;

    private Integer orderType;

    private String tradingNumber;

    private Integer slaveOrderQuantity;

    private Integer orderStatus;

    private Long userId;

    private Integer fromOnline;

    private Long passengerId;

    private Long personId;

    private Long createTime;

    private String rideCode;

    private Integer ticketType;

    private Integer insuranceStatus;

    private Long shuttleShiftId;

    private BigDecimal totalAmount;

    private BigDecimal refundAmount;

    private String masterOrderNumber;

    private Integer isDelete;
}
