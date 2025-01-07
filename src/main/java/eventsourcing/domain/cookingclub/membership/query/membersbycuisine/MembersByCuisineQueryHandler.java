package eventsourcing.domain.cookingclub.membership.query.membersbycuisine;

import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import eventsourcing.common.query.Query;
import eventsourcing.common.query.QueryHandler;
import eventsourcing.domain.cookingclub.membership.projection.membersbycuisine.CuisineRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class MembersByCuisineQueryHandler extends QueryHandler {
    private final CuisineRepository cuisineRepository;

    public MembersByCuisineQueryHandler(
            MongoTransactionalProjectionOperator mongoTransactionalProjectionOperator,
            CuisineRepository cuisineRepository
    ) {
        super(mongoTransactionalProjectionOperator);
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public Object handleQuery(Query query) {
        if (query instanceof MembersByCuisineQuery) {
            return cuisineRepository.findAll();
        }

        throw new IllegalArgumentException("Unsupported query type: " + query.getClass().getName());
    }
}