package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/20:12
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginInfoVo {

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String password;

    @JsonProperty(value = "check_code")
    private String checkCode;
}
