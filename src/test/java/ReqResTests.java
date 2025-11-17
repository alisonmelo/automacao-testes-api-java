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

    @Test
    public void testRegisterNewUser_MissingPassword() {
        // Corpo da requisição: Enviamos o email, mas OMITIMOS a senha
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\" }";

        given()
                // Headers necessários que já descobrimos
                .header("Content-Type", "application/json")
                .header("x-api-key", "reqres-free-v1")
                .body(requestBody)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                // A nossa ferramenta de depuração, útil se o teste falhar
                .log().body()
                // A EXPECTATIVA AGORA É UM ERRO 400
                .statusCode(400)
                // Verificamos se a API nos deu a mensagem de erro correta
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testUpdateUser() {
        String requestBody = "{ \"name\": \"Alison Matheus\", \"job\": \"QA Pleno Senior\" }";

        given()
                .header("Content-Type", "application/json")
                .header("x-api-key", "reqres-free-v1")
                .body(requestBody)
                .when()
                // A ação agora é um PUT para um recurso específico (user 2)
                .put("https://reqres.in/api/users/2")
                .then()
                .log().body()
                // A expectativa padrão para um UPDATE bem-sucedido é 200 OK
                .statusCode(200)
                // Verificamos se a resposta da API reflete a atualização que fizemos
                .body("job", equalTo("QA Pleno Senior"));
    }
}