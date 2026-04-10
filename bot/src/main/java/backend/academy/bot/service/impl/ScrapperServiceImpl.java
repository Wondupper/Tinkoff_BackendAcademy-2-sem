package backend.academy.bot.service.impl;

import backend.academy.bot.client.ScrapperClient;
import backend.academy.bot.service.ScrapperService;
import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperServiceImpl implements ScrapperService {
    private final ScrapperClient scrapperClient;

    @Override
    public void registerChat(Long chatId) {
        log.info("Registering chat {}", chatId);
        scrapperClient.registerChat(chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        log.info("Deleting chat {}", chatId);
        scrapperClient.deleteChat(chatId);
    }

    @Override
    public List<LinkResponse> getLinks(Long chatId) {
        log.info("Getting links for {}", chatId);
        return scrapperClient.getLinks(chatId).links();
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        log.info("Adding link {} into chat {}", addLinkRequest, chatId);
        return scrapperClient.addLink(chatId, addLinkRequest);
    }

    @Override
    public LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Removing link {} from {}", removeLinkRequest, chatId);
        return scrapperClient.removeLink(chatId, removeLinkRequest);
    }
}
