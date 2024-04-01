package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        // добавляем логику теста, в рамках которого будет выполнена попытка входа в личный кабинет
        // с учётными данными зарегистрированного активного пользователя, для заполнения полей формы
        // используем  пользователя registeredUser
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button").click();
        $("h2").shouldHave(text("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        // добавляем логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        // незарегистрированного пользователя, для заполнения полей формы
        // используем пользователя notRegisteredUser
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button").click();
        $(".notification__content").shouldHave(text("Ошибка!"), text("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        // добавляем логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        // заблокированного пользователя, для заполнения полей формы
        // используем пользователя blockedUser
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button").click();
        $(".notification__content").shouldHave(text("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        // добавляем логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        // с неверным логином, для заполнения поля формы "Логин" используем переменную wrongLogin,
        // "Пароль" - пользователя registeredUser
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button").click();
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        // добавляем логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        // с неверным паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        // "Пароль" - переменную wrongPassword
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button").click();
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(15));
    }
}