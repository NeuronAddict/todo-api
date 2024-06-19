package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class LogEntry extends PanacheEntity {

    public LogEntryType type;

    @ManyToOne
    public Message message;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    public CustomUser customUser;

    public LogEntry(LogEntryType type, Message message, CustomUser initiator) {
        this.type = type;
        this.message = message;
        this.customUser = initiator;
    }

    protected LogEntry() {

    }
}
