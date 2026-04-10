package backend.academy.scrapper.service.impl;

import backend.academy.scrapper.exception.NotFoundException;
import backend.academy.scrapper.repository.ChatRepository;
import backend.academy.scrapper.repository.TrackedLinkRepository;
import backend.academy.scrapper.repository.entity.Chat;
import backend.academy.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final TrackedLinkRepository trackedLinkRepository;

    @Override
    public void registerChat(Long id) {
        log.info("Attempting to register chat with id: {}", id);

        if (chatRepository.findById(id).isEmpty()) {
            chatRepository.save(new Chat(id));
            log.info("Chat with id {} successfully registered", id);
        }
    }

    @Override
    public void deleteChat(Long id) {
        log.info("Attempting to delete chat with id: {}", id);

        if (chatRepository.findById(id).isEmpty()) {
            log.warn("Chat with id {} not found", id);
            throw new NotFoundException("Chat with id " + id + " not found");
        }

        trackedLinkRepository.deleteByChatId(id);

        chatRepository.deleteById(id);

        log.info("Chat with id {} successfully deleted", id);
    }
}
