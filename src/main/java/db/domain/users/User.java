package db.domain.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Roles;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Roles> roles;
}