package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/01/10/23:43
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactPersonVo {
    @JsonProperty(value = "contact_person_id")
    private Long personId;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String email;

    private String name;
}
