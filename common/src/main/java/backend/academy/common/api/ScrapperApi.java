package backend.academy.common.api;

import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.ApiErrorResponse;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Scrapper API", description = "API для работы с чатами и ссылками")
public interface ScrapperApi {
    @Operation(summary = "Зарегистрировать чат")
    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/tg-chat/{id}")
    void registerChat(@Parameter(description = "ID чата") @PathVariable Long id);

    @Operation(summary = "Удалить чат")
    @ApiResponse(responseCode = "200", description = "Чат успешно удалён")
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping("/tg-chat/{id}")
    void deleteChat(@Parameter(description = "ID чата") @PathVariable Long id);

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены",
            content = @Content(schema = @Schema(implementation = ListLinksResponse.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping("/links")
    ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") Long chatId);

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно добавлена",
            content = @Content(schema = @Schema(implementation = LinkResponse.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/links")
    LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody AddLinkRequest request);

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно убрана",
            content = @Content(schema = @Schema(implementation = LinkResponse.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Ссылка не найдена",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping("/links")
    LinkResponse removeLink(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody RemoveLinkRequest request);
}
