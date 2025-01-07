package eventsourcing.domain.cookingclub.membership.aggregate;

import eventsourcing.common.aggregate.Aggregate;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
public class Membership extends Aggregate {
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String status;
}