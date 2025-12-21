package tests.users;

import api.client.UserApiClient;
import api.dto.auth.RegisterDto;
import api.dto.auth.UserResponseDto;
import db.domain.users.User;
import db.steps.UserServiceDbSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.DbName;
import utils.DbUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Позитивная регистрация пользователя")
public class RegisterUserDbIntegrationTestPositive {

    private UserApiClient userApiClient;
    private UserServiceDbSteps userServiceDbSteps;

    @BeforeEach
    void setUp() {
        userApiClient = new UserApiClient();
        userServiceDbSteps = new UserServiceDbSteps(DbUtils.getCredentials(DbName.DB_MOVIES));
    }

    @Test
    @DisplayName("Регистрация пользователя через API и проверка записи в БД")
    void createUserAndCheckInDbTest() {
        // подготовка данных
        String email = "test" + UUID.randomUUID() + "@example.com";
        String fullName = "ТестАлександра";
        String password = "SuperSecretPassword1234";

        // Собираем тело запроса
        RegisterDto request = RegisterDto.builder()
                .email(email)
                .fullName(fullName)
                .password(password)
                .passwordRepeat(password)
                .build();

        // Вызываем апишку, передаем тело запроса
        UserResponseDto response = userApiClient.createUser(request);
        // Получаем айди из ответа
        String userId = response.getId();
        // Достаем юзера из базы по полученному айди
        User userFromDb = userServiceDbSteps.getUserByUserId(userId);

        //Проверки
        assertThat(userFromDb.getId()).isEqualTo(response.getId());
        assertThat(userFromDb.getEmail()).isEqualTo(response.getEmail());
        assertThat(userFromDb.getFullName()).isEqualTo(response.getFullName());

    }
}
