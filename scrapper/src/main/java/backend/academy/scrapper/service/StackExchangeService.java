package backend.academy.scrapper.service;

import backend.academy.scrapper.tracking.model.StackExchangeAnswerInfo;
import backend.academy.scrapper.tracking.model.StackExchangeCommentInfo;
import java.util.List;

public interface StackExchangeService {

    /**
     * Retrieves answers for a specific question from the Stack Exchange API.
     *
     * @param questionId the ID of the question
     * @param site the site identifier (e.g., "stackoverflow")
     * @param fromDate the date and time to filter answers since (in Unix epoch time)
     * @return a list of answers
     */
    List<StackExchangeAnswerInfo> getAnswers(Long questionId, String site, Long fromDate);

    /**
     * Retrieves comments for a specific question from the Stack Exchange API.
     *
     * @param questionId the ID of the question
     * @param site the site identifier (e.g., "stackoverflow")
     * @param fromDate the date and time to filter comments since (in Unix epoch time)
     * @return a list of comments
     */
    List<StackExchangeCommentInfo> getComments(Long questionId, String site, Long fromDate);
}
