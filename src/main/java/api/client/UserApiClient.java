package api.client;

import api.dto.auth.AuthResponseDto;
import api.dto.auth.LoginRequestDto;
import api.dto.auth.RegisterDto;
import api.dto.auth.UserResponseDto;
import io.qameta.allure.Step;

import static api.spec.CinescopeSpecs.*;
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
                .spec(responseSpecCreatedOk())
                .extract()
                .as(UserResponseDto.class);
    }

    @Step("Отправка запроса на получение юзера")
    public UserResponseDto getUser(String id) {
        return given()
                .spec(authRequestSpec())
                .when()
                .get("/user/" + id)
                .then()
                .spec(responseSpecOk())
                .extract()
                .as(UserResponseDto.class);
    }

    @Step("Авторизация: логин по email {request.email}")
    public static AuthResponseDto loginAndGetToken(LoginRequestDto request) {
        return given()
                .spec(authRequestSpec())
                .body(request)
                .when()
                .post("/login")
                .then()
                .spec(responseSpecOk())
                .extract()
                .as(AuthResponseDto.class);
    }
}