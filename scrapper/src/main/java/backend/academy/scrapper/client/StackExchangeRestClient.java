package backend.academy.scrapper.client;

import backend.academy.scrapper.tracking.model.StackExchangeAnswersResponse;
import backend.academy.scrapper.tracking.model.StackExchangeCommentsResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface StackExchangeRestClient {

    /**
     * Retrieves answers for a specific question from the Stack Exchange API.
     *
     * @param questionId the ID of the question
     * @param site the site identifier (e.g., "stackoverflow")
     * @param fromDate the date and time to filter answers since (in Unix epoch time)
     * @return response containing the list of answers
     */
    @GetExchange("/questions/{questionId}/answers")
    StackExchangeAnswersResponse getAnswers(
            @PathVariable Long questionId, @RequestParam String site, @RequestParam("fromdate") Long fromDate);

    /**
     * Retrieves comments for a specific question from the Stack Exchange API.
     *
     * @param questionId the ID of the question
     * @param site the site identifier (e.g., "stackoverflow")
     * @param fromDate the date and time to filter comments since (in Unix epoch time)
     * @return response containing the list of comments
     */
    @GetExchange("/questions/{questionId}/comments")
    StackExchangeCommentsResponse getComments(
            @PathVariable Long questionId, @RequestParam String site, @RequestParam("fromdate") Long fromDate);
}
