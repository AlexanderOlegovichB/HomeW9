package api.dto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String email;
    private String fullName;
    private String password;
    private String passwordRepeat;
}
