package ua.edu.lab.web.dto;

public class CreateCommentRequest {
    private long bookId;
    private String author;
    private String text;

    public CreateCommentRequest() {
    }

    public CreateCommentRequest(long bookId, String author, String text) {
        this.bookId = bookId;
        this.author = author;
        this.text = text;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


