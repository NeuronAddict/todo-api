package tech.woodandsafety.data;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Message extends PanacheEntity {

    String message;

    @ManyToOne
    @JoinColumn(name = "author_id", updatable = false)
    CustomUser author;

    LocalDate dueDate;

    public Message(String message, CustomUser author, LocalDate dueDate) {
        this.message = message;
        this.author = author;
        this.dueDate = dueDate;
    }

    protected Message() {
    }

    public String getMessage() {
        return message;
    }

    public CustomUser getAuthor() {
        return author;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public static Uni<List<Message>> findByAuthor(String name) {
        return find("SELECT m FROM Message m JOIN m.author a WHERE a.name = ?1", name).list();
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", author=" + author +
                ", dueDate=" + dueDate +
                '}';
    }
}
