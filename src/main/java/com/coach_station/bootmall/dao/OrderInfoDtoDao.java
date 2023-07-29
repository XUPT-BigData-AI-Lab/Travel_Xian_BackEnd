package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.dto.OrderInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/11/17:38
 * @Description:
 */
public interface OrderInfoDtoDao extends JpaRepository<OrderInfoDto,Integer> {

    @Query(value = "select o.order_id,o.order_info,o.total_amount,o.order_status,s.shuttle_shift_date,s.shuttle_shift_time,l.start_region,l.final_region " +
            " from order_info o" +
            " inner join shuttle_shift s" +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line l" +
            " on s.shuttle_line_id = l.line_id" +
            " where o.user_id = ?1 " +
            " and o.order_type = ?2 " +
            " and o.is_delete = 0 " +
            " and o.create_time between ?3 and ?4 " +
            " order by o.order_id desc ",nativeQuery = true)
    List<OrderInfoDto> findAllMasterOrdersByTime(Long userId, Integer orderType, Long startTime, Long endTime);

    @Query(value = "select o.order_id,o.order_number,o.total_amount,o.order_status,s.shuttle_shift_date,s.shuttle_shift_time,l.start_region,l.final_region " +
            " from order_info o" +
            " inner join shuttle_shift s" +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line l" +
            " on s.shuttle_line_id = l.line_id" +
            " where o.user_id = ?1 " +
            " and o.order_type = ?2 " +
            " and o.order_status = ?3"+
            " and o.is_delete = 0 " +
            " and o.create_time between ?4 and ?5 " +
            " order by o.order_id desc ",nativeQuery = true)
    List<OrderInfoDto> findMasterOrdersByTimeAndOrderStatus(Long userId, Integer orderType, Integer orderStatus, Long startTime, Long endTime);

    @Query(value = "select o.order_id,o.order_number,o.total_amount,o.order_status,s.shuttle_shift_date,s.shuttle_shift_time,l.start_region,l.final_region " +
            " from order_info o" +
            " inner join shuttle_shift s" +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line l" +
            " on s.shuttle_line_id = l.line_id" +
            " where o.user_id = ?1 " +
            " and o.order_type = ?2 " +
            " and o.order_status in (?3,?4) "+
            " and o.is_delete = 0 " +
            " and o.create_time between ?5 and ?6 " +
            " order by o.order_id desc ",nativeQuery = true)
    List<OrderInfoDto> findRefundMasterOrdersByTime(Long userId, Integer orderType, Integer refundAll, Integer refundPart, Long startTime, Long endTime);

    @Query(value = "select o.order_id,o.order_number,o.total_amount,o.order_status,s.shuttle_shift_date,s.shuttle_shift_time,l.start_region,l.final_region " +
            " from order_info o" +
            " inner join shuttle_shift s" +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line l" +
            " on s.shuttle_line_id = l.line_id" +
            " where o.user_id = ?1 " +
            " and o.order_type = ?2 " +
            " and o.is_delete = 0 " +
            " order by o.order_id desc ",nativeQuery = true)
    List<OrderInfoDto> findAllMasterOrders(Long userId, Integer orderType);

    @Query(value = "select o.order_id,o.order_number,o.total_amount,o.order_status,s.shuttle_shift_date,s.shuttle_shift_time,l.start_region,l.final_region " +
            " from order_info o" +
            " inner join shuttle_shift s" +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line l" +
            " on s.shuttle_line_id = l.line_id" +
            " where o.user_id = ?1 " +
            " and o.order_type = ?2 " +
            " and o.order_status = ?3"+
            " and o.is_delete = 0 " +
            " order by o.order_id desc ",nativeQuery = true)
    List<OrderInfoDto> findMasterOrdersByOrderStatus(Long userId, Integer orderType, Integer orderStatus);

    @Query(value = "select o.order_id,o.order_number,o.total_amount,o.order_status,s.shuttle_shift_date,s.shuttle_shift_time,l.start_region,l.final_region " +
            " from order_info o" +
            " inner join shuttle_shift s" +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line l" +
            " on s.shuttle_line_id = l.line_id" +
            " where o.user_id = ?1 " +
            " and o.order_type = ?2 " +
            " and o.is_delete = 0 " +
            " and o.order_status in (?3,?4) "+
            " order by o.order_id desc ",nativeQuery = true)
    List<OrderInfoDto> findRefundOrders(Long userId, Integer orderType, Integer refundAll, Integer refundPart);

}
