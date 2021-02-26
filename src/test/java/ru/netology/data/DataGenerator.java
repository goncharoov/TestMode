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

        public static Faker faker = new Faker(new Locale("en"));
        private Registration() {}

        public static AuthInfo generateUser(String status) {
            AuthInfo user = new AuthInfo(faker.name().firstName(), faker.internet().password(), status);
            Send.makeRequest(user);
            return user;
        }

        public static AuthInfo generateUserWithInvalidLogin () {
            String login = faker.name().firstName();
            Send.makeRequest(new AuthInfo(login, faker.internet().password(), "active"));
            return new AuthInfo(login, faker.internet().password(), "active");
        }

        public static AuthInfo generateUserWithInvalidPassword() {
            String password = faker.internet().password();
            Send.makeRequest(new AuthInfo(faker.name().firstName(), password, "active"));
            return new AuthInfo(faker.name().firstName(), password, "active");
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

        public static void makeRequest(AuthInfo user) {
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


