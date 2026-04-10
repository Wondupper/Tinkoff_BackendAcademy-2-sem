package backend.academy.scrapper.tracking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackExchangeCommentsResponse(@JsonProperty List<StackExchangeCommentInfo> items) {}
