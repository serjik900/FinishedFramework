package APIPackage;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

// To change the order of execution, we use FixMethodOrder since it is executing in top to bottom approach, which is not good for us.
// This method sorters will execute my methods in ascending/alphabetically order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExamples {

    // One thing to remember
    // Base URI - base URL
    // And then using the WHEN keyword, we will send the endpoint

    String baseURI =  RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";

    // We need to perform CRUD operations
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzQwODc5MTMsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY3NDEzMTExMywidXNlcklkIjoiNDcyNSJ9.usxFfksgB6QFw5pwTD8p14ydO3kEG0XxTZ3tzF0816g";
    static String employee_id;

    @Test
    public void bgetOneEmployee(){
        // Prepare the request
        // To prepare the request, we use RequestSpecification
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .queryParam("employee_id", employee_id);

        // To hit the endpoint / to make the request which will return response
        Response response = request.when().get("/getOneEmployee.php");

        // System.out.println(response.asString());
        response.prettyPrint();
        // Verifying the status code
        response.then().assertThat().statusCode(200);

        // Using the jsonPath method, we are extracting the value from the response body
        String firstName = response.jsonPath().getString("employee.emp_firstname");
        System.out.println(firstName);
        // First way of assertion
        Assert.assertEquals(firstName, "sara");
        // Second way of assertion to verify the value in response body using hamcrest matchers
        response.then().assertThat().body("employee.emp_firstname", equalTo("sara"));
    }

    @Test
    public void acreateEmployee(){
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json").
                body("{\n" +
                        "  \"emp_firstname\": \"sara\",\n" +
                        "  \"emp_lastname\": \"bou\",\n" +
                        "  \"emp_middle_name\": \"ms\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"2011-01-12\",\n" +
                        "  \"emp_status\": \"confirmed\",\n" +
                        "  \"emp_job_title\": \"QA Engineer\"\n" +
                        "}");
        Response response = request.when().post("/createEmployee.php");
        response.prettyPrint();
        // Verifying the status code which is 201
        response.then().assertThat().statusCode(201);
        // Getting the employee ID from the response and using it as static one
        employee_id = response.jsonPath().getString("Employee.employee_id");
        System.out.println(employee_id);
        response.then().assertThat().body("Employee.emp_lastname", equalTo("bou"));
        response.then().assertThat().body("Employee.emp_middle_name", equalTo("ms"));
        // Verify console header
        response.then().assertThat().header("Server", "Apache/2.4.39 (Win64) PHP/7.2.18");

    }

    @Test
    public void cupdateEmployee(){
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json").
                body("{\n" +
                        "  \"employee_id\": \""+ employee_id +"\",\n" +
                        "  \"emp_firstname\": \"sohel\",\n" +
                        "  \"emp_lastname\": \"abbasi\",\n" +
                        "  \"emp_middle_name\": \"ms\",\n" +
                        "  \"emp_gender\": \"M\",\n" +
                        "  \"emp_birthday\": \"2015-01-12\",\n" +
                        "  \"emp_status\": \"conf\",\n" +
                        "  \"emp_job_title\": \"qa\"\n" +
                        "}");

        Response response = request.when().put("/updateEmployee.php");
        response.prettyPrint();
        // Verification
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("Message", equalTo("Employee record Updated"));
    }

    @Test
    public void dgetUpdatedEmployee(){
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .queryParam("employee_id", employee_id);

        // To hit the endpoint / to make the request which will return response
        Response response = request.when().get("/getOneEmployee.php");

        // System.out.println(response.asString());
        response.prettyPrint();
        // Verifying the status code
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("employee.emp_job_title", equalTo("QA"));
    }
}
