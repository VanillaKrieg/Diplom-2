import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.IngredientsGenerator;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserGenerator;

import java.io.IOException;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class OrderCreationTest {
    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    private User user;


    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() throws IOException {
        user = UserGenerator.getRandom();
        userClient = new UserClient();
        userClient.create(user);
        orderClient = new OrderClient();

        order = new Order(IngredientsGenerator.getRandom(), user.getToken());
    }

    @After
    public void clearData() {
            userClient.delete(user);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void orderCreationWithAuthorization() {
        orderClient.create(order)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order._id", is(notNullValue()));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void orderCreationWithoutAuthorization() {
        order.setToken("");

        orderClient.create(order)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order._id", is(nullValue()));
    }

    @Test
    @DisplayName("Заказ не может быть создан без ингредиентов")
    public void orderCanNotBeCreatedWithoutIngredients() {
        order.setIngredients(new String[]{});

        orderClient.create(order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Заказ не может быть создан с неверным ингредиентом")
    public void orderCanNotBeCreatedWithInvalidIngredient() {
        order.setIngredients(new String[]{"Invalid ingredient"});

        orderClient.create(order)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
