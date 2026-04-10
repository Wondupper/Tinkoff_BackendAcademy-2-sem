package backend.academy.scrapper.service.impl;

import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import backend.academy.scrapper.exception.NotFoundException;
import backend.academy.scrapper.repository.ChatRepository;
import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.repository.TrackedLinkRepository;
import backend.academy.scrapper.repository.entity.Chat;
import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.repository.entity.TrackedLink;
import backend.academy.scrapper.service.LinkService;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.pattern.LinkPattern;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final TrackedLinkRepository trackedLinkRepository;
    private final List<LinkPattern> patterns;

    @Override
    public ListLinksResponse getLinks(Long chatId) {
        log.info("Fetching links for chat with id: {}", chatId);

        if (chatRepository.findById(chatId).isEmpty()) {
            log.warn("Chat with id {} not found", chatId);
            throw new IllegalArgumentException("Chat with id " + chatId + " not found");
        }

        List<LinkResponse> links = trackedLinkRepository.findByChatId(chatId).stream()
                .map(this::toLinkResponse)
                .toList();

        log.info("Found {} links for chat with id {}", links.size(), chatId);
        return new ListLinksResponse(links, links.size());
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        log.info("Adding link '{}' to chat with id {}", request.link(), chatId);

        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with id " + chatId + " not found"));

        Link link = linkRepository.findByUrl(request.link()).orElseGet(() -> saveLink(request));

        TrackedLink trackedLink = TrackedLink.builder()
                .link(link)
                .chat(chat)
                .tags(request.tags())
                .filters(request.filters())
                .build();

        log.info("Successfully added link '{}' to chat with id {}", request.link(), chatId);
        return toLinkResponse(trackedLinkRepository.save(trackedLink));
    }

    @Override
    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        log.info("Removing link '{}' from chat with id {}", request.link(), chatId);

        TrackedLink trackedLink = trackedLinkRepository
                .findByChatIdAndUrl(chatId, request.link())
                .orElseThrow(() -> new NotFoundException("Link not found"));

        trackedLinkRepository.deleteByChatIdAndUrl(chatId, request.link());

        log.info("Successfully removed link '{}' from chat with id {}", request.link(), chatId);

        return toLinkResponse(trackedLink);
    }

    private Link saveLink(AddLinkRequest request) {
        Link newLink = Link.builder()
                .url(request.link())
                .service(getSupportedService(URI.create(request.link())))
                .lastUpdated(OffsetDateTime.now(ZoneId.systemDefault()))
                .build();

        return linkRepository.save(newLink);
    }

    private SupportedServices getSupportedService(URI url) {
        for (LinkPattern pattern : patterns) {
            if (pattern.matches(url)) {
                return pattern.getService();
            }
        }

        log.error("No supported service found for url {}", url);
        throw new IllegalArgumentException("No supported service found for url " + url);
    }

    private LinkResponse toLinkResponse(TrackedLink trackedLink) {
        return new LinkResponse(
                trackedLink.link().id(), trackedLink.link().url(), trackedLink.tags(), trackedLink.filters());
    }
}
