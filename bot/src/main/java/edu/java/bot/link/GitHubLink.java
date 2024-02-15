package edu.java.bot.link;

import org.springframework.stereotype.Component;

@Component
public class GitHubLink implements Link {
    @Override
    public String getHostName() {
        return "github.com";
    }
}
