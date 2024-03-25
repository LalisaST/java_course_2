package edu.java.repositories.jpa;

import edu.java.model.entity.Link;
import edu.java.model.scheme.Type;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    void deleteAllByChatsEmpty();

    Optional<Link> findLinkByUrl(URI url);

    List<Link> findAllByChatsId(Long id);

    Optional<Link> findByIdAndChatsId(Long linkId, Long chatId);

    @Query(value = "select * from link  where extract(epoch from current_timestamp - last_check) > ?1",
           nativeQuery = true)
    List<Link> findByLastCheckGreaterThanSomeSeconds(Long seconds);

    @Modifying
    @Query(value = "update link set last_update = ?2 where id = ?1", nativeQuery = true)
    void updateLastUpdateById(Long id, OffsetDateTime time);

    @Modifying
    @Query(value = "update link set last_check = ?2 where id = ?1", nativeQuery = true)
    void updateLastCheckById(Long id, OffsetDateTime time);

    @Modifying
    @Query(value = "update link set commit_count = ?2 where id = ?1", nativeQuery = true)
    void updateCommitCountById(Long id, Integer count);

    @Modifying
    @Query(value = "update link set answer_count = ?2 where id = ?1", nativeQuery = true)
    void updateAnswerCountById(Long id, Integer count);

    @Modifying
    @Query(value = "update link set comment_count = ?2 where id = ?1", nativeQuery = true)
    void updateCommentCountById(Long id, Integer count);

    @Modifying
    @Query(value = "update link set type = ?2 where id = ?1", nativeQuery = true)
    void updateTypeById(Long id, Type type);
}
