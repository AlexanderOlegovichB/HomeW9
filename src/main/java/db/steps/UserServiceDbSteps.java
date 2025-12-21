package db.steps;

import db.DbBaseSteps;
import db.dao.reviews.ReviewsDao;
import db.dao.users.UsersDao;
import db.domain.reviews.Review;
import db.domain.users.User;
import io.qameta.allure.Step;
import utils.DbCredentials;

import java.util.List;

/**
 * Класс шагов (steps) для работы с пользователями в автотестах.
 * Наследует настройки подключения и конфигурацию JDBI из DbBaseSteps.
 */
public class UserServiceDbSteps extends DbBaseSteps {

    /**
     * Конструктор, проксирует параметры подключения в базовый класс.
     *
     * @param url      JDBC-url базы данных.
     * @param username Логин пользователя базы.
     * @param password Пароль пользователя базы.
     */
    public UserServiceDbSteps(String url, String username, String password) {
        super(url, username, password);
    }

    /**
     * Конструктор, принимает объект DbCredentials и передаёт данные базовому классу.
     * Формирует JDBC URL и инициализирует подключение.
     *
     * @param creds - объект с параметрами подключения к базе данных.
     */
    public UserServiceDbSteps(DbCredentials creds) {
        super(creds);
    }

    /**
     * Получить пользователя по userId.
     * Делает вызов DAO для выбора пользователя из базы.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект User, полученный из базы.
     */
    @Step("Получить юзера по айди {userId}")
    public User getUserByUserId(String userId) {
        // withExtension создаёт и закрывает DAO для выполнения запроса SELECT
        return dbClient.withExtension(UsersDao.class, dao -> dao.selectByUserId(String.valueOf(userId)));
    }

    /**
     * Получить список всех пользователей.
     * Вызывает соответствующий метод DAO.
     *
     * @return Список пользователей.
     */
    @Step("Выбрать всех юзеров")
    public List<User> getAllUsers() {
        // Короткая форма вызова DAO через ссылку на метод
        return dbClient.withExtension(UsersDao.class, UsersDao::selectAll);
    }

    /**
     * Вставить нового пользователя в базу.
     * Использует DAO для выполнения INSERT.
     *
     * @param user Объект User для добавления.
     */
    @Step("Добавить юзера")
    public void insertUser(User user) {
        // Здесь используем useExtension, так как insertUser ничего не возвращает (void)
        dbClient.useExtension(UsersDao.class, dao -> dao.insertUser(user));
    }

    /**
     * Удалить пользователя по userId.
     * Вызывает метод DAO с операцией DELETE.
     *
     * @param userId Идентификатор пользователя для удаления.
     */
    @Step("Удалить юзера")
    public void deleteUserById(long userId) {
        // Аналогично, useExtension подходит для void-метода удаления
        dbClient.useExtension(UsersDao.class, dao -> dao.deleteUserById(String.valueOf(userId)));
    }


    /**
     * Получить отзыв по userId + movieId.
     * Делает вызов DAO для выбора отзыва из базы.
     *
     * @param userId Идентификатор пользователя.
     * @param movieId Идентификатор фильма.
     * @return Объект Review, полученный из базы.
     */
    @Step("Получить отзыв по айди фльма {movieId} и юзера {userId}")
    public Review getReviewByUserAndMovieId(String userId, Integer movieId) {
        // withExtension создаёт и закрывает DAO для выполнения запроса SELECT
        return dbClient.withExtension(ReviewsDao.class, dao -> dao.selectByMovieIdAndUserId(movieId, userId));
    }
}