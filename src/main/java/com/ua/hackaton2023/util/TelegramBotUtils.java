package com.ua.hackaton2023.util;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.CarrierResponse;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.services.TelegramService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TelegramBotUtils extends TelegramLongPollingBot {
    private final TelegramService telegramService;

    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, CargoDto> cargoDrafts = new HashMap<>();
    private final Map<Long, CarDto> carDrafts = new HashMap<>();
    private final Map<Long, CarrierResponseDto> responseDrafts = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public String getBotToken() {
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (data.startsWith("finishCargo_")) {
            handleFinishDeliveryButton(chatId, data);
        }
        if (data.startsWith("accept_")) {
            handleAcceptButton(chatId, data);
        }
        if (data.startsWith("rate_")) {
            handleRatingButton(chatId, data);
        }
    }

    private void handleFinishDeliveryButton(Long chatId, String data) {
        // Створюємо клавіатуру з кнопками оцінок
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(i + " ⭐");
            button.setCallbackData("rate_" + data.split("_")[1] + "_" + i); // Значення callbackData, що відповідає оцінці
            rowInline.add(button);
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        // Відправляємо повідомлення з клавіатурою оцінок
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Оберіть оцінку:");
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRatingButton(Long chatId, String data) {
        sendMessage(chatId, telegramService.finishCargo(data.split("_")));
    }

    private void handleIncomingMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();

        if (text.equals("/start")) {
            sendLoginButton(chatId);
        } else {
            try {
                String role = handleLogin(chatId);
                String state = userStates.get(chatId);

                if (state != null) {
                    if (role.equals("ROLE_CUSTOMER")) {
                        handleCustomerStates(chatId, text, state);
                    } else if (role.equals("ROLE_CARRIER")) {
                        handleCarrierStates(chatId, text, state);
                    }
                } else {
                    if (role.equals("ROLE_CUSTOMER")) {
                        handleCustomerMenu(chatId, text);
                    } else if (role.equals("ROLE_CARRIER")) {
                        handleCarrierMenu(chatId, text);
                    }
                }
            } catch (Exception exception) {
                sendMessage(chatId, "Користувача не знайдено. Попробуйте авторизуватися знову");
            }
        }
    }

    private void sendLoginButton(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.enableHtml(true);
        message.setText("<b>Вас вітає логістична система перевезення вантажів для Сил Оборони України</b>" +
                " \uD83D\uDE9B\nСпочатку увійдіть\n" +
                "<a href='http://13.60.49.197/login?chatId=" + chatId + "'>Увійти</a>");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Увійти");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private String handleLogin(Long chatId) {
        User user = telegramService.getUserByChatId(chatId);
        UserDetails userDetails = telegramService.getUserDetails(user.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return user.getRoles().stream().findFirst().orElseThrow().getName();
    }

    private void handleCustomerStates(Long chatId, String text, String state) {
        switch (state) {
            case "ADD_CARGO_NAME":
                handleAddCargoName(chatId, text);
                break;
            case "ADD_CARGO_DESCRIPTION":
                handleAddCargoDescription(chatId, text);
                break;
            case "ADD_CARGO_WEIGHT":
                handleAddCargoWeight(chatId, text);
                break;
            case "ADD_CARGO_START":
                handleAddCargoStart(chatId, text);
                break;
            case "ADD_CARGO_END":
                handleAddCargoEnd(chatId, text);
                break;
            case "DELETE_CARGO":
                handleDeleteCargo(chatId, text);
                break;
            default:
                userStates.remove(chatId); // Clear state if unrecognized
                sendCustomerMenu(chatId); // Show menu again
                break;
        }
    }

    private void handleAddCargoDescription(Long chatId, String description) {
        CargoDto cargoDto = cargoDrafts.get(chatId);
        if (cargoDto != null) {
            cargoDto.setDescription(description);
            userStates.put(chatId, "ADD_CARGO_WEIGHT");
            sendMessage(chatId, "Введіть вагу вантажу:");
        }
    }

    private void handleCustomerMenu(Long chatId, String text) {
        switch (text) {
            case "Додати вантаж":
                startAddCargoProcess(chatId);
                break;
            case "Видалити вантаж":
                startDeleteCargoProcess(chatId);
                break;
            case "Огляд вантажів":
                showUserCargos(chatId);
                break;
            case "Переглянути відповіді на вантажі":
                showResponses(chatId);
                break;
            default:
                sendCustomerMenu(chatId);
                break;
        }
    }

    private void handleAcceptButton(Long chatId, String data) {
        sendMessage(chatId, telegramService.chooseCargoCarrier(data.split("_")));
    }

    private void showResponses(Long chatId) {
        List<CarrierResponse> list = telegramService.getAllCarrierResponsesByCustomerCargos();
        if (!list.isEmpty()) {
            for (CarrierResponse response : list) {
                String str = "\nID грузу: " + response.getCargo().getId() +
                        "\nНазва перевізника: " + response.getCarrier().getName() +
                        "\nРейтинг перевізника: " + response.getCarrier().getAverageScore() +
                        "\nКількість відгуків: " + response.getCarrier().getTotalRatings() +
                        "\nТекст відгуку: " + response.getDescription() +
                        "\nЦіна: " + response.getCost();

                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());
                message.setText(str);

                // Створюємо інлайн-клавіатуру з кнопкою "Прийняти"
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton acceptButton = new InlineKeyboardButton();
                acceptButton.setText("Прийняти");
                // задаємо callback data
                acceptButton.setCallbackData("accept_" + response.getCargo().getId() + "_" + response.getId());

                // Додаємо кнопку до рядка та рядок до клавіатури
                rowInline.add(acceptButton);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);

                // Встановлюємо клавіатуру повідомлення
                message.setReplyMarkup(markupInline);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            sendMessage(chatId, "Відгуків на перевезення ваших грузів немає");
        }
    }

    private void handleCarrierStates(Long chatId, String text, String state) {
        switch (state) {
            case "ADD_CAR_NAME":
                handleAddCarName(chatId, text);
                break;
            case "ADD_CAR_WEIGHT":
                handleAddCarWeight(chatId, text);
                break;
            case "DELETE_CAR":
                handleDeleteCar(chatId, text);
                break;
            case "RESPOND_TO_CARGO_ID":
                handleRespondToCargoId(chatId, text);
                break;
            case "RESPOND_TO_CARGO_DESCRIPTION":
                handleRespondToCargoDescription(chatId, text);
                break;
            case "RESPOND_TO_CARGO_COST":
                handleRespondToCargoCost(chatId, text);
                break;
            default:
                userStates.remove(chatId); // Clear state if unrecognized
                sendCarrierMenu(chatId); // Show menu again
                break;
        }
    }

    private void handleRespondToCargoCost(Long chatId, String text) {
        double cost;
        try {
            cost = Double.parseDouble(text);
        } catch (Exception ignored) {
            sendMessage(chatId, "Помилка, введіть число (якщо це раціональне число використовуйте крапку)");
            return;
        }
        CarrierResponseDto carrierResponse = responseDrafts.get(chatId);
        carrierResponse.setCost(cost);
        responseDrafts.remove(chatId);
        userStates.remove(chatId);
        sendMessage(chatId, telegramService.addCarrierResponse(carrierResponse));
    }

    private void handleRespondToCargoDescription(Long chatId, String description) {
        CarrierResponseDto carrierResponse = responseDrafts.get(chatId);
        if (carrierResponse != null) {
            carrierResponse.setDescription(description);
            userStates.put(chatId, "RESPOND_TO_CARGO_COST");
            sendMessage(chatId, "Введіть вашу ціну:");
        }
    }

    private void handleRespondToCargoId(Long chatId, String text) {
        long cargoId;
        try {
            cargoId = Long.parseLong(text);
        } catch (Exception ignored) {
            sendMessage(chatId, "Помилка, введіть число");
            return;
        }
        CarrierResponseDto carrierResponse = new CarrierResponseDto();
        carrierResponse.setCargoId(cargoId);
        responseDrafts.put(chatId, carrierResponse);
        userStates.put(chatId, "RESPOND_TO_CARGO_DESCRIPTION");
        sendMessage(chatId, "Введіть сповіщення для відгуку:");
    }

    private void startRespondToCargoProcess(Long chatId) {
        userStates.put(chatId, "RESPOND_TO_CARGO_ID");
        sendMessage(chatId, "Введіть ID вантажу для відповіді:");
    }

    private void startAddCarProcess(Long chatId) {
        userStates.put(chatId, "ADD_CAR_NAME");
        sendMessage(chatId, "Введіть назву транспортний засіб:");
    }

    private void handleAddCarName(Long chatId, String name) {
        CarDto car = new CarDto();
        car.setName(name);
        carDrafts.put(chatId, car);
        userStates.put(chatId, "ADD_CAR_WEIGHT");
        sendMessage(chatId, "Введіть вантажопідйомність транспортного засобу:");
    }

    private void handleAddCarWeight(Long chatId, String text) {
        double weight;
        try {
            weight = Double.parseDouble(text);
            CarDto car = carDrafts.get(chatId);
            if (car != null) {
                car.setWeight(weight);
                telegramService.addCar(car);
                userStates.remove(chatId);
                carDrafts.remove(chatId);
                sendMessage(chatId, "Транспортний засіб успішно додано");
            }
        } catch (Exception ignored) {
            sendMessage(chatId, "Помилка, введіть число");
        }
    }

    private void startDeleteCarProcess(Long chatId) {
        userStates.put(chatId, "DELETE_CAR");
        sendMessage(chatId, "Введіть ID транспортний засіб для видалення:");
    }

    private void handleDeleteCar(Long chatId, String carId) {
        sendMessage(chatId, telegramService.deleteCar(carId));
        userStates.remove(chatId);
    }

    private void showAllCars(Long chatId) {
        List<Car> carList = telegramService.getAllCars();
        if (!carList.isEmpty()) {
            for (Car car : carList) {
                String str = "\nID транспортного засобу: " + car.getId() +
                        "\nНазва: " + car.getName() +
                        "\nВантажопідйомність: " + car.getWeight();

                sendMessage(chatId, str);
            }
        } else {
            sendMessage(chatId, "У вас немає транспорних засобів");
        }
    }

    private void showAllCargos(Long chatId) {
        List<Cargo> cargoList = telegramService.getAllCargosIsActive();
        if (!cargoList.isEmpty()) {
            for (Cargo cargo : cargoList) {
                String str = "\nID вантажу: " + cargo.getId() +
                        "\nНазва: " + cargo.getName() +
                        "\nОпис: " + cargo.getDescription() +
                        "\nВага: " + cargo.getWeight() +
                        "\nПочаткова адреса: " + cargo.getStartAddress() +
                        "\nКінцева адреса: " + cargo.getEndAddress() +
                        "\nДата: " + cargo.getDate();

                sendMessage(chatId, str);
            }
        } else {
            sendMessage(chatId, "Немає доступних вантажів");
        }
    }

    private void handleCarrierMenu(Long chatId, String text) {
        switch (text) {
            case "Огляд вантажів":
                showAllCargos(chatId);
                break;
            case "Відгукнутись на вантаж":
                startRespondToCargoProcess(chatId);
                break;
            case "Додати транспортний засіб":
                startAddCarProcess(chatId);
                break;
            case "Подивитися всі транспортні засоби":
                showAllCars(chatId);
                break;
            case "Видалити транспортний засіб":
                startDeleteCarProcess(chatId);
                break;
            default:
                sendCarrierMenu(chatId);
                break;
        }
    }

    private void sendCustomerMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Вітаю! Ви увійшли як замовник, виберіть опцію:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Додати вантаж");
        row1.add("Видалити вантаж");
        row1.add("Огляд вантажів");
        row1.add("Переглянути відповіді на вантажі");
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendCarrierMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Вітаю! Ви увійшли як перевізник, виберіть опцію:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Огляд вантажів"));
        row1.add(new KeyboardButton("Відгукнутись на вантаж"));
        row1.add(new KeyboardButton("Додати транспортний засіб"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Подивитися всі транспортні засоби"));
        row2.add(new KeyboardButton("Видалити транспортний засіб"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void startAddCargoProcess(Long chatId) {
        userStates.put(chatId, "ADD_CARGO_NAME");
        System.out.println("State set to ADD_CARGO_NAME for chatId: " + chatId); // Debugging line
        sendMessage(chatId, "Введіть назву вантажу:");
    }

    private void startDeleteCargoProcess(Long chatId) {
        userStates.put(chatId, "DELETE_CARGO");
        sendMessage(chatId, "Введіть ID вантажу для видалення:");
    }

    private void showUserCargos(Long chatId) {
        List<Cargo> cargoList = telegramService.getUserCargos();
        if (!cargoList.isEmpty()) {
            String str;
            for (Cargo cargo : cargoList) {
                str = "\nID вантажу: " + cargo.getId() +
                        "\nНазва: " + cargo.getName() +
                        "\nОпис: " + cargo.getDescription() +
                        "\nВага: " + cargo.getWeight() +
                        "\nПочаткова адреса: " + cargo.getStartAddress() +
                        "\nКінцева адреса: " + cargo.getEndAddress() +
                        "\nАктивний: " + cargo.isActive() +
                        "\nЗавершений: " + cargo.isFinished() +
                        "\nДата: " + cargo.getDate();

                if (!cargo.isActive() && !cargo.isFinished()) {
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId.toString());
                    message.setText(str);

                    // Створюємо інлайн-клавіатуру з кнопкою "Завершити"
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    InlineKeyboardButton finishButton = new InlineKeyboardButton();
                    finishButton.setText("Завершити перевезення та оцінити перевізника");
                    finishButton.setCallbackData("finishCargo_" + cargo.getId());

                    rowInline.add(finishButton);
                    rowsInline.add(rowInline);
                    markupInline.setKeyboard(rowsInline);

                    message.setReplyMarkup(markupInline);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    sendMessage(chatId, str);
                }
            }
        } else {
            sendMessage(chatId, "У вас немає вантажів");
        }
    }


    private void handleAddCargoName(Long chatId, String name) {
        CargoDto cargo = new CargoDto();
        cargo.setName(name);
        cargoDrafts.put(chatId, cargo);
        userStates.put(chatId, "ADD_CARGO_DESCRIPTION");

        sendMessage(chatId, "Введіть додатковий опис вантажу:");
    }

    private void handleAddCargoWeight(Long chatId, String text) {
        double weight;
        try {
            weight = Double.parseDouble(text);
            CargoDto cargo = cargoDrafts.get(chatId);
            if (cargo != null) {
                cargo.setWeight(weight);
                userStates.put(chatId, "ADD_CARGO_START");
                sendMessage(chatId, "Введіть адресу відправлення:");
            }
        } catch (Exception ignored) {
            sendMessage(chatId, "Помилка, введіть число");
        }
    }

    private void handleAddCargoStart(Long chatId, String startAddress) {
        CargoDto cargo = cargoDrafts.get(chatId);
        if (cargo != null) {
            cargo.setStartAddress(startAddress);
            userStates.put(chatId, "ADD_CARGO_END");
            sendMessage(chatId, "Введіть адресу призначення:");
        }
    }

    private void handleAddCargoEnd(Long chatId, String endAddress) {
        CargoDto cargo = cargoDrafts.get(chatId);
        if (cargo != null) {
            cargo.setEndAddress(endAddress);
            telegramService.addCargo(cargo);
            userStates.remove(chatId);
            cargoDrafts.remove(chatId);
            sendMessage(chatId, "Вантаж додано успішно ✅");

        }
    }

    private void handleDeleteCargo(Long chatId, String text) {
        sendMessage(chatId, telegramService.deleteCargo(text));
        userStates.remove(chatId);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
