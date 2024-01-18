package com.spandev.app.controller;

import com.spandev.app.model.Role;
import com.spandev.app.model.User;
import com.spandev.app.model.Weight;
import com.spandev.app.repositories.UserRepository;
import com.spandev.app.repositories.WeightRepository;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
class UserController {

    private final UserRepository userRepository;

    private final WeightRepository weightRepository;

    @GetMapping("/user")
    public String getUserPage(Principal principal, Model model) {
        User u = userRepository.findByUsername(principal.getName()).isPresent() ?
                userRepository.findByUsername(principal.getName()).get() : null;
        model.addAttribute("user", u);
        List<String> r = new ArrayList<>();
        assert u != null;
        for (Role role : u.getRoleSet()) {
            r.add(role.getRoleName());
        }
        model.addAttribute("role", r);
        return "user";
    }

    @PostMapping("/add_weight")
    public ResponseEntity<Boolean> addWeight(@RequestBody Map<Object, String> data, Principal principal) {
        if (userRepository.findByUsername(principal.getName()).isPresent()) {
            Weight weight = new Weight();
            weight.setWeight(Double.parseDouble(data.get("weight")));
            LocalDate date = LocalDate.parse(data.get("weight_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            weight.setDate(date);
            weight.setUserId(userRepository.findByUsername(principal.getName()).get().getId());
            weightRepository.save(weight);
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }

}
