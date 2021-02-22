package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private DataGenerator() {}

    public static class Registration {

        private Registration() {}

        public static AuthInfo generateActiveUser() {
            Faker faker = new Faker(new Locale("en"));
            String login = faker.name().firstName();
            String password = faker.internet().password();
            String status = "active";
            return new AuthInfo(login, password, status);
        }

        public static AuthInfo generateBlockedUser() {
            Faker faker = new Faker(new Locale("en"));
            String login = faker.name().firstName();
            String password = faker.internet().password();
            String status = "blocked";
            return new AuthInfo(login, password, status);
        }

        public static AuthInfo generateUserWithInvalidLogin () {
            Faker faker = new Faker(new Locale("en"));
            String login = faker.name().firstName();;
            String password = faker.internet().password();
            String status = "active";
            return new AuthInfo("petya", password, status);
        }

        public static AuthInfo generateUserWithInvalidPassword () {
            Faker faker = new Faker(new Locale("en"));
            String login = faker.name().firstName();
            String password = faker.internet().password();;
            String status = "active";
            return new AuthInfo(login, "password" , status);
        }
    }

    public static class Send {
        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        public static void setUpAll(AuthInfo user) {
            given()
                    .spec(Send.requestSpec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }
    }
}


