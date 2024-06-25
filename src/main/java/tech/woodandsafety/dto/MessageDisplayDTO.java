package tech.woodandsafety.dto;

import java.time.LocalDate;

public record MessageDisplayDTO(Long id, String message, String author, LocalDate dueDate) {
}
