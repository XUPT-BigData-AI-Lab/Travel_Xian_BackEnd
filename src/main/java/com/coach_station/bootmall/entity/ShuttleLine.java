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
 * @Date: 2022/01/07/17:56
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ShuttleLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineId;

    private String startRegion;

    private String finalRegion;

    private Long startRegionId;

    private Long finalRegionId;

    private String viaRegionsId;

    private String viaRegionsName;

    private Long totalStars;
}
