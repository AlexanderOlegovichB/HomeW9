package api.helper;

import api.dto.reviews.CreateReviewRequestDto;
import io.qameta.allure.Step;

public class MovieHelper {
    @Step("Создание отзыва")
    public static CreateReviewRequestDto customReview(Integer movieId, String text, int rating) {
        return CreateReviewRequestDto.builder()
                .movieId(movieId)
                .text(text)
                .rating(rating)
                .build();
    }
}
