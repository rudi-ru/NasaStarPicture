import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Telegrambot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String URL = "https://api.nasa.gov/planetary/apod?" +
            "api_key=3Vg0bTIzkObSUosRqCgnDQrjQ4cXQRVN8HuVdahY";


    public Telegrambot(String BOT_NAME, String BOT_TOKEN) throws TelegramApiException {
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
                String zapros = update.getMessage().getText();

                long chatId = update.getMessage().getChatId();

            switch (zapros) {

                case "/help":
                case "Помощь":
                    sendMessage("Демонстрация бота\n\n"+
                            "Вы можете выбирать команды из меню или набрав вручную.\n\n" +
                            "Старт - начало работы с ботом (/start)\n\n" +
                            "Помощь - открывает это окно (/help)\n\n" +
                            "Получить картинку - получить картинку дня (/image)\n\n" +
                            "Получить картинку в формате HD - получить картинку в качестве HD (/HD)\n\n" +
                            "Получить картинку на определенную дату - вводим в формате: год-месяц-день (пример: 2019-06-08) (/date)", chatId);
                    break;

                case "/start":
                case "Старт":
                case "/image":
                case "Получить картинку":
                    String image = Utils.getUrl(URL);
                    sendMessage(image, chatId);
                    break;
                case "/HD":
                case "Получить картинку в формате HD":
                    image = Utils.getHDUrl(URL);
                    sendMessage(image, chatId);
                    break;

                case "/date":
                case "Получить картинку на определенную дату":
                    sendMessage("Введите дату в формате ГГГГ-ММ-ДД:", chatId);
                    break;
                default:
                    if(zapros.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        String date = zapros;
                        image = Utils.getUrl(URL + "&date=" + date);
                        sendMessage(image, chatId);
                    } else  sendMessage("Моя твоя не понимай", chatId);
            }

        }
    }
    void sendMessage(String msg, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        // Делаем клавиатуру боту
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // разметка для клавиатуры
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>(); // список рядов нашей клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow(); // создаем первый ряд
        keyboardFirstRow.add("Старт");
        keyboardFirstRow.add("Помощь");// добавляем кнопку с описанием
        keyboard.add(keyboardFirstRow); //добавляем наш первый ряд в список рядов
        KeyboardRow keyboardSecondRow = new KeyboardRow(); // создаем второй ряд
        keyboardSecondRow.add("Получить картинку"); // добавляем кнопку во втором ряду с описанием
        keyboardSecondRow.add("Получить картинку в формате HD");
        keyboard.add(keyboardSecondRow); //добавляем наш второй ряд в список рядов
        KeyboardRow keyboardThirdRow = new KeyboardRow(); // Добавляем третий ряд
        keyboardThirdRow.add("Получить картинку на определенную дату"); // добавляем кнопку в третьем ряду с описанием
        keyboard.add(keyboardThirdRow);// добавляем третий ряд в список рядов

        replyKeyboardMarkup.setKeyboard(keyboard); // добавляем все ряды
        message.setReplyMarkup(replyKeyboardMarkup); // привязываем клавиатуру к сообщению



        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return BOT_NAME; // геттер для имени бота
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN; // геттер для токена
    }
}
