package tests.movies;

import api.client.MovieClient;
import api.dto.reviews.CreateReviewRequestDto;
import api.dto.reviews.ReviewResponseDto;
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


@DisplayName("Позитивное создание отзыва")
@Tag("smoke")
public class CreateReviewDbIntegrationTestPositive {
    private MovieClient movieClient;
    private UserServiceDbSteps userServiceDbSteps;
    private AuthHelper authHelper;
    private Integer cleanMovieId;
    private String cleanUserId;

    @BeforeEach
    void setUp() {
        movieClient = new MovieClient();
        authHelper = new AuthHelper();
        userServiceDbSteps = new UserServiceDbSteps(DbUtils.getCredentials(DbName.DB_MOVIES));
        cleanUserId = null;
        cleanMovieId = null;
    }

    @Test
    @DisplayName("Создание отзыва через АПИ и проверка в БД")
    public void shouldCreateReviewAndPersistInDbTest() {
        // ДАнные
        Integer movieId = 53;
        String text = "Фильм получается ага";
        Integer rating = 4;

        // логинимся и получаем токен для выполнения запроса и юзерайди для поиска отзыва
        Pair<String, String> authData = authHelper.loginAndGetToken(RoleCreds.USER);
        String userId = authData.getLeft();
        String userToken = authData.getRight();

        // Фиксируем для метода очистки
        cleanMovieId = movieId;
        cleanUserId = userId;

        // Готовим запрос
        CreateReviewRequestDto request = CreateReviewRequestDto.builder()
                .movieId(movieId)
                .text(text)
                .rating(rating)
                .build();

        // Прокидываем запрос
        ReviewResponseDto response = movieClient.createReview(request, userToken);
        //Смотрим отзыв в БД
        Review reviewFromDb = userServiceDbSteps.getReviewByUserAndMovieId(userId, movieId);
        //Проверки
        assertThat(reviewFromDb.getUserId())
                .as("userId в БД должен совпадать с userId из ответа создания")
                .isEqualTo(response.getUserId());
        assertThat(reviewFromDb.getText())
                .as("userId в БД должен совпадать с userId из ответа создания")
                .isEqualTo(response.getText());
        assertThat(reviewFromDb.getRating())
                .as("userId в БД должен совпадать с userId из ответа создания")
                .isEqualTo(response.getRating());
        assertThat(reviewFromDb.getMovieId())
                .as("userId в БД должен совпадать с userId из ответа создания")
                .isEqualTo(movieId);
        assertThat(reviewFromDb.getHidden())
                .as("userId в БД должен совпадать с userId из ответа создания")
                .isEqualTo(response.isHidden());
        assertThat(reviewFromDb.getCreatedAt())
                .as("userId в БД должен совпадать с userId из ответа создания")
                .isNotNull();
    }


    @AfterEach
    void cleanUp() {
        // логинимся Админом и удаляем отзыв юзера (потому что удаление юзером не работает)
        if (cleanMovieId != null && cleanUserId != null) {
            String adminToken = authHelper.login(RoleCreds.ADMIN).getAccessToken();

            movieClient.deleteReview(cleanMovieId, cleanUserId, adminToken);
        }
    }
}
