package tech.woodandsafety.mapper;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;

public interface CreateMapper<Entity extends PanacheEntityBase, DisplayDTO, CreateDTO> extends DisplayMapper<Entity, DisplayDTO> {

    Uni<Entity> toEntity(CreateDTO dto);

    Uni<Entity> updateWithDTO(Entity entity, CreateDTO dto);
}
