package edu.java.scrapper.services.jpa;

import edu.java.scrapper.dto.scrapper.AddLinkRequest;
import edu.java.scrapper.dto.scrapper.LinkResponse;
import edu.java.scrapper.dto.scrapper.ListLinksResponse;
import edu.java.scrapper.dto.scrapper.RemoveLinkRequest;
import edu.java.scrapper.exception.NotFoundException;
import edu.java.scrapper.exception.RepeatedLinkException;
import edu.java.scrapper.model.entity.Chat;
import edu.java.scrapper.model.entity.Link;
import edu.java.scrapper.model.scheme.Type;
import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.repositories.jpa.JpaLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaLinkServiceTest {
    @Mock
    private JpaLinkRepository jpaLinkRepository;
    @Mock
    private JpaChatRepository jpaChatRepository;

    private final URI url = URI.create("https://github.com/LalisaST/java_course_2");
    private final RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
    private final AddLinkRequest addLinkRequest = new AddLinkRequest(url);
    private final Long id = 0L;
    private final OffsetDateTime time = OffsetDateTime.now();
    private final Chat chat = new Chat();
    private final Link link = new Link();
    private JpaLinkService jpaLinkService;

    @BeforeEach
    public void setUp() {
        chat.setCreateAt(time);
        chat.setId(id);
        link.setId(id);
        link.setUrl(url);
        link.setLastUpdate(time);
        link.setLastCheck(time);
        link.setType(Type.GITHUB);
        link.setCommitCount(0);
        link.setAnswerCount(0);
        link.setCommentCount(0);
        jpaLinkService = new JpaLinkService(jpaChatRepository, jpaLinkRepository);
    }

    @Test
    @DisplayName("Обращение к несуществующему чату")
    public void accessingNonexistentChat() {
        when(!jpaChatRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> jpaLinkService.getLinks(0L)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> jpaLinkService.addLink(0L, addLinkRequest))
            .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> jpaLinkService.deleteLink(0L, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка получения ссылок")
    public void checkingGettingLinks() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);
        when(jpaLinkRepository.findAllByChatsId(id)).thenReturn(List.of(link));

        List<LinkResponse> links = List.of(new LinkResponse(id, url));
        ListLinksResponse listLinksResponse = new ListLinksResponse(links, links.size());

        assertThat(jpaLinkService.getLinks(id)).isEqualTo(listLinksResponse);
    }

    @Test
    @DisplayName("Проверка добавления ссылки")
    public void checkingAddingLink() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);
        when(jpaLinkRepository.save(any())).thenReturn(link);
        when(jpaChatRepository.findById(id)).thenReturn(Optional.of(chat));

        LinkResponse linkResponse = new LinkResponse(id, addLinkRequest.url());
        assertThat(jpaLinkService.addLink(id, addLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка повторного добавления ссылки")
    public void checkingRepeatedLink() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);
        when(jpaLinkRepository.save(any())).thenReturn(link);
        when(jpaLinkRepository.findByIdAndChatsId(id, id)).thenReturn(Optional.of(new Link()));

        assertThatThrownBy(() -> jpaLinkService.addLink(id, addLinkRequest)).isInstanceOf(RepeatedLinkException.class);
    }

    @Test
    @DisplayName("Проверка удаления ссылки")
    public void checkingDeletingLink() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);
        when(jpaLinkRepository.findLinkByUrl(url)).thenReturn(Optional.of(link));
        when(jpaLinkRepository.findByIdAndChatsId(id, id)).thenReturn(Optional.of(new Link()));
        when(jpaChatRepository.findById(id)).thenReturn(Optional.of(chat));

        LinkResponse linkResponse = new LinkResponse(id, removeLinkRequest.url());
        assertThat(jpaLinkService.deleteLink(id, removeLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка удаления несуществующей ссылки")
    public void checkingDeletingNonexistentLink() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);
        when(jpaLinkRepository.findLinkByUrl(url)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jpaLinkService.deleteLink(id, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка удаления неотслеживаемой ссылки")
    public void checkingDeletingUntraceableLink() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);
        when(jpaLinkRepository.findLinkByUrl(url)).thenReturn(Optional.of(link));
        when(jpaLinkRepository.findByIdAndChatsId(id, id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jpaLinkService.deleteLink(id, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }
}
