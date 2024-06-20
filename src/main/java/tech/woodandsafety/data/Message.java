package tech.woodandsafety.data;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Message extends PanacheEntity {

    String message;

    @ManyToOne
    @JoinColumn(name = "author_id")
    CustomUser author;

    @OneToMany
    List<LogEntry> logEntry;

    LocalDate dueDate;

    public Message(String message, CustomUser author, LocalDate dueDate) {
        this.message = message;
        this.author = author;
        this.dueDate = dueDate;
    }

    protected Message() {}

    public String getMessage() {
        return message;
    }

    public CustomUser getAuthor() {
        return author;
    }

    public List<LogEntry> getLogEntry() {
        return logEntry;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
