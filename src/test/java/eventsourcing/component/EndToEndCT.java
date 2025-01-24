package eventsourcing.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import dev.lydtech.component.framework.client.service.ServiceClient;
import dev.lydtech.component.framework.extension.ComponentTestExtension;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static wiremock.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
@ExtendWith(ComponentTestExtension.class)
@ActiveProfiles("component-test")
public class EndToEndCT {

    @BeforeEach
    public void setup() {
        String serviceBaseUrl = ServiceClient.getInstance().getBaseUrl();
        RestAssured.baseURI = serviceBaseUrl;
    }

    /**
     * Sends in two applications.
     *
     * The first should be rejected, so does not end up in the materialised view.
     *
     * The second should be approved, so does end up in the materialised view and can be queried.
     */
    @Test
    public void testFlow() {

        // Submit an application that should be rejected.
        String firstNameToReject = "Steve";
        String lastNameToReject = randomAlphabetic(1).toUpperCase()+randomAlphabetic(5).toLowerCase();
        String cuisineTypeToReject = randomAlphabetic(1).toUpperCase()+randomAlphabetic(3).toLowerCase() + " Pie";
        SubmitApplicationRequest requestToReject = SubmitApplicationRequest.builder()
                .firstName(firstNameToReject)
                .lastName(lastNameToReject)
                .favoriteCuisine(cuisineTypeToReject)
                .numberOfCookingBooksRead(5)
                .yearsOfProfessionalExperience(2)
                .build();
        sendSubmitApplicationRequest(requestToReject);

        // Submit an application that should be approved.
        String firstNameToApprove = "John";
        String lastNameToApprove = randomAlphabetic(1).toUpperCase()+randomAlphabetic(5).toLowerCase();
        String cuisineTypeToApprove = randomAlphabetic(1).toUpperCase()+randomAlphabetic(3).toLowerCase() + " Curry";
        SubmitApplicationRequest requestToApprove = SubmitApplicationRequest.builder()
                .firstName(firstNameToApprove)
                .lastName(lastNameToApprove)
                .favoriteCuisine(cuisineTypeToApprove)
                .numberOfCookingBooksRead(3)
                .yearsOfProfessionalExperience(0)
                .build();
        sendSubmitApplicationRequest(requestToApprove);

        // Lookup the members from the materialised view and assert the expected application is approved.
        await()
            .atMost(5, TimeUnit.SECONDS)
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                Response response = getMembersByCuisine();

                List<Object> membersByCuisine = response.jsonPath().getList("$");
                assertThat(membersByCuisine, notNullValue());
                assertThat(membersByCuisine, hasSize(greaterThan(0)));

                Optional<LinkedHashMap<String, List<String>>> expectedCuisineType = membersByCuisine.stream()
                        .filter(item -> item instanceof LinkedHashMap)
                        .map(item -> (LinkedHashMap<String, List<String>>) item)
                        .filter(cuisineEntry -> cuisineTypeToApprove.equals(cuisineEntry.get("id")))
                        .findFirst();

                assertThat(expectedCuisineType.isPresent(), is(true));

                expectedCuisineType.ifPresent(cuisineEntry -> {
                    List<String> memberNames = cuisineEntry.get("memberNames");
                    assertThat(memberNames, hasItem(firstNameToApprove + " " + lastNameToApprove));
            });
        });

        // Assert the expected application is rejected.
        Response response2 = getMembersByCuisine();
        List<Object> membersByCuisine = response2.jsonPath().getList("$");
        Optional<LinkedHashMap<String, Object>> expectedCuisineType = membersByCuisine.stream()
                .filter(item -> item instanceof LinkedHashMap)
                .map(item -> (LinkedHashMap<String, Object>) item)
                .filter(cuisineEntry -> cuisineTypeToReject.equals(cuisineEntry.get("id")))
                .findFirst();
        assertThat(expectedCuisineType.isPresent(), is(false));
    }

    private static void sendSubmitApplicationRequest(SubmitApplicationRequest request) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post("/api/v1/cooking-club/membership/command/submit-application")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    private static Response getMembersByCuisine() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .post("/api/v1/cooking-club/membership/query/members-by-cuisine")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
    }
}
