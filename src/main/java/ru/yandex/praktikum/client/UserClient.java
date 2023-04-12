package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.BurgerRestClient;
import ru.yandex.praktikum.model.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BurgerRestClient {
    private static final String AUTH_URI = "auth/";

    @Step("Создание {user}")
    public ValidatableResponse create(User user) {
        ValidatableResponse response = given()
                .spec(getBaseReqSpec())
                .body(user)
                .post(AUTH_URI + "register")
                .then();
        user.setToken(response.extract().path("accessToken"));

        return response;
    }

    @Step("Логин {user}")
    public ValidatableResponse login(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .post(AUTH_URI + "login")
                .then();
    }

    @Step("Удаление {user}")
    public ValidatableResponse delete(User user) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", user.getToken())
                .delete(AUTH_URI + "user")
                .then();
    }

    @Step("Обновление {user}")
    public ValidatableResponse update(User user) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", user.getToken())
                .body(user)
                .patch(AUTH_URI + "user")
                .then();
    }
}
