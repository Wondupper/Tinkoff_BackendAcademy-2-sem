package backend.academy.bot.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.repository.StateRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StateMachineTest {

    @Mock
    private StateRepository stateRepository;

    @Mock
    private LinkStateRepository linkStateRepository;

    @Mock
    private StateHandler stateHandler1;

    @Mock
    private StateHandler stateHandler2;

    private StateMachine stateMachine;

    private final Long chatId = 1L;
    private final String input = "test input";
    private final BotState initialState = BotState.AWAITING_TRACK_LINK;
    private final BotState nextState = BotState.AWAITING_TAGS;

    @BeforeEach
    void setUp() {
        when(stateHandler1.getState()).thenReturn(initialState);
        when(stateHandler2.getState()).thenReturn(nextState);

        stateMachine = new StateMachine(stateRepository, linkStateRepository, List.of(stateHandler1, stateHandler2));
    }

    @Test
    @DisplayName("handleStatefulInput() should call the correct StateHandler and update the state")
    void handleStatefulInput_ShouldCallCorrectStateHandlerAndUpdateState() {
        // Arrange
        when(stateRepository.getState(chatId)).thenReturn(initialState);
        when(stateHandler1.handle(chatId, input)).thenReturn(nextState);

        // Act
        stateMachine.handleStatefulInput(chatId, input);

        // Assert
        verify(stateHandler1).handle(chatId, input);
        verify(stateRepository).setState(chatId, nextState);
    }

    @Test
    @DisplayName("getState() should return the current state")
    void getState_ShouldReturnTheCurrentState() {
        // Arrange
        when(stateRepository.getState(chatId)).thenReturn(initialState);

        // Act
        BotState state = stateMachine.getState(chatId);

        // Assert
        assertEquals(initialState, state);
        verify(stateRepository).getState(chatId);
    }

    @Test
    @DisplayName("setState() should update the state in StateRepository")
    void setState_ShouldUpdateStateInStateRepository() {
        // Act
        stateMachine.setState(chatId, nextState);

        // Assert
        verify(stateRepository).setState(chatId, nextState);
    }

    @Test
    @DisplayName("reset() should clear the state in StateRepository and LinkStateRepository")
    void reset_ShouldClearStateInStateRepositoryAndLinkStateRepository() {
        // Act
        stateMachine.reset(chatId);

        // Assert
        verify(stateRepository).clearState(chatId);
        verify(linkStateRepository).clearState(chatId);
    }
}
