package com.coach_station.bootmall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/13:40
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserProfileVo implements Serializable {
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String name;

    private String sex;

    @JsonProperty(value = "card_type")
    private String cardType;

    @JsonProperty(value = "card_number")
    private String cardNumber;
}
