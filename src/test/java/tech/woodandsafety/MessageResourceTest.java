package tech.woodandsafety;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.woodandsafety.dto.MessageDTO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MessageResourceTest {

    List<MessageDTO> messages;

    @BeforeEach
    void setUp() {
        messages = List.of(
          new MessageDTO("hello", )
        );
    }

    @Test
    public void testGetMessages() {
        given()
                .when().get("/messages")
                .then()
                .statusCode(200)
                .body(is(messages));
    }

}