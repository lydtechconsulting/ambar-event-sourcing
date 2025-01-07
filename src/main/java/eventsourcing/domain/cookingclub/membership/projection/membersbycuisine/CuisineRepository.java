package eventsourcing.domain.cookingclub.membership.projection.membersbycuisine;

import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Optional;

@RequestScope
@Service
@RequiredArgsConstructor
public class CuisineRepository {
    private final MongoTransactionalProjectionOperator mongoTransactionalProjectionOperator;
    private static final String COLLECTION_NAME = "CookingClub_MembersByCuisine_Cuisine";

    public void save(final Cuisine cuisine) {
        mongoTransactionalProjectionOperator.operate().save(cuisine, COLLECTION_NAME);
    }

    public Optional<Cuisine> findOneById(final String id) {
        return Optional.ofNullable(mongoTransactionalProjectionOperator.operate().findOne(
                Query.query(Criteria.where("id").is(id)),
                Cuisine.class,
                COLLECTION_NAME
        ));
    }

    public List<Cuisine> findAll() {
        return mongoTransactionalProjectionOperator.operate().find(
                new Query(),
                Cuisine.class,
                COLLECTION_NAME
        );
    }
}