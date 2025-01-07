package eventsourcing.domain.cookingclub.membership.reaction.evaluateapplication;

import eventsourcing.common.event.Event;
import eventsourcing.common.eventstore.AggregateAndEventIdsInLastEvent;
import eventsourcing.common.eventstore.PostgresTransactionalEventStore;
import eventsourcing.common.reaction.ReactionHandler;
import eventsourcing.domain.cookingclub.membership.aggregate.Membership;
import eventsourcing.domain.cookingclub.membership.aggregate.MembershipStatus;
import eventsourcing.domain.cookingclub.membership.event.ApplicationEvaluated;
import eventsourcing.domain.cookingclub.membership.event.ApplicationSubmitted;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Instant;

import static eventsourcing.common.util.IdGenerator.generateDeterministicId;

@Service
@RequestScope
public class EvaluateApplicationReactionHandler extends ReactionHandler {

    public EvaluateApplicationReactionHandler(PostgresTransactionalEventStore eventStore) {
        super(eventStore);
    }

    @Override
    public void react(Event event) {
        if (!(event instanceof ApplicationSubmitted applicationSubmitted)) {
            return;
        }

        AggregateAndEventIdsInLastEvent aggregateAndEventIds =
                postgresTransactionalEventStore.findAggregate(applicationSubmitted.getAggregateId());

        if (!(aggregateAndEventIds.getAggregate() instanceof Membership membership)) {
            throw new RuntimeException("Invalid aggregate type");
        }

        if (!membership.getStatus().equals(MembershipStatus.Requested.name())) {
            return;
        }

        String reactionEventId = generateDeterministicId(
                "CookingClub_Membership_ReviewedApplication:" + applicationSubmitted.getEventId());

        if (postgresTransactionalEventStore.doesEventAlreadyExist(reactionEventId)) {
            return;
        }

        boolean shouldApprove = applicationSubmitted.getYearsOfProfessionalExperience() == 0
                && applicationSubmitted.getNumberOfCookingBooksRead() > 0;

        ApplicationEvaluated reactionEvent = ApplicationEvaluated.builder()
                .eventId(reactionEventId)
                .aggregateId(membership.getAggregateId())
                .aggregateVersion(membership.getAggregateVersion() + 1)
                .causationId(aggregateAndEventIds.getEventIdOfLastEvent())
                .correlationId(aggregateAndEventIds.getCorrelationIdOfLastEvent())
                .recordedOn(Instant.now())
                .evaluationOutcome(shouldApprove ? MembershipStatus.Approved : MembershipStatus.Rejected)
                .build();

        postgresTransactionalEventStore.saveEvent(reactionEvent);
    }
}