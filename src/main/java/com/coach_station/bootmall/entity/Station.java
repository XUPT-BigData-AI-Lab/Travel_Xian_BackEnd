package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/01/07/17:47
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    private String stationNumber;

    private Long regionId;

    private String stationName;

    private String stationEnglishName;

    private String stationAddress;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Integer star;
}
