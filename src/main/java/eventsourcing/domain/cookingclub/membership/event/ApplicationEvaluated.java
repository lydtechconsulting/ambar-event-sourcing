package eventsourcing.domain.cookingclub.membership.event;

import eventsourcing.common.event.TransformationEvent;
import eventsourcing.domain.cookingclub.membership.aggregate.Membership;
import eventsourcing.domain.cookingclub.membership.aggregate.MembershipStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ApplicationEvaluated extends TransformationEvent<Membership> {
    @NonNull private MembershipStatus evaluationOutcome;

    @Override
    public Membership transformAggregate(Membership aggregate) {
        return aggregate.toBuilder()
                .aggregateId(aggregateId)
                .aggregateVersion(aggregateVersion)
                .firstName(aggregate.getFirstName())
                .lastName(aggregate.getLastName())
                .status(evaluationOutcome.name())
                .build();
    }
}