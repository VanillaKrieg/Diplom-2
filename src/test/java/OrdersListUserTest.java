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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class OrdersListUserTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private Order order;


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
        orderClient.create(order);
    }

    @After
    public void clearData() {
            userClient.delete(user);
    }

    @Test
    @DisplayName("Список заказов пользователя может быть получен с авторизацией")
    public void ordersListUserCanBeGottenWithAuthorization() {
        orderClient.get(order)
                .assertThat()
                .statusCode(SC_OK)
                .body("orders", hasSize(greaterThan(0)));
    }

    @Test
    @DisplayName("Список заказов пользователя не может быть получен без авторизации")
    public void ordersListUserCanNotBeGottenWithoutAuthorization() {
        order.setToken("");

        orderClient.get(order)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("You should be authorised"));
    }
}
