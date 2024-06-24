package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import tech.woodandsafety.data.CustomUser;
import tech.woodandsafety.data.Message;
import tech.woodandsafety.dto.MessageDTO;
import tech.woodandsafety.data.MessageCreateMapper;

import java.net.URI;
import java.util.List;

@Path("messages")
public class MessageResource {

    private static final Logger LOGGER = Logger.getLogger(MessageResource.class);

    private final MessageCreateMapper messageMapper;

    public MessageResource(MessageCreateMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get() {

        return Message.<Message>listAll().map(messages -> messages.stream().map(messageMapper::toDisplayDTO).toList())
                .map(messageDTOS -> Response.ok(messageDTOS).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(@PathParam("id") Long id) {

        return Message.<Message>findById(id).map(messageMapper::toDisplayDTO)
                .map(messageDTO -> Response.ok(messageDTO).build());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(MessageDTO message) {

        return Uni.createFrom().item(message).flatMap(messageMapper::toEntity)
                .flatMap(message1 -> Panache.withTransaction(message1::<Message>persistAndFlush))
                .log()
                .map(message1 -> Response.created(URI.create(uriInfo.getPath() + "/" + message1.id)).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(@PathParam("id") Long id, MessageDTO messageDTO) {

        return Message.<Message>findById(id)
                .onItem()
                .ifNull()
                .failWith(() -> new UnsupportedOperationException("Can't find Message with id " + id))
                .invoke(message -> System.out.println("Find message in PUT : " + message))
                        .flatMap(message -> messageMapper.updateWithDTO(message, messageDTO))
                        .flatMap(message -> Panache.withTransaction(message::<Message>persistAndFlush)
                )
                .replaceWith(Response.noContent().build());
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Message.<Message>findById(id)
                .onItem()
                .ifNotNull()
                .transformToUni(message -> Panache.withTransaction(message::delete))
                .onItem().transform(item -> Response.noContent().build())
                .onFailure().invoke(LOGGER::error)
                .onItem()
                .ifNull()
                .fail()
                .replaceWith(Response.status(404).build());
    }
}
