package db.domain.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Review {
    private Integer movieId;
    private String userId;
    private Boolean hidden;
    private String text;
    private Integer rating;
    private LocalDateTime createdAt;
}
