package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Auther: yjw
 * @Date: 2022/01/23/14:21
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticsId;

    private Integer statisticsType;

    private Integer shardingKey;

    private Long orderQuantity;

    private String date;
}