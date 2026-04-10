package backend.academy.scrapper.tracking.pattern;

import backend.academy.scrapper.tracking.SupportedServices;
import java.net.URI;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowPattern implements LinkPattern {
    public static final Pattern PATTERN =
            Pattern.compile("https://stackoverflow\\.com/questions/(?<questionId>\\d+)(/.*)?");

    @Override
    public SupportedServices getService() {
        return SupportedServices.STACKOVERFLOW_QUESTION;
    }

    @Override
    public boolean matches(URI uri) {
        return PATTERN.matcher(uri.toString()).matches();
    }
}
