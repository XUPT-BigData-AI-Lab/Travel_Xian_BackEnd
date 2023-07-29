package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.RelationLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/05/13/16:13
 * @Description:
 */
public interface RelationLineDao extends JpaRepository<RelationLine,Integer> {
    List<RelationLine> findAllByFrontLine(String frontLine);
}
