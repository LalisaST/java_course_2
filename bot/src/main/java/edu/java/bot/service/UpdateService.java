package edu.java.bot.service;

import edu.java.bot.dto.bot.LinkUpdateRequest;
import edu.java.bot.exception.UpdateAlreadyExistsException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
    private final Set<LinkUpdateRequest> updates = new HashSet<>();

    public void addUpdate(LinkUpdateRequest linkUpdateRequest) {
        if (!updates.add(linkUpdateRequest)) {
            throw new UpdateAlreadyExistsException("Update already exists");
        }
    }
}
