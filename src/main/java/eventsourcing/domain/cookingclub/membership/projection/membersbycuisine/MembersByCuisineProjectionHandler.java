package eventsourcing.domain.cookingclub.membership.projection.membersbycuisine;

import eventsourcing.common.event.Event;
import eventsourcing.common.projection.ProjectionHandler;
import eventsourcing.domain.cookingclub.membership.aggregate.MembershipStatus;
import eventsourcing.domain.cookingclub.membership.event.ApplicationEvaluated;
import eventsourcing.domain.cookingclub.membership.event.ApplicationSubmitted;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;

@Service
@RequestScope
@RequiredArgsConstructor
public class MembersByCuisineProjectionHandler extends ProjectionHandler {
    private final CuisineRepository cuisineRepository;
    private final MembershipApplicationRepository membershipApplicationRepository;

    @Override
    public void project(Event event) {
        if (event instanceof ApplicationSubmitted applicationSubmitted) {
            membershipApplicationRepository.save(MembershipApplication.builder()
                    .id(applicationSubmitted.getAggregateId())
                    .firstName(applicationSubmitted.getFirstName())
                    .lastName(applicationSubmitted.getLastName())
                    .favoriteCuisine(applicationSubmitted.getFavoriteCuisine())
                    .build());
        } else if (event instanceof ApplicationEvaluated applicationEvaluated) {
            if (applicationEvaluated.getEvaluationOutcome() == MembershipStatus.Approved) {
                MembershipApplication membershipApplication = membershipApplicationRepository
                        .findOneById(applicationEvaluated.getAggregateId())
                        .orElseThrow(() -> new RuntimeException("Membership application not found"));

                Cuisine cuisine = cuisineRepository
                        .findOneById(membershipApplication.getFavoriteCuisine())
                        .orElse(Cuisine.builder()
                                .id(membershipApplication.getFavoriteCuisine())
                                .memberNames(new ArrayList<>())
                                .build());

                cuisine.getMemberNames().add(
                        String.format("%s %s",
                                membershipApplication.getFirstName(),
                                membershipApplication.getLastName()));

                cuisineRepository.save(cuisine);
            }
        }
    }
}