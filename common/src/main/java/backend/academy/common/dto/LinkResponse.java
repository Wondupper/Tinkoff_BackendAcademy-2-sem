package backend.academy.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LinkResponse(
        @JsonProperty Long id,
        @JsonProperty String url,
        @JsonProperty List<String> tags,
        @JsonProperty List<String> filters) {}
