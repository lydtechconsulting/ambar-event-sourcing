package eventsourcing.common.reaction;

import eventsourcing.common.event.Event;
import eventsourcing.common.eventstore.PostgresTransactionalEventStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ReactionHandler {
    final protected PostgresTransactionalEventStore postgresTransactionalEventStore;
    public abstract void react(final Event event) throws JsonProcessingException;
}
