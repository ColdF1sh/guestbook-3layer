package ua.edu.lab.core.port;

import ua.edu.lab.core.domain.Comment;

import java.util.List;

public interface CommentRepositoryPort {
    List<Comment> listCommentsByBookId(long bookId);
    Comment addComment(long bookId, String author, String text);
}



