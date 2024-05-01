package com.ua.hackaton2023;

import com.ua.hackaton2023.util.TelegramBotUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Hackaton2023Application {
    public static void main(String[] args) {
        SpringApplication.run(Hackaton2023Application.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBotUtils telegramBot = new TelegramBotUtils();
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

}
