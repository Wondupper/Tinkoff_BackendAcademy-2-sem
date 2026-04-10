package backend.academy.scrapper.tracking.supplier;

import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import java.util.List;

public interface TrackingSupplier {

    /**
     * Retrieves tracking events for the specified link.
     *
     * @param link the link for which to retrieve tracking events
     * @return a list of tracking events
     */
    List<TrackingEvent> getEvents(Link link);

    /**
     * Returns the service associated with this tracking supplier.
     *
     * @return the supported service
     */
    SupportedServices getService();
}
