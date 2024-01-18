package com.spandev.app.service;

import com.spandev.app.dto.UserWeightDTO;
import com.spandev.app.model.Role;
import com.spandev.app.model.User;
import com.spandev.app.repositories.RoleRepository;
import com.spandev.app.repositories.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegService {

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Transactional
    public void registerNewUser(UserWeightDTO userWeightDTO) {
        Optional<User> doesUserExist = userRepository.findByUsername(userWeightDTO.getUsername());
        if (doesUserExist.isEmpty()) {
            Set<Role> roleForNewUser = new HashSet<>();
            Optional<Role> maybeRole = roleRepository.findByRoleName("ROLE_USER");
            if (maybeRole.isPresent()) {
                maybeRole.get().setUsers(null);
                roleForNewUser.add(maybeRole.get());
            }
            userRepository.save(
                    new User().setName(userWeightDTO.getName())
                            .setUsername(userWeightDTO.getUsername())
                            .setAge(userWeightDTO.getAge())
                            .setPassword(passwordEncoder.encode(userWeightDTO.getPassword()))
                            .setGender(userWeightDTO.getGender())
                            .setRoleSet(roleForNewUser)
                            .setEmail(userWeightDTO.getEmail())
                            .setId(null));
        }
    }

}
