package backend.academy.scrapper.client;

import static backend.academy.scrapper.constants.DateTimeConstants.DATE_TIME_FORMAT;

import backend.academy.scrapper.tracking.model.GitHubCommitInfo;
import backend.academy.scrapper.tracking.model.GitHubIssueCommentInfo;
import backend.academy.scrapper.tracking.model.GitHubIssueInfo;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface GitHubRestClient {

    /**
     * Retrieves a list of issues from a GitHub repository since a specified date and time.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param since the date and time to filter issues since
     * @return list of GitHub issues
     */
    @GetExchange("/repos/{owner}/{repo}/issues")
    List<GitHubIssueInfo> getIssues(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) OffsetDateTime since);

    /**
     * Retrieves a list of issue comments from a GitHub repository since a specified date and time.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param since the date and time to filter issue comments since
     * @return list of GitHub issue comments
     */
    @GetExchange("/repos/{owner}/{repo}/issues/comments")
    List<GitHubIssueCommentInfo> getIssueComments(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) OffsetDateTime since);

    /**
     * Retrieves a list of commits from a GitHub repository since a specified date and time.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param since the date and time to filter commits since
     * @return list of GitHub commits
     */
    @GetExchange("/repos/{owner}/{repo}/commits")
    List<GitHubCommitInfo> getCommits(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) OffsetDateTime since);
}
