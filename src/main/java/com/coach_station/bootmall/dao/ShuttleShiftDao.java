package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.entity.ShuttleShift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/20:26
 * @Description:
 */
public interface ShuttleShiftDao extends JpaRepository<ShuttleShift,Integer> {
    List<ShuttleShift> findByShuttleLineIdAndShuttleShiftDateAndIsDelete(Long shuttleLineId, String shuttleShiftDate, Integer isDelete);

    ShuttleShift findByShiftId(Long shuttleShiftId);

    List<ShuttleShift> findByShuttleShiftDate(String shuttleShiftDate);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<ShuttleShift> findByShiftIdIn(Set<Long> shuttleShiftIds);

    @Transactional
    @Query(value = "select * from shuttle_shift as o where o.shift_id = ?1 for update ", nativeQuery = true)
    ShuttleShift findByShiftIdAndLockRow(Long shuttleShiftId);

    List<ShuttleShift> findByShuttleLineIdInAndLineType(Set<Long> shuttleLineIds,Integer lineType);

    Page<ShuttleShift> findByShuttleLineIdAndShuttleShiftDateAndIsDelete(Long shuttleLineId, String shuttleShiftDate, Integer isDelete, Pageable pageable);

    @Transactional
    Integer removeByShiftId(Long shiftId);

}
