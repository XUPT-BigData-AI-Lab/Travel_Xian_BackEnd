package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.RelationLine;
import com.coach_station.bootmall.entity.ShuttleLine;
import com.coach_station.bootmall.entity.ShuttleShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/23:52
 * @Description:
 */
public interface ShuttleDao extends JpaRepository<ShuttleLine,Integer> {
    ShuttleLine findByStartRegionIdAndAndFinalRegionId(Long startReginId, Long finnalRegionId);

    ShuttleLine findByStartRegionAndFinalRegion(String startRegin, String finnalRegion);

    ShuttleLine findByLineId(Long line_id);

    List<ShuttleLine>  findAllByLineIdIn(List<Long> lineIds);

    @Transactional
    @Query(value = "SELECT * FROM shuttle_line as o where o.via_regions_id like \"%(?1)%(?2)%\"", nativeQuery = true)
    List<ShuttleLine> findByViaStartRegionIdAndViaFinalRegionId(Long viaStartReginId, Long viaFinnalRegionId);
}
