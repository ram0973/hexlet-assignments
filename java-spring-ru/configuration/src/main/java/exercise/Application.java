package exercise;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.User;
import exercise.component.UserProperties;

@SpringBootApplication
@RestController
public class Application {

    // Все пользователи
    private final List<User> users = Data.getUsers();

    // BEGIN
    // Добавьте в приложение обработчик, который при GET-запросе на адрес
    // /admins вернет список имен администраторов. Список должен быть отсортирован по имени пользователя в прямом порядке.


    private final UserProperties userProperties;

    @Autowired
    public Application(UserProperties userProperties) {
        this.userProperties = userProperties;
    }

    @GetMapping("/admins")
    public ResponseEntity<List<String>> admins() {
        List<String> admins = Data.getUsers().stream()
                .filter(user -> userProperties.getAdmins().contains(user.getEmail()))
                .map(User::getName)
                .sorted()
                .toList();
        return ResponseEntity.ok(admins);
    }
    // END

    @GetMapping("/users")
    public List<User> index() {
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<User> show(@PathVariable Long id) {
        return users.stream()
            .filter(u -> Objects.equals(u.getId(), id))
            .findFirst();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
