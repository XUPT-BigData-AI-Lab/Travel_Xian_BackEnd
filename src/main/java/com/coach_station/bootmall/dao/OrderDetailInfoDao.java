package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.dto.OrderDetailInfoDto;
import com.coach_station.bootmall.dto.OrderInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/9:44
 * @Description:
 */
public interface OrderDetailInfoDao extends JpaRepository<OrderDetailInfoDto,Integer> {

    @Query(value = "select o.order_id,o.order_number,o.total_amount,o.order_status,s.shuttle_shift_date," +
            " s.shuttle_shift_time,l.start_region,l.final_region,o.ticket_type,p.card_type,p.card_number," +
            " p.name,o.refund_amount,o.ride_code,s.start_station,s.final_station,s.ticket_price,s.station_fee," +
            " s.refund_fee,s.insurance_price,o.insurance_status,s.shuttle_shift_type,s.line_type,c.car_model,s.shift_id" +
            " from order_info as o " +
            " inner join shuttle_shift as s " +
            " on o.shuttle_shift_id = s.shift_id" +
            " inner join shuttle_line as l " +
            " on s.shuttle_line_id = l.line_id" +
            " inner join passenger as p" +
            " on p.passenger_id = o.passenger_id" +
            " inner join car as c" +
            " on s.car_id = c.car_id" +
            " where o.master_order_number = ?1 " +
            " and o.user_id = ?2 " +
            " and o.order_type = ?3 " +
            " and o.is_delete = 0 " +
            " order by o.order_id desc", nativeQuery = true)
    List<OrderDetailInfoDto> findOrderDetailInfoByMasterOrderNumber(String masterOrderNumber, Long userId, Integer orderType);

}
