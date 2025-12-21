package db.dao.reviews;

import db.domain.reviews.Review;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface ReviewsDao {

    /**
     * Запрос на получение одного пользователя по его user_id.
     *
     * @param userId  идентификатор пользователя для поиска.
     * @param movieId идентификатор фильма для поиска.
     * @return объект Review, десериализованный из JSONB-колонки.
     * <p>
     * Аннотации:
     * - @Json: указывает, что результат SQL запроса — JSONB, который нужно преобразовать в объект User через Jackson.
     * - @SqlQuery: SQL SELECT-запрос с использованием именованного параметра :userId.
     * - @Bind: связывает параметр метода userId с параметром SQL-контекста.
     */
    @Json
    @SqlQuery("SELECT to_jsonb(r) FROM public.reviews as r WHERE movie_id = :movieId AND user_id = :userId")
    Review selectByMovieIdAndUserId(@Bind("movieId") Integer movieId, @Bind("userId") String userId);
}
