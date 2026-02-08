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

@DisplayName("Негативный тест создания отзыва")
@Tag("Regress")
public class CreateReviewDbIntegrationTestNegative {
    private MovieClient movieClient;
    private AuthHelper authHelper;
    private UserServiceDbSteps userServiceDbSteps;
    private Integer cleanMovieId;
    private String cleanUserId;

    //подготовка
    @BeforeEach
    void setUp() {
        movieClient = new MovieClient();
        authHelper = new AuthHelper();
        userServiceDbSteps = new UserServiceDbSteps(DbUtils.getCredentials(DbName.DB_MOVIES));
        cleanMovieId = null;
        cleanUserId = null;
    }

    @Test
    @DisplayName("Повторное создание отзыва с проверкой наличия первичного отзыва в БД")
    void shouldNotDuplicateReview() {
        // данные
        String text = "Not Duplicate";
        Integer movieId = 53;
        Integer rating = 4;

        // Логин и получение токена и юззерайди
        Pair<String, String> authData = authHelper.loginAndGetToken(RoleCreds.USER);
        String userId = authData.getLeft();
        String userToken = authData.getRight();

        // Сохранение для метода очистки
        cleanUserId = userId;
        cleanMovieId = movieId;

        // Подготовка тела отзыва
        CreateReviewRequestDto request = CreateReviewRequestDto.builder()
                .movieId(movieId)
                .text(text)
                .rating(rating)
                .build();

        //Отправка отзыва
        movieClient.createReview(request, userToken);

        //Проверка что после первого создания отзыв появился
        Review firstReviewFromDb = userServiceDbSteps.getReviewByUserAndMovieId(userId, movieId);

        assertThat(firstReviewFromDb)
                .as("Отзыв должен присутствовать после первого создания")
                .isNotNull();
        assertThat(firstReviewFromDb.getText())
                .as("Текст в бд должен соответствовать отправляемому тексту")
                .isEqualTo(text);

        //Проверка что после первого создания повторная попытка падает по 409 (responseSpecConflict)
        movieClient.createDuplicateReview(request, userToken);
    }

    //Очистка
    @AfterEach
    void cleanUp() {
        if (cleanMovieId != null && cleanUserId != null) {
            String adminToken = authHelper.login(RoleCreds.ADMIN).getAccessToken();

            movieClient.deleteReview(cleanMovieId, cleanUserId, adminToken);
        }
    }
}
