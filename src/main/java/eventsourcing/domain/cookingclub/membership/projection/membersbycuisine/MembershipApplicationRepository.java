package eventsourcing.domain.cookingclub.membership.projection.membersbycuisine;

import eventsourcing.common.projection.MongoTransactionalProjectionOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@RequestScope
@Service
@RequiredArgsConstructor
public class MembershipApplicationRepository {
    private final MongoTransactionalProjectionOperator mongoTransactionalProjectionOperator;
    private static final String COLLECTION_NAME = "CookingClub_MembersByCuisine_MembershipApplication";

    public void save(final MembershipApplication membershipApplication) {
        mongoTransactionalProjectionOperator.operate().save(membershipApplication, COLLECTION_NAME);
    }

    public Optional<MembershipApplication> findOneById(final String id) {
        return Optional.ofNullable(mongoTransactionalProjectionOperator.operate().findOne(
                Query.query(Criteria.where("id").is(id)),
                MembershipApplication.class,
                COLLECTION_NAME
        ));
    }
}