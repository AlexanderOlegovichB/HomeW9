package api.helper;

import api.client.UserApiClient;
import api.dto.auth.AuthResponseDto;
import api.dto.auth.LoginRequestDto;
import io.qameta.allure.Step;
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

    public String getAuthToken() {
        return getAuthToken(RoleCreds.USER);
    }

    public String getAuthToken(RoleCreds role) {
        return login(role).getAccessToken();
    }
}
