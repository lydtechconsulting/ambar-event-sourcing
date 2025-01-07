package eventsourcing.domain.cookingclub.membership.command.submitapplication;

import eventsourcing.common.command.Command;
import eventsourcing.common.command.CommandHandler;
import eventsourcing.common.eventstore.PostgresTransactionalEventStore;
import eventsourcing.common.util.IdGenerator;
import eventsourcing.domain.cookingclub.membership.event.ApplicationSubmitted;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Instant;

@Service
@RequestScope
public class SubmitApplicationCommandHandler extends CommandHandler {

    public SubmitApplicationCommandHandler(PostgresTransactionalEventStore postgresTransactionalEventStore) {
        super(postgresTransactionalEventStore);
    }

    @Override
    public void handleCommand(Command command) {
        if (command instanceof SubmitApplicationCommand submitApplicationCommand) {
            handleSubmitApplication(submitApplicationCommand);
        } else {
            throw new IllegalArgumentException("Unsupported command type: " + command.getClass().getName());
        }
    }

    private void handleSubmitApplication(SubmitApplicationCommand command) {
        String eventId = IdGenerator.generateRandomId();
        String aggregateId = IdGenerator.generateRandomId();

        ApplicationSubmitted applicationSubmitted = ApplicationSubmitted.builder()
                .eventId(eventId)
                .aggregateId(aggregateId)
                .aggregateVersion(1)
                .correlationId(eventId)
                .causationId(eventId)
                .recordedOn(Instant.now())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .favoriteCuisine(command.getFavoriteCuisine())
                .yearsOfProfessionalExperience(command.getYearsOfProfessionalExperience())
                .numberOfCookingBooksRead(command.getNumberOfCookingBooksRead())
                .build();

        postgresTransactionalEventStore.saveEvent(applicationSubmitted);
    }
}