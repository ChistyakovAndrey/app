package com.spandev.app.service;

import com.spandev.app.model.Role;
import com.spandev.app.model.User;
import com.spandev.app.repositories.RoleRepository;
import com.spandev.app.repositories.UserRepository;
import com.spandev.app.repositories.WeightRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final WeightRepository weightRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public UserService(WeightRepository weightRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.weightRepository = weightRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            Optional<User> byUsername = userRepository.findByUsername(s);
            if (byUsername.isEmpty()) {
                return null;
            }
            User user = byUsername.get();
            List<Map<String, Object>> strRoles = roleRepository.getRoles(user.getId());
            Set<Role> roleSet = new HashSet<>();
            for (Map<String, Object> role : strRoles) {
                Role r = new Role();
                int roleId = Integer.parseInt(role.get("id").toString());
                r.setId(roleId);
                r.setRoleName(role.get("role_name").toString());
                roleSet.add(r);
            }
            user.setRoleSet(roleSet);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(roleSet));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream().map(e -> new SimpleGrantedAuthority(e.getRoleName())).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getWeightList(Integer userId, String scaleDate) {
        return weightRepository.getWeight(userId, scaleDate);
    }
    public List<Map<String, Object>> getWeightList(Integer userId) {
        return weightRepository.getWeight(userId);
    }

}
