package org.example;

import io.restassured.http.ContentType;
import junit.framework.Assert;
import org.example.api.UserColor;
import org.example.api.UserData;
import org.example.api.UserError;
import org.example.api.UserRequest;
import org.example.api.UserResp;
import org.example.spec.Specifications;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class ExampleTest {

    private static final String URL = "https://reqres.in/";

    @Test
    public void testExample() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecStatus(200));
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then()
                .log().body()
                .extract().body().jsonPath().getList("data", UserData.class);
        int a = 2;

        users.forEach(x -> {
            Assertions.assertTrue(x.getAvatar().contains(Integer.toString(x.getId())));
            Assertions.assertTrue(x.getEmail().endsWith("reqres.in"));
        });

        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void testCreateUser() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecStatus(201));
        UserRequest user = new UserRequest("mmma@regres.in", "dsfsdfsdfewf");

        UserResp userResp = given()
                .contentType(ContentType.JSON)
                .when()
                .body(user)
                .post("api/users")
                .then()
                .log().body()
                .body("id", notNullValue())
                .body("createdAt", containsString(String.valueOf(LocalDate.now())))
                .extract().response().as(UserResp.class);

        Assertions.assertTrue(userResp.getCreatedAt().contains(String.valueOf(LocalDate.now())));
        Assertions.assertFalse(userResp.getId().isEmpty());
    }

    @Test
    public void testNegativeUser() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecStatus(400));
        UserRequest user = new UserRequest("maz@regres.in", "");

        UserError message = given()
                .when()
                .body(user)
                .post("api/register")
                .then()
                .log().all()
//                .body("id", notNullValue())
//                .body("createdAt", containsString(String.valueOf(LocalDate.now())))
                .extract().response().as(UserError.class);

        Assertions.assertEquals("Missing password", message.getError());
    }

    @Test
    public void checkSortedIds() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecStatus(200));

        List<UserColor> userColors = given()
                .when()
                .get("api/unknown")
                .then()
                .log().body()
                .extract().body().jsonPath().getList("data", UserColor.class);

        List<Integer> years = userColors.stream().map(UserColor::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(sortedYears,years);
    }

    @Test
    public void testDeleteUser() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecStatus(204));
        given()
                .when()
                .delete("api/users/650")
                .then()
                .log().all();
    }

}
