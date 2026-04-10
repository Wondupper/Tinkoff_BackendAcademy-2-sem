package backend.academy.scrapper.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.pattern.LinkPattern;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {
    private static final Long CHAT_ID = 1L;
    private static final String URL = "https://example.com";
    private static final Long LINK_ID = 10L;
    private static final Long TRACKED_LINK_ID = 100L;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private TrackedLinkRepository trackedLinkRepository;

    @Mock
    private LinkPattern linkPattern;

    private LinkServiceImpl linkService;

    @BeforeEach
    void setUp() {
        linkService = new LinkServiceImpl(chatRepository, linkRepository, trackedLinkRepository, List.of(linkPattern));
    }

    @Test
    @DisplayName("getLinks() should return links when chat exists")
    void getLinks_ShouldReturnLinks_WhenChatExists() {
        Chat chat = createChat();
        Link link = createLink();
        TrackedLink trackedLink = createTrackedLink(chat, link, List.of("tag1"), List.of("filter1"));

        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));
        when(trackedLinkRepository.findByChatId(CHAT_ID)).thenReturn(Set.of(trackedLink));

        ListLinksResponse response = linkService.getLinks(CHAT_ID);

        assertThat(response.links()).hasSize(1).first().satisfies(linkResponse -> assertThat(linkResponse.url())
                .isEqualTo(URL));

        verify(chatRepository).findById(CHAT_ID);
        verify(trackedLinkRepository).findByChatId(CHAT_ID);
    }

    @Test
    @DisplayName("getLinks() should throw exception when chat does not exist")
    void getLinks_ShouldThrowException_WhenChatDoesNotExist() {
        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.getLinks(CHAT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat with id " + CHAT_ID + " not found");

        verify(chatRepository).findById(CHAT_ID);
        verifyNoInteractions(trackedLinkRepository);
    }

    @Test
    @DisplayName("addLink() should add a new link when it does not exist")
    void addLink_ShouldAddNewLink_WhenItDoesNotExist() {
        Chat chat = createChat();
        Link link = createLink();
        AddLinkRequest request = new AddLinkRequest(URL, List.of("tag1"), List.of("filter1"));
        TrackedLink trackedLink = createTrackedLink(chat, link, request.tags(), request.filters());

        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));
        when(linkRepository.findByUrl(URL)).thenReturn(Optional.empty());
        when(linkRepository.save(any())).thenReturn(link);
        when(trackedLinkRepository.save(any())).thenReturn(trackedLink);
        when(linkPattern.matches(any())).thenReturn(true);

        LinkResponse response = linkService.addLink(CHAT_ID, request);

        assertThat(response.url()).isEqualTo(URL);
        verify(chatRepository).findById(CHAT_ID);
        verify(linkRepository).findByUrl(URL);
        verify(linkRepository).save(any());
        verify(trackedLinkRepository).save(any());
    }

    @Test
    @DisplayName("addLink() should throw exception when chat does not exist")
    void addLink_ShouldThrowException_WhenChatDoesNotExist() {
        AddLinkRequest request = new AddLinkRequest(URL, List.of("tag1"), List.of("filter1"));

        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.addLink(CHAT_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat with id " + CHAT_ID + " not found");
    }

    @Test
    @DisplayName("addLink() should throw exception when link does not matches")
    void addLink_ShouldThrowException_WhenLinkDoesNotMatch() {
        Chat chat = createChat();
        AddLinkRequest request = new AddLinkRequest(URL, List.of("tag1"), List.of("filter1"));

        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));
        when(linkRepository.findByUrl(URL)).thenReturn(Optional.empty());
        when(linkPattern.matches(any())).thenReturn(false);

        assertThatThrownBy(() -> linkService.addLink(CHAT_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No supported service found for url " + URL);
    }

    @Test
    @DisplayName("removeLink() should remove the link when it exists")
    void removeLink_ShouldRemoveLink_WhenItExists() {
        Chat chat = createChat();
        Link link = createLink();
        RemoveLinkRequest request = new RemoveLinkRequest(URL);
        TrackedLink trackedLink = createTrackedLink(chat, link, List.of(), List.of());

        when(trackedLinkRepository.findByChatIdAndUrl(CHAT_ID, URL)).thenReturn(Optional.of(trackedLink));

        LinkResponse response = linkService.removeLink(CHAT_ID, request);

        assertThat(response.url()).isEqualTo(URL);
        verify(trackedLinkRepository).findByChatIdAndUrl(CHAT_ID, URL);
        verify(trackedLinkRepository).deleteByChatIdAndUrl(CHAT_ID, URL);
    }

    @Test
    @DisplayName("removeLink() should throw exception when link does not exist")
    void removeLink_ShouldThrowException_WhenLinkDoesNotExist() {
        RemoveLinkRequest request = new RemoveLinkRequest(URL);

        when(trackedLinkRepository.findByChatIdAndUrl(CHAT_ID, URL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.removeLink(CHAT_ID, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Link not found");

        verify(trackedLinkRepository).findByChatIdAndUrl(CHAT_ID, URL);
        verify(trackedLinkRepository, never()).deleteByChatIdAndUrl(anyLong(), anyString());
    }

    private Chat createChat() {
        return new Chat(CHAT_ID);
    }

    private Link createLink() {
        return new Link(LINK_ID, URL, SupportedServices.GITHUB_REPOSITORY, OffsetDateTime.now());
    }

    private TrackedLink createTrackedLink(Chat chat, Link link, List<String> tags, List<String> filters) {
        return new TrackedLink(TRACKED_LINK_ID, link, chat, tags, filters);
    }
}
