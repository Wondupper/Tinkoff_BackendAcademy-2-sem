package backend.academy.scrapper.job;

import backend.academy.common.dto.LinkUpdate;
import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.repository.TrackedLinkRepository;
import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.service.UpdateService;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import backend.academy.scrapper.tracking.supplier.TrackingSupplier;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
public class LinkUpdatesService {
    private final LinkRepository linkRepository;
    private final TrackedLinkRepository trackedLinkRepository;
    private final UpdateService updateService;
    private final Map<SupportedServices, TrackingSupplier> trackingSupplierMap;

    public LinkUpdatesService(
            LinkRepository linkRepository,
            UpdateService updateService,
            TrackedLinkRepository trackedLinkRepository,
            List<TrackingSupplier> trackingSuppliers) {
        this.linkRepository = linkRepository;
        this.updateService = updateService;
        this.trackedLinkRepository = trackedLinkRepository;
        this.trackingSupplierMap = trackingSuppliers.stream()
                .map(trackingSupplier -> Map.entry(trackingSupplier.getService(), trackingSupplier))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Scheduled(cron = "${scheduler.link-updates.cron}")
    public void updateLinks() {
        log.info("Updating links");
        linkRepository.findAll().forEach(this::updateLink);
    }

    private void updateLink(Link link) {
        log.info("Updating link {}", link);

        List<TrackingEvent> trackingEventList =
                trackingSupplierMap.get(link.service()).getEvents(link);

        if (trackingEventList.isEmpty()) {
            log.info("No updates available at the link {}", link);
            return;
        }

        List<Long> tgChatsIds = trackedLinkRepository.findByUrl(link.url()).stream()
                .map(trackedLink -> trackedLink.chat().id())
                .toList();

        LinkUpdate update =
                new LinkUpdate(link.id(), link.url(), buildLinkUpdateDescription(trackingEventList), tgChatsIds);

        log.debug("Send update {}", update);
        updateService.processUpdate(update);
        log.info("Update successfully processed for link: {}", link.url());

        link.lastUpdated(OffsetDateTime.now(ZoneId.systemDefault()));
        linkRepository.save(link);
    }

    private static String buildLinkUpdateDescription(List<TrackingEvent> trackingEvents) {
        return trackingEvents.stream()
                .map(event -> event.title() + System.lineSeparator() + event.description())
                .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));
    }
}
