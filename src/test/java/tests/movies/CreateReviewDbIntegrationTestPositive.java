package tests.movies;

import api.client.MovieClient;
import api.dto.auth.AuthResponseDto;
import api.dto.reviews.CreateReviewRequestDto;
import api.dto.reviews.ReviewResponseDto;
import api.helper.AuthHelper;
import db.domain.reviews.Review;
import db.steps.UserServiceDbSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.DbName;
import utils.DbUtils;
import utils.RoleCreds;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Позитивное создание отзыва")
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
        RoleCreds userRole = RoleCreds.USER;
        Integer movieId = 689;
        String text = "Фильм получается ага";
        Integer rating = 4;

        // логинимся и получаем токен для выполнения запроса и юзерайди для поиска отзыва
        AuthResponseDto auth = authHelper.login(userRole);
        String token = auth.getAccessToken();
        String userId = auth.getUser().getId();

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
        ReviewResponseDto response = movieClient.createReview(request, token);
        //Смотрим отзыв в БД
        Review reviewFromDb = userServiceDbSteps.getReviewByUserAndMovieId(userId, movieId);
        //Проверки
        assertThat(reviewFromDb.getUserId()).isEqualTo(response.getUserId());
        assertThat(reviewFromDb.getText()).isEqualTo(response.getText());
        assertThat(reviewFromDb.getRating()).isEqualTo(response.getRating());
        assertThat(reviewFromDb.getMovieId()).isEqualTo(movieId);
        assertThat(reviewFromDb.getHidden()).isEqualTo(response.isHidden());
        assertThat(reviewFromDb.getCreatedAt()).isNotNull();
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
