package tech.woodandsafety.data;

import tech.woodandsafety.dto.MessageDisplayDTO;

public record LogEntryDisplayDTO(Long id, LogEntryType logEntryType, MessageDisplayDTO message, String initiator) {
}
