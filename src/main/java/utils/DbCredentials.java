package utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbCredentials {
    private String host;
    private int port;
    private String dbName;
    private String username;
    private String password;
}
