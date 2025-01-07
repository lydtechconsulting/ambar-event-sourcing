package eventsourcing.domain.cookingclub.membership.aggregate;

public enum MembershipStatus {
    Requested,
    Approved,
    Rejected;

    public static MembershipStatus fromString(String status) {
        try {
            return MembershipStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid MembershipStatus: " + status);
        }
    }
}