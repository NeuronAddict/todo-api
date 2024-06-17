package tech.woodandsafety;

public interface Mapper<DTO, Entity> {
    Entity toEntity(DTO dto);
}
