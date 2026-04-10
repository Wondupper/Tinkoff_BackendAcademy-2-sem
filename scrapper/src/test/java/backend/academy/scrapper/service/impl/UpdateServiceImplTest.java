package backend.academy.scrapper.service.impl;

import static org.mockito.Mockito.verify;

import backend.academy.common.dto.LinkUpdate;
import backend.academy.scrapper.client.BotRestClient;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateServiceImplTest {
    @Mock
    private BotRestClient botRestClient;

    @InjectMocks
    private UpdateServiceImpl updateService;

    @Test
    @DisplayName("processUpdate() should call sendUpdate()")
    void processUpdate_ShouldCallSendUpdate() {
        // Arrange
        LinkUpdate update = new LinkUpdate(1L, "url", "description", List.of(1L, 2L));

        // Act
        updateService.processUpdate(update);

        // Assert
        verify(botRestClient).sendUpdate(update);
    }
}
