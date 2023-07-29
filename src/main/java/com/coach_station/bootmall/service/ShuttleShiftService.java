package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.ShuttleShiftDao;
import com.coach_station.bootmall.entity.ShuttleShift;
import com.coach_station.bootmall.enumAndConst.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/20:36
 * @Description:
 */
@Service
public class ShuttleShiftService {
    @Autowired
    ShuttleShiftDao shuttleShiftDao;

    List<ShuttleShift> findByShuttleLineIdAndShuttleShiftDate(Long shuttleLineId, String shuttleShiftDate){
        return shuttleShiftDao.findByShuttleLineIdAndShuttleShiftDateAndIsDelete(shuttleLineId, shuttleShiftDate, Const.NOT_DELETE);
    }

    Page<ShuttleShift> findByShuttleLineIdAndShuttleShiftDate(Long shuttleLineId, String shuttleShiftDate, Pageable pageable){
        return shuttleShiftDao.findByShuttleLineIdAndShuttleShiftDateAndIsDelete(shuttleLineId, shuttleShiftDate, Const.NOT_DELETE, pageable);
    }

    ShuttleShift findByShiftId(Long shuttleShiftId){
        return shuttleShiftDao.findByShiftId(shuttleShiftId);
    }

    ShuttleShift findByShiftIdAndLock(Long shuttleShiftId){
        return shuttleShiftDao.findByShiftIdAndLockRow(shuttleShiftId);
    }

    public List<ShuttleShift> findByShiftIdIn(Set<Long> shuttleShiftIds){
        return shuttleShiftDao.findByShiftIdIn(shuttleShiftIds);
    }

    ShuttleShift addShuttleShift(ShuttleShift shuttleShift){
        return shuttleShiftDao.save(shuttleShift);
    }

    public List<ShuttleShift> addShuttleShifts(List<ShuttleShift> shuttleShifts){
        return shuttleShiftDao.saveAll(shuttleShifts);
    }

    public List<ShuttleShift> findByShuttleShiftDate(String shuttleShiftDate){
        return shuttleShiftDao.findByShuttleShiftDate(shuttleShiftDate);
    }

    public List<ShuttleShift> saveAll(List<ShuttleShift> shuttleShifts){
        return shuttleShiftDao.saveAll(shuttleShifts);
    }

    public ShuttleShift save(ShuttleShift shuttleShift){
        return shuttleShiftDao.save(shuttleShift);
    }

    public Integer deleteByShiftId(Long shuttleId){
        return shuttleShiftDao.removeByShiftId(shuttleId);
    }

    public List<ShuttleShift> findByShuttleLineIdInAndLineType(Set<Long> shuttleLineIds,Integer lineType){
        return shuttleShiftDao.findByShuttleLineIdInAndLineType(shuttleLineIds,lineType);
    }
}
