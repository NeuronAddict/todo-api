package tech.woodandsafety.data;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

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

    @Transactional
    Uni<Message> updateAuthor(String author) {
        return CustomUser.findByName(author).flatMap(user -> {
            this.author = user;
            return this.persistAndFlush();
        });
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", author=" + author +
                ", logEntry=" + logEntry +
                ", dueDate=" + dueDate +
                '}';
    }
}
