package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;

@Entity
public class Message extends PanacheEntity {

    public String message;

    @ManyToOne
    public CustomUser author;

    public Date dueDate;
}
