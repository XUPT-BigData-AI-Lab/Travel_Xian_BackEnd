package com.coach_station.bootmall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: yjw
 * @Date: 2022/03/07/17:23
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleVo {
    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "content")
    private String content;
}
