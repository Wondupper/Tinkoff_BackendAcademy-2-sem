package backend.academy.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListLinksResponse(@JsonProperty List<LinkResponse> links, @JsonProperty Integer size) {}
