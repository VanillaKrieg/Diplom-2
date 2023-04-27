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

public class UserCreationTest {
    private UserClient userClient;
    private User user;


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
    }

    @After
    public void clearData() throws IllegalArgumentException {
        try {
            userClient.delete(user);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    @DisplayName("Пользователь может быть создан с валидными данными")
    public void userCanBeCreatedWithValidData() {
        userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Пользователь не может быть создан с тем же email")
    public void userCanNotBeCreatedWithSameEmail() {
        userClient.create(user);

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Пользователь не может быть создан без email")
    public void userCanNotBeCreatedWithoutEmail() {
        user.setEmail("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Пользователь не может быть создан без пароля")
    public void userCanNotBeCreatedWithoutPassword() {
        user.setPassword("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Пользователь не может быть создан без имени")
    public void userCanNotBeCreatedWithoutName() {
        user.setName("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", is("Email, password and name are required fields"));
    }
}
