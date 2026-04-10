package backend.academy.scrapper.tracking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackExchangeCommentInfo(
        @JsonProperty("creation_date") OffsetDateTime createdAt, @JsonProperty("owner") AuthorInfo author) {

    public record AuthorInfo(
            @JsonProperty("display_name") String displayName, @JsonProperty("link") String profileUrl) {}
}
