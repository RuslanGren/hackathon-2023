package com.ua.hackaton2023.util;

import com.ua.hackaton2023.entity.Car;
import com.ua.hackaton2023.entity.Cargo;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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

    private Map<Long, String> userStates = new HashMap<>();
    private Map<Long, CargoDto> cargoDrafts = new HashMap<>();
    private Map<Long, CarDto> carDrafts = new HashMap<>();
    private Map<Long, CarrierResponseDto> responseDrafts = new HashMap<>();

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
        }
    }

    private void handleIncomingMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();

        if (text.equals("/start")) {
            sendLoginButton(chatId);
        } else if (text.equals("Увійти")) {
            handleLogin(chatId);
        } else {
            User user = telegramService.getUserByChatId(chatId);
            if (user != null) {
                String role = user.getRoles().stream().findFirst().get().getName();
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
            } else {
                sendMessage(chatId, "Користувача не знайдено.");
            }
        }
    }

    private void handleCustomerStates(Long chatId, String text, String state) {
        switch (state) {
            case "ADD_CARGO_NAME":
                handleAddCargoName(chatId, text);
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

    private void handleCustomerMenu(Long chatId, String text) {
        switch (text) {
            case "Додати груз":
                startAddCargoProcess(chatId);
                break;
            case "Видалити груз":
                startDeleteCargoProcess(chatId);
                break;
            case "Показати всі грузи користувача":
                showUserCargos(chatId);
                break;
            default:
                sendCustomerMenu(chatId);
                break;
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
            case "ADD_CAR_VOLUME":
                handleAddCarVolume(chatId, text);
                break;
            case "ADD_CAR_INSURANCE":
                handleAddCarInsurance(chatId, text);
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
        telegramService.addCarrierResponse(carrierResponse);
        responseDrafts.remove(chatId);
        userStates.remove(chatId);
        sendMessage(chatId, "Відгук на груз успішно відправлений");
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
        Long cargoId;
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
        sendMessage(chatId, "Введіть ID грузу для відповіді:");
    }

    private void startAddCarProcess(Long chatId) {
        userStates.put(chatId, "ADD_CAR_NAME");
        sendMessage(chatId, "Введіть назву машини:");
    }

    private void handleAddCarName(Long chatId, String name) {
        CarDto car = new CarDto();
        car.setName(name);
        carDrafts.put(chatId, car);
        userStates.put(chatId, "ADD_CAR_WEIGHT");
        sendMessage(chatId, "Введіть вагу машини:");
    }

    private void handleAddCarWeight(Long chatId, String weight) {
        CarDto car = carDrafts.get(chatId);
        if (car != null) {
            car.setWeight(Double.parseDouble(weight));
            userStates.put(chatId, "ADD_CAR_VOLUME");
            sendMessage(chatId, "Введіть об'єм машини:");
        }
    }

    private void handleAddCarVolume(Long chatId, String volume) {
        CarDto car = carDrafts.get(chatId);
        if (car != null) {
            car.setVolume(Double.parseDouble(volume));
            userStates.put(chatId, "ADD_CAR_INSURANCE");
            sendMessage(chatId, "Введіть інформацію про страховку:");
        }
    }

    private void handleAddCarInsurance(Long chatId, String insurance) {
        CarDto car = carDrafts.get(chatId);
        if (car != null) {
            car.setInsurance(insurance);
            telegramService.addCar(car);
            userStates.remove(chatId);
            carDrafts.remove(chatId);
            sendMessage(chatId, "Машину додано успішно.");
        }
    }

    private void startDeleteCarProcess(Long chatId) {
        userStates.put(chatId, "DELETE_CAR");
        sendMessage(chatId, "Введіть ID машини для видалення:");
    }

    private void handleDeleteCar(Long chatId, String carId) {
        sendMessage(chatId, telegramService.deleteCar(carId));
        userStates.remove(chatId);
    }

    private void showAllCars(Long chatId) {
        List<Car> carList = telegramService.getAllCars();
        if (!carList.isEmpty()) {
            for (Car car : carList) {
                String str = "\nID машини: " + car.getId() +
                        "\nНазва: " + car.getName() +
                        "\nВага: " + car.getWeight() +
                        "\nОб'єм: " + car.getVolume() +
                        "\nСтраховка: " + car.getInsurance();

                sendMessage(chatId, str);
            }
        } else {
            sendMessage(chatId, "У вас немає машин");
        }
    }

    private void showAllCargos(Long chatId) {
        List<Cargo> cargoList = telegramService.getAllCargos();
        if (!cargoList.isEmpty()) {
            for (Cargo cargo : cargoList) {
                String str = "\nID груза: " + cargo.getId() +
                        "\nНазва: " + cargo.getName() +
                        "\nОпис: " + cargo.getDescription() +
                        "\nВага: " + cargo.getWeight() +
                        "\nОб'єм: " + cargo.getVolume() +
                        "\nСтраховка: " + cargo.getInsurance() +
                        "\nПочаткова адресса: " + cargo.getStartAddress() +
                        "\nКінцева адресса: " + cargo.getEndAddress() +
                        "\nАктивний: " + cargo.isActive() +
                        "\nЗавершений: " + cargo.isFinished() +
                        "\nДата: " + cargo.getDate();

                sendMessage(chatId, str);
            }
        } else {
            sendMessage(chatId, "Немає доступних грузів");
        }
    }




    private void handleCarrierMenu(Long chatId, String text) {
        switch (text) {
            case "Подивитись всі грузи":
                showAllCargos(chatId);
                break;
            case "Відповісти на груз":
                startRespondToCargoProcess(chatId);
                break;
            case "Додати машину":
                startAddCarProcess(chatId);
                break;
            case "Подивитися всі машини":
                showAllCars(chatId);
                break;
            case "Видалити машину":
                startDeleteCarProcess(chatId);
                break;
            default:
                sendCarrierMenu(chatId);
                break;
        }
    }

    private void sendLoginButton(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Вітаю. Спочатку увійдіть\n http://localhost:8080/register");

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

    private void handleLogin(Long chatId) {
        User user = telegramService.getUserByChatId(chatId);
        if (user != null) {
            UserDetails userDetails = telegramService.getUserDetails(user.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String role = user.getRoles().stream().findFirst().get().getName();
            if (role.equals("ROLE_CUSTOMER")) {
                sendCustomerMenu(chatId);
            } else if (role.equals("ROLE_CARRIER")) {
                sendCarrierMenu(chatId);
            }
        } else {
            sendMessage(chatId, "Користувача не знайдено.");
        }
    }

    private void sendCustomerMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Виберіть опцію:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Додати груз");
        row1.add("Видалити груз");
        row1.add("Показати всі грузи користувача");
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
        message.setText("Виберіть опцію:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Подивитись всі грузи"));
        row1.add(new KeyboardButton("Відповісти на груз"));
        row1.add(new KeyboardButton("Додати машину"));
        row1.add(new KeyboardButton("Подивитися всі машини"));
        row1.add(new KeyboardButton("Видалити машину"));
        keyboard.add(row1);
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
        sendMessage(chatId, "Введіть назву грузу:");
    }

    private void startDeleteCargoProcess(Long chatId) {
        userStates.put(chatId, "DELETE_CARGO");
        sendMessage(chatId, "Введіть ID грузу для видалення:");
    }

    private void showUserCargos(Long chatId) {
        List<Cargo> cargoList = telegramService.getUserCargos();
        if (!cargoList.isEmpty()) {
            String str;
            for (Cargo cargo : cargoList) {
                str = "\nID груза: " + cargo.getId() +
                        "\nНазва: " + cargo.getName() +
                        "\nОпис: " + cargo.getDescription() +
                        "\nВага: " + cargo.getWeight() +
                        "\nОб'єм: " + cargo.getVolume() +
                        "\nСтраховка: " + cargo.getInsurance() +
                        "\nПочаткова адресса: " + cargo.getStartAddress() +
                        "\nКінцева адресса: " + cargo.getEndAddress() +
                        "\nАктивний: " + cargo.isActive() +
                        "\nЗавершений: " + cargo.isFinished() +
                        "\nДата: " + cargo.getDate();

                sendMessage(chatId, str);
            }
        } else {
            sendMessage(chatId, "У вас немає грузів");
        }
    }


    private void handleAddCargoName(Long chatId, String name) {
        CargoDto cargo = new CargoDto();
        cargo.setName(name);
        cargoDrafts.put(chatId, cargo);
        userStates.put(chatId, "ADD_CARGO_WEIGHT");

        sendMessage(chatId, "Введіть вагу грузу:");
    }

    private void handleAddCargoWeight(Long chatId, String weight) {
        CargoDto cargo = cargoDrafts.get(chatId);
        if (cargo != null) {
            cargo.setWeight(Double.parseDouble(weight));
            userStates.put(chatId, "ADD_CARGO_START");
            sendMessage(chatId, "Введіть адресу відправлення:");
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
            sendMessage(chatId, "Груз додано успішно.");
        }
    }

    private void handleDeleteCargo(Long chatId, String cargoId) {
        sendMessage(chatId, telegramService.deleteCargo(cargoId));
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
