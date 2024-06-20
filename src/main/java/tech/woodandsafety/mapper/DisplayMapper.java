package tech.woodandsafety.mapper;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

public interface DisplayMapper<Entity extends PanacheEntityBase, DTO> {

    DTO toDisplayDTO(Entity entity);
}
