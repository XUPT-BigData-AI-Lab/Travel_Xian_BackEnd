package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: yjw
 * @Date: 2020/03/15/10:32
 * @Description:
 */
public interface ArticleDao extends JpaRepository<Article,Integer> {
    Article findByArticleId(Long articleId);
}
