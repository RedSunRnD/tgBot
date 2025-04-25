package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotService {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private NotificationTaskRepository notificationTaskRepository;
    @Autowired
    private TelegramBot telegramBot;

    @Transactional
    public boolean createNotification(String message, Long chatId) {
        Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String dateTimeString = matcher.group(1);
            String textMessage = matcher.group(3);

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            NotificationTask task = new NotificationTask();
            task.setChatId(chatId);
            task.setDateTime(dateTime);
            task.setTextMessage(textMessage);
            task.setDone(false);

            notificationTaskRepository.save(task);
            logger.info("Notification created successfully for chatId={} with dateTime={}", chatId, dateTime);

            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasksForSend = notificationTaskRepository.findByDateTime(currentTime);
        if (!tasksForSend.isEmpty()) {
            logger.info("Found {} notifications to send at time={}", tasksForSend.size(), currentTime);
            for (NotificationTask task : tasksForSend) {
                long chatId = task.getChatId();
                String textMessage = task.getTextMessage();
                logger.info("Sending notification to chatId={}: {}", chatId, textMessage);
                telegramBot.execute(new SendMessage(chatId, textMessage));
                logger.info("Notification marked as done for chatId={}", chatId);
                task.setDone(true);
                notificationTaskRepository.save(task);
            }
        }
    }
}
