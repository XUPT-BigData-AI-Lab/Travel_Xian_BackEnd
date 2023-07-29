package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Auther: yjw
 * @Date: 2022/01/07/17:53
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderOperate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operateId;

    private String orderNumber;

    private Integer orderStatus;

    private Long operateTime;

    public OrderOperate(String orderNumber, Integer orderStatus, Long operateTime) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.operateTime = operateTime;
    }
}
