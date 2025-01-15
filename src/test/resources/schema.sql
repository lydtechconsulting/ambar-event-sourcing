CREATE TABLE public.event_store (
    id SERIAL PRIMARY KEY,
    event_id TEXT NOT NULL,
    event_name TEXT NOT NULL,
    aggregate_id TEXT NOT NULL,
    aggregate_version BIGINT NOT NULL,
    json_payload TEXT NOT NULL,
    json_metadata TEXT NOT NULL,
    recorded_on TEXT NOT NULL,
    causation_id TEXT NOT NULL,
    correlation_id TEXT NOT NULL
);
