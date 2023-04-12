package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.BurgerRestClient;
import ru.yandex.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BurgerRestClient {
    private static final String ORDERS_URI = "orders";

    @Step("Создание {order}")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", order.getToken())
                .body(order)
                .post(ORDERS_URI)
                .then();
    }

    @Step("Получение для конкретного пользователя {order}")
    public ValidatableResponse get(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", order.getToken())
                .get(ORDERS_URI)
                .then();
    }
}
