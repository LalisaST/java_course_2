package edu.java.bot.controller;

import edu.java.bot.dto.bot.LinkUpdateRequest;
import edu.java.bot.service.UpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/updates")
@RestController
public class BotApiController {
    private final UpdateService updateService;

    @PostMapping
    public void update(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        updateService.addUpdate(linkUpdateRequest);
        log.info("The update has been processed");
    }
}
