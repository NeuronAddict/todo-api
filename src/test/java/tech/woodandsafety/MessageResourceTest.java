package tech.woodandsafety;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;
import tech.woodandsafety.dto.MessageCreateDTO;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
@TestTransaction
class MessageResourceTest {

    @Context
    UriInfo uriInfo;

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    List<MessageCreateDTO> messageCreateDTOS = List.of(
            new MessageCreateDTO("hello", "alice", LocalDate.of(2024, 12, 12))
    );

    MessageCreateDTO newCreated = new MessageCreateDTO("hello2", "alice", LocalDate.of(2024, 12, 12));


    @Test
    public void testModifyMessage() {

        assertThat(given().auth().oauth2(token("alice"))
                .when().get("/messages")
                .then()
                .log()
                .everything(true)
                .statusCode(200)
                .extract().as(new TypeRef<List<MessageCreateDTO>>() {
                })).isEqualTo(messageCreateDTOS);

        String newEntityLocation = given().auth().oauth2(token("alice"))
                .contentType(ContentType.JSON)
                .body(messageCreateDTOS.get(0))
                .when().post("/messages")
                .then()
                .log()
                .everything(true)
                .statusCode(RestResponse.StatusCode.CREATED)
                .extract()
                .header("Location");

        assertThat(given().auth().oauth2(token("alice"))
                .when().get(newEntityLocation)
                .then()
                .log()
                .everything(true)
                .statusCode(RestResponse.StatusCode.OK)
                .extract().as(MessageCreateDTO.class)).isEqualTo(messageCreateDTOS.get(0));


        given().auth().oauth2(token("alice"))
                .contentType(ContentType.JSON)
                .body(newCreated)
                .when().put(newEntityLocation)
                .then()
                .log()
                .everything(true)
                .statusCode(204);

        assertThat(given().auth().oauth2(token("alice"))
                .when().get(newEntityLocation)
                .then()
                .log()
                .everything(true)
                .statusCode(RestResponse.StatusCode.OK)
                .extract().as(MessageCreateDTO.class)).isEqualTo(newCreated);

        given().auth().oauth2(token("alice"))
                .when().delete(newEntityLocation)
                .then()
                .log()
                .everything(true)
                .statusCode(RestResponse.StatusCode.NO_CONTENT);

        assertThat(given().auth().oauth2(token("alice"))
                .when().get("/messages")
                .then()
                .log()
                .everything(true)
                .statusCode(200)
                .extract().as(new TypeRef<List<MessageCreateDTO>>() {
                })).isEqualTo(messageCreateDTOS);
    }

    @Test
    public void testGetMissing() {

        given().auth().oauth2(token("alice"))
                .when().get("/messages/99999")
                .then()
                .log()
                .everything(true)
                .statusCode(404);
    }

    @Test
    public void testDeleteMissing() {

        given().auth().oauth2(token("alice"))
                .when().delete("/messages/99999")
                .then()
                .log()
                .everything(true)
                .statusCode(404);
    }

    protected String token(@SuppressWarnings("SameParameterValue") String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}