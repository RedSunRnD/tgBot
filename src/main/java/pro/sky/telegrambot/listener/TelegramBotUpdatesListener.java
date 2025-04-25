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
                String messageText = message.text();

                if ("/start".equals(messageText)) {
                    String welcomeText =    "Привет! Добро пожаловать!\n" +
                                            "Чтобы создать напоминание, отправьте сообщение в формате:\n" +
                                            "DD.MM.YYYY HH:mm текст_сообщения";
                    telegramBot.execute(new SendMessage(chatId, welcomeText));
                } else {
                    if (telegramBotService.createNotification(message.text(), chatId)) {
                        String confirmationText = "Напоминание создано";
                        telegramBot.execute(new SendMessage(chatId, confirmationText));
                        logger.info("Notification created successfully for chatId={}", chatId);
                    } else {
                        logger.warn("Invalid message format received from chatId={}", chatId);
                        String errorText = "Неверный формат. Попробуйте такой: ДД.ММ.ГГГГ Текст напоминания";
                        telegramBot.execute(new SendMessage(chatId, errorText));
                    }
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
