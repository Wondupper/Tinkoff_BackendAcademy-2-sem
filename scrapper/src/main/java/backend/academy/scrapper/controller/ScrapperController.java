package backend.academy.scrapper.controller;

import backend.academy.common.api.ScrapperApi;
import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import backend.academy.scrapper.service.ChatService;
import backend.academy.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScrapperController implements ScrapperApi {
    private final ChatService chatService;
    private final LinkService linkService;

    @Override
    public void registerChat(Long id) {
        chatService.registerChat(id);
    }

    @Override
    public void deleteChat(Long id) {
        chatService.deleteChat(id);
    }

    @Override
    public ListLinksResponse getLinks(Long chatId) {
        return linkService.getLinks(chatId);
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        return linkService.addLink(chatId, request);
    }

    @Override
    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        return linkService.removeLink(chatId, request);
    }
}
