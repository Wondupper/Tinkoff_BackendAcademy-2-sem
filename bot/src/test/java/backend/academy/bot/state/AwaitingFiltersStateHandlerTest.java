package backend.academy.bot.state;

import static backend.academy.bot.constants.StateHandlerConstants.SKIP_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.common.dto.AddLinkRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AwaitingFiltersStateHandlerTest {

    @Mock
    private LinkStateRepository linkStateRepository;

    @Mock
    private TrackLinkService trackLinkService;

    @InjectMocks
    private AwaitingFiltersStateHandler awaitingFiltersStateHandler;

    private final Long chatId = 1L;
    private final String input = "filter1 filter2";
    private final String trackingLink = "https://example.com";
    private final List<String> tags = List.of("tag1", "tag2");
    private final List<String> filters = List.of("filter1", "filter2");

    @BeforeEach
    void setUp() {
        when(linkStateRepository.getTrackingLink(chatId)).thenReturn(trackingLink);
        when(linkStateRepository.getTags(chatId)).thenReturn(tags);
    }

    @Test
    @DisplayName("handle() should set filters, create AddLinkRequest, call trackLinkService, and clear state")
    void handle_ShouldSetFilters_CreateAddLinkRequest_CallTrackLinkService_AndClearState() {
        // Arrange
        AddLinkRequest expectedRequest = new AddLinkRequest(trackingLink, tags, filters);
        when(linkStateRepository.getTrackingLink(chatId)).thenReturn(expectedRequest.link());
        when(linkStateRepository.getTags(chatId)).thenReturn(expectedRequest.tags());
        when(linkStateRepository.getFilters(chatId)).thenReturn(expectedRequest.filters());

        // Act
        BotState result = awaitingFiltersStateHandler.handle(chatId, input);

        // Assert
        verify(linkStateRepository).setFilters(chatId, filters);
        verify(trackLinkService).trackLink(chatId, expectedRequest);
        verify(linkStateRepository).clearState(chatId);
        assertEquals(BotState.IDLE, result);
    }

    @Test
    @DisplayName("handle() should skip setting filters if input is SKIP_INPUT")
    void handle_ShouldSkipSettingFilters_IfInputIsSKIP_INPUT() {
        // Arrange
        AddLinkRequest expectedRequest = new AddLinkRequest(trackingLink, tags, List.of());
        when(linkStateRepository.getTrackingLink(chatId)).thenReturn(expectedRequest.link());
        when(linkStateRepository.getTags(chatId)).thenReturn(expectedRequest.tags());

        // Act
        BotState result = awaitingFiltersStateHandler.handle(chatId, SKIP_INPUT);

        // Assert
        verify(linkStateRepository, never()).setFilters(eq(chatId), anyList());
        verify(trackLinkService).trackLink(chatId, expectedRequest);
        verify(linkStateRepository).clearState(chatId);
        assertEquals(BotState.IDLE, result);
    }

    @Test
    @DisplayName("handle() should handle multiple filters correctly")
    void handle_ShouldHandleMultipleFiltersCorrectly() {
        // Arrange
        String multipleFiltersInput = "filter1 filter2 filter3";
        List<String> multipleFilters = List.of("filter1", "filter2", "filter3");
        AddLinkRequest expectedRequest = new AddLinkRequest(trackingLink, tags, multipleFilters);
        when(linkStateRepository.getTrackingLink(chatId)).thenReturn(expectedRequest.link());
        when(linkStateRepository.getTags(chatId)).thenReturn(expectedRequest.tags());
        when(linkStateRepository.getFilters(chatId)).thenReturn(expectedRequest.filters());

        // Act
        BotState result = awaitingFiltersStateHandler.handle(chatId, multipleFiltersInput);

        // Assert
        verify(linkStateRepository).setFilters(chatId, multipleFilters);
        verify(trackLinkService).trackLink(chatId, expectedRequest);
        verify(linkStateRepository).clearState(chatId);
        assertEquals(BotState.IDLE, result);
    }

    @Test
    @DisplayName("handle() should handle single filter correctly")
    void handle_ShouldHandleSingleFilterCorrectly() {
        // Arrange
        String singleFilterInput = "filter1";
        List<String> singleFilter = List.of("filter1");
        AddLinkRequest expectedRequest = new AddLinkRequest(trackingLink, tags, singleFilter);
        when(linkStateRepository.getTrackingLink(chatId)).thenReturn(expectedRequest.link());
        when(linkStateRepository.getTags(chatId)).thenReturn(expectedRequest.tags());
        when(linkStateRepository.getFilters(chatId)).thenReturn(expectedRequest.filters());

        // Act
        BotState result = awaitingFiltersStateHandler.handle(chatId, singleFilterInput);

        // Assert
        verify(linkStateRepository).setFilters(chatId, singleFilter);
        verify(trackLinkService).trackLink(chatId, expectedRequest);
        verify(linkStateRepository).clearState(chatId);
        assertEquals(BotState.IDLE, result);
    }
}
