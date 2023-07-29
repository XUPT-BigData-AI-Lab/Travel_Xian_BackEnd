package com.coach_station.bootmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @Auther: yjw
 * @Date: 2022/01/12/13:54
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class OrderInfoDto {
    @Id
    private Long orderId;

    private String orderNumber;

    private BigDecimal totalAmount;

    private String shuttleShiftTime;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date shuttleShiftDate;

    private Integer orderStatus;

    private String startRegion;

    private String finalRegion;
}
