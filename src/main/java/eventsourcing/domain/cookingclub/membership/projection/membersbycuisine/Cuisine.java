package eventsourcing.domain.cookingclub.membership.projection.membersbycuisine;

import jakarta.persistence.Id;
import lombok.*;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Cuisine {
    @Id @NonNull private String id;
    @NonNull private List<String> memberNames;
}