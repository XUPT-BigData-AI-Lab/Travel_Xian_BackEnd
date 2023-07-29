package com.coach_station.bootmall.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Auther: yjw
 * @Date: 2022/01/07/16:37
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

    @JsonProperty(value = "card_type")
    private Integer cardType;

    @JsonProperty(value = "card_number")
    private String cardNumber;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "is_delete")
    private Integer isDelete;
}
