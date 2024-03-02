package edu.java.scrapper.services;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.exeption.NotFoundException;
import edu.java.exeption.RepeatedLinkException;
import edu.java.services.LinkService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class LinkServiceTest {
    private LinkService linkService;
    private RemoveLinkRequest removeLinkRequest;
    private AddLinkRequest addLinkRequest;

    @BeforeEach
    public void setUp() {
        addLinkRequest = new AddLinkRequest(URI.create("url1"));
        removeLinkRequest = new RemoveLinkRequest(URI.create("url"));

        linkService = new LinkService();
        linkService.addChat(1L, new ArrayList<>(List.of()));
        linkService.addLink(1L, new AddLinkRequest(URI.create("url")));
    }

    @Test
    @DisplayName("Обращение к несуществующему чату")
    public void accessingNonexistentChat() {
        assertThatThrownBy(() -> linkService.getLinks(2L)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> linkService.addLink(2L, addLinkRequest)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> linkService.deleteLink(2L, removeLinkRequest)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Проверка получения ссылок")
    public void checkingGettingLinks() {
        List<LinkResponse> links = List.of(new LinkResponse(0L, URI.create("url")));
        ListLinksResponse listLinksResponse = new ListLinksResponse(links, links.size());

        assertThat(linkService.getLinks(1L)).isEqualTo(listLinksResponse);
    }

    @Test
    @DisplayName("Проверка добавления ссылки")
    public void checkingAddingLink() {
        LinkResponse linkResponse = new LinkResponse(1L, addLinkRequest.url());
        assertThat(linkService.addLink(1L, addLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка повторного добавления ссылки")
    public void checkingRepeatedLink() {
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("url"));
        assertThatThrownBy(() -> linkService.addLink(1L, addLinkRequest)).isInstanceOf(RepeatedLinkException.class);
    }

    @Test
    @DisplayName("Проверка удаления ссылки")
    public void checkingDeletingLink() {
        LinkResponse linkResponse = new LinkResponse(0L, removeLinkRequest.url());
        assertThat(linkService.deleteLink(1L, removeLinkRequest)).isEqualTo(linkResponse);
    }

    @Test
    @DisplayName("Проверка удаления несуществующей ссылки")
    public void checkingDeletingNonexistentLink() {
        RemoveLinkRequest removeLinkRequest1 = new RemoveLinkRequest(URI.create("url1"));
        assertThatThrownBy(() -> linkService.deleteLink(1L, removeLinkRequest1)).isInstanceOf(NotFoundException.class);
    }
}
