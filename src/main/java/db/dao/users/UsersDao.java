package db.dao.users;

import db.domain.users.User;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO-интерфейс для работы с таблицей пользователей (public.users).
 * Методы здесь описывают операции с БД в виде аннотированных SQL-запросов.
 */

public interface UsersDao {

    /**
     * Запрос на получение одного пользователя по его user_id.
     *
     * @param id идентификатор пользователя для поиска.
     * @return объект User, десериализованный из JSONB-колонки.
     * <p>
     * Аннотации:
     * - @Json: указывает, что результат SQL запроса — JSONB, который нужно преобразовать в объект User через Jackson.
     * - @SqlQuery: SQL SELECT-запрос с использованием именованного параметра :userId.
     * - @Bind: связывает параметр метода userId с параметром SQL-контекста.
     */
    @Json
    @SqlQuery("SELECT to_jsonb(u) FROM public.users as u WHERE id = :id")
    User selectByUserId(@Bind("id") String id);

    /**
     * Запрос на получение списка всех пользователей.
     *
     * @return список объектов User, каждый из которых десериализован из JSONB.
     * <p>
     * Аннотации такие же, как и в selectByUserId.
     */
    @Json
    @SqlQuery("SELECT to_jsonb(u) FROM public.users as u")
    List<User> selectAll();

    /**
     * Запрос обновления времени регистрации пользователя.
     *
     * @param id        идентификатор пользователя
     * @param updatedAt новое значение времени регистрации (LocalDateTime)
     * @return true, если обновление прошло успешно (обычно если изменена хотя бы одна строка)
     * если результат не нужен, то можно ничего не возращать и написать void
     * <p>
     * Аннотации:
     * - @SqlUpdate: SQL-запрос изменения данных (UPDATE).
     * - @Bind: связывает параметры метода с параметрами SQL.
     */
    @SqlUpdate("UPDATE public.users SET updated_at = :updatedAt WHERE id = :id")
    boolean updateRegisteredAtTime(@Bind("id") String id, @Bind("updatedAt") LocalDateTime updatedAt);

    /**
     * Вставка нового пользователя в таблицу users.
     *
     * @param user объект User, поля которого автоматически маппятся на колонки таблицы (через @BindBean)
     * @return количество добавленных строк (обычно 1 при успешной вставке)
     * если результат не нужен, то можно ничего не возращать и написать void
     */
    @SqlUpdate("INSERT INTO public.users (user_id, email, created_at) VALUES (:userId, :email, :created_at)")
    int insertUser(@BindBean User user);

    /**
     * Удаление пользователя по user_id.
     *
     * @param id идентификатор пользователя, которого нужно удалить
     * @return количество удалённых строк (0 если такого user_id нет, 1 если удалён)
     * если результат не нужен, то можно ничего не возращать и написать void
     */
    @SqlUpdate("DELETE FROM public.users WHERE id = :id")
    int deleteUserById(@Bind("id") String id);
}
