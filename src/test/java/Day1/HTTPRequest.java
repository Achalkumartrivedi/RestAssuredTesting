package Day1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.Matchers.*;

public class HTTPRequest {

    int id; //make global var

    //GET method
    @Test
    void getuser(){
      given()
              .when()
                .get("https://reqres.in/api/users?page=2")

              .then()
                .statusCode(200)
                .body("page",equalTo(2))
                .log().all();
    }

    //Post method - simple
//    @Test
//    void createuser(){
//
//       HashMap data = new HashMap();
//       data.put("name", "Achal");
//       data.put("job", "QA");
//
//        given()
//                .contentType("application/json")
//                .body(data)
//
//        .when()
//                .post("https://reqres.in/api/users")
//
//         .then()
//                .statusCode(201)
//                .log().all();
//    }

    //Post method - return id from response
    @Test(priority = 1)
    void createuserPOST201(){

        HashMap<String, String> data = new HashMap<>();
        data.put("name", "Achal");
        data.put("job", "QA");

        //pass id to given mathod
    id = given()
                .contentType("application/json")
                .body(data)

                .when()
                .post("https://reqres.in/api/users")
                .jsonPath().getInt("id"); //return value of id from response

                // then() - can't create and showing error, it expect response object as return but return id
                //Use extract().respose() method in then() instead of use jsonPath() in when()
                // see UpdateAfterCreate class for this solution

    }
//PUT - update the name in api
    @Test(priority = 2,dependsOnMethods = {"createuserPOST201"})
    void updateuserPUT200(){
        HashMap<String, String> data = new HashMap<>();
        data.put("name", "Achal kumar"); //change to Achal kumar
        data.put("job", "Senior QA");


        //pass id to given mathod
        given()
                .contentType("application/json")
                .body(data)

                .when()
                .put("https://reqres.in/api/users/"+id)

                .then()
                .statusCode(200)
                .log().all()
                .body("name",equalTo("Achal kumar")); //validate the updation

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


// .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application.json", ContentType.TEXT)))
