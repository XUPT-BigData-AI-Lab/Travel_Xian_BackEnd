package com.coach_station.bootmall.service;

import org.springframework.web.client.RestTemplate;

/**
 * @Auther: yjw
 * @Date: 2022/01/08/15:53
 * @Description:
 */
public class Test {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        //params 请求参数拼接
        StringBuilder urlbuilder = new StringBuilder("http://localhost:8085/edit");
        System.out.println("进来了");
        System.out.println(restTemplate.getForObject(urlbuilder.toString() , String.class));
    }
}
