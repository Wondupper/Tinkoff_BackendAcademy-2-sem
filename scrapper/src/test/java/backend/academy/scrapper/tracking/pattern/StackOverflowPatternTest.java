package backend.academy.scrapper.tracking.pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StackOverflowPatternTest {
    private final StackOverflowPattern stackOverflowPattern = new StackOverflowPattern();

    @ParameterizedTest
    @MethodSource("provideValidUris")
    @DisplayName("matches() should return true for valid StackOverflow question URI")
    void matches_ShouldReturnTrue_ForValidStackOverflowQuestionURI(URI uri) {
        // Act & Assert
        assertTrue(stackOverflowPattern.matches(uri));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUris")
    @DisplayName("matches() should return false for invalid StackOverflow question URI")
    void matches_ShouldReturnFalse_ForInvalidStackOverflowQuestionURI(URI uri) {
        // Act & Assert
        assertFalse(stackOverflowPattern.matches(uri));
    }

    private static Stream<Arguments> provideValidUris() {
        return Stream.of(
                Arguments.of(URI.create("https://stackoverflow.com/questions/123456")),
                Arguments.of(URI.create("https://stackoverflow.com/questions/987654/what-is-java")),
                Arguments.of(URI.create("https://stackoverflow.com/questions/56789/how-to-use-regex")),
                Arguments.of(URI.create("https://stackoverflow.com/questions/42/regex-matching")));
    }

    private static Stream<Arguments> provideInvalidUris() {
        return Stream.of(
                Arguments.of(URI.create("https://stackoverflow.com/")),
                Arguments.of(URI.create("https://stackoverflow.com/questions/")),
                Arguments.of(URI.create("https://stackoverflow.com/questions/abc123")),
                Arguments.of(URI.create("https://stackoverflow.com/users/12345")),
                Arguments.of(URI.create("https://example.com/questions/123456")));
    }
}
