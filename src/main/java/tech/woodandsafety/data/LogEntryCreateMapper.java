package tech.woodandsafety.data;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.inject.Singleton;
import tech.woodandsafety.dto.LogEntryCreateDTO;
import tech.woodandsafety.mapper.CreateMapper;

@Singleton
public class LogEntryCreateMapper implements CreateMapper<LogEntry, LogEntryDisplayDTO, LogEntryCreateDTO> {

    private final MessageCreateMapper messageCreateMapper;

    public LogEntryCreateMapper(MessageCreateMapper messageCreateMapper) {
        this.messageCreateMapper = messageCreateMapper;
    }

    @Override
    public Uni<LogEntry> toEntity(LogEntryCreateDTO logEntryCreateDTO) {
        return CustomUser.findByName(logEntryCreateDTO.initiator())
                .flatMap(customUser -> Message.<Message>findById(logEntryCreateDTO.message().id())
                        .map(message -> Tuple2.of(customUser, message)))
                .map(objects -> new LogEntry(logEntryCreateDTO.logEntryType(), objects.getItem2(), objects.getItem1()));
    }

    @Override
    public Uni<LogEntry> updateWithDTO(LogEntry logEntry, LogEntryCreateDTO logEntryCreateDTO) {
        throw new UnsupportedOperationException("Can't update log entry");
    }

    @Override
    public LogEntryDisplayDTO toDisplayDTO(LogEntry logEntry) {
        return new LogEntryDisplayDTO(logEntry.id, logEntry.type, messageCreateMapper.toDisplayDTO(logEntry.message), logEntry.initiator.getName());
    }
}
