package edu.java.bot.link;

import org.springframework.stereotype.Component;

@Component
public class StackOverflowLink implements Link {
    @Override
    public String getHostName() {
        return "stackoverflow.com";
    }
}
