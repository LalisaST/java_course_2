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
import edu.java.model.Type;
import edu.java.repositories.interfaces.ChatDao;
import edu.java.repositories.interfaces.ChatLinkDao;
import edu.java.repositories.interfaces.LinkDao;
import edu.java.services.DefaultLinkService;
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
public class DefaultLinkServiceTest {
    @Mock
    private ChatDao chatDao;
    @Mock
    private ChatLinkDao chatLinkDao;
    @Mock
    private LinkDao linkDao;

    private final URI url = URI.create("https://github.com/LalisaST/java_course_2");
    private final RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
    private final AddLinkRequest addLinkRequest = new AddLinkRequest(url);
    private final Long id = 0L;
    private final OffsetDateTime time = OffsetDateTime.now();
    private final Chat chat = new Chat(id, time);
    private final Link link = new Link(id, url, time, time, Type.GITHUB,0, 0, 0);
    private final ChatLink chatLink = new ChatLink(id, id);
    private DefaultLinkService defaultLinkService;

    @BeforeEach
    public void setUp() {
        defaultLinkService = new DefaultLinkService(linkDao, chatDao, chatLinkDao);
    }

    @Test
    @DisplayName("Обращение к несуществующему чату")
    public void accessingNonexistentChat() {
        when(chatDao.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> defaultLinkService.getLinks(2L)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> defaultLinkService.addLink(2L, addLinkRequest))
            .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> defaultLinkService.deleteLink(2L, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка получения ссылок")
    public void checkingGettingLinks() {
        when(chatDao.findAll()).thenReturn(List.of(chat));
        when(linkDao.findLinksByChatId(id)).thenReturn(List.of(link));

        List<LinkResponse> links = List.of(new LinkResponse(id, url));
        ListLinksResponse listLinksResponse = new ListLinksResponse(links, links.size());

        assertThat(defaultLinkService.getLinks(id)).isEqualTo(listLinksResponse);
    }

    @Test
    @DisplayName("Проверка добавления ссылки")
    public void checkingAddingLink() {
        when(chatDao.findAll()).thenReturn(List.of(chat));
        when(linkDao.add(url, Type.GITHUB)).thenReturn(link);

        LinkResponse linkResponse = new LinkResponse(id, addLinkRequest.url());
        assertThat(defaultLinkService.addLink(id, addLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка повторного добавления ссылки")
    public void checkingRepeatedLink() {
        when(chatDao.findAll()).thenReturn(List.of(chat));
        when(linkDao.findByUrl(url)).thenReturn(Optional.of(link));
        when(chatLinkDao.findById(id, id)).thenReturn(Optional.of(chatLink));

        assertThatThrownBy(() -> defaultLinkService.addLink(id, addLinkRequest)).isInstanceOf(RepeatedLinkException.class);
    }

    @Test
    @DisplayName("Проверка удаления ссылки")
    public void checkingDeletingLink() {
        when(chatDao.findAll()).thenReturn(List.of(chat));
        when(linkDao.findByUrl(url)).thenReturn(Optional.of(link));
        when(chatLinkDao.findById(id, id)).thenReturn(Optional.of(chatLink));

        LinkResponse linkResponse = new LinkResponse(id, removeLinkRequest.url());
        assertThat(defaultLinkService.deleteLink(id, removeLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка удаления несуществующей ссылки")
    public void checkingDeletingNonexistentLink() {
        when(chatDao.findAll()).thenReturn(List.of(chat));
        when(linkDao.findByUrl(url)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> defaultLinkService.deleteLink(id, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка удаления неотслеживаемой ссылки")
    public void checkingDeletingUntraceableLink() {
        when(chatDao.findAll()).thenReturn(List.of(chat));
        when(linkDao.findByUrl(url)).thenReturn(Optional.of(link));
        when(chatLinkDao.findById(id, id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> defaultLinkService.deleteLink(id, removeLinkRequest))
            .isInstanceOf(NotFoundException.class);
    }
}
