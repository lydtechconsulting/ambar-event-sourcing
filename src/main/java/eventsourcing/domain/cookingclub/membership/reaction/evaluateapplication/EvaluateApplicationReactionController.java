package eventsourcing.domain.cookingclub.membership.reaction.evaluateapplication;

import eventsourcing.common.ambar.AmbarAuth;
import eventsourcing.common.ambar.AmbarHttpRequest;
import eventsourcing.common.eventstore.PostgresTransactionalEventStore;
import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import eventsourcing.common.reaction.ReactionController;
import eventsourcing.common.serializedevent.Deserializer;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestScope
@RequestMapping("/api/v1/cooking-club/membership/reaction")
@AmbarAuth
public class EvaluateApplicationReactionController extends ReactionController {
    private final EvaluateApplicationReactionHandler evaluateApplicationReactionHandler;

    public EvaluateApplicationReactionController(
            PostgresTransactionalEventStore eventStore,
            MongoTransactionalProjectionOperator mongoOperator,
            Deserializer deserializer,
            EvaluateApplicationReactionHandler evaluateApplicationReactionHandler) {
        super(eventStore, mongoOperator, deserializer);
        this.evaluateApplicationReactionHandler = evaluateApplicationReactionHandler;
    }

    @PostMapping(value = "/evaluate-application",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String reactWithEvaluateApplication(@Valid @RequestBody AmbarHttpRequest request) {
        return processReactionHttpRequest(request, evaluateApplicationReactionHandler);
    }
}