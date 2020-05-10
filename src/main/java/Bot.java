import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 * Token получен у бота BotFather командой /newbot
 * Адрес бота в телеграм: t.me/ScheduleHelpMeBot
 * Api бота: github.com/rubenlagus/TelegramBots/
 */
public class Bot  extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init(); //инициализируем Api Context
        TelegramBotsApi botsApi = new TelegramBotsApi(); //получаем Telegram Bots API
        try {
            botsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace(); //регистрируем бота
        }
    }

    /**
     * Обработка события Update
     *
     * @param update принятое событие
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage(); //входящее сообщение
            switch (message.getText()) {
                case "/help":
                    processMessage(message, "Чем вам помочь?");
                    break;
                case "/settings":
                    processMessage(message, "Что нужно настроить?");
                    break;
                default:
                    processMessage(message, "Неверная опция");
            }
        }
    }

    /**
     * Метод формирования и отправки ответного сообщения
     *
     * @param message принятое входящее сообщение
     * @param text сгенерированный текст ответа
     */
    public void processMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage
                .enableMarkdown(true) //включаем разметку
                .setChatId(message.getChatId()) //идентификатор чата
                .setReplyToMessageId(message.getMessageId()) //идентификатор сообщения,
                .setText(text); //текст сообщения
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "ScheduleHelpMeBot";
    }

    @Override
    public String getBotToken() {
        return "token";
    }
}