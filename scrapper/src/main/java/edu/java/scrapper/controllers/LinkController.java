package edu.java.scrapper.controllers;

import edu.java.scrapper.dto.scrapper.AddLinkRequest;
import edu.java.scrapper.dto.scrapper.LinkResponse;
import edu.java.scrapper.dto.scrapper.ListLinksResponse;
import edu.java.scrapper.dto.scrapper.RemoveLinkRequest;
import edu.java.scrapper.services.interfaces.LinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/links")
@RestController
public class LinkController {
    private final LinkService linkService;

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") @NotNull Long id) {
        return linkService.getLinks(id);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") @NotNull Long id,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        return linkService.addLink(id, addLinkRequest);
    }

    @DeleteMapping
    public LinkResponse deleteLink(
        @RequestHeader("Tg-Chat-Id") @NotNull Long id,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.deleteLink(id, removeLinkRequest);
    }
}
