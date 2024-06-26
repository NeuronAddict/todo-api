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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.woodandsafety.data.LogEntryDisplayDTO;
import tech.woodandsafety.data.LogEntryType;
import tech.woodandsafety.dto.LogEntryCreateDTO;
import tech.woodandsafety.dto.MessageDisplayDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
@TestTransaction
class LogResourceTest {

    @Context
    UriInfo uriInfo;

    KeycloakTestClient keycloakClient = new KeycloakTestClient();
    private List<LogEntryDisplayDTO> initialLogEntriesDisplayDTO;
    private LogEntryCreateDTO logEntryToAdd;
    private LogEntryDisplayDTO addedLogEntry;

    @BeforeEach
    void setUp() {

        MessageDisplayDTO existingMessageDisplayDTO = new MessageDisplayDTO(0L, "hello", "alice", LocalDate.of(2024, 12, 12));

        initialLogEntriesDisplayDTO = List.of(
                new LogEntryDisplayDTO(0L, LogEntryType.add, existingMessageDisplayDTO, "alice")
        );

        logEntryToAdd = new LogEntryCreateDTO(LogEntryType.delete, existingMessageDisplayDTO, "alice");

        addedLogEntry = new LogEntryDisplayDTO(1L, LogEntryType.delete, existingMessageDisplayDTO, "alice");

    }

    @Test
    public void testModifyMessage() {

        assertThat(given().auth().oauth2(token("alice"))
                .when().get("/logs")
                .then()
                .log()
                .everything(true)
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<LogEntryDisplayDTO>>() {
                }))
                .isEqualTo(initialLogEntriesDisplayDTO);

        String newEntityLocation = given().auth().oauth2(token("alice"))
                .contentType(ContentType.JSON)
                .body(logEntryToAdd)
                .when().post("/logs")
                .then()
                .log()
                .everything(true)
                .statusCode(RestResponse.StatusCode.CREATED)
                .extract()
                .header("Location");

        assertThat(given().log().everything(true).auth().oauth2(token("alice"))
                .when().get(newEntityLocation)
                .then()
                .log()
                .everything(true)
                .statusCode(RestResponse.StatusCode.OK)
                .extract().as(LogEntryDisplayDTO.class))
                .isEqualTo(addedLogEntry);

        List<LogEntryDisplayDTO> finalResults = new ArrayList<>(initialLogEntriesDisplayDTO);
        finalResults.add(addedLogEntry);

        assertThat(given().auth().oauth2(token("alice"))
                .when().get("/logs")
                .then()
                .log()
                .everything(true)
                .statusCode(200)
                .extract().as(new TypeRef<List<LogEntryDisplayDTO>>() {
                })).isEqualTo(finalResults);
    }

    @Test
    public void testGetMissing() {

        given().auth().oauth2(token("alice"))
                .when().get("/logs/99999")
                .then()
                .log()
                .everything(true)
                .statusCode(404);
    }

    protected String token(@SuppressWarnings("SameParameterValue") String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}