package backend.academy.scrapper.tracking;

import java.time.OffsetDateTime;

public record TrackingEvent(String title, String description, OffsetDateTime timestamp) {}
