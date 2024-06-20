package tech.woodandsafety.dto;

import java.time.LocalDate;

public record MessageDTO(String message, String author, LocalDate dueDate) {
}
