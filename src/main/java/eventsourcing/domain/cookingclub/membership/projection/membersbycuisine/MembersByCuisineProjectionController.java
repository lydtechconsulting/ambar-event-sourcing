package eventsourcing.domain.cookingclub.membership.projection.membersbycuisine;

import eventsourcing.common.ambar.AmbarAuth;
import eventsourcing.common.ambar.AmbarHttpRequest;
import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import eventsourcing.common.projection.ProjectionController;
import eventsourcing.common.serializedevent.Deserializer;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;


@RestController
@RequestScope
@RequestMapping("/api/v1/cooking-club/membership/projection")
@AmbarAuth
public class MembersByCuisineProjectionController extends ProjectionController {
    private final MembersByCuisineProjectionHandler membersByCuisineProjectionHandler;

    public MembersByCuisineProjectionController(
            MongoTransactionalProjectionOperator mongoOperator,
            Deserializer deserializer,
            MembersByCuisineProjectionHandler membersByCuisineProjectionHandler) {
        super(mongoOperator, deserializer);
        this.membersByCuisineProjectionHandler = membersByCuisineProjectionHandler;
    }

    @PostMapping(value = "/members-by-cuisine",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String projectMembersByCuisine(@Valid @RequestBody AmbarHttpRequest request) {
        return processProjectionHttpRequest(
                request,
                membersByCuisineProjectionHandler,
                "CookingClub_Membership_MembersByCuisine");
    }
}