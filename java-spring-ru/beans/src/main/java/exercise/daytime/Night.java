package exercise.daytime;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Night implements Daytime {
    private final String name = "night";

    // BEGIN
    @PostConstruct()
    public void init() {
        System.out.println("Bean Night is initialized!");
    }
    // END
}
