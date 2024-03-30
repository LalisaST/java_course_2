package edu.java.scrapper.scheduler;

import edu.java.scrapper.scheduler.service.LinkUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.scheduler.enable")
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        linkUpdater.update();
    }
}
