package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Message extends PanacheEntity {

    public String message;

    @ManyToOne
    @JoinColumn(name = "author_id")
    public CustomUser author;

    @OneToMany
    public List<LogEntry> logEntry;

    public LocalDate dueDate;

    public Message(String message, CustomUser author, LocalDate dueDate) {
        this.message = message;
        this.author = author;
        this.dueDate = dueDate;
    }

    protected Message() {

    }
}
