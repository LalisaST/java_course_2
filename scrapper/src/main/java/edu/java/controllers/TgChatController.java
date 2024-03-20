package edu.java.controllers;

import edu.java.services.jdbc.JdbcTgChatService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/tg-chat")
@RestController
public class TgChatController {
    private static final String CHAT_STRING = "CHAT";
    private final JdbcTgChatService jdbcTgChatService;

    @PostMapping("/{id}")
    public void registrationChat(@PathVariable @NotNull Long id) {
        jdbcTgChatService.registerChat(id);
        log.info(CHAT_STRING + id + "is registered");
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable @NotNull Long id) {
        jdbcTgChatService.deleteChat(id);
        log.info(CHAT_STRING + id + "deleted");
    }
}
