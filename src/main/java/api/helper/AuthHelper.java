package api.helper;

import api.client.UserApiClient;
import api.dto.auth.AuthResponseDto;
import api.dto.auth.LoginRequestDto;
import io.qameta.allure.Step;
import org.apache.commons.lang3.tuple.Pair;
import utils.RoleCreds;

public class AuthHelper {
    private final UserApiClient authApiClient = new UserApiClient();

    @Step("авторизация под ролью {user}")
    public AuthResponseDto login(RoleCreds user) {
        LoginRequestDto request = LoginRequestDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        return UserApiClient.loginAndGetToken(request);
    }

    @Step("Логин с ролью {role} и получение учетных данных для запросов")
    public Pair<String, String> loginAndGetToken(RoleCreds role) {
        AuthResponseDto auth = login(role);
        String userId = auth.getUser().getId();
        String token = auth.getAccessToken();
        return Pair.of(userId, token);
    }

    public String getAuthToken(RoleCreds role) {
        return login(role).getAccessToken();
    }
}
