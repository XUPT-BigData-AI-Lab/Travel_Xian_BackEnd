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
 * @Date: 2022/01/06/23:46
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "contact_person_id")
    private Long personId;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String name;

    @JsonProperty(value = "user_id")
    private Long userId;

    private String email;

    @JsonProperty(value = "is_delete")
    private Integer isDelete;

}
