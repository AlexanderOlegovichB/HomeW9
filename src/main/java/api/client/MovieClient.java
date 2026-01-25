package api.client;

import api.dto.reviews.CreateReviewRequestDto;
import api.dto.reviews.ReviewResponseDto;
import io.qameta.allure.Step;

import static api.spec.CinescopeSpecs.*;
import static io.restassured.RestAssured.given;

public class MovieClient {

    @Step("Создание отзыва на фильм с айди {request.movieId}")
    public ReviewResponseDto createReview(CreateReviewRequestDto request, String accessToken) {
        return given()
                .spec(apiRequestSpec())
                .auth().oauth2(accessToken)
                .body(request)
                .when()
                .post("/movies/{movieId}/reviews", request.getMovieId())
                .then()
                .spec(responseSpecCreatedOk())
                .extract()
                .as(ReviewResponseDto.class);
    }

    @Step("Невозможность повторного создания отзыва на фильм с айди {request.movieId}")
    public void createDuplicateReview(CreateReviewRequestDto request, String accessToken) {
        given()
                .spec(apiRequestSpec())
                .auth().oauth2(accessToken)
                .body(request)
                .when()
                .post("/movies/{movieId}/reviews", request.getMovieId())
                .then()
                .spec(responseSpecConflict());
    }

    @Step("Изменение отзыва на фильм с айди {updatedRequest.movieId}")
    public ReviewResponseDto updateReview(CreateReviewRequestDto updatedRequest, String accessToken) {
        return given()
                .spec(apiRequestSpec())
                .auth().oauth2(accessToken)
                .body(updatedRequest)
                .when()
                .put("/movies/{movieId}/reviews", updatedRequest.getMovieId())
                .then()
                .spec(responseSpecOk())
                .extract()
                .as(ReviewResponseDto.class);
    }

    @Step("Удаление отзыва на фильм с айди {movieId}")
    public void deleteReview(Integer movieId, String userId, String accessToken) {
        given()
                .spec(apiRequestSpec())
                .auth().oauth2(accessToken)
                .queryParam("userId", userId)
                .when()
                .delete("/movies/{movieId}/reviews", movieId)
                .then()
                .spec(responseSpecOk());
    }
}
