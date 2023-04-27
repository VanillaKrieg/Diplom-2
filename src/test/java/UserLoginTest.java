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

public class UserLoginTest {
    private UserClient userClient;
    private User user;
    private User user_invalid;


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
        user_invalid = new User(user);
        userClient.create(user);
    }

    @After
    public void clearData() {
            userClient.delete(user);
    }

    @Test
    @DisplayName("Пользователь может залогиниться с валидными данными")
    public void userCanLoginWithValidData() {
        userClient.login(user)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Пользователь не может залогиниться с невалидным email")
    public void userCanNotLoginWithInvalidEmail() {
        user_invalid.setEmail("invalid_email");

        userClient.login(user_invalid)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Пользователь не может залогиниться с невалидным паролем")
    public void userCanNotLoginWithInvalidPassword() {
        user_invalid.setPassword("invalid_password");

        userClient.login(user_invalid)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Пользователь не может залогиниться без email")
    public void userCanNotLoginWithoutEmail() {
        user_invalid.setEmail("");

        userClient.login(user_invalid)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Пользователь не может залогиниться без пароля")
    public void userCanNotLoginWithoutPassword() {
        user_invalid.setPassword("");

        userClient.login(user_invalid)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
    }
}
