package tech.woodandsafety.mapper;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;

public interface DisplayMapper<Entity extends PanacheEntityBase, DTO> {

    DTO toDisplayDTO(Entity entity);
}
