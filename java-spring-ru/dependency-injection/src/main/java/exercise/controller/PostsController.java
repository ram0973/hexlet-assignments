package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostsController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public List<Post> index() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post showPost(@PathVariable int id) {
        return postRepository.findById((long) id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Post post) {
        postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable int id, @RequestBody Post post) {
        var maybePost = postRepository.findById((long) id).orElseThrow(() -> new ResourceNotFoundException("ResourceNotFoundException"));
        maybePost.setTitle(post.getTitle());
        maybePost.setBody(post.getBody());
        postRepository.save(maybePost);
        return maybePost;
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        postRepository.deleteById((long) id);
        commentRepository.deleteByPostId(id);
    }
}
// END
