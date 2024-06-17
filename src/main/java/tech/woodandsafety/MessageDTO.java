package tech.woodandsafety;

import java.util.Date;

public record MessageDTO(String message, CustomUser author, Date dueDate) {
}
