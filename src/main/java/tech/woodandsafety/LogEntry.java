package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class LogEntry extends PanacheEntity {
    public LogEntryType type;

    @OneToOne
    public Message message;

    @ManyToOne
    public CustomUser initiator;
}
