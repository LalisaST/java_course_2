package edu.java.repositories.interfaces;

import edu.java.model.scheme.Link;
import edu.java.model.scheme.Type;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkDao {
    Link add(URI url, Type type);

    void remove(Long linkId);

    List<Link> findAll();

    List<Link> findLinksByChatId(Long chatId);

    Optional<Link> findByUrl(URI url);

    List<Link> searchForUpdateLinks(Long second);

    void updateLastUpdate(Long linkId, OffsetDateTime time);

    void updateLastCheck(Long linkId, OffsetDateTime time);

    void updateCommitCount(Long linkId, Integer count);

    void updateAnswerCount(Long linkId, Integer count);

    void updateCommentCount(Long linkId, Integer count);

    void updateType(Long linkId, Type type);
}
