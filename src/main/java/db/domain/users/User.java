package db.domain.users;

import lombok.*;
import utils.Roles;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String id;
    private String email;
    private String fullName;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean verified;
    private Boolean banned;
    private Roles roles;
}