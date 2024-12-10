package exercise.controller.users;

import exercise.Data;
import exercise.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BEGIN

// GET /api/users/{id}/posts — список всех постов, написанных пользователем с таким же userId, как id в маршруте
//POST /api/users/{id}/posts – создание нового поста, привязанного к пользователю по id.
// Код должен возвращать статус 201, тело запроса должно содержать slug, title и body.
// Обратите внимание, что userId берется из маршрута
// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts") // Список страниц
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(defaultValue = "32") Long limit,
                                            @PathVariable("id") Integer id) {
        var result = posts.stream().filter(post -> post.getUserId() == id).skip((long) (page - 1) * pageSize).limit(limit).toList();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.size())).body(result);
    }

    @PostMapping("/users/{id}/posts") // Создание страницы
    public ResponseEntity<Post> create(@RequestBody Post post, @PathVariable("id") Integer id) {
        post.setUserId(id);
        posts.add(post);
        System.out.println(posts.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}
// END
