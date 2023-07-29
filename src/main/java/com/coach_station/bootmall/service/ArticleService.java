package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.ArticleDao;
import com.coach_station.bootmall.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: yjw
 * @Date: 2020/03/15/10:35
 * @Description:
 */
@Service
public class ArticleService {
    @Autowired
    ArticleDao articleDao;

    public Article findByArticleId(Long articleId){
        return articleDao.findByArticleId(articleId);
    }

}
