import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqResTests {

    @BeforeAll
    //configurar e gera arquivo de log
    public static void setup() throws FileNotFoundException {
        new java.io.File("logs").mkdirs();
        PrintStream logStream = new PrintStream(new FileOutputStream("logs/test-execution.log"));

        // Configura o RestAssured para usar filtro de log
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(logStream));
        RestAssured.filters(new RequestLoggingFilter(logStream)); // Loga a requisição
    }

    // ... testes
    @Test
    public void testGetListOfUsers() {
        given().header("x-api-key", "reqres-free-v1")
                .when().get("https://reqres.in/api/users?page=2")
                .then().log().ifValidationFails().statusCode(200).body("page", equalTo(2));
    }

    @Test
    public void testGetSingleUserAuthenticated() {
        given().header("x-api-key", "reqres-free-v1")
                .when().get("https://reqres.in/api/users/2")
                .then().log().ifValidationFails().statusCode(200).body("data.id", equalTo(2));
    }

    @Test
    public void testGetSingleResourceFound() {
        given().header("x-api-key", "reqres-free-v1").pathParam("id", 2)
                .when().get("https://reqres.in/api/unknown/{id}")
                .then().log().ifValidationFails().statusCode(200).body("data.id", equalTo(2));
    }

    @Test
    public void testGetSingleResourceNotFound() {
        given().header("x-api-key", "reqres-free-v1").pathParam("id", 23)
                .when().get("https://reqres.in/api/unknown/{id}")
                .then().log().ifValidationFails().statusCode(404);
    }

    @Test
    public void testRegisterNewUserSuccessfully() {
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given().header("Content-Type", "application/json").header("x-api-key", "reqres-free-v1").body(requestBody)
                .when().post("https://reqres.in/api/register")
                .then().log().ifValidationFails().statusCode(200).body("token", notNullValue());
    }

    @Test
    public void testDeleteUser() {
        given().header("x-api-key", "reqres-free-v1")
                .when().delete("https://reqres.in/api/users/2")
                .then().log().ifValidationFails().statusCode(204);
    }
}