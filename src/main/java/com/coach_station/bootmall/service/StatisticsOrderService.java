package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.StatisticsOrderDao;
import com.coach_station.bootmall.entity.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/23/20:07
 * @Description:
 */
@Service
public class StatisticsOrderService {

    @Autowired
    StatisticsOrderDao statisticsOrderDao;

    public List<OrderStatistics> getTodayOrderQuantity(){
        return statisticsOrderDao.getTodayOrderQuantity();
    }

    public List<OrderStatistics> saveAll(List<OrderStatistics> statisticsOrders){
        return statisticsOrderDao.saveAll(statisticsOrders);
    }

    public OrderStatistics save(OrderStatistics statisticsOrder){
        return statisticsOrderDao.save(statisticsOrder);
    }

    public OrderStatistics findByStatisticsTypeAndDate(Integer statisticsType, String date){
        return statisticsOrderDao.findByStatisticsTypeAndDate(statisticsType,date);
    }
}
