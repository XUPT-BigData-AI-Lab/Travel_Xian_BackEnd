package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.RegionDao;
import com.coach_station.bootmall.dao.RelationLineDao;
import com.coach_station.bootmall.entity.Region;
import com.coach_station.bootmall.entity.RelationLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/05/13/16:14
 * @Description:
 */
@Service
public class RelationLineService {
    @Autowired
    RelationLineDao relationLineDao;

    public void deleteAll(){
        relationLineDao.deleteAll();
    }

    public List<RelationLine> findAllByFrontLine(String frontLine){
        return relationLineDao.findAllByFrontLine(frontLine);
    }

    public void insertRelationLines(List<RelationLine> relationLines){
        relationLineDao.saveAll(relationLines);
    }

}
