package com.coach_station.bootmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Auther: yjw
 * @Date: 2022/03/07/16:31
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String title;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    private String content;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    private String htmlContent; // 将 md 转为 html
}
