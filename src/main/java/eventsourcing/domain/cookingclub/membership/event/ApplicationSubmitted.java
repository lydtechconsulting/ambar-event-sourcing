package eventsourcing.domain.cookingclub.membership.event;

import eventsourcing.common.event.CreationEvent;
import eventsourcing.domain.cookingclub.membership.aggregate.Membership;
import eventsourcing.domain.cookingclub.membership.aggregate.MembershipStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ApplicationSubmitted extends CreationEvent<Membership> {
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String favoriteCuisine;
    @NonNull private Integer yearsOfProfessionalExperience;
    @NonNull private Integer numberOfCookingBooksRead;

    @Override
    public Membership createAggregate() {
        return Membership.builder()
                .aggregateId(aggregateId)
                .aggregateVersion(aggregateVersion)
                .firstName(firstName)
                .lastName(lastName)
                .status(MembershipStatus.Requested.name())
                .build();
    }
}