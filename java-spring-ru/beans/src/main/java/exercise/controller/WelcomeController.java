package exercise.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import exercise.daytime.Daytime;

// BEGIN
@RestController
public class WelcomeController {
    private final Daytime dayTime;

    public WelcomeController(@Qualifier("getDaytime") Daytime dayTime) {
        this.dayTime = dayTime;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return String.format("It is %s now! Welcome to Spring!", dayTime.getName());
    }
}
// END
