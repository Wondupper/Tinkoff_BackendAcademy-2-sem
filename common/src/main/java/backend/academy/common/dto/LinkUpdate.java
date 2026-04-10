package backend.academy.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LinkUpdate(
        @JsonProperty Long id,
        @JsonProperty String url,
        @JsonProperty String description,
        @JsonProperty List<Long> tgChatIds) {}
