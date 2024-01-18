package com.spandev.app.controller;

import com.spandev.app.dto.UserWeightDTO;
import com.spandev.app.repositories.UserRepository;
import com.spandev.app.service.RegService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
class RegController {

    private final UserRepository userRepository;

    private final RegService regService;

    @PostMapping(value = "/addUser", consumes = "application/json")
    public String addUser(@RequestBody UserWeightDTO userWeightDTO) {
        regService.registerNewUser(userWeightDTO);
        return "redirect:/login";
    }

    @PostMapping("/doesUserExist")
    public ResponseEntity<Boolean> doesUserExist(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("/doesEmailExist")
    public ResponseEntity<Boolean> doesEmailExist(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        return userRepository.findByEmail(email).isPresent() ? ResponseEntity.ok().body(true) : ResponseEntity.ok().body(false);
//        if (userRepository.findByEmail(email).isPresent()) {
//            return ResponseEntity.ok().body(true);
//        } else {
//            return ResponseEntity.ok().body(false);
//        }
    }

}
