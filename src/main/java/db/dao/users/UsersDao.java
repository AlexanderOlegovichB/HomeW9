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
 * DAO-интерфейс для работы с таблицей пользователей (global.users).
 * Методы здесь описывают операции с БД в виде аннотированных SQL-запросов.
 */

public interface UsersDao {

    /**
     * Запрос на получение одного пользователя по его user_id.
     * @param userId идентификатор пользователя для поиска.
     * @return объект User, десериализованный из JSONB-колонки.
     *
     * Аннотации:
     *  - @Json: указывает, что результат SQL запроса — JSONB, который нужно преобразовать в объект User через Jackson.
     *  - @SqlQuery: SQL SELECT-запрос с использованием именованного параметра :userId.
     *  - @Bind: связывает параметр метода userId с параметром SQL-контекста.
     */
    @Json
    @SqlQuery("SELECT to_jsonb(u) FROM global.users as u WHERE user_id = :userId")
    User selectByUserId(@Bind("userId") String userId);

    /**
     * Запрос на получение списка всех пользователей.
     * @return список объектов User, каждый из которых десериализован из JSONB.
     *
     * Аннотации такие же, как и в selectByUserId.
     */
    @Json
    @SqlQuery("SELECT to_jsonb(u) FROM global.users as u")
    List<User> selectAll();

    /**
     * Запрос обновления времени регистрации пользователя.
     * @param userId идентификатор пользователя
     * @param registeredAt новое значение времени регистрации (LocalDateTime)
     * @return true, если обновление прошло успешно (обычно если изменена хотя бы одна строка)
     * если результат не нужен, то можно ничего не возращать и написать void
     *
     * Аннотации:
     *  - @SqlUpdate: SQL-запрос изменения данных (UPDATE).
     *  - @Bind: связывает параметры метода с параметрами SQL.
     */
    @SqlUpdate("UPDATE global.users SET registered_at = :registeredAt WHERE user_id = :userId")
    boolean updateRegisteredAtTime(@Bind("userId") long userId, @Bind("registeredAt") LocalDateTime registeredAt);

    /**
     * Вставка нового пользователя в таблицу users.
     * @param user объект User, поля которого автоматически маппятся на колонки таблицы (через @BindBean)
     * @return количество добавленных строк (обычно 1 при успешной вставке)
     * если результат не нужен, то можно ничего не возращать и написать void
     */
    @SqlUpdate("INSERT INTO global.users (user_id, email, registered_at) VALUES (:userId, :email, :registeredAt)")
    int insertUser(@BindBean User user);

    /**
     * Удаление пользователя по user_id.
     * @param userId идентификатор пользователя, которого нужно удалить
     * @return количество удалённых строк (0 если такого user_id нет, 1 если удалён)
     * если результат не нужен, то можно ничего не возращать и написать void
     */
    @SqlUpdate("DELETE FROM global.users WHERE user_id = :userId")
    int deleteUserById(@Bind("userId") long userId);

}
