package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("messages")
public class MessageResource {

    private static final Logger LOGGER = Logger.getLogger(MessageResource.class);

    private final MessageMapper messageMapper;

    public MessageResource(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Message>> get() {

        return Message.listAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Message> post(MessageDTO message) {

        return Uni.createFrom().item(message).map(messageMapper::toEntity)
                .flatMap(message1 -> message1.persist());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Message> put(@PathParam("id") Long id, MessageDTO message) {

        return Uni.createFrom().item(message).map(messageMapper::toEntity).map(message1 -> {
            message1.id = id;
            return message1;
        }).flatMap(message1 -> message1.persist());
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Message.<Message>findById(id)
                .onItem().transform(item -> Response.noContent().build())
                .onFailure().invoke(LOGGER::error)
                .onFailure().recoverWithItem(Response.status(404).build());
    }
}
