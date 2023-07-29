package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/09/19:47
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyPasswordVo {

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String password;

    @JsonProperty(value = "old_password")
    private String oldPassword;
}
