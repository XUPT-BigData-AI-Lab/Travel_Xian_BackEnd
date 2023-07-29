package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Auther: yjw
 * @Date: 2022/01/06/22:16
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
      //
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long userId;

      private String phoneNumber;

      private String name;

      private int sex;

      private int cardType;

      private String cardNumber;

      private String email;

      private String oldPassword;

      private String password;

      private String totalLength;

      private String totalDuration;

      private String totalOrderQuantity;

      private String totalRegionsId;

      private String perms;

}
