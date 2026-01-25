package utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Пользователи для передачи кредов на логин
@Getter
@RequiredArgsConstructor
public enum RoleCreds {


    USER("ip.boroday@gmail.com", "Test2025"),
    ADMIN("test-admin@mail.com", "KcLMmxkJMjBD1");

    private final String email;
    private final String password;
}
