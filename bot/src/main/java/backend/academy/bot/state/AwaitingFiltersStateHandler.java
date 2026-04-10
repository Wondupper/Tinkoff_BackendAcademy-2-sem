package backend.academy.bot.state;

import static backend.academy.bot.constants.RegexpConstants.WHITESPACE_PATTERN;
import static backend.academy.bot.constants.StateHandlerConstants.SKIP_INPUT;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.common.dto.AddLinkRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwaitingFiltersStateHandler implements StateHandler {
    private final LinkStateRepository linkStateRepository;
    private final TrackLinkService trackLinkService;

    @Override
    public BotState getState() {
        return BotState.AWAITING_FILTERS;
    }

    @Override
    public BotState handle(Long chatId, String input) {
        log.info("Handling state {}", getState());

        if (!SKIP_INPUT.equalsIgnoreCase(input)) {
            linkStateRepository.setFilters(chatId, Arrays.asList(input.split(WHITESPACE_PATTERN)));
        }

        AddLinkRequest request = new AddLinkRequest(
                linkStateRepository.getTrackingLink(chatId),
                linkStateRepository.getTags(chatId),
                linkStateRepository.getFilters(chatId));

        trackLinkService.trackLink(chatId, request);

        linkStateRepository.clearState(chatId);

        return BotState.IDLE;
    }
}
