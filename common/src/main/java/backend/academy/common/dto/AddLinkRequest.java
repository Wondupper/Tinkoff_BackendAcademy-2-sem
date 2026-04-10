package backend.academy.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AddLinkRequest(
        @JsonProperty String link, @JsonProperty List<String> tags, @JsonProperty List<String> filters) {}
