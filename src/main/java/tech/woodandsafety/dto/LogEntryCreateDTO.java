package tech.woodandsafety.dto;

import tech.woodandsafety.data.LogEntryType;

public record LogEntryCreateDTO(LogEntryType logEntryType, MessageDisplayDTO message, String initiator) {

}
