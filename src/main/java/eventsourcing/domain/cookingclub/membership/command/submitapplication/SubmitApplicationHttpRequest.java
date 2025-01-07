package eventsourcing.domain.cookingclub.membership.command.submitapplication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
public class SubmitApplicationHttpRequest {
    @NotNull private String firstName;
    @NotNull private String lastName;
    @NotNull private String favoriteCuisine;
    @PositiveOrZero private int yearsOfProfessionalExperience;
    @PositiveOrZero private int numberOfCookingBooksRead;
}