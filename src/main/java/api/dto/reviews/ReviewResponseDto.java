package api.dto.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private String userId;
    private String text;
    private int rating;
    private boolean hidden;
    private LocalDateTime createdAt;
    private ReviewUserDto user;
}
