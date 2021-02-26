package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.AuthInfo;

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
        AuthInfo activeUser = DataGenerator.Registration.generateUser("active");
        $("[name = login]").setValue(activeUser.getLogin());
        $("[name = password]").setValue(activeUser.getPassword());
        $("[data-test-id = action-login]").click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        AuthInfo blockedUser = DataGenerator.Registration.generateUser("blocked");
        $("[name = login]").setValue(blockedUser.getLogin());
        $("[name = password]").setValue(blockedUser.getPassword());
        $("[data-test-id = action-login]").click();
        $("[data-test-id = error-notification]")
                .shouldBe(visible, ofMillis(15000))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        AuthInfo invalidPasswordUser = DataGenerator.Registration.generateUserWithInvalidPassword();
        $("[name = login]").setValue(invalidPasswordUser.getLogin());
        $("[name = password]").setValue(invalidPasswordUser.getPassword());
        $("[data-test-id = action-login]").click();
        $("[data-test-id = error-notification]")
                .shouldBe(visible, ofMillis(15000))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        AuthInfo invalidLoginUser = DataGenerator.Registration.generateUserWithInvalidLogin();
        $("[name = login]").setValue(invalidLoginUser.getLogin());
        $("[name = password]").setValue(invalidLoginUser.getPassword());
        $("[data-test-id = action-login]").click();
        $("[data-test-id = error-notification]")
                .shouldBe(visible, ofMillis(15000))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}