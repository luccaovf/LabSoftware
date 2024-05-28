package com.labdesoft.roteiro1.integration;

import com.labdesoft.roteiro1.Roteiro1Application;
import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.enums.TaskPriority;
import com.labdesoft.roteiro1.enums.TaskType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Roteiro1Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerIntegrationTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    public void givenNewTask_whenCreatesTask_thenCorrect() {
        Task newTask = new Task();
        newTask.setDescription("Nova tarefa");
        newTask.setType(TaskType.LIVRE);
        newTask.setPriority(TaskPriority.MEDIA);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newTask)
                .when()
                .post("/api/task")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .assertThat()
                .body("description", equalTo("Nova tarefa"));
    }
    @Test
    @Order(2)
    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasRequiredKV_thenCorrect() {
        get("/api/task").then().statusCode(200);
    }

    @Test
    @Order(3)
    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasOneTask_thenCorrect() {
        get("/api/task/1").then().statusCode(200)
                .assertThat().body("description", equalTo("Nova tarefa"));
    }

    @Test
    @Order(4)
    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasCompletedTask_thenCorrect() {
        put("/api/task/1").then().statusCode(200)
                .assertThat().body("completed", equalTo(true));
    }
}
