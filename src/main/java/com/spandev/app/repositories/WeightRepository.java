package com.spandev.app.repositories;

import com.spandev.app.model.Weight;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Integer> {

    @Query(
            value = "SELECT w.date, w.weight FROM weight w WHERE w.user_id = :userId ORDER BY date",
            nativeQuery = true
    )
    List<Map<String, Object>> getWeight(Integer userId);

    @Query(
            value = "SELECT w.date, w.weight " +
                    "FROM weight w " +
                    "WHERE w.user_id = :userId " +
                    "AND (CASE WHEN :scaleDate IS NOT NULL THEN w.date > CAST(:scaleDate AS DATE) ELSE true END) " +
                    "ORDER BY date",
            nativeQuery = true
    )
    List<Map<String, Object>> getWeight(Integer userId, String scaleDate);

}

