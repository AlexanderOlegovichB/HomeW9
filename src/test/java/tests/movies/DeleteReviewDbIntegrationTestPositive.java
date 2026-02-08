package tests.movies;

import api.client.MovieClient;
import api.dto.reviews.CreateReviewRequestDto;
import api.helper.AuthHelper;
import db.domain.reviews.Review;
import db.steps.UserServiceDbSteps;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.DbName;
import utils.DbUtils;
import utils.RoleCreds;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Позитивное удаление отзыва")
@Tag("Smoke")
public class DeleteReviewDbIntegrationTestPositive {
    private MovieClient movieClient;
    private UserServiceDbSteps userServiceDbSteps;
    private AuthHelper authHelper;
    private String cleanUserId;
    private Integer cleanMovieId;

    @BeforeEach
    void setUp() {
        movieClient = new MovieClient();
        authHelper = new AuthHelper();
        userServiceDbSteps = new UserServiceDbSteps(DbUtils.getCredentials(DbName.DB_MOVIES));
        cleanMovieId = null;
        cleanUserId = null;
    }

    @Test
    @DisplayName("Удаление отзыва по АПИ и проверка в БД")
    public void shouldDeleteReviewAndNotPersistInDb() {
        // Данные теста
        Integer movieId = 53;
        String text = "Отзыва для теста удаления";
        Integer rating = 5;

        // Логинимся юзером для создания отзыва и получаем токен для запроса и юзерайди для поиска отзыва
        Pair<String, String> authData = authHelper.loginAndGetToken(RoleCreds.USER);
        String userId = authData.getLeft();
        String userToken = authData.getRight();

        //Фиксируем для очистки
        cleanUserId = userId;
        cleanMovieId = movieId;

        //готовим запрос на создание отзыва
        CreateReviewRequestDto request = CreateReviewRequestDto.builder()
                .movieId(movieId)
                .text(text)
                .rating(rating)
                .build();

        //прокидываем запрос на создание отзыва
        movieClient.createReview(request, userToken);

        //Проверяем что отзщыв не пустой/создался таки
        Review reviewBeforeDelete = userServiceDbSteps.getReviewByUserAndMovieId(userId, movieId);
        assertThat(reviewBeforeDelete)
                .as("Отзыв должен присутствовать после создания")
                .isNotNull();
        assertThat(reviewBeforeDelete.getText())
                .as("Значение text в БД после создания отзыва должно быть равно значению text указанному при создании")
                .isEqualTo(text);

        //логинимся админом для удаления отзыва
        String adminToken = authHelper.login(RoleCreds.ADMIN).getAccessToken();

        //прокидываем запрос на удаление отзыва
        movieClient.deleteReview(movieId, userId, adminToken);

        //проверки
        Review reviewAfterDelete = userServiceDbSteps.getReviewByUserAndMovieId(userId, movieId);
        assertThat(reviewAfterDelete)
                .as("Отзыв должен отсутствовать после удаления")
                .isNull();
    }

    @AfterEach
    void cleanUp() {
        Review review = userServiceDbSteps.getReviewByUserAndMovieId(cleanUserId, cleanMovieId);
        if (review != null) {
                String adminToken = authHelper.login(RoleCreds.ADMIN).getAccessToken();

                movieClient.deleteReview(cleanMovieId, cleanUserId, adminToken);
        }
    }
}
