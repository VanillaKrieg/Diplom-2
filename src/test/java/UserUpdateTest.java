import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class UserUpdateTest {
    private UserClient userClient;
    private User user;
    private User user_updated;


    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
    }

    @After
    public void clearData() throws IllegalArgumentException {
        try {
            userClient.delete(user);
            userClient.delete(user_updated);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    @DisplayName("Пользователь может обновить свои данные с авторизацией")
    public void userCanUpdateDataWithAuthorization() {
        user_updated = UserGenerator.getRandom();
        user_updated.setToken(user.getToken());

        userClient.update(user_updated)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Пользователь не может обновить свои данные без авторизации")
    public void userCanNotUpdateDataWithoutAuthorization() {
        user_updated = UserGenerator.getRandom();

        userClient.update(user_updated)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Пользователь не может обновить свой email на уже существующий")
    public void userCanNotUpdateEmailWithSameEmail() {
        user_updated = UserGenerator.getRandom();
        userClient.create(user_updated);
        user.setEmail(user_updated.getEmail());

        userClient.update(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", is("User with such email already exists"));
    }
}
