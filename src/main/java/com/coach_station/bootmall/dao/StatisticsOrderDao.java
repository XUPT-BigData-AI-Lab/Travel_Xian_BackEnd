package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.OrderStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/23/14:25
 * @Description:
 */
public interface StatisticsOrderDao extends JpaRepository<OrderStatistics,Integer> {

    @Transactional
    @Query(value = "SELECT * " +
            "FROM order_statistics o " +
            "where o.sharding_key between 0 and 9 for update ",nativeQuery = true)
    List<OrderStatistics> getTodayOrderQuantity();

    OrderStatistics findByStatisticsTypeAndDate(Integer statisticsType, String date);
}
