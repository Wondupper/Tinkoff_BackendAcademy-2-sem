package backend.academy.bot.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryStateRepositoryTest {

    private StateRepository stateRepository;

    @BeforeEach
    void setUp() {
        stateRepository = new InMemoryStateRepository();
    }

    @Test
    @DisplayName("setState() should set the state for a chatId")
    void setState_ShouldSetState_ForChatId() {
        // Arrange
        Long chatId = 1L;
        BotState state = BotState.IDLE;

        // Act
        stateRepository.setState(chatId, state);

        // Assert
        assertEquals(state, stateRepository.getState(chatId));
    }

    @Test
    @DisplayName("getState() should return the default state if chatId is not found")
    void getState_ShouldReturnDefaultState_IfChatIdNotFound() {
        // Arrange
        Long chatId = 1L;

        // Act & Assert
        assertEquals(BotState.IDLE, stateRepository.getState(chatId));
    }

    @Test
    @DisplayName("getState() should return the correct state if chatId is found")
    void getState_ShouldReturnCorrectState_IfChatIdFound() {
        // Arrange
        Long chatId = 1L;
        BotState state = BotState.AWAITING_TAGS;
        stateRepository.setState(chatId, state);

        // Act & Assert
        assertEquals(state, stateRepository.getState(chatId));
    }

    @Test
    @DisplayName("clearState() should clear the state for a chatId")
    void clearState_ShouldClearState_ForChatId() {
        // Arrange
        Long chatId = 1L;
        BotState state = BotState.AWAITING_FILTERS;
        stateRepository.setState(chatId, state);

        // Act
        stateRepository.clearState(chatId);

        // Assert
        assertEquals(BotState.IDLE, stateRepository.getState(chatId));
    }

    @Test
    @DisplayName("clearState() should do nothing if chatId is not found")
    void clearState_ShouldDoNothing_IfChatIdNotFound() {
        // Arrange
        Long chatId = 1L;

        // Act
        stateRepository.clearState(chatId);

        // Assert
        assertEquals(BotState.IDLE, stateRepository.getState(chatId));
    }
}
