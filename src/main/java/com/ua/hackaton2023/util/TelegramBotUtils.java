package com.ua.hackaton2023.util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBotUtils extends TelegramLongPollingBot {
    public TelegramBotUtils() {
        super("BOT_TOKEN");
    }

    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            Long chatId = message.getChatId();
            sendStartMessage(chatId);
        }
    }

    private void sendStartMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableHtml(true);
        message.setText("<b>Вітаю!</b> Спочатку увійдіть у свій профіль: <a href='http://localhost:8080/login'>увійти</a>");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Головне меню:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Додати вантаж");
        row1.add("Видалити вантаж");
        keyboard.add(row1);

            KeyboardRow row2 = new KeyboardRow();
            row2.add("Вибрати перевізника");
            row2.add("Завершити перевезення");
            keyboard.add(row2);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addCargo(Long chatId) {
        try {
            SendMessage nameMessage = new SendMessage();
            nameMessage.setChatId(chatId);
            nameMessage.setText("Введіть назву вантажу:");
            execute(nameMessage);

            SendMessage weightMessage = new SendMessage();
            weightMessage.setChatId(chatId);
            weightMessage.setText("Введіть вагу вантажу (у кілограмах):");
            execute(weightMessage);

            SendMessage destinationMessage = new SendMessage();
            destinationMessage.setChatId(chatId);
            destinationMessage.setText("Введіть адресу доставки вантажу:");
            execute(destinationMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
    private  void deleteCargo(long chatId){
        SendMessage deleteMessage = new SendMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setText("введіть назву вантажу для видалення:");
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e ){
            System.out.println(e.getMessage());
        }
    }

    private  void  menuTransporter(long chatId,String text){
        SendMessage menuTransporter = new SendMessage();
        menuTransporter.setChatId(chatId);
        menuTransporter.setText("список активних водіїв\n/1_oleg_m \n/2_ivan_r\n/3_nazar_b\n/4_ignat\n/5_lol \n/6_rick\n");


        try {
            execute(menuTransporter);

        } catch (TelegramApiException e){
            System.out.println(e.getMessage());
        }

    }
}