package eventsourcing.domain.cookingclub.membership.projection.membersbycuisine;

import jakarta.persistence.Id;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class MembershipApplication {
    @Id @NonNull private String id;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String favoriteCuisine;
}