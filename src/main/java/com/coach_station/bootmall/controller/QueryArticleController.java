package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.QueryService;
import com.coach_station.bootmall.vo.ArticleVo;
import com.coach_station.bootmall.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Auther: yjw
 * @Date: 2022/03/07/16:06
 * @Description:
 */
@RequestMapping("/article")
@Controller
@CrossOrigin
public class QueryArticleController {
    @Autowired
    QueryService queryService;

    // 获取文档接口
    @GetMapping("/getArticleInfo")
    @ResponseBody
    public Result getArticleInfo(@RequestParam(name = "article_id") Long articleId) {
        HashMap<String, Object> articleVo = queryService.getArticleInfo(articleId);
        if (articleVo == null){
            return Result.setResult(ResultCodeEnum.QUERY_GETARTCLEINFO_ERROR);
        }
        return Result.ok().data(articleVo);
    }
}
