package security.git.McaProject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @GetMapping("/")
    public String home() {
        return "J-Sentinel AI is running 🚀";
    }
}