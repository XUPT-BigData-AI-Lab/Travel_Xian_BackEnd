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
 * @Date: 2022/01/07/17:51
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionnId;

    private String regionName;

    private String regionEnglishName;

    private String cityName;

    private String provinceName;
}
