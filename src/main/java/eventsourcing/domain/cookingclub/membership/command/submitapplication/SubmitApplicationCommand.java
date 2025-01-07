package eventsourcing.domain.cookingclub.membership.command.submitapplication;

import eventsourcing.common.command.Command;
import lombok.*;

@Builder
@Getter
public class SubmitApplicationCommand extends Command {
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String favoriteCuisine;
    @NonNull private Integer yearsOfProfessionalExperience;
    @NonNull private Integer numberOfCookingBooksRead;
}
