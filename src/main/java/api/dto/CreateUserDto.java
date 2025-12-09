package api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
private String fullName;
private String email;
private String password;
private Boolean verified;
private Boolean banned;
}
