package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.Mock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.woodandsafety.data.CustomUser;
import tech.woodandsafety.data.Message;
import tech.woodandsafety.dto.MessageDTO;
import tech.woodandsafety.mapper.CreateMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
@TestTransaction
class MessageResourceTest {

    CustomUser user = new CustomUser("alice", "secret", "user");

    List<Message> messages = List.of(
            new Message("hello", user, LocalDate.of(2024,12,12))
    );

    List<MessageDTO> messageDTOs = List.of(
            new MessageDTO("hello", "alice", LocalDate.of(2024,12,12))
    );

    @Test
    public void testGetMessages() {
        PanacheMock.mock(Message.class);
        PanacheMock.mock(CustomUser.class);

        Mockito.when(Message.<Message>listAll()).thenReturn(Uni.createFrom().item(messages));
        MessageDTO[] result =  given().auth().oauth2(token("alice", "user"))
                .when().get("/messages")
                .then()
                .log()
                .everything(true)
                .statusCode(200)
                .extract()
                .as(MessageDTO[].class);
        System.out.println(Arrays.asList(result));
        assertEquals(Arrays.asList(result), messageDTOs);
    }

    private String token(String username, String role) {
        return Jwt.preferredUserName(username)
                .groups(role)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }

}