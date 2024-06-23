package tech.woodandsafety.data;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;

@Entity
public class LogEntry extends PanacheEntity {

    public LogEntryType type;

    @ManyToOne
    public Message message;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    public CustomUser initiator;

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
}
