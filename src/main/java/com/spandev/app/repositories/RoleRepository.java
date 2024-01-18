package com.spandev.app.repositories;

import com.spandev.app.model.Role;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String name);
    @Query(
            value = "SELECT r.id, r.role_name FROM roles r WHERE r.id IN (SELECT u.role_id FROM users_roles u WHERE u.user_id = ?);",
            nativeQuery = true
    )
    List<Map<String, Object>> getRoles(Integer userId);
}
