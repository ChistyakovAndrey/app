package com.spandev.app.controller;

import com.spandev.app.dto.UserCredDTO;
import com.spandev.app.model.User;
import com.spandev.app.repositories.UserRepository;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
class LoginController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(value = "/isUserValid", produces = "application/json")
    public ResponseEntity<Boolean> isUserValid(@RequestBody UserCredDTO usr) {
        User user = userRepository.findByUsername(usr.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(false);
        }
        boolean isPassCorrect = passwordEncoder.matches(usr.getPassword(), user.getPassword());
        return ResponseEntity.ok(isPassCorrect);
    }

}
