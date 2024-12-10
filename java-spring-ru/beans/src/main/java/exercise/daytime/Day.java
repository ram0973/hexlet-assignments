package exercise.daytime;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Day implements Daytime {
    private final String name = "day";

    // BEGIN
    @PostConstruct()
    public void init() {
        System.out.println("Bean Day is initialized!");
    }
    // END
}
