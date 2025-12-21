package api.dto.auth;

import lombok.*;
import utils.Roles;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @EqualsAndHashCode.Exclude
    private String id;
    private String email;
    private String fullName;
    private List<Roles> roles;
    private Boolean verified;
    private LocalDateTime createdAt;
    private Boolean banned;
}
