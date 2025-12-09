package db;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import utils.DbCredentials;

public abstract class DbBaseSteps {

    // Главный клиент JDBI для всех операций с базой
    protected Jdbi dbClient;

    /**
     * Конструктор: настраивает подключение к базе, регистрирует плагины и ObjectMapper.
     * @param url JDBC-url вашей базы (например, "jdbc:postgresql://localhost:5432/mydb")
     * @param username Логин для подключения
     * @param password Пароль для подключения
     */
    public DbBaseSteps(String url, String username, String password) {
        // Создаём подключение вместе с плагинами (метод реализован ниже)
        dbClient = buildDbClient(url, username, password);
        // Настраиваем ObjectMapper для сериализации/десериализации JSON и маппинга полей
        initMapper();
    }

    /**
     * Конструктор с параметром DbCredentials (объяснение будет в конце)
     */
    public DbBaseSteps(DbCredentials creds) {
        this(
                "jdbc:postgresql://" + creds.getHost() + ":" + creds.getPort() + "/" + creds.getDbName(),
                creds.getUsername(),
                creds.getPassword()
        );
    }

    // Создаёт и настраивает Jdbi с нужными плагинами
    private Jdbi buildDbClient(String url, String username, String password) {
        Jdbi jdbi = Jdbi.create(url, username, password);
        // Подключаем плагин для PostgreSQL (замените, если используете другую СУБД)
        jdbi.installPlugin(new PostgresPlugin());
        // Подключаем поддержку аннотированных DAO-интерфейсов (@SqlQuery, @SqlUpdate и др.)
        jdbi.installPlugin(new SqlObjectPlugin());
        // Подключаем поддержку автоматического маппинга объектов через Jackson
        jdbi.installPlugin(new Jackson2Plugin());
        return jdbi;
    }

    // Настраивает ObjectMapper для корректной сериализации/десериализации
    private void initMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Устанавливаем snake_case: user_id <-> userId
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        // Для поддержки работы с датами и временем из Java 8+ (JSR-310)
        mapper.registerModule(new JavaTimeModule());
        // Применяем этот ObjectMapper ко всем операциям JDBI c JSON/объектами
        dbClient.getConfig(Jackson2Config.class).setMapper(mapper);
    }
}
