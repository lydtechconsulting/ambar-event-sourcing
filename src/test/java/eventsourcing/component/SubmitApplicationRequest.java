package eventsourcing.component;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubmitApplicationRequest {

    private String firstName;
    private String lastName;
    private String favoriteCuisine;
    private int yearsOfProfessionalExperience;
    private int numberOfCookingBooksRead;
}
