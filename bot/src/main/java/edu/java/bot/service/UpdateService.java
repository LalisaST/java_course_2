package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;
import edu.java.bot.dto.bot.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {
    private final Bot bot;

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {

        for (Long chatIds : linkUpdateRequest.tgChatIds()) {
            SendMessage message =
                new SendMessage(chatIds, linkUpdateRequest.url() + " " + linkUpdateRequest.description());
            bot.execute(message);
        }
    }
}
