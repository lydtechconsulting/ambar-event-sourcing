package eventsourcing.domain.cookingclub.membership.command.submitapplication;

import eventsourcing.common.command.CommandController;
import eventsourcing.common.eventstore.PostgresTransactionalEventStore;
import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestScope
@RequestMapping("/api/v1/cooking-club/membership/command")
public class SubmitApplicationCommandController extends CommandController {
    private final SubmitApplicationCommandHandler submitApplicationCommandHandler;

    public SubmitApplicationCommandController(
            PostgresTransactionalEventStore postgresTransactionalEventStore,
            MongoTransactionalProjectionOperator mongoTransactionalProjectionOperator,
            SubmitApplicationCommandHandler submitApplicationCommandHandler
    ) {
        super(postgresTransactionalEventStore, mongoTransactionalProjectionOperator);
        this.submitApplicationCommandHandler = submitApplicationCommandHandler;
    }

    @PostMapping("/submit-application")
    @ResponseStatus(HttpStatus.OK)
    public void submitApplication(@Valid @RequestBody SubmitApplicationHttpRequest request) {
        SubmitApplicationCommand command = SubmitApplicationCommand.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .favoriteCuisine(request.getFavoriteCuisine())
                .yearsOfProfessionalExperience(request.getYearsOfProfessionalExperience())
                .numberOfCookingBooksRead(request.getNumberOfCookingBooksRead())
                .build();

        processCommand(command, submitApplicationCommandHandler);
    }
}
