package backend.academy.scrapper.tracking.pattern;

import backend.academy.scrapper.tracking.SupportedServices;
import java.net.URI;

public interface LinkPattern {

    /**
     * Returns the service associated with this link pattern.
     *
     * @return the supported service
     */
    SupportedServices getService();

    /**
     * Checks if the given URI matches this link pattern.
     *
     * @param uri the URI to be checked
     * @return true if the URI matches the pattern, false otherwise
     */
    boolean matches(URI uri);
}
