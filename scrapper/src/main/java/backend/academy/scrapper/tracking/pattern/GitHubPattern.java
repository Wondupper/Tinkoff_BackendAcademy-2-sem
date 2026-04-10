package backend.academy.scrapper.tracking.pattern;

import backend.academy.scrapper.tracking.SupportedServices;
import java.net.URI;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GitHubPattern implements LinkPattern {
    public static final Pattern PATTERN =
            Pattern.compile("https://github\\.com/(?<owner>[a-zA-Z0-9_-]+)/(?<repo>[a-zA-Z0-9_-]+)/?");

    @Override
    public SupportedServices getService() {
        return SupportedServices.GITHUB_REPOSITORY;
    }

    @Override
    public boolean matches(URI uri) {
        return PATTERN.matcher(uri.toString()).matches();
    }
}
