package Day1;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateAfterCreate {

    Response response; //create Response variable of Response class
    int id;

    //Post method - create data with return response objecta and validation in then()
    @Test(priority = 1)
    void createReturnResponse201(){

        HashMap<String, String> data = new HashMap<>();
        data.put("name", "mitesh");
        data.put("job", "QA");

        // Send POST request and store the response
        response = given()   //2)store response in response object
                .contentType("application/json")
                .body(data)

                .when()
                .post("https://reqres.in/api/users")

                .then()
                .statusCode(201)
                .log().all()
                .extract().response();  //1) extract all response in then()
    }

    //PUT - change name in put request and validation in then()
    @Test(priority = 2,dependsOnMethods = {"createReturnResponse201"})
    void updateuserByExtractID200(){
        HashMap<String, String> data = new HashMap<>();
        data.put("name", "vishal");  //change data for put request
        data.put("job", "Senior QA");

        //3)Extract id from response object and pass into PUT
        id = response.jsonPath().getInt("id");
        given()
                .contentType("application/json")
                .body(data)

                .when()
                .put("https://reqres.in/api/users/"+id) // id pass from response object

                .then()
                .statusCode(200)
                .log().all()
                .body("name",equalTo("vishal")); // check changed data

    }

    @Test(priority = 3)
    void Deleteuser204(){

        given()

                .when()
                .delete("https://reqres.in/api/users/"+id)

                .then()
                .statusCode(204)
                .log().all()
        ;
    }
}
