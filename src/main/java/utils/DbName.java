package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbName {
    DB_MOVIES("db_movies");
    private final String name;
}
