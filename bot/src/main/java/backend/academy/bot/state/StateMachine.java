package backend.academy.bot.state;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.repository.StateRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class StateMachine {
    private final StateRepository stateRepository;
    private final LinkStateRepository linkStateRepository;
    private final Map<BotState, StateHandler> stateHandlers;

    public StateMachine(
            StateRepository stateRepository,
            LinkStateRepository linkStateRepository,
            List<StateHandler> stateHandlers) {
        this.stateRepository = stateRepository;
        this.linkStateRepository = linkStateRepository;
        this.stateHandlers =
                stateHandlers.stream().collect(Collectors.toMap(StateHandler::getState, Function.identity()));
    }

    public void handleStatefulInput(Long chatId, String input) {
        StateHandler stateHandler = stateHandlers.get(stateRepository.getState(chatId));

        setState(chatId, stateHandler.handle(chatId, input));
    }

    public BotState getState(Long chatId) {
        return stateRepository.getState(chatId);
    }

    public void setState(Long chatId, BotState state) {
        stateRepository.setState(chatId, state);
    }

    public void reset(Long chatId) {
        stateRepository.clearState(chatId);
        linkStateRepository.clearState(chatId);
    }
}
