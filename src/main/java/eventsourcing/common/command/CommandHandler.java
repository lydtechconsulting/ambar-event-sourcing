package eventsourcing.common.command;

import eventsourcing.common.eventstore.PostgresTransactionalEventStore;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract public class CommandHandler {
    final protected PostgresTransactionalEventStore postgresTransactionalEventStore;
    public abstract void handleCommand(Command command);
}
