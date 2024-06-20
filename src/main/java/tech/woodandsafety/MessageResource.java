package tech.woodandsafety;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import tech.woodandsafety.data.Message;
import tech.woodandsafety.dto.MessageDTO;
import tech.woodandsafety.mapper.MessageMapper;

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
    public Uni<MessageDTO> post(MessageDTO message) {

        return Uni.createFrom().item(message).map(messageMapper::toEntity)
                .flatMap(message1 -> message1.<Message>persist()).map(messageMapper::toDTO);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MessageDTO> put(@PathParam("id") Long id, MessageDTO message) {

        return Uni.createFrom().item(message).map(messageMapper::toEntity).map(message1 -> {
            message1.id = id;
            return message1;
        }).flatMap(message1 -> message1.<Message>persist()).map(messageMapper::toDTO);
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
