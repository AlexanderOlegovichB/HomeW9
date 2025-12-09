package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class DbUtils {

    private static final String CREDENTIALS_FILE = "db_credentials.json";

    private static DbCredentials credentials;

    static {
        try (InputStream is = DbUtils.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE)) {
            if (is == null) {
                throw new RuntimeException("Файл " + CREDENTIALS_FILE + " не найден");
            }
            ObjectMapper mapper = new ObjectMapper();
            credentials = mapper.readValue(is, DbCredentials.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке креденшалов", e);
        }
    }

    public static DbCredentials getCredentials(DbName dbName) {
        // В простом варианте возвращаем credentials, если имя совпадает
        if (credentials.getDbName().equals(dbName.getName())) {
            return credentials;
        } else {
            throw new RuntimeException("Креденшалы для базы " + dbName + " не найдены");
        }
    }
}
