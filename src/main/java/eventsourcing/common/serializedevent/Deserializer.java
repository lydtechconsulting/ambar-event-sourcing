package eventsourcing.common.serializedevent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eventsourcing.common.event.Event;
import eventsourcing.domain.cookingclub.membership.aggregate.MembershipStatus;
import eventsourcing.domain.cookingclub.membership.event.ApplicationEvaluated;
import eventsourcing.domain.cookingclub.membership.event.ApplicationSubmitted;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class Deserializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Event deserialize(SerializedEvent serializedEvent) {
        if ("CookingClub_Membership_ApplicationSubmitted".equals(serializedEvent.getEventName())) {
            return ApplicationSubmitted.builder()
                    .eventId(serializedEvent.getEventId())
                    .aggregateId(serializedEvent.getAggregateId())
                    .aggregateVersion(serializedEvent.getAggregateVersion())
                    .correlationId(serializedEvent.getCorrelationId())
                    .causationId(serializedEvent.getCausationId())
                    .recordedOn(toInstant(serializedEvent.getRecordedOn()))
                    .firstName(payloadString(serializedEvent.getJsonPayload(), "firstName"))
                    .lastName(payloadString(serializedEvent.getJsonPayload(), "lastName"))
                    .favoriteCuisine(payloadString(serializedEvent.getJsonPayload(), "favoriteCuisine"))
                    .yearsOfProfessionalExperience(payloadInt(serializedEvent.getJsonPayload(), "yearsOfProfessionalExperience"))
                    .numberOfCookingBooksRead(payloadInt(serializedEvent.getJsonPayload(), "numberOfCookingBooksRead"))
                    .build();
        } else if ("CookingClub_Membership_ApplicationEvaluated".equals(serializedEvent.getEventName())) {
            return ApplicationEvaluated.builder()
                    .eventId(serializedEvent.getEventId())
                    .aggregateId(serializedEvent.getAggregateId())
                    .aggregateVersion(serializedEvent.getAggregateVersion())
                    .correlationId(serializedEvent.getCorrelationId())
                    .causationId(serializedEvent.getCausationId())
                    .recordedOn(toInstant(serializedEvent.getRecordedOn()))
                    .evaluationOutcome(MembershipStatus.fromString(payloadString(serializedEvent.getJsonPayload(), "evaluationOutcome")))
                    .build();
        }
        throw new RuntimeException("Unknown event type: " + serializedEvent.getEventName());
    }

    private Instant toInstant(String recordedOn) {
        if (!recordedOn.endsWith(" UTC")) {
            throw new IllegalArgumentException("Invalid date format: " + recordedOn);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS z");
        return ZonedDateTime.parse(recordedOn, formatter).toInstant();
    }

    private String payloadString(String jsonString, String fieldName) {
        try {
            JsonNode node = objectMapper.readTree(jsonString);
            JsonNode value = node.get(fieldName);
            if (value == null) {
                throw new IllegalArgumentException("Required field " + fieldName + " is missing");
            }
            return value.asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read field: " + fieldName, e);
        }
    }

    private int payloadInt(String jsonString, String fieldName) {
        try {
            JsonNode node = objectMapper.readTree(jsonString);
            JsonNode value = node.get(fieldName);
            if (value == null) {
                throw new IllegalArgumentException("Required field " + fieldName + " is missing");
            }
            return value.asInt();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read field: " + fieldName, e);
        }
    }
}