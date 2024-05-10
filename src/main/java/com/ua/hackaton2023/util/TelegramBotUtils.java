package com.ua.hackaton2023.util;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.services.impl.TelegramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TelegramBotUtils extends TelegramLongPollingBot {
    private TelegramServiceImpl telegramService;

    @Autowired
    public void setTelegramService(TelegramServiceImpl telegramService) {
        this.telegramService = telegramService;

    }

    public TelegramBotUtils() {
        super("6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E");
    }

    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        if ("/start".equals(message.getText())) {
            sendStartMessage(chatId);
        }
        handleAuthorisation(chatId, message.getText());
    }

    private void sendStartMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableHtml(true);
        message.setText(String.format("<b>Вітаю!</b> Спочатку увійдіть у свій профіль: http://localhost:8080/login?chatId=%d", chatId));


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Перевірити");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }
    private void handleAuthorisation(Long chatId, String text){
        if ("Перевірити".equals(text)){

               User user = telegramService.getUserByChatId(chatId);
               System.out.println("User get name");
               sendMessage(chatId, "Ви успішно авторизувались!");


        }

    }


    private void sendMessage( Long chatID, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }

    }


}