package backend.academy.scrapper.service.impl;

import backend.academy.scrapper.client.StackExchangeRestClient;
import backend.academy.scrapper.service.StackExchangeService;
import backend.academy.scrapper.tracking.model.StackExchangeAnswerInfo;
import backend.academy.scrapper.tracking.model.StackExchangeCommentInfo;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackExchangeServiceImpl implements StackExchangeService {
    private final StackExchangeRestClient stackExchangeRestClient;

    @Override
    public List<StackExchangeAnswerInfo> getAnswers(Long questionId, String site, Long fromDate) {
        return Optional.ofNullable(stackExchangeRestClient
                        .getAnswers(questionId, site, fromDate)
                        .items())
                .orElse(List.of());
    }

    @Override
    public List<StackExchangeCommentInfo> getComments(Long questionId, String site, Long fromDate) {
        return Optional.ofNullable(stackExchangeRestClient
                        .getComments(questionId, site, fromDate)
                        .items())
                .orElse(List.of());
    }
}
