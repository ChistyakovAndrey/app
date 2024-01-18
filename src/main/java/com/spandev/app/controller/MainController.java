package com.spandev.app.controller;

import com.spandev.app.model.User;
import com.spandev.app.model.WeightChartData;
import com.spandev.app.repositories.UserRepository;
import com.spandev.app.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
class MainController {

    private final WeightChartData weightChartData;

    private final UserService userService;

    private final UserRepository userRepository;

    @GetMapping
    public String getMainPage(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        return "main";
    }

    @GetMapping("/weight-app")
    public String getWeightPage(@RequestParam(value = "scale", required = false) String scale,
                                Principal principal,
                                Model model) {
        User user = userRepository.findByUsername(principal.getName()).get();
        model.addAttribute("user", user);

        List<Map<String, Object>> weights = userService.getWeightList(user.getId(), weightChartData.getScaleDate(scale));
        if (weights.isEmpty()) {
            model.addAttribute("empty", true);
            return "weight_app";
        }
        model.addAttribute("empty", false);
//        model.addAttribute("weight", weights);
        List<Map<Integer, Double>> filly = weightChartData.getYdata(user, scale);
        model.addAttribute("chartY", filly);
        List<Map<String, Object>> fillx = weightChartData.getXdata(user, 1920, scale);
        model.addAttribute("chartX", fillx);
        return "weight_app";
    }

    @GetMapping("/registration")
    public String register() {
        return "register";
    }

}
