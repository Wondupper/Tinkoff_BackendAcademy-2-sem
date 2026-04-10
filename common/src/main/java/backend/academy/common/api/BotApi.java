package backend.academy.common.api;

import backend.academy.common.dto.ApiErrorResponse;
import backend.academy.common.dto.LinkUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "BotApi", description = "API для работы с обновлениями")
public interface BotApi {

    @Operation(summary = "Отправить сообщения")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/updates")
    void sendUpdate(@RequestBody LinkUpdate update);
}
