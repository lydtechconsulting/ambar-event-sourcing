package eventsourcing.common.util;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@RequiredArgsConstructor
public class MongoInitializerApi {
    private static final Logger log = LogManager.getLogger(MongoInitializerApi.class);

    private final MongoTemplate mongoTemplate;

    public void initialize() {
        log.info("Creating collections");
        mongoTemplate.createCollection("CookingClub_MembersByCuisine_MembershipApplication");
        mongoTemplate.createCollection("CookingClub_MembersByCuisine_Cuisine");
        mongoTemplate.createCollection("ProjectionIdempotency_ProjectedEvent");
        log.info("Created collections");

        log.info("Creating indexes");
        mongoTemplate.indexOps("CookingClub_MembersByCuisine_MembershipApplication")
                .ensureIndex(new Index().on("favoriteCuisine", org.springframework.data.domain.Sort.Direction.ASC));
        mongoTemplate.indexOps("ProjectionIdempotency_ProjectedEvent")
                .ensureIndex(new Index()
                        .on("eventId", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("projectionName", org.springframework.data.domain.Sort.Direction.ASC)
                        .unique()
                );
        log.info("Created indexes");
    }
}
