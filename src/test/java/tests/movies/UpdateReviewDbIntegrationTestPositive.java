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


@DisplayName("Позитивное обновление отзыва")
@Tag("regress")
public class UpdateReviewDbIntegrationTestPositive {
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
        cleanMovieId = null;
        cleanUserId = null;
    }

    @Test
    @DisplayName("Обновление отзыва через АПИ и проверка в БД")
    public void shouldCreateReviewAndPersistInDbTest() {
        // Данные для создания отзыва
        Integer movieId = 53;
        String text = "Фильм получается ага";
        Integer rating = 4;

        // логинимся и получаем токен для выполнения запроса и юзерайди для поиска отзыва
        Pair<String, String> authData = authHelper.loginAndGetToken(RoleCreds.USER);
        String userId = authData.getLeft();
        String userToken = authData.getRight();

        // Фиксируем для метода очистки
        cleanUserId = userId;
        cleanMovieId = movieId;

        // Готовим запрос на создание отзыва
        CreateReviewRequestDto request = CreateReviewRequestDto.builder()
                .movieId(movieId)
                .text(text)
                .rating(rating)
                .build();

        // Прокидываем запрос на создание отзыва
        ReviewResponseDto response = movieClient.createReview(request, userToken);

        // Данные для обновления отзыва (роль и MovieId менять не нужно)
        String updatedText = "НУ такое";
        Integer updatedRating = 3;

        // Готовим запрос на обновление отзыва
        CreateReviewRequestDto updatedRequest = CreateReviewRequestDto.builder()
                .movieId(movieId)
                .text(updatedText)
                .rating(updatedRating)
                .build();

        // Прокидываем запрос на изменение отзыва
        ReviewResponseDto updatedResponse = movieClient.updateReview(updatedRequest, userToken);

        //Смотрим отзыв в БД
        Review reviewFromDb = userServiceDbSteps.getReviewByUserAndMovieId(userId, movieId);
        //Проверки
        assertThat(reviewFromDb.getUserId())
                .as("userId в БД должен совпадать с userId из ответа обновления")
                .isEqualTo(updatedResponse.getUserId());
        assertThat(reviewFromDb.getText())
                .as("Текст отзыва в БД должен совпадать с текстом после обновления")
                .isEqualTo(updatedResponse.getText());
        assertThat(reviewFromDb.getRating())
                .as("Рейтинг в БД должен совпадать с рейтингом после обновления")
                .isEqualTo(updatedResponse.getRating());
        assertThat(reviewFromDb.getMovieId())
                .as("movieId в БД должен совпадать с movieId, по которому обновлялся отзыв")
                .isEqualTo(movieId);
        assertThat(reviewFromDb.getHidden())
                .as("Флаг hidden в БД должен совпадать со значением hidden из ответа обновления")
                .isEqualTo(updatedResponse.isHidden());
        assertThat(reviewFromDb.getCreatedAt())
                .as("Поле createdAt в БД должно быть заполнено после создания и обновления отзыва")
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
