package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentRepository commentRepository;

    public CommentsController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public List<Comment> index() {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Comment showComment(@PathVariable int id) {
        return commentRepository.findById((long) id).orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Comment comment) {
        commentRepository.save(comment);
    }

    @PutMapping("/{id}")
    public Comment update(@PathVariable int id, @RequestBody Comment comment) {
        var maybeComment = commentRepository.findById((long) id).orElseThrow(() -> new ResourceNotFoundException("ResourceNotFoundException"));
        maybeComment.setBody(comment.getBody());
        commentRepository.save(maybeComment);
        return maybeComment;
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable int id) {
        commentRepository.deleteById((long) id);
    }
}
// END
