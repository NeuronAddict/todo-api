package tech.woodandsafety;

import java.time.LocalDate;

public record MessageDTO(String message, CustomUser author, LocalDate dueDate) {
}
