package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.command.BotCommand;
import pro.sky.telegrambot.service.TelegramBotService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private TelegramBotService telegramBotService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        logger.info("Telegram bot updates listener initialized.");
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Message message = update.message();
            if (message != null && message.text() != null) {
                long chatId = message.chat().id();
                String messageText = message.text().trim();

                BotCommand command = BotCommand.fromString(messageText);
                if (command != null) {
                    command.execute(chatId, messageText, telegramBot);
                } else {
                    if (telegramBotService.createNotification(messageText, chatId)) {
                        sendResponse(chatId, "Напоминание создано!");
                    } else {
                        sendResponse(chatId, """
                                Неверный формат.
                                Используйте: DD.MM.YYYY HH:mm текст_сообщения
                                """);
                    }
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendResponse(long chatId, String messageText) {
        telegramBot.execute(new SendMessage(chatId, messageText));
    }

}
