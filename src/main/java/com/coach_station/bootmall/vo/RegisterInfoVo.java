package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/16:15
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterInfoVo {

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "phone_code")
    private String checkCode;

    private String password;
}
