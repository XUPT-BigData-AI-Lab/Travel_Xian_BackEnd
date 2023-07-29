package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.ShuttleDao;
import com.coach_station.bootmall.entity.ShuttleLine;
import com.coach_station.bootmall.vo.ShuttleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/0:26
 * @Description:
 */
@Service
public class ShuttleService {

    @Autowired
    ShuttleDao shuttleDao;

    Page<ShuttleLine> findAll(Pageable pageable){
        return shuttleDao.findAll(pageable);
    }

    ShuttleLine findByLineId(Long line_id){
        return shuttleDao.findByLineId(line_id);
    }

    ShuttleLine findByStartRegionIdAndAndFinalRegionId(Long startReginId, Long finnalRegionId){
        return shuttleDao.findByStartRegionIdAndAndFinalRegionId(startReginId,finnalRegionId);
    }

    public List<ShuttleLine> findByViaStartRegionIdAndViaFinalRegionId(Long viaStartReginId, Long viaFinnalRegionId){
        return shuttleDao.findByViaStartRegionIdAndViaFinalRegionId(viaStartReginId,viaFinnalRegionId);
    }

    ShuttleLine findByStartRegionAndFinalRegion(String startRegin, String finnalRegion){
        return shuttleDao.findByStartRegionAndFinalRegion(startRegin,finnalRegion);
    }

    List<ShuttleLine> findAllByLineIdIn(List<Long> lineIds){
        return shuttleDao.findAllByLineIdIn(lineIds);
    }

    public List<ShuttleLine> findAll(){
        return shuttleDao.findAll();
    }
}
