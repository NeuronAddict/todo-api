package tech.woodandsafety;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;
import tech.woodandsafety.dto.MessageDTO;
import io.quarkus.test.keycloak.client.KeycloakTestClient;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;


@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
@TestTransaction
class MessageResourceTest {

    @Context
    UriInfo uriInfo;

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    List<MessageDTO> messageDTOs = List.of(
            new MessageDTO("hello", "alice", LocalDate.of(2024,12,12))
    );

    MessageDTO newCreated = new MessageDTO("hello2", "alice3", LocalDate.of(2024,12,12));



    @Test
    public void testModifyMessage() {

        assertThat(given().auth().oauth2(token("alice"))
                .when().get("/messages")
                .then()
                .log()
                .everything(true)
                .statusCode(200)
                .extract().as(new TypeRef<List<MessageDTO>>() {
                        })).isEqualTo(messageDTOs);

        String newEntityLocation = given().auth().oauth2(token("alice"))
                .contentType(ContentType.JSON)
                .body(messageDTOs.get(0))
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
                .extract().as(MessageDTO.class)).isEqualTo(messageDTOs.get(0));


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
                .extract().as(MessageDTO.class)).isEqualTo(newCreated);


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

    protected String token(@SuppressWarnings("SameParameterValue") String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}