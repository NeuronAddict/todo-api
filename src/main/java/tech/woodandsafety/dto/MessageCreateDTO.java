package tech.woodandsafety.dto;

import java.time.LocalDate;

public record MessageCreateDTO(String message, String author, LocalDate dueDate) {
}
