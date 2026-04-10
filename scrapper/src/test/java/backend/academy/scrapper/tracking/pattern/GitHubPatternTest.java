package backend.academy.scrapper.tracking.pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GitHubPatternTest {
    private final GitHubPattern gitHubPattern = new GitHubPattern();

    @ParameterizedTest
    @MethodSource("provideValidUris")
    @DisplayName("matches() should return true for valid GitHub repository URI")
    void matches_ShouldReturnTrue_ForValidGitHubRepositoryURI(URI uri) {
        // Act & Assert
        assertTrue(gitHubPattern.matches(uri));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUris")
    @DisplayName("matches() should return false for invalid GitHub repository URI")
    void matches_ShouldReturnFalse_ForInvalidGitHubRepositoryURI(URI uri) {
        // Act & Assert
        assertFalse(gitHubPattern.matches(uri));
    }

    private static Stream<Arguments> provideValidUris() {
        return Stream.of(
                Arguments.of(URI.create("https://github.com/user/repo")),
                Arguments.of(URI.create("https://github.com/user/repo/")),
                Arguments.of(URI.create("https://github.com/user12/repo45")),
                Arguments.of(URI.create("https://github.com/User-123/rEpo-a_456")));
    }

    private static Stream<Arguments> provideInvalidUris() {
        return Stream.of(
                Arguments.of(URI.create("https://github.com")),
                Arguments.of(URI.create("https://github.com/user/")),
                Arguments.of(URI.create("https://github.com/user/repo/subpath")),
                Arguments.of(URI.create("https://github.com/user/repo?query=param")),
                Arguments.of(URI.create("https://github.com/user/repo#fragment")));
    }
}
