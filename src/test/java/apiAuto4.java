import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.json.JSONObject;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class apiAuto4 {
    @Test
    public void getAllUsersTest() {
        //Test GET API /public/v1/users with total data 10
        RestAssured.given().when().get("https://reqres.in/api/users?page=2")
                .then()
                .log().all() //is used to print the entire request to console (optional)
                .assertThat().statusCode(200) //assert the status code to be 200 (Ok)
                .assertThat().body("per_page", Matchers.equalTo(6)) //assert that the "per-page" in the body matches to 6
                .assertThat().body("total", Matchers.equalTo(12)); //assert that the "total" in the body matches to 12
    }
    @Test
    public void getSingleUserTest() {
        //Test GET API /public/v1/users with total data 10
        RestAssured.given().when().get("https://reqres.in/api/users/7")
                .then()
                .log().all() //is used to print the entire request to console (optional)
                .assertThat().statusCode(200) //assert the status code to be 200 (Ok)
                .assertThat().body("data.first_name", Matchers.equalTo("Michael")) //assert that the "first_name" in the body matches to Michael
                .assertThat().body("data.last_name", Matchers.equalTo("Lawson")); //assert that the "last_name" in the body matches to Lawson
    }

    @Test
    public void getSingleUserNotFoundTest() {
        //Test GET API /public/v1/users with total data 10
        RestAssured.given().when().get("https://reqres.in/api/users/23")
                .then()
                .log().all() //is used to print the entire request to console (optional)
                .assertThat().statusCode(404); //assert the status code to be 200 (Ok)
    }

    @Test
    public void createNewUserTest() {
        //create POST body with parameter below in JSON format
        String name = "Andi";
        String job = "Student";
        //HashMap Alternative
        JSONObject bodyObject = new JSONObject();
        bodyObject.put ("name", name);
        bodyObject.put ("job", job);
        //Test POST with header that accept json format
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObject.toString())//convert json to string format -> (name, email, gender, status)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all() //is used to print hte entire request to console (optional)
                .assertThat().statusCode(201) //assert the status code to be 201 (created)
                .assertThat().body("name", Matchers.equalTo(name)) //assert response body "name"
                .assertThat().body("job", Matchers.equalTo(job)) //assert response body "job"
                .assertThat().body("$", Matchers.hasKey("id")) //assert response has key "id"
                .assertThat().body("$", Matchers.hasKey("createdAt")); //assert response body has key"createdAt"
    }

    @Test
    public void updateUserTest() {
        //define base URL
        RestAssured.baseURI = "https://reqres.in/";
        //data to update
        int userId = 2;
        String newfName = "Fulan";
        String newlName = "Fulan1";
        String newEmail = "fulan.fulan1@gmail.com";
        //test put user id 2 -> update first name
        //first, get the attribute of user id 2
        String fname = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.last_name");
        String avatar = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.avatar");
        String email = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.email");
        System.out.println("fname before = "+fname);
        System.out.println("lname before = "+lname);
        System.out.println("email before = "+email);

        //change first name to "Fulan"
        //create body request with Hashmap and convet it to Json
        HashMap<String, Object>bodyMap = new HashMap<>();
        bodyMap.put("id", userId);
        bodyMap.put("email", newEmail);
        bodyMap.put("first_name", newfName);
        bodyMap.put("last_name", newlName);
        bodyMap.put("avatar", avatar);
        JSONObject bodyObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json") //set the header to accept json
                .body(bodyObject.toString()) //convert jsonObject to string format
                .put("api/users/"+userId)
                .then().log().all() //log.all() here is used to print the entire response to console (optional)
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newfName)) //assert response body "newfName"
                .assertThat().body("last_name", Matchers.equalTo(newlName)) //assert response body "newlName"
                .assertThat().body("email", Matchers.equalTo(newEmail)); //assert response body "newEmail"
    }

    @Test
    public void patchUserTest () {
        //define base URL
        RestAssured.baseURI = "https://reqres.in/";
        //data to update
        int userId = 3;
        String newEmail = "emma.wong@gmail.com";
        //test patch user id = 3 -> update email
        //first, get the email of user id 3
        String email = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.email");
        System.out.println("email before = "+email);

        //change email to newEmail
        //create body request with hashmap and convert it to json
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("email", newEmail);
        JSONObject bodyObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json") //set the header to accept json
                .body(bodyObject.toString()) //convert jsonObject to string format
                .patch("api/users/"+userId)
                .then().log().all() //log.all() here is used to print the entire response to console (optional)
                .assertThat().statusCode(200)
                .assertThat().body("email", Matchers.equalTo(newEmail)); //assert the updated email
    }

    @Test
    public void deleteUserTest() {
        //define base URL
        RestAssured.baseURI = "https://reqres.in/";
        //data to delete
        int userToDelete = 4;
        //test delete api/users/4
        given().log().all()
                .when().delete("api/users/" + userToDelete)
                .then().log().all()
                .assertThat().statusCode(204);
    }

}
