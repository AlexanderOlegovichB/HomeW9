package api.dto;

import lombok.*;
import utils.Roles;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @EqualsAndHashCode.Exclude
        private String id;
        private String email;
        private String fullName;
        private Roles roles;
        private Boolean verified;
        private LocalDateTime createdAt;
        private Boolean banned;
}
