package backend.academy.scrapper.repository.entity;

import java.util.List;
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
public class TrackedLink {
    private Long id;
    private Link link;
    private Chat chat;
    private List<String> tags;
    private List<String> filters;
}
