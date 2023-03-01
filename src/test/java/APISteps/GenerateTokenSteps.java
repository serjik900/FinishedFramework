package APISteps;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class GenerateTokenSteps {

    String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";

    // To use my token in the project, I am using a static variable

    public static String token;

    @Given("a JWT is generated")
    public void a_jwt_is_generated() {

        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"name\": \"batch14\",\n" +
                        "  \"email\": \"batch14HTHJ@test.com\",\n" +
                        "  \"password\": \"TestABC@123\"\n" +
                        "}");

        Response response = request.when().post("/generateToken.php");

        // Printing the response body
        // response.prettyPrint();

        token = "Bearer " + response.jsonPath().getString("token");
        System.out.println(token);
    }
}
