package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.AuthInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.*;


class AuthInfoTest {

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldLoginActiveUser() {
        AuthInfo data = DataGenerator.Registration.generateActiveUser();
        DataGenerator.Send.setUpAll(data);
        $("[name = login]").setValue(data.getLogin());
        $("[name = password]").setValue(data.getPassword());
        $("[data-test-id = action-login]").click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldBeInvalidUser() {
        AuthInfo data = DataGenerator.Registration.generateBlockedUser();
        DataGenerator.Send.setUpAll(data);
        $("[name = login]").setValue(data.getLogin());
        $("[name = password]").setValue(data.getPassword());
        $("[data-test-id = action-login]").click();
        $("[data-test-id = error-notification]")
                .shouldBe(visible, ofMillis(15000))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        AuthInfo data = DataGenerator.Registration.generateUserWithInvalidPassword();
        DataGenerator.Send.setUpAll(data);
        $("[name = login]").setValue(data.getLogin());
        $("[name = password]").setValue(String.valueOf("ads"));
        $("[data-test-id = action-login]").click();
        $("[data-test-id = error-notification]")
                .shouldBe(visible, ofMillis(15000))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        AuthInfo data = DataGenerator.Registration.generateUserWithInvalidLogin();
        DataGenerator.Send.setUpAll(data);
        $("[name = login]").setValue(String.valueOf("paett"));
        $("[name = password]").setValue(data.getPassword());
        $("[data-test-id = action-login]").click();
        $("[data-test-id = error-notification]").shouldBe(visible);
    }
}