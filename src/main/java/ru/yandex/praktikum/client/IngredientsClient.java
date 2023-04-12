package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.BurgerRestClient;
import ru.yandex.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends BurgerRestClient {
    private static final String INGREDIENTS_URI = "ingredients";

    @Step("Получение списка ингредиентов")
    public Response get() {
        return given()
                .spec(getBaseReqSpec())
                .get(INGREDIENTS_URI);
    }
}
