package com.ua.hackaton2023.util;

import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.services.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBotUtils extends TelegramLongPollingBot {
    private final TelegramService telegramService;

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
        
        User user = handleAuthorisation(chatId, message.getText());
        if (user != null) {
            String role = user.getRoles().stream().findFirst().get().getName();
            UserDetails userDetails = telegramService.getUserDetails(user);
            if (role.equals("ROLE_CUSTOMER")) {
                // TODO: customer class/method
                customerMethod(chatId);
            } else if (role.equals("ROLE_CARRIER")) {
                // TODO: carrier class/method
                carrierMethod(chatId);
            }
        }
    }

    private void customerMethod(Long chatId) {
        sendMessage(chatId, "i love martin by customer");
    }

    private void carrierMethod(Long chatId) {
        sendMessage(chatId, "i love martin by carrier");
    }

    private void sendStartMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
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

    private User handleAuthorisation(Long chatId, String text) {

        if ("Перевірити".equals(text)) {
            try {
                User user = telegramService.getUserByChatId(chatId);
                sendMessage(chatId, "Ви успішно авторизувались!");
                sendEmptyKeyboard(chatId, "Авторизація успішна!");
                return user;
            } catch (Exception ignored) {
                sendMessage(chatId, "Cпроба увійти провалена, спробуйте ще раз!");
                sendStartMessage(chatId);
                return null;
            }

        } else {
            return null;
        }
    }


    private void sendMessage(Long chatID, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }

    }

    private void sendEmptyKeyboard(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        // Очищення клавіатури
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        keyboardRemove.setRemoveKeyboard(true);
        message.setReplyMarkup(keyboardRemove);

        try {
            execute(message);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public String getBotToken() {
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }
}