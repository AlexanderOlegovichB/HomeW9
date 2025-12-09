package api.client;

import api.dto.RegisterDto;
import api.dto.UserResponseDto;
import io.qameta.allure.Step;

import static api.spec.CinescopeSpecs.authRequestSpec;
import static api.spec.CinescopeSpecs.responseSpecOk;
import static io.restassured.RestAssured.given;

public class UserApiClient {
    @Step("Отправка запроса на регистрацию юзера")
    public UserResponseDto createUser(RegisterDto request) {
        return given()
                .spec(authRequestSpec())
                .body(request)
                .when()
                .post("/register")
                .then()
                .extract()
                .as(UserResponseDto.class);
    }

    @Step("Отправка запроса на получение юзера")
    public UserResponseDto getUser(String id) {
        return given()
                .spec(authRequestSpec())
                .when()
                .get("/user/")
                .then()
                .spec(responseSpecOk())
                .extract()
                .as(UserResponseDto.class);
    }
}