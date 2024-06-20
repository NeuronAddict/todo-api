package tech.woodandsafety.mapper;

public interface Mapper<DTO, Entity> {

    Entity toEntity(DTO dto);

    DTO toDTO(Entity entity);
}
