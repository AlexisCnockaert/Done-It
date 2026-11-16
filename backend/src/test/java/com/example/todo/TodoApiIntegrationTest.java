package com.example.todo;

import com.example.todo.dto.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.data.mongodb.uri=mongodb://127.0.0.1:27017/doneit_test",
                "jwt.secret=test-secret-key-for-doneit-with-at-least-32-bytes",
                "jwt.expiration=86400000",
                "ia.api.key="
        }
)
class TodoApiIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;
    private String createdTodoId;

    @BeforeEach
    void registerUser() {
        RestAssured.port = port;
        String unique = UUID.randomUUID().toString().replace("-", "");

        AuthResponse user = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "username": "qa%s",
                          "email": "qa-%s@example.com",
                          "password": "Password123!"
                        }
                        """.formatted(unique, unique))
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201)
                .extract()
                .as(AuthResponse.class);

        token = user.token();
    }

    @AfterEach
    void deleteCreatedTodo() {
        if (createdTodoId != null) {
            given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete("/api/todos/{id}", createdTodoId)
                    .then()
                    .statusCode(204);
        }
    }

    @Test
    void createsTodoWithExpectedFields() {
        String title = uniqueTitle();

        Response response = authenticated()
                .body("{\"title\":\"" + title + "\"}")
                .when()
                .post("/api/todos");

        response.then()
                .statusCode(201)
                .body("title", equalTo(title))
                .body("done", equalTo(false))
                .body("id", not(blankOrNullString()));
        createdTodoId = response.jsonPath().getString("id");
    }

    @Test
    void listsCreatedTodo() {
        String title = createTodo();

        authenticated()
                .when()
                .get("/api/todos")
                .then()
                .statusCode(200)
                .body("title", hasItem(title));
    }

    @Test
    void togglesTodoToDone() {
        createTodo();

        authenticated()
                .when()
                .put("/api/todos/{id}/toggle", createdTodoId)
                .then()
                .statusCode(200)
                .body("done", equalTo(true));
    }

    @Test
    void deletesTodoAndReturnsNotFoundOnRead() {
        createTodo();
        String todoId = createdTodoId;

        authenticated()
                .when()
                .delete("/api/todos/{id}", todoId)
                .then()
                .statusCode(204);

        createdTodoId = null;
        authenticated()
                .when()
                .get("/api/todos/{id}", todoId)
                .then()
                .statusCode(404);
    }

    @Test
    void rejectsBlankTitle() {
        authenticated()
                .body("{\"title\":\"   \"}")
                .when()
                .post("/api/todos")
                .then()
                .statusCode(400)
                .body("errors.title", not(blankOrNullString()));
    }

    private String createTodo() {
        String title = uniqueTitle();
        createdTodoId = authenticated()
                .body("{\"title\":\"" + title + "\"}")
                .when()
                .post("/api/todos")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        return title;
    }

    private io.restassured.specification.RequestSpecification authenticated() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token);
    }

    private String uniqueTitle() {
        return "QA task " + UUID.randomUUID();
    }
}
