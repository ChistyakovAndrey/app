package com.spandev.app.repositories;

import com.spandev.app.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(
            value = "SELECT u.ext_feature -> 'weight' ->> 'goal_weight' as g_weight, u.ext_feature -> 'weight' ->> 'goal_date' as g_date from users u WHERE u.id = :userId",
            nativeQuery = true
    )
    List<Map<String, String>> weightInit(Integer userId);

}
