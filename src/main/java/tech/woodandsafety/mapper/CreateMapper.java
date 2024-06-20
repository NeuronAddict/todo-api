package tech.woodandsafety.mapper;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

public interface CreateMapper<Entity extends PanacheEntityBase, DisplayDTO, CreateDTO> extends DisplayMapper<Entity, DisplayDTO> {

    Entity toEntity(CreateDTO dto);
}
