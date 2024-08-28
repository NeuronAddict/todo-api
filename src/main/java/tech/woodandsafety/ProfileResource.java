package tech.woodandsafety;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import tech.woodandsafety.data.CustomUser;
import tech.woodandsafety.data.ProfileDisplayMapper;

@Path("profile")
public class ProfileResource {

    private static final Logger LOGGER = Logger.getLogger(ProfileResource.class);

    private final ProfileDisplayMapper profileDisplayMapper;

    public ProfileResource(ProfileDisplayMapper profileDisplayMapper) {
        this.profileDisplayMapper = profileDisplayMapper;
    }

    @Inject
    SecurityIdentity identity;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get() {

        return Uni.createFrom().item(identity.getPrincipal().getName())
                .invoke(name -> LOGGER.debug("Get profile for user : " + name))
                .flatMap(CustomUser::findByName)
                .map(profileDisplayMapper::toDisplayDTO)
                .map(profileDisplayDTO -> Response.ok(profileDisplayDTO).build());
    }

}
