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
 * @Date: 2022/01/07/17:55
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Car {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long carId;

      private String carModel;
      private String carColor;
      private String carNumber;
 }
