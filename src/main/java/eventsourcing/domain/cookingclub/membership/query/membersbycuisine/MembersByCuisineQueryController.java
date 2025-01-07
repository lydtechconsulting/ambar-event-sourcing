package eventsourcing.domain.cookingclub.membership.query.membersbycuisine;

import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import eventsourcing.common.query.QueryController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestScope
@RequestMapping("/api/v1/cooking-club/membership/query")
public class MembersByCuisineQueryController extends QueryController {
    private final MembersByCuisineQueryHandler membersByCuisineQueryHandler;

    public MembersByCuisineQueryController(
            MongoTransactionalProjectionOperator mongoTransactionalProjectionOperator,
            MembersByCuisineQueryHandler membersByCuisineQueryHandler
    ) {
        super(mongoTransactionalProjectionOperator);
        this.membersByCuisineQueryHandler = membersByCuisineQueryHandler;
    }

    @PostMapping("/members-by-cuisine")
    @ResponseStatus(HttpStatus.OK)
    public Object listMembersByCuisine() {
        MembersByCuisineQuery query = MembersByCuisineQuery.builder().build();
        return processQuery(query, membersByCuisineQueryHandler);
    }
}
