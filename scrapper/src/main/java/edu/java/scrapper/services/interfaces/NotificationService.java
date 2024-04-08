package edu.java.scrapper.services.interfaces;

import edu.java.scrapper.dto.bot.LinkUpdateRequest;

public interface NotificationService {
    void send(LinkUpdateRequest update);
}
