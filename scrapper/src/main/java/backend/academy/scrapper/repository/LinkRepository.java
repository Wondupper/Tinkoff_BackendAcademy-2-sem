package backend.academy.scrapper.repository;

import backend.academy.scrapper.repository.entity.Link;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    /**
     * Saves a link entity to the repository.
     *
     * @param link the link entity to be saved
     * @return the saved link entity
     */
    Link save(Link link);

    /**
     * Retrieves all link entities from the repository.
     *
     * @return a list of all link entities
     */
    List<Link> findAll();

    /**
     * Finds a link entity by its URL.
     *
     * @param url the URL of the link
     * @return an Optional containing the link entity if found, or an empty Optional if not found
     */
    Optional<Link> findByUrl(String url);
}
