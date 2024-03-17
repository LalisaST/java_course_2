package edu.java.scrapper.services;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedLinkException;
import edu.java.model.Chat;
import edu.java.model.ChatLink;
import edu.java.model.Link;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.repositories.jdbc.JdbcLinkDao;
import edu.java.services.jdbc.JdbcLinkService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JdbcLinkServiceTest {
    @Mock
    private JdbcChatDao jdbcChatDao;
    @Mock
    private JdbcChatLinkDao jdbcChatLinkDao;
    @Mock
    private JdbcLinkDao jdbcLinkDao;

    private final URI url = URI.create("url");
    private final RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
    private final AddLinkRequest addLinkRequest = new AddLinkRequest(url);
    private final Long id = 0L;
    private final OffsetDateTime time = OffsetDateTime.now();
    private final Chat chat = new Chat(id, time);
    private final Link link = new Link(id, url, time, time);
    private final ChatLink chatLink = new ChatLink(id, id);
    private JdbcLinkService jdbcLinkService;

    @BeforeEach
    public void setUp() {
        jdbcLinkService = new JdbcLinkService(jdbcLinkDao, jdbcChatDao, jdbcChatLinkDao);
    }

    @Test
    @DisplayName("Обращение к несуществующему чату")
    public void accessingNonexistentChat() {
        when(jdbcChatDao.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> jdbcLinkService.getLinks(2L)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> jdbcLinkService.addLink(2L, addLinkRequest))
            .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> jdbcLinkService.deleteLink(2L, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка получения ссылок")
    public void checkingGettingLinks() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));
        when(jdbcLinkDao.findLinksByChatId(id)).thenReturn(List.of(link));

        List<LinkResponse> links = List.of(new LinkResponse(id, url));
        ListLinksResponse listLinksResponse = new ListLinksResponse(links, links.size());

        assertThat(jdbcLinkService.getLinks(id)).isEqualTo(listLinksResponse);
    }

    @Test
    @DisplayName("Проверка добавления ссылки")
    public void checkingAddingLink() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));
        when(jdbcLinkDao.add(url)).thenReturn(link);

        LinkResponse linkResponse = new LinkResponse(id, addLinkRequest.url());
        assertThat(jdbcLinkService.addLink(id, addLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка повторного добавления ссылки")
    public void checkingRepeatedLink() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));
        when(jdbcLinkDao.findByUrl(url)).thenReturn(Optional.of(link));
        when(jdbcChatLinkDao.findById(id, id)).thenReturn(Optional.of(chatLink));

        assertThatThrownBy(() -> jdbcLinkService.addLink(id, addLinkRequest)).isInstanceOf(RepeatedLinkException.class);
    }

    @Test
    @DisplayName("Проверка удаления ссылки")
    public void checkingDeletingLink() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));
        when(jdbcLinkDao.findByUrl(url)).thenReturn(Optional.of(link));
        when(jdbcChatLinkDao.findById(id, id)).thenReturn(Optional.of(chatLink));

        LinkResponse linkResponse = new LinkResponse(id, removeLinkRequest.url());
        assertThat(jdbcLinkService.deleteLink(id, removeLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка удаления несуществующей ссылки")
    public void checkingDeletingNonexistentLink() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));
        when(jdbcLinkDao.findByUrl(url)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jdbcLinkService.deleteLink(id, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка удаления неотслеживаемой ссылки")
    public void checkingDeletingUntraceableLink() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));
        when(jdbcLinkDao.findByUrl(url)).thenReturn(Optional.of(link));
        when(jdbcChatLinkDao.findById(id, id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jdbcLinkService.deleteLink(id, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }
}
