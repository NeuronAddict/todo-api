package tech.woodandsafety.data;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class LogEntry extends PanacheEntity {

    @Enumerated(EnumType.STRING)
    LogEntryType type;

    @ManyToOne
    @JoinColumn(name = "message_id")
    Message message;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    CustomUser initiator;

    public LogEntry(LogEntryType type, Message message, CustomUser initiator) {
        this.type = type;
        this.message = message;
        this.initiator = initiator;
    }

    protected LogEntry() {

    }

    public static Uni<List<LogEntry>> findByInitiator(String name) {
        return LogEntry.find("SELECT l FROM LogEntry l JOIN l.initiator i WHERE i.name = ?1", name).list();
    }

    public LogEntryType getType() {
        return type;
    }

    public Message getMessage() {
        return message;
    }

    public CustomUser getInitiator() {
        return initiator;
    }
}
