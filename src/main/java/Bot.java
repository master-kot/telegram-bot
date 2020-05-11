import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Token получен у бота BotFather командой /newbot
 * Адрес бота в телеграм: t.me/ScheduleHelpMeBot
 * Api бота: github.com/rubenlagus/TelegramBots/
 */
public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        //инициализируем Api Context
        ApiContextInitializer.init();
        //получаем Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            //Регистрируем бота
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static final String BOT_NAME = "ScheduleHelpMeBot";
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");

    private static final String PROXY_HOST = "catalog.live.ovh" /* proxy host */;
    private static final Integer PROXY_PORT = 443 /* proxy port */;

    protected Bot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    protected Bot() {
        super();
    }

    private static DefaultBotOptions setProxyParameters() {
            //Устанавливаем параметры Http proxy
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            //Выбрать тип прокси: [HTTP|SOCKS4|SOCKS5]
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            return botOptions;
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
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}