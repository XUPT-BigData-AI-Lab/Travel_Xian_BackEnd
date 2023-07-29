package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/16/17:56
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookOrderVo {
        @JsonProperty(value = "shuttle_shift_id")
        private Long shuttleShiftId;

        @JsonProperty(value = "contact_person_id")
        private Long contactPersonId;

        @JsonProperty(value = "contact_person_phone_number")
        private String contactPersonPhoneNumber;

        @JsonProperty(value = "contact_person_name")
        private String contactPersonName;

        @JsonProperty(value = "contact_person_email")
        private String contactPersonEmail;

        @JsonProperty(value = "passenger")
        private List<OrderPassengerVo> passengers;
}
