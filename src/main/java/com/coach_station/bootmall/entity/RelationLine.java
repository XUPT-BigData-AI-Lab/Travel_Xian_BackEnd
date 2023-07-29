package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @Auther: yjw
 * @Date: 2022/05/13/15:43
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RelationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationLineId;

    private String frontLine;

    private String EndLine;

    private BigDecimal confidence;
}