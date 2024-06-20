package tech.woodandsafety.dto;

import tech.woodandsafety.data.CustomUser;

import java.time.LocalDate;

public record MessageDTO(String message, CustomUserDTO author, LocalDate dueDate) {
}
