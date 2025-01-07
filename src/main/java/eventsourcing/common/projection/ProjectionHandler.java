package eventsourcing.common.projection;

import eventsourcing.common.event.Event;

public abstract class ProjectionHandler {
    public abstract void project(final Event event);
}
