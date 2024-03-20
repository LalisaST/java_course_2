package edu.java.controllers;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.services.jdbc.JdbcLinkService;
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
    private final JdbcLinkService jdbcLinkService;

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") @NotNull Long id) {
        return jdbcLinkService.getLinks(id);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") @NotNull Long id,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        return jdbcLinkService.addLink(id, addLinkRequest);
    }

    @DeleteMapping
    public LinkResponse deleteLink(
        @RequestHeader("Tg-Chat-Id") @NotNull Long id,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        return jdbcLinkService.deleteLink(id, removeLinkRequest);
    }
}
