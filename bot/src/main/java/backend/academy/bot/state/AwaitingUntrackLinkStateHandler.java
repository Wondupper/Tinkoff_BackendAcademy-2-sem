package backend.academy.bot.state;

import backend.academy.bot.model.BotState;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.common.dto.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwaitingUntrackLinkStateHandler implements StateHandler {
    private final TrackLinkService trackLinkService;

    @Override
    public BotState getState() {
        return BotState.AWAITING_UNTRACK_LINK;
    }

    @Override
    public BotState handle(Long chatId, String input) {
        log.info("Handling state {}", getState());

        trackLinkService.untrackLink(chatId, new RemoveLinkRequest(input));

        return BotState.IDLE;
    }
}
