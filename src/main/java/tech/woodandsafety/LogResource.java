package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import tech.woodandsafety.data.LogEntry;
import tech.woodandsafety.data.LogEntryCreateMapper;
import tech.woodandsafety.dto.LogEntryCreateDTO;

import java.net.URI;

@Path("/logs")
public class LogResource {

    private static final Logger LOGGER = Logger.getLogger(MessageResource.class);

    @Context
    UriInfo uriInfo;

    @Inject
    LogEntryCreateMapper logEntryCreateMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAll() {
        return LogEntry.<LogEntry>listAll().map(logEntries ->
                        logEntries.stream().map(logEntry -> logEntryCreateMapper.toDisplayDTO(logEntry)).toList())
                .map(logEntryDisplayDTOS -> Response.ok(logEntryDisplayDTOS).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(@PathParam("id") Long id) {
        return LogEntry.<LogEntry>findById(id)
                .onItem()
                .ifNotNull()
                .transform(logEntry -> Response.ok(logEntryCreateMapper.toDisplayDTO(logEntry)).build())
                .onItem()
                .ifNull()
                .continueWith(() -> Response.status(Response.Status.NOT_FOUND).build())
                .onFailure()
                .invoke(LOGGER::error);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(LogEntryCreateDTO logEntryCreateDTO) {
        return logEntryCreateMapper.toEntity(logEntryCreateDTO)
                .flatMap(logEntry -> Panache.withTransaction(logEntry::<LogEntry>persistAndFlush))
                .map(logEntry -> Response.created(URI.create(uriInfo.getPath() + "/" + logEntry.id)).build());
    }
}
