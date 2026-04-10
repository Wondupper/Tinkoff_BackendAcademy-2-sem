package backend.academy.scrapper.repository.entity;

import backend.academy.scrapper.tracking.SupportedServices;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Link {
    private Long id;
    private String url;
    private SupportedServices service;
    private OffsetDateTime lastUpdated;
}
