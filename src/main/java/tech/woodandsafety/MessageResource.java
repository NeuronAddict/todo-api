package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import tech.woodandsafety.data.Message;
import tech.woodandsafety.data.MessageCreateMapper;
import tech.woodandsafety.dto.MessageCreateDTO;

import java.net.URI;

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

        return Message.<Message>findById(id)
                .onItem()
                .ifNotNull()
                .transform(message -> Response.ok(messageMapper.toDisplayDTO(message)).build())
                .onItem()
                .ifNull()
                .continueWith(() -> Response.status(Response.Status.NOT_FOUND).build())
                .onFailure()
                .invoke(LOGGER::error);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(MessageCreateDTO message) {

        return Uni.createFrom().item(message).flatMap(messageMapper::toEntity)
                .flatMap(message1 -> Panache.withTransaction(message1::<Message>persistAndFlush))
                .log()
                .map(message1 -> Response.created(URI.create(uriInfo.getPath() + "/" + message1.id)).build())
                .onFailure()
                .invoke(LOGGER::error);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(@PathParam("id") Long id, MessageCreateDTO messageCreateDTO) {

        return Message.<Message>findById(id)
                .onItem()
                .ifNull()
                .failWith(() -> new UnsupportedOperationException("Can't find Message with id " + id))
                .invoke(message -> System.out.println("Find message in PUT : " + message))
                .flatMap(message -> messageMapper.updateWithDTO(message, messageCreateDTO))
                .flatMap(message -> Panache.withTransaction(message::<Message>persistAndFlush)
                )
                .replaceWith(Response.noContent().build())
                .onFailure()
                .invoke(LOGGER::error);
    }

    @GET
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Panache.withTransaction(() -> Message.<Message>findById(id)
                        .log("delete")
                        .onItem().ifNotNull().transformToUni(message ->
                                message.delete()
                                        .log("delete.notNull")
                                        .onItem().transform(item -> Response.noContent().build()))
                        .onItem()
                        .ifNull()
                        .continueWith(Response.status(Response.Status.NOT_FOUND).build()))
                .onFailure()
                .invoke(LOGGER::error);
    }
}
