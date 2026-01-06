package ua.edu.lab.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.lab.core.domain.Comment;
import ua.edu.lab.core.service.CommentService;
import ua.edu.lab.web.dto.CreateCommentRequest;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@RequestBody CreateCommentRequest request) {
        return commentService.addComment(
            request.getBookId(),
            request.getAuthor(),
            request.getText()
        );
    }
}


